package eu.builderscoffee.commons.bukkit.listeners.redisson;

import eu.builderscoffee.api.common.events.EventHandler;
import eu.builderscoffee.api.common.events.events.HeartBeatEvent;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.api.common.redisson.listeners.PacketListener;
import eu.builderscoffee.api.common.redisson.listeners.ProcessPacket;
import eu.builderscoffee.api.common.redisson.packets.types.common.HeartBeatPacket;
import eu.builderscoffee.commons.bukkit.CommonsBukkit;
import lombok.val;
import org.bukkit.Bukkit;
import org.redisson.api.RSortedSet;

import java.util.Objects;

/**
 * This class is used to catch {@link HeartBeatPacket} and add the heartbeat into the servers list
 */
public class HeartBeatListener implements PacketListener {

    @ProcessPacket
    public void onHeartBeat(HeartBeatPacket packet){
        // Create server data container
        val server = new Server();
        server.setHostName(CommonsBukkit.getInstance().getSettings().getName());
        server.setHostAddress(Bukkit.getIp());
        server.setHostPort(Bukkit.getPort());
        server.setServerType(Server.ServerType.SPIGOT);
        server.setStartingMethod(CommonsBukkit.getInstance().getSettings().getStartingMethod());
        server.setPlayerCount(Bukkit.getOnlinePlayers().size());
        server.setPlayerMaximum(Bukkit.getMaxPlayers());

        // Create an event for eventually be modified by another sub-plugin
        val event = new HeartBeatEvent(server);
        EventHandler.getInstance().callEvent(event);

        // Stop if canceled
        if (event.isCanceled()) return;

        // Add server to servers list
        final RSortedSet<Server> servers = Redis.getRedissonClient().getSortedSet("servers");
        if(Objects.isNull(servers) || Objects.isNull(event) || Objects.isNull(event.getServer())) return;
        if(servers.stream().anyMatch(s -> s.getHostName().equals(event.getServer().getHostName()))) servers.remove(event.getServer());
        servers.add(event.getServer());
    }
}
