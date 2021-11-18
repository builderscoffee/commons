package eu.builderscoffee.commons.bungeecord.listeners.redisson;

import eu.builderscoffee.api.common.data.DataManager;
import eu.builderscoffee.api.common.data.tables.ServerActivityEntity;
import eu.builderscoffee.api.common.events.EventHandler;
import eu.builderscoffee.api.common.events.events.HeartBeatEvent;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.api.common.redisson.listeners.PacketListener;
import eu.builderscoffee.api.common.redisson.listeners.ProcessPacket;
import eu.builderscoffee.api.common.redisson.packets.types.common.HeartBeatPacket;
import eu.builderscoffee.commons.bungeecord.CommonsBungeeCord;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import org.redisson.api.RSortedSet;

/**
 * This class is used to catch {@link HeartBeatPacket} and add the heartbeat into the servers list
 */
public class HeartBeatListener implements PacketListener {

    private Server.ServerStatus lastStatus = Server.ServerStatus.STARTING;

    @ProcessPacket
    public void onHeartBeat(HeartBeatPacket packet){
        // Create server data container
        val serverInfo = new Server();
        serverInfo.setHostName(CommonsBungeeCord.getInstance().getSettings().getName());
        CommonsBungeeCord.getInstance().getProxy().getConfig().getListeners().forEach(li-> {
            serverInfo.setHostAddress(li.getHost().getHostName());
            serverInfo.setHostPort(li.getQueryPort());
        });
        serverInfo.setServerType(Server.ServerType.BUNGEECORD);
        serverInfo.setServerStatus(Server.ServerStatus.RUNNING);
        serverInfo.setStartingMethod(CommonsBungeeCord.getInstance().getSettings().getStartingMethod());
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
            if(servers.stream().anyMatch(s -> s.getHostName().equals(event.getServer().getHostName()))) servers.remove(event.getServer());
            servers.add(event.getServer());
        }

        if(event.getServer().getServerStatus() != lastStatus){
            val entity = new ServerActivityEntity();
            entity.setServerName(event.getServer().getHostName());
            entity.setMessage("Server status of " + event.getServer().getHostName() + " changed from ''" + lastStatus + " to ''" + event.getServer().getServerStatus());
            DataManager.getServerActivityStore().insert(entity);
            lastStatus = event.getServer().getServerStatus();
        }
    }
}
