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
        val serverInfo = new Server();
        serverInfo.setHostName(Bukkit.getServerName());
        serverInfo.setHostAddress(Bukkit.getIp());
        serverInfo.setHostPort(Bukkit.getPort());
        serverInfo.setServerType(Server.ServerType.SPIGOT);
        serverInfo.setStartingMethod(Main.getInstance().getSettings().getStartingMethod());
        serverInfo.setPlayerCount(Bukkit.getOnlinePlayers().size());
        serverInfo.setPlayerMaximum(Bukkit.getMaxPlayers());

        val event = new HeartBeatEvent(serverInfo);
        EventHandler.getInstance().callEvent(event);

        if (event.isCanceled()) return;

        final RSortedSet<Server> servers = Redis.getRedissonClient().getSortedSet("servers");
        if(servers != null) {
            servers.add(event.getServer());
        }
    }
}
