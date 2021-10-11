package eu.builderscoffee.commons.bukkit.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import lombok.Data;

@Data
@Configuration("permissions")
public final class PermissionsConfiguration {

    /* Network Inventory */
    String serverManagerSee = "builderscoffee.bukkit.servermanager.see";
}
