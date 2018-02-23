package com.icekredit.service.monitor.entitiy.environment;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.icekredit.service.monitor.entitiy.Variable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EnvironmentVariable extends Variable {

    @Override
    public String toString() {
        return "EnvironmentVariable{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", enabled=" + enabled +
                ", type='" + type + '\'' +
                '}';
    }
}
