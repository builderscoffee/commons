package eu.builderscoffee.commons.common.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import lombok.Data;
import org.bukkit.Bukkit;

@Data
@Configuration("redis")
public class RedisConfig {

    private String clientName = "test";
    private String ip = "54.36.124.50";
    private String password = "LbWKFC4gcTa5cepd2siPk2ifibVVGGfqDR2aPp7ZTVYF3gxvn9SwxJ5eFTXanxr2";
    private int port = 6379;



}
