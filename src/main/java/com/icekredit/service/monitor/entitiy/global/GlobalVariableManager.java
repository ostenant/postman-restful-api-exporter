package com.icekredit.service.monitor.entitiy.global;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GlobalVariableManager implements Serializable {

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

    @JsonProperty("values")
    private List<GlobalVariable> variable = new ArrayList<>();

    private boolean isFound = true;

    private static final GlobalVariableManager DEFAULT = new GlobalVariableManager(false);

    public GlobalVariableManager() {
    }

    public GlobalVariableManager(boolean isFound) {
        this.isFound = isFound;
    }

    public static GlobalVariableManager defaultOne() {
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

    public List<GlobalVariable> getVariable() {
        return variable;
    }

    public void setVariable(List<GlobalVariable> variable) {
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
        return "GlobalVariableManager{" +
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
