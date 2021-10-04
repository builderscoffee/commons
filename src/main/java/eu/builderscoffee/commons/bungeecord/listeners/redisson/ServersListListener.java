package eu.builderscoffee.commons.bungeecord.listeners.redisson;

import eu.builderscoffee.api.common.redisson.listeners.PubSubListener;
import eu.builderscoffee.api.common.redisson.packets.Packet;
import eu.builderscoffee.api.common.redisson.packets.types.common.BungeecordPacket;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;

import java.net.InetSocketAddress;

public class ServersListListener implements PubSubListener {
    @Override
    public void onMessage(String json) {
        val temp = Packet.deserialize(json);

        if(!(temp instanceof BungeecordPacket)) return;
        val bp = (BungeecordPacket) temp;

        if(bp.getServerStatus() == BungeecordPacket.ServerStatus.STARTED){
            val si = ProxyServer.getInstance().constructServerInfo(bp.getHostName(), new InetSocketAddress(bp.getHostAddress(), bp.getHostPort()), "", false);
            ProxyServer.getInstance().getServers().put(si.getName(), si);
        }
        else {
            ProxyServer.getInstance().getServers().remove(bp.getHostName());
        }
    }
}
