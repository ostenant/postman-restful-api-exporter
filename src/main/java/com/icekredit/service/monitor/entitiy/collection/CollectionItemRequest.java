package com.icekredit.service.monitor.entitiy.collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CollectionItemRequest implements Serializable {

    @JsonProperty("url")
    private CollectionItemRequestUrl url;

    @JsonProperty("method")
    private String method;

    @JsonProperty("header")
    private List<Map<String, String>> header = new ArrayList<>();

    @JsonProperty("body")
    private CollectionItemRequestBody body;

    @JsonProperty("description")
    private String description;

    public CollectionItemRequestUrl getUrl() {
        return url;
    }

    public void setUrl(CollectionItemRequestUrl url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Map<String, String>> getHeader() {
        return header;
    }

    public void setHeader(List<Map<String, String>> header) {
        this.header = header;
    }

    public CollectionItemRequestBody getBody() {
        return body;
    }

    public void setBody(CollectionItemRequestBody body) {
        this.body = body;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CollectionItemRequest{" +
                "url=" + url +
                ", method='" + method + '\'' +
                ", header=" + header +
                ", body=" + body +
                ", description='" + description + '\'' +
                '}';
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class CollectionItemRequestUrl implements Serializable {

        @JsonProperty("raw")
        private String raw;

        @JsonProperty("host")
        private List<String> host;

        @JsonProperty("path")
        private List<String> path;

        @JsonProperty("query")
        private List<CollectionItemRequestUrlQuery> queries = new ArrayList<>();

        @JsonProperty("variable")
        private List<String> variable;

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public List<String> getHost() {
            return host;
        }

        public void setHost(List<String> host) {
            this.host = host;
        }

        public List<String> getPath() {
            return path;
        }

        public void setPath(List<String> path) {
            this.path = path;
        }

        public List<String> getVariable() {
            return variable;
        }

        public void setVariable(List<String> variable) {
            this.variable = variable;
        }

        public List<CollectionItemRequestUrlQuery> getQueries() {
            return queries;
        }

        public void setQueries(List<CollectionItemRequestUrlQuery> queries) {
            this.queries = queries;
        }

        @Override
        public String toString() {
            return "CollectionItemRequestUrl{" +
                    "raw='" + raw + '\'' +
                    ", host=" + host +
                    ", path=" + path +
                    ", queries=" + queries +
                    ", variable=" + variable +
                    '}';
        }

        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        public static class CollectionItemRequestUrlQuery implements Serializable {

            @JsonProperty("key")
            private String key;

            @JsonProperty("value")
            private String value;

            @JsonProperty("equals")
            private boolean equals;

            @JsonProperty("description")
            private String description;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public boolean isEquals() {
                return equals;
            }

            public void setEquals(boolean equals) {
                this.equals = equals;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            @Override
            public String toString() {
                return "CollectionItemRequestUrlQuery{" +
                        "key='" + key + '\'' +
                        ", value='" + value + '\'' +
                        ", equals=" + equals +
                        ", description='" + description + '\'' +
                        '}';
            }
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class CollectionItemRequestBody implements Serializable {

        @JsonProperty("mode")
        private String mode;

        @JsonProperty("raw")
        private String raw;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        @Override
        public String toString() {
            return "CollectionItemRequestBody{" +
                    "mode='" + mode + '\'' +
                    ", raw='" + raw + '\'' +
                    '}';
        }
    }

}
