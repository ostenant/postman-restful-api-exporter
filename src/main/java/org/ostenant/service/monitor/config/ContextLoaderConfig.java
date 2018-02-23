package org.ostenant.service.monitor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ostenant.service.monitor.entitiy.Variable;
import org.ostenant.service.monitor.entitiy.collection.CollectionItem;
import org.ostenant.service.monitor.entitiy.collection.CollectionItemEvent;
import org.ostenant.service.monitor.entitiy.collection.CollectionManager;
import org.ostenant.service.monitor.entitiy.environment.EnvironmentVariable;
import org.ostenant.service.monitor.entitiy.environment.EnvironmentVariableManager;
import org.ostenant.service.monitor.entitiy.global.GlobalVariable;
import org.ostenant.service.monitor.entitiy.global.GlobalVariableManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@Import({ExporterPathProperties.class, SerializationConfig.class})
public class ContextLoaderConfig {

    private final Pattern argumentPattern = Pattern.compile("(?<=\\{\\{)(.+?)(?=}})");
    private final Pattern propertyPattern = Pattern.compile("(?<=jsonData.)(.+?)(?====)");
    private final Pattern resultPattern = Pattern.compile("(?<====)(.+?)(?=;)");

    private final static String START_PLACE_HOLDER = "{{";
    private final static String END_PLACE_HOLDER = "}}";

    @Bean
    public GlobalVariableManager globalVariableManager(ExporterPathProperties pathProperties, ObjectMapper objectMapper) throws Exception {
        final String globalJsonPath = pathProperties.getGlobal();
        if (StringUtils.isBlank(globalJsonPath)) {
            return GlobalVariableManager.defaultOne();
        } else {
            File file = new File(globalJsonPath);
            if (!file.exists() || file.isDirectory()) {
                throw new RuntimeException("请检查全局变量配置文件的路径: " + globalJsonPath);
            }

            String fileStr = FileUtils.readFileToString(file);
            return objectMapper.readValue(fileStr, GlobalVariableManager.class);
        }
    }

    @Bean
    public EnvironmentVariableManager envVariableManager(ExporterPathProperties pathProperties, ObjectMapper objectMapper) throws Exception {
        final String envJsonPath = pathProperties.getEnvironment();
        if (StringUtils.isBlank(envJsonPath)) {
            return EnvironmentVariableManager.defaultOne();
        } else {
            File file = new File(envJsonPath);
            if (!file.exists() || file.isDirectory()) {
                throw new RuntimeException("请检查环境变量配置文件的路径: " + envJsonPath);
            }

            String fileStr = FileUtils.readFileToString(file);
            return objectMapper.readValue(fileStr, EnvironmentVariableManager.class);
        }
    }

    @Bean
    public CollectionManager collectionManager(ExporterPathProperties pathProperties,
                                               ObjectMapper objectMapper,
                                               GlobalVariableManager globalVariableManager,
                                               EnvironmentVariableManager envVariableManager) throws Exception {
        final String collectionJsonPath = pathProperties.getCollection();
        if (StringUtils.isBlank(collectionJsonPath)) {
            throw new RuntimeException("必须配置集合配置");
        }

        File file = new File(collectionJsonPath);
        if (!file.exists() && !file.isFile()) {
            throw new RuntimeException("请检查集合配置文件的路径: " + collectionJsonPath);
        }

        String fileStr = FileUtils.readFileToString(file);

        Matcher matcher = argumentPattern.matcher(fileStr);
        Set<String> templateSet = new HashSet<>();
        while (matcher.find()) {
            String group = matcher.group();
            templateSet.add(group);
        }

        if (templateSet.isEmpty()) {
            return objectMapper.readValue(fileStr, CollectionManager.class);
        }

        Map<String, Variable> mergedMap = new HashMap<>();
        if (Objects.nonNull(envVariableManager) &&
                CollectionUtils.isNotEmpty(envVariableManager.getVariable())) {
            List<EnvironmentVariable> variableList = envVariableManager.getVariable();
            for (EnvironmentVariable variable : variableList) {
                if (Objects.nonNull(variable)) {
                    mergedMap.put(variable.getKey(), variable);
                }
            }
        }

        if (Objects.nonNull(globalVariableManager) &&
                CollectionUtils.isNotEmpty(globalVariableManager.getVariable())) {
            List<GlobalVariable> variableList = globalVariableManager.getVariable();
            for (GlobalVariable variable : variableList) {
                if (Objects.nonNull(variable)) {
                    mergedMap.put(variable.getKey(), variable);
                }
            }
        }

        for (String template : templateSet) {
            if (!mergedMap.containsKey(template)) {
                throw new RuntimeException("环境变量和全局变量中未找到变量：" + template);
            }

            Variable variable = MapUtils.getObject(mergedMap, template);
            if (Objects.isNull(variable) || StringUtils.isBlank(variable.getValue())) {
                throw new RuntimeException("环境变量和全局变量中变量的值为空：" + template);
            }

            if (!StringUtils.equalsIgnoreCase(template, "token_id")) {
                String placeHolder = START_PLACE_HOLDER + template + END_PLACE_HOLDER;
                fileStr = StringUtils.replace(fileStr, placeHolder, variable.getValue());
            }
        }

        CollectionManager collectionManager = objectMapper.readValue(fileStr, CollectionManager.class);
        loadExpectedTestResult(collectionManager);
        return collectionManager;
    }

    private void loadExpectedTestResult(CollectionManager collectionManager) {
        List<CollectionItem> collectionItemList = collectionManager.getItem();
        for (CollectionItem collectionItem : collectionItemList) {
            List<CollectionItem.CollectionItemDetail> itemDetails = collectionItem.getItemDetails();
            for (CollectionItem.CollectionItemDetail itemDetail : itemDetails) {
                List<CollectionItemEvent> events = itemDetail.getEvents();
                for (CollectionItemEvent event : events) {
                    CollectionItemEvent.CollectionItemEventScript eventScript = event.getEventScript();

                    List<String> execList = eventScript.getExec();
                    Map<String, Object> execStrMap = eventScript.getExecStrMap();

                    for (String exec : execList) {
                        if (StringUtils.contains(exec, "tests")) {
                            Matcher resultMatcher = resultPattern.matcher(exec);
                            Matcher propertyMatcher = propertyPattern.matcher(exec);
                            if (propertyMatcher.find() && resultMatcher.find()) {
                                String expectedProperty = propertyMatcher.group().trim();
                                if (StringUtils.contains(expectedProperty, "['")) {
                                    expectedProperty = StringUtils.replace(expectedProperty, "['", ".");
                                }

                                if (StringUtils.contains(expectedProperty, "']")) {
                                    expectedProperty = StringUtils.replace(expectedProperty, "']", "");
                                }

                                String expectedResult = resultMatcher.group().trim();
                                execStrMap.put(expectedProperty, StringUtils.replace(expectedResult,
                                        "\"",
                                        "").trim());
                            }
                        }
                    }
                }
            }
        }
    }

}
