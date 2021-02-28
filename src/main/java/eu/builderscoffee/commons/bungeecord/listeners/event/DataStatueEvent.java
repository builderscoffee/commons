package eu.builderscoffee.commons.bungeecord.listeners.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.plugin.Event;
import org.bukkit.event.HandlerList;

public abstract class DataStatueEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Load extends DataStatueEvent {
        private final String uniqueId;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Save extends DataStatueEvent {
        private final String uniqueId;
    }
}
