package eu.builderscoffee.commons.bungeecord.listeners.redisson;

import eu.builderscoffee.api.common.redisson.listeners.PacketListener;
import eu.builderscoffee.api.common.redisson.listeners.ProcessPacket;
import eu.builderscoffee.api.common.redisson.packets.types.common.BungeecordPacket;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;

import java.net.InetSocketAddress;

public class ServersListListener implements PacketListener {

    @ProcessPacket
    public void onBungeecordPacket(BungeecordPacket packet){
        if(packet.getServerStatus() == BungeecordPacket.ServerStatus.STARTED){
            System.out.println("Packet ip: " + packet.getHostAddress());
            System.out.println("Packet port: " + packet.getHostPort());
            val si = ProxyServer.getInstance().constructServerInfo(packet.getHostName(), new InetSocketAddress(packet.getHostAddress(), packet.getHostPort()), "", false);
            ProxyServer.getInstance().getServers().put(si.getName(), si);
            System.out.println("Ip: " + si.getAddress().getAddress());
            System.out.println("Port: " + si.getAddress().getPort());
        }
        else {
            ProxyServer.getInstance().getServers().remove(packet.getHostName());
        }
    }
}
