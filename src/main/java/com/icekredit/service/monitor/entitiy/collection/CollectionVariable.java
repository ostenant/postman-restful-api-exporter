package com.icekredit.service.monitor.entitiy.collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.icekredit.service.monitor.entitiy.Variable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CollectionVariable extends Variable {

    @Override
    public String toString() {
        return "CollectionVariable{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", enabled=" + enabled +
                ", type='" + type + '\'' +
                '}';
    }
}
