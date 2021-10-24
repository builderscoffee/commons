package eu.builderscoffee.commons.bungeecord.listeners.redisson;

import eu.builderscoffee.api.common.events.EventHandler;
import eu.builderscoffee.api.common.events.events.HeartBeatEvent;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.api.common.redisson.listeners.PacketListener;
import eu.builderscoffee.api.common.redisson.listeners.ProcessPacket;
import eu.builderscoffee.api.common.redisson.packets.types.common.HeartBeatPacket;
import eu.builderscoffee.commons.bungeecord.Main;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import org.redisson.api.RSortedSet;

/**
 * This class is used to catch {@link HeartBeatPacket} and add the heartbeat into the servers list
 */
public class HeartBeatListener implements PacketListener {

    @ProcessPacket
    public void onHeartBeat(HeartBeatPacket packet){
        // Create server data container
        val serverInfo = new Server();
        serverInfo.setHostName(Main.getInstance().getSettings().getName());
        Main.getInstance().getProxy().getConfig().getListeners().forEach(li-> {
            serverInfo.setHostAddress(li.getHost().getHostName());
            serverInfo.setHostPort(li.getQueryPort());
        });
        serverInfo.setServerType(Server.ServerType.BUNGEECORD);
        serverInfo.setStartingMethod(Main.getInstance().getSettings().getStartingMethod());
        serverInfo.setPlayerCount(ProxyServer.getInstance().getPlayers().size());
        serverInfo.setPlayerMaximum(ProxyServer.getInstance().getConfig().getPlayerLimit());

        // Create an event for eventually be modified by another sub-plugin
        val event = new HeartBeatEvent(serverInfo);
        EventHandler.getInstance().callEvent(event);

        // Stop if canceled
        if (event.isCanceled()) return;

        // Add server to servers list
        final RSortedSet<Server> servers = Redis.getRedissonClient().getSortedSet("servers");
        if(servers != null) {
            servers.add(event.getServer());
        }
    }
}
