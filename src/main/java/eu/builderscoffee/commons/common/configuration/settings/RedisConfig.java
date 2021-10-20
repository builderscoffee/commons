package eu.builderscoffee.commons.common.configuration.settings;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import lombok.Data;
import org.bukkit.Bukkit;

@Data
public class RedisConfig {

    private String clientName, ip, password = "";
    private int port = 6379;
}
