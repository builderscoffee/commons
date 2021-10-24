package eu.builderscoffee.commons.common.configuration.settings;

import lombok.Data;

/**
 * This class stores common Redis data configuration
 */
@Data
public class RedisConfig {

    private String clientName, ip, password = "";
    private int port = 6379;
}
