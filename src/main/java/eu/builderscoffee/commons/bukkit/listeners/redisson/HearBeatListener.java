package eu.builderscoffee.commons.bukkit.listeners.redisson;

import eu.builderscoffee.api.common.redisson.events.HeartBeatEventHandler;
import eu.builderscoffee.api.common.redisson.listeners.PubSubListener;
import eu.builderscoffee.api.common.redisson.packets.Packet;
import eu.builderscoffee.api.common.redisson.packets.types.common.HeartBeatPacket;
import eu.builderscoffee.api.common.redisson.serverinfos.Server;
import lombok.val;
import org.bukkit.Bukkit;

public class HearBeatListener implements PubSubListener {
    @Override
    public void onMessage(String json) {
        // Déserialisation du message
        val temp = Packet.deserialize(json);

        if(temp instanceof HeartBeatPacket){
            val serverInfo = new Server();
            serverInfo.setHostName(Bukkit.getServerName());
            serverInfo.setHostAddress(Bukkit.getIp());
            serverInfo.setHostPort(Bukkit.getPort());
            serverInfo.setServerType(Server.ServerType.SPIGOT);
            serverInfo.setStartingMethod(Server.ServerStartingMethod.STATIC);
            serverInfo.setPlayerCount(Bukkit.getOnlinePlayers().size());
            serverInfo.setPlayerMaximum(Bukkit.getMaxPlayers());

            HeartBeatEventHandler.sendHeartBeatResponse(serverInfo);
        }
    }
}
