package org.ostenant.service.monitor.entitiy;

import java.util.List;
import java.util.Map;

public class MapBean {

    private Map<String, Object> valueMap;
    private Object valueObject;
    private List<String> prefixList;

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }

    public Object getValueObject() {
        return valueObject;
    }

    public void setValueObject(Object valueObject) {
        this.valueObject = valueObject;
    }

    public List<String> getPrefixList() {
        return prefixList;
    }

    public void setPrefixList(List<String> prefixList) {
        this.prefixList = prefixList;
    }

    @Override
    public String toString() {
        return "MapBean{" +
                "valueMap=" + valueMap +
                ", valueObject=" + valueObject +
                ", prefixList=" + prefixList +
                '}';
    }
}
