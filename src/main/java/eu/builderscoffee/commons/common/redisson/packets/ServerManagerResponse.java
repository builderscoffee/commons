package eu.builderscoffee.commons.common.redisson.packets;

import eu.builderscoffee.api.bukkit.utils.serializations.SingleItemSerialization;
import eu.builderscoffee.api.common.redisson.packets.types.RequestPacket;
import eu.builderscoffee.api.common.redisson.packets.types.ResponsePacket;
import eu.builderscoffee.commons.common.utils.Quadlet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * This class is used to send the available configuration as response to a {@link ServerManagerRequest}
 */
@Getter
@Setter
public class ServerManagerResponse extends ResponsePacket {

    protected String title;
    @Setter(AccessLevel.NONE)
    protected ArrayList<Quadlet<Integer, Integer, String, String>> items = new ArrayList<>();
    protected boolean finished = false;

    public ServerManagerResponse(String packetId) {
        super(packetId);
    }

    public ServerManagerResponse(RequestPacket requestPacket) {
        super(requestPacket);
    }

    public void addItem(int row, int column, ItemStack item, String action){
        items.add(new Quadlet(row, column, SingleItemSerialization.serializeItemAsString(item), action));
    }
}
