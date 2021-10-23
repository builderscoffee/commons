package eu.builderscoffee.commons.common.configuration;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.commons.common.configuration.settings.MySQLConfig;
import eu.builderscoffee.commons.common.configuration.settings.RedisConfig;
import lombok.Data;

@Data
@Configuration("settings")
public class SettingsConfig {

    private Server.ServerStartingMethod startingMethod = Server.ServerStartingMethod.STATIC;
    private PluginMode pluginMode = PluginMode.DEVELOPMENT;
    private LoadMode loadMode = LoadMode.NORMAL;
    private MySQLConfig mySQL = new MySQLConfig();
    private RedisConfig redis = new RedisConfig();

    public enum PluginMode{
        @JsonEnumDefaultValue
        DEVELOPMENT,
        PRODUCTION
    }

    public enum LoadMode{
        @JsonEnumDefaultValue
        NORMAL,
        LAZY
    }
}
