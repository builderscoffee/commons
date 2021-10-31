package eu.builderscoffee.commons.common.redisson.packets;

import eu.builderscoffee.api.bukkit.utils.serializations.SingleItemSerialization;
import eu.builderscoffee.api.common.redisson.packets.types.RequestPacket;
import eu.builderscoffee.api.common.redisson.packets.types.ResponsePacket;
import eu.builderscoffee.commons.common.utils.Quadlet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * This class is used to send the available configuration as response to a {@link ServerManagerRequest}
 */
@Getter
@Setter
public class ServerManagerResponse extends ResponsePacket {

    private boolean finished = false;
    @Setter(AccessLevel.NONE)
    private Set<Action> actions = new HashSet<>();

    protected ServerManagerResponse(){
        super();
    }

    public ServerManagerResponse(String packetId) {
        super(packetId);
    }

    public ServerManagerResponse(RequestPacket requestPacket) {
        super(requestPacket);
    }

    public static abstract class Action{
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return getClass().equals(o.getClass());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getClass());
        }
    }

    @Getter
    public static class Items extends Action{
        @Setter private String type;
        private ArrayList<Quadlet<Integer, Integer, String, String>> items = new ArrayList<>();

        public void addItem(int row, int column, ItemStack item, String action){
            items.add(new Quadlet(row, column, SingleItemSerialization.serializeItemAsString(item), action));
        }
    }

    @Getter @Setter
    public static class ChatRequest extends Action {
        private String type;
        private String message;
    }

    @Getter @Setter
    public static class ChatResponse extends Action {
        private String message;
    }
}
