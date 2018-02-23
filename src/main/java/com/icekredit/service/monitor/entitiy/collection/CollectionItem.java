package com.icekredit.service.monitor.entitiy.collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CollectionItem implements Serializable {

    @JsonIgnore
    public static final String KEY = "key";

    @JsonIgnore
    public static final String VALUE = "value";

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("item")
    private List<CollectionItemDetail> itemDetails = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CollectionItemDetail> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(List<CollectionItemDetail> itemDetails) {
        this.itemDetails = itemDetails;
    }

    @Override
    public String toString() {
        return "CollectionItem{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", itemDetails=" + itemDetails +
                '}';
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class CollectionItemDetail implements Serializable {

        @JsonProperty("name")
        private String name;

        @JsonProperty("event")
        private List<CollectionItemEvent> events = new ArrayList<>();

        @JsonProperty("request")
        private CollectionItemRequest request;

        @JsonProperty("response")
        private List<String> response;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CollectionItemEvent> getEvents() {
            return events;
        }

        public void setEvents(List<CollectionItemEvent> events) {
            this.events = events;
        }

        public CollectionItemRequest getRequest() {
            return request;
        }

        public void setRequest(CollectionItemRequest request) {
            this.request = request;
        }

        public List<String> getResponse() {
            return response;
        }

        public void setResponse(List<String> response) {
            this.response = response;
        }

        @Override
        public String toString() {
            return "CollectionItemDetail{" +
                    "name='" + name + '\'' +
                    ", events=" + events +
                    ", request=" + request +
                    ", response=" + response +
                    '}';
        }
    }
}
