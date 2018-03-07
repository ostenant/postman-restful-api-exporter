package org.ostenant.service.monitor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ostenant.service.monitor.config.ExporterPathProperties;
import org.ostenant.service.monitor.config.TokenProperties;
import org.ostenant.service.monitor.entitiy.ExporterResult;
import org.ostenant.service.monitor.entitiy.MapBean;
import org.ostenant.service.monitor.entitiy.MethodType;
import org.ostenant.service.monitor.entitiy.TokenResponse;
import org.ostenant.service.monitor.entitiy.collection.CollectionItem;
import org.ostenant.service.monitor.entitiy.collection.CollectionItemEvent;
import org.ostenant.service.monitor.entitiy.collection.CollectionItemRequest;
import org.ostenant.service.monitor.entitiy.collection.CollectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MetricsController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final Semaphore semaphore = new Semaphore(20);
    private final ExecutorService executor = Executors.newFixedThreadPool(50);

    private final CollectionManager collectionManager;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final TokenProperties tokenProperties;

    @Autowired
    public MetricsController(CollectionManager collectionManager,
                             RestTemplate restTemplate,
                             ObjectMapper objectMapper,
                             TokenProperties tokenProperties) {
        this.collectionManager = collectionManager;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.tokenProperties = tokenProperties;
    }

    @GetMapping("/metrics")
    @SuppressWarnings("unchecked")
    public void collect(HttpServletResponse resp) throws Exception {
        final URI tokenRequestUri = UriComponentsBuilder.newInstance()
                .scheme(tokenProperties.getScheme())
                .host(tokenProperties.getHost())
                .path(tokenProperties.getPath())
                .queryParam(TokenProperties.MERCHANT_NAME, tokenProperties.getUsername())
                .queryParam(TokenProperties.MERCHANT_PWD, tokenProperties.getPassword())
                .build().toUri();

        String response = restTemplate.getForObject(tokenRequestUri, String.class);
        TokenResponse tokenResponse = objectMapper.readValue(response, TokenResponse.class);
        final String tokenId = tokenResponse.getTokenId();

        AtomicInteger allTestCases = new AtomicInteger();
        AtomicInteger failedTestCases = new AtomicInteger();

        List<CollectionItem> collectionItemList = collectionManager.getItem();
        int totalTestCases = 0;
        for (CollectionItem aCollectionItemList : collectionItemList) {
            List<CollectionItem.CollectionItemDetail> itemDetailList = aCollectionItemList.getItemDetails();
            for (int j = 0; j < itemDetailList.size(); j++) {
                totalTestCases++;
            }
        }

        CountDownLatch countDownLatch = new CountDownLatch(totalTestCases);
        ConcurrentLinkedQueue<ExporterResult> resultQueue = new ConcurrentLinkedQueue<>();

        for (CollectionItem collectionItem : collectionItemList) {
            List<CollectionItem.CollectionItemDetail> itemDetailList = collectionItem.getItemDetails();
            for (CollectionItem.CollectionItemDetail itemDetail : itemDetailList) {
                semaphore.acquire();
                executor.execute(() -> {
                    CollectionItemRequest request = itemDetail.getRequest();
                    CollectionItemEvent event = itemDetail.getEvents().get(0);
                    Map<String, Object> execMap = event.getEventScript().getExecStrMap();

                    String url = StringUtils.replace(request.getUrl().getRaw(), ExporterPathProperties.TOKEN_TEMPLATE, tokenId);
                    String method = request.getMethod();
                    List<Map<String, String>> headerListMap = request.getHeader();
                    String body = request.getBody().getRaw();
                    HttpMethod httpMethod = HttpMethod.GET;
                    switch (method) {
                        case MethodType.POST:
                            httpMethod = HttpMethod.POST;
                            break;
                        case MethodType.PUT:
                            httpMethod = HttpMethod.PUT;
                            break;
                        case MethodType.DELETE:
                            httpMethod = HttpMethod.DELETE;
                            break;
                    }

                    HttpHeaders headers = new HttpHeaders();
                    for (Map<String, String> headerMap : headerListMap) {
                        String key = headerMap.get(CollectionItem.KEY);
                        String value = headerMap.get(CollectionItem.VALUE);
                        if (headers.containsKey(key)) {
                            List<String> valueList = headers.get(key);
                            valueList.add(value);
                        } else {
                            List<String> valueList = new ArrayList<>();
                            valueList.add(value);
                            headers.put(key, valueList);
                        }
                    }

                    URI uri = URI.create(url);
                    final ResponseEntity<String> responseEntity = restTemplate.exchange(
                            new RequestEntity<>(
                                    body,
                                    headers,
                                    httpMethod,
                                    uri
                            ), String.class);

                    final String responseBody = responseEntity.getBody();
                    Map<String, Object> responseMap = null;
                    try {
                        responseMap = objectMapper.readValue(responseBody, Map.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Map<String, Object> targetMap = new LinkedHashMap<>();
                    Map<String, Object> flatMap = flatMap(responseMap, targetMap);

                    addResult(resultQueue, itemDetail.getName());

                    for (Map.Entry<String, Object> execEntry : execMap.entrySet()) {
                        String execKey = execEntry.getKey();
                        String exec = (String) execEntry.getValue();
                        boolean containsKey = flatMap.containsKey(execKey);
                        if (!containsKey) {
                            addFailedResult(resultQueue, itemDetail.getName());
                            failedTestCases.getAndIncrement();
                            break;
                        }

                        Object res = flatMap.get(execKey);
                        if (res instanceof Integer) {
                            Integer resInteger = (Integer) res;
                            Integer execInteger = Integer.parseInt(exec);
                            if (resInteger.intValue() != execInteger.intValue()) {
                                addFailedResult(resultQueue, itemDetail.getName(), execKey, exec, res);

                                logger.warn("测试用例: [{}]; 期待结果: [{}={}]; 实际结果: [{}={}] ",
                                        itemDetail.getName(), execKey, exec, execKey, res);
                                failedTestCases.getAndIncrement();
                                break;
                            }
                        } else if (res instanceof Long) {
                            Long resLong = (Long) res;
                            Long execLong = Long.parseLong(exec);
                            if (resLong.longValue() != execLong.longValue()) {
                                addFailedResult(resultQueue, itemDetail.getName(), execKey, exec, res);

                                logger.warn("测试用例: [{}]; 期待结果: [{}={}]; 实际结果: [{}={}] ",
                                        itemDetail.getName(), execKey, exec, execKey, res);
                                failedTestCases.getAndIncrement();
                                break;
                            }
                        } else if (res instanceof Double) {
                            Double resDouble = (Double) res;
                            Double execDouble = Double.parseDouble(exec);
                            if (resDouble.doubleValue() != execDouble.doubleValue()) {
                                addFailedResult(resultQueue, itemDetail.getName(), execKey, exec, res);

                                logger.warn("测试用例: [{}]; 期待结果: [{}={}]; 实际结果: [{}={}] ",
                                        itemDetail.getName(), execKey, exec, execKey, res);
                                failedTestCases.getAndIncrement();
                                break;
                            }
                        } else if (res instanceof Boolean) {
                            Boolean resBoolean = (Boolean) res;
                            Boolean execBoolean = Boolean.parseBoolean(exec);
                            if (!resBoolean.equals(execBoolean)) {
                                addFailedResult(resultQueue, itemDetail.getName(), execKey, exec, res);

                                logger.warn("测试用例: [{}]; 期待结果: [{}={}]; 实际结果: [{}={}] ",
                                        itemDetail.getName(), execKey, exec, execKey, res);
                                failedTestCases.getAndIncrement();
                                break;
                            }
                        } else {
                            if (!Objects.equals(exec, res)) {
                                addFailedResult(resultQueue, itemDetail.getName(), execKey, exec, res);

                                logger.warn("测试用例: [{}]; 期待结果: [{}={}]; 实际结果: [{}={}] ",
                                        itemDetail.getName(), execKey, exec, execKey, res);
                                failedTestCases.getAndIncrement();
                                break;
                            }
                        }
                    }
                    allTestCases.getAndIncrement();
                    countDownLatch.countDown();
                    semaphore.release();
                });
            }
        }

        try {
            countDownLatch.await();
        } catch (Exception ex) {
            logger.error("中断异常：", ex);
            throw ex;
        }

        resp.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = resp.getWriter();
        resultQueue.forEach(writer::println);
    }

    private void addResult(ConcurrentLinkedQueue<ExporterResult> resultQueue, String testCase) {
        ExporterResult result = new ExporterResult(testCase, ExporterResult.OKAY);
        resultQueue.offer(result);
    }

    private void addFailedResult(ConcurrentLinkedQueue<ExporterResult> resultQueue, String testCase) {
        ExporterResult result = new ExporterResult(testCase, ExporterResult.FAILED);
        resultQueue.offer(result);
    }

    private void addFailedResult(ConcurrentLinkedQueue<ExporterResult> resultQueue, String testCase, String execKey, String exec, Object res) {
        ExporterResult result = new ExporterResult(testCase, ExporterResult.FAILED);
        result.getExpectedMap().put(execKey, exec);
        result.getActualMap().put(execKey, String.valueOf(res));
        resultQueue.offer(result);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> flatMap(Map<String, Object> sourceMap,
                                               Map<String, Object> targetMap) {
        LinkedList<MapBean> mapBeanList = new LinkedList<>();
        MapBean mapBean = new MapBean();
        mapBean.setPrefixList(new ArrayList<>());
        mapBean.setValueMap(sourceMap);
        mapBeanList.add(mapBean);

        while (CollectionUtils.isNotEmpty(mapBeanList)) {
            MapBean first = mapBeanList.removeFirst();
            List<String> prefixList = first.getPrefixList();
            Map<String, Object> valueMap = first.getValueMap();
            Set<String> keySet = valueMap.keySet();
            for (String key : keySet) {
                Object valueObject = valueMap.get(key);
                List<String> prefixListCopy = new ArrayList<>(prefixList);
                prefixListCopy.add(key);
                if (valueObject instanceof Map) {
                    Map<String, Object> subMap = (Map<String, Object>) valueObject;
                    MapBean subMapBean = new MapBean();
                    subMapBean.setPrefixList(prefixListCopy);
                    subMapBean.setValueMap(subMap);
                    mapBeanList.addLast(subMapBean);
                } else {
                    String flatKey = StringUtils.join(prefixListCopy, ".");
                    targetMap.put(flatKey, valueObject);
                }
            }
        }

        return targetMap;
    }

}
