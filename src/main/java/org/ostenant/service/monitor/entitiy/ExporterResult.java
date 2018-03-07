package org.ostenant.service.monitor.entitiy;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ExporterResult implements Serializable {
    public static final int OKAY = 200;
    public static final int FAILED = 400;

    private String uri;
    private Integer code;
    private final Map<String, String> expectedMap;
    private final Map<String, String> actualMap;

    public ExporterResult(String uri, Integer code) {
        this.uri = uri;
        this.code = code;
        this.expectedMap = new HashMap<>();
        this.actualMap = new HashMap<>();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Map<String, String> getExpectedMap() {
        return expectedMap;
    }

    public Map<String, String> getActualMap() {
        return actualMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExporterResult that = (ExporterResult) o;

        return uri.equals(that.uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(uri);
        JSONObject jsonObject = new JSONObject();
        if (expectedMap.size() > 0) {
            for (Map.Entry<String, String> entry : expectedMap.entrySet()) {
                jsonObject.put("expect[" + entry.getKey() + "]", entry.getValue());
            }
        }

        if (actualMap.size() > 0) {
            for (Map.Entry<String, String> entry : actualMap.entrySet()) {
                jsonObject.put("actual[" + entry.getKey() + "]", entry.getValue());
            }
        }

        if (jsonObject.size() > 0) {
            builder.append(jsonObject.toString());
        }

        builder.append(" ").append(code);
        return builder.toString();
    }
}
