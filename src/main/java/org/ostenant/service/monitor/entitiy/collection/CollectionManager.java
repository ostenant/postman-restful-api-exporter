package org.ostenant.service.monitor.entitiy.collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CollectionManager implements Serializable {

    @JsonProperty("variables")
    private List<CollectionVariable> variables = new ArrayList<>();

    @JsonProperty("info")
    private CollectionInfo info;

    @JsonProperty("item")
    private List<CollectionItem> item = new ArrayList<>();

    public List<CollectionVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<CollectionVariable> variables) {
        this.variables = variables;
    }

    public CollectionInfo getInfo() {
        return info;
    }

    public void setInfo(CollectionInfo info) {
        this.info = info;
    }

    public List<CollectionItem> getItem() {
        return item;
    }

    public void setItem(List<CollectionItem> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "CollectionManager{" +
                "variables=" + variables +
                ", info=" + info +
                ", item=" + item +
                '}';
    }
}
