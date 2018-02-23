package org.ostenant.service.monitor.entitiy.collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CollectionInfo implements Serializable {

    @JsonProperty("name")
    private String name;

    @JsonProperty("_postman_id")
    private String postmanId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("schema")
    private String schema;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostmanId() {
        return postmanId;
    }

    public void setPostmanId(String postmanId) {
        this.postmanId = postmanId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "CollectionInfo{" +
                "name='" + name + '\'' +
                ", postmanId='" + postmanId + '\'' +
                ", description='" + description + '\'' +
                ", schema='" + schema + '\'' +
                '}';
    }
}
