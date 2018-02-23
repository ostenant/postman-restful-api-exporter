package org.ostenant.service.monitor.entitiy.global;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.ostenant.service.monitor.entitiy.Variable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GlobalVariable extends Variable {

    @Override
    public String toString() {
        return "GlobalVariable{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", enabled=" + enabled +
                ", type='" + type + '\'' +
                '}';
    }
}
