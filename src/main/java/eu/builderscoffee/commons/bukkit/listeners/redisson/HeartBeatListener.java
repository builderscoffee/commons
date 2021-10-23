package eu.builderscoffee.commons.bukkit.listeners.redisson;

import eu.builderscoffee.api.common.events.EventHandler;
import eu.builderscoffee.api.common.events.events.HeartBeatEvent;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.api.common.redisson.listeners.PacketListener;
import eu.builderscoffee.api.common.redisson.listeners.ProcessPacket;
import eu.builderscoffee.api.common.redisson.packets.types.common.HeartBeatPacket;
import eu.builderscoffee.commons.bukkit.Main;
import lombok.val;
import org.bukkit.Bukkit;
import org.redisson.api.RSortedSet;

public class HeartBeatListener implements PacketListener {

    @ProcessPacket
    public void onHeartBeat(HeartBeatPacket packet){
        val server = new Server();
        server.setHostName(Bukkit.getServerName());
        server.setHostAddress(Bukkit.getIp());
        server.setHostPort(Bukkit.getPort());
        server.setServerType(Server.ServerType.SPIGOT);
        server.setStartingMethod(Main.getInstance().getSettings().getStartingMethod());
        server.setPlayerCount(Bukkit.getOnlinePlayers().size());
        server.setPlayerMaximum(Bukkit.getMaxPlayers());

        val event = new HeartBeatEvent(server);
        EventHandler.getInstance().callEvent(event);

        if (event.isCanceled()) return;

        final RSortedSet<Server> servers = Redis.getRedissonClient().getSortedSet("servers");
        if(servers == null) return;
        if(servers.contains(event.getServer())) servers.remove(event.getServer());
        servers.add(event.getServer());
    }
}
