package eu.builderscoffee.commons.common.configuration.settings;

import lombok.Data;

@Data
public class RedisConfig {

    private String clientName, ip, password = "";
    private int port = 6379;
}
