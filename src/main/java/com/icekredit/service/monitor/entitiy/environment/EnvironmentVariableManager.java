package com.icekredit.service.monitor.entitiy.environment;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EnvironmentVariableManager implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("_postman_variable_scope")
    private String postmanVariableScope;

    @JsonProperty("_postman_exported_at")
    private String postmanExportedAt;

    @JsonProperty("_postman_exported_using")
    private String postmanExportedUsing;

    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("values")
    private List<EnvironmentVariable> variable = new ArrayList<>();

    @JsonIgnore
    private boolean isFound = true;

    private static final EnvironmentVariableManager DEFAULT = new EnvironmentVariableManager(false);

    public EnvironmentVariableManager() {
    }

    public EnvironmentVariableManager(boolean isFound) {
        this.isFound = isFound;
    }

    public static EnvironmentVariableManager defaultOne() {
        return DEFAULT;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostmanVariableScope() {
        return postmanVariableScope;
    }

    public void setPostmanVariableScope(String postmanVariableScope) {
        this.postmanVariableScope = postmanVariableScope;
    }

    public String getPostmanExportedAt() {
        return postmanExportedAt;
    }

    public void setPostmanExportedAt(String postmanExportedAt) {
        this.postmanExportedAt = postmanExportedAt;
    }

    public String getPostmanExportedUsing() {
        return postmanExportedUsing;
    }

    public void setPostmanExportedUsing(String postmanExportedUsing) {
        this.postmanExportedUsing = postmanExportedUsing;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<EnvironmentVariable> getVariable() {
        return variable;
    }

    public void setVariable(List<EnvironmentVariable> variable) {
        this.variable = variable;
    }

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }

    @Override
    public String toString() {
        return "EnvironmentVariableManager{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", postmanVariableScope='" + postmanVariableScope + '\'' +
                ", postmanExportedAt='" + postmanExportedAt + '\'' +
                ", postmanExportedUsing='" + postmanExportedUsing + '\'' +
                ", variable=" + variable +
                ", isFound=" + isFound +
                '}';
    }
}
