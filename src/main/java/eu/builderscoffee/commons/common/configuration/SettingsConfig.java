package eu.builderscoffee.commons.common.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import eu.builderscoffee.commons.common.configuration.settings.MySQLConfig;
import eu.builderscoffee.commons.common.configuration.settings.RedisConfig;
import lombok.Data;

@Data
@Configuration("settings")
public class SettingsConfig {

    private PluginMode pluginMode = PluginMode.DEVELOPMENT;
    private MySQLConfig mySQL = new MySQLConfig();
    private RedisConfig redis = new RedisConfig();

    public enum PluginMode{
        DEVELOPMENT,
        PRODUCTION
    }
}
