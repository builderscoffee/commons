package eu.builderscoffee.commons.bukkit.configuration.messages;

import lombok.Data;

/**
 * This class stores quit messages configuration
 */
@Data
public class Quit {

    String message = "&8[&2-&8] &7%player% ";
    int weight = 750;
}
