package eu.builderscoffee.commons.bungeecord.listeners.redisson;

import eu.builderscoffee.api.common.events.EventHandler;
import eu.builderscoffee.api.common.events.events.HeartBeatEvent;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.api.common.redisson.listeners.PacketListener;
import eu.builderscoffee.api.common.redisson.listeners.ProcessPacket;
import eu.builderscoffee.api.common.redisson.packets.types.common.HeartBeatPacket;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import org.redisson.api.RSortedSet;

public class HeartBeatListener implements PacketListener {

    @ProcessPacket
    public void onHeartBeat(HeartBeatPacket packet){
        val serverInfo = new Server();
        serverInfo.setHostName(ProxyServer.getInstance().getName());
        // TODO Avoir l'ip et le port du bungee
        serverInfo.setHostAddress("");
        serverInfo.setHostPort(0);
        serverInfo.setServerType(Server.ServerType.BUNGEECORD);
        serverInfo.setStartingMethod(Server.ServerStartingMethod.STATIC);
        serverInfo.setPlayerCount(ProxyServer.getInstance().getPlayers().size());
        serverInfo.setPlayerMaximum(ProxyServer.getInstance().getConfig().getPlayerLimit());

        val event = new HeartBeatEvent(serverInfo);
        EventHandler.getInstance().callEvent(event);

        if (event.isCanceled()) return;

        final RSortedSet<Server> servers = Redis.getRedissonClient().getSortedSet("servers");
        if(servers != null) {
            servers.add(event.getServer());
        }
    }
}
