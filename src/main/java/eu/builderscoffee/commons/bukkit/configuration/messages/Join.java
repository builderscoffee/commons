package eu.builderscoffee.commons.bukkit.configuration.messages;

import lombok.Data;

@Data
public class Join {

    String message = "&8[&2+&8] &7%prefix%%player%%suffix% ";
    int weight = 750;
}
