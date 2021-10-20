package eu.builderscoffee.commons.common.redisson.packets;

import eu.builderscoffee.api.common.redisson.packets.types.RequestPacket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerManagerRequest extends RequestPacket<ServerManagerResponse> {

    protected String action;
}
