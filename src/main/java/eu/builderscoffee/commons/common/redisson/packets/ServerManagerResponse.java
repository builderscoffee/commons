package eu.builderscoffee.commons.common.redisson.packets;

import eu.builderscoffee.api.common.redisson.packets.types.RequestPacket;
import eu.builderscoffee.api.common.redisson.packets.types.ResponsePacket;
import eu.builderscoffee.commons.common.utils.Quadlet;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import reactor.util.function.Tuple4;

import java.util.ArrayList;

@Getter
@Setter
public class ServerManagerResponse extends ResponsePacket {

    protected String title;
    protected ArrayList<Quadlet<Integer, Integer, ItemStack, String>> items = new ArrayList<>();

    public ServerManagerResponse(String packetId) {
        super(packetId);
    }

    public ServerManagerResponse(RequestPacket requestPacket) {
        super(requestPacket);
    }

    public void addItem(int row, int column, ItemStack item, String action){
        items.add(new Quadlet(row, column, item, action));
    }
}
