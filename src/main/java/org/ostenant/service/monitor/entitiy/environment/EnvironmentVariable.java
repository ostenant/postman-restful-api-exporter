package org.ostenant.service.monitor.entitiy.environment;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.ostenant.service.monitor.entitiy.Variable;

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
