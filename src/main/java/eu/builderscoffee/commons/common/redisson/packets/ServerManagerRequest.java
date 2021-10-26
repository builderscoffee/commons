package eu.builderscoffee.commons.common.redisson.packets;

import eu.builderscoffee.api.common.redisson.packets.types.RequestPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * This class is used to send a request to a specific server which will response by its available configuration
 */
@Getter
@Setter
public class ServerManagerRequest extends RequestPacket<ServerManagerResponse> {

    private String type;
    private String data;
}
