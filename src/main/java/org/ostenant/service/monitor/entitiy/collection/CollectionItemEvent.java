package org.ostenant.service.monitor.entitiy.collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CollectionItemEvent implements Serializable {

    @JsonProperty("listen")
    private String listen;

    @JsonProperty("script")
    private CollectionItemEventScript eventScript;

    public String getListen() {
        return listen;
    }

    public void setListen(String listen) {
        this.listen = listen;
    }

    public CollectionItemEventScript getEventScript() {
        return eventScript;
    }

    public void setEventScript(CollectionItemEventScript eventScript) {
        this.eventScript = eventScript;
    }

    @Override
    public String toString() {
        return "CollectionItemEvent{" +
                "listen='" + listen + '\'' +
                ", eventScript=" + eventScript +
                '}';
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class CollectionItemEventScript implements Serializable {

        @JsonProperty("type")
        private String type;

        @JsonProperty("exec")
        private List<String> exec;

        @JsonIgnore
        private Map<String, Object> execStrMap = new HashMap<>();

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getExec() {
            return exec;
        }

        public void setExec(List<String> exec) {
            this.exec = exec;
        }

        public Map<String, Object> getExecStrMap() {
            return execStrMap;
        }

        public void setExecStrMap(Map<String, Object> execStrMap) {
            this.execStrMap = execStrMap;
        }

        @Override
        public String toString() {
            return "CollectionItemEventScript{" +
                    "type='" + type + '\'' +
                    ", exec=" + exec +
                    ", execStrMap=" + execStrMap +
                    '}';
        }
    }
}
