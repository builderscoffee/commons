package eu.builderscoffee.commons.bungeecord.listeners.redisson;

import eu.builderscoffee.api.common.redisson.events.HeartBeatEventHandler;
import eu.builderscoffee.api.common.redisson.listeners.PubSubListener;
import eu.builderscoffee.api.common.redisson.packets.Packet;
import eu.builderscoffee.api.common.redisson.packets.types.common.HeartBeatPacket;
import eu.builderscoffee.api.common.redisson.serverinfos.Server;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;

public class HearBeatListener implements PubSubListener {
    @Override
    public void onMessage(String json) {
        // Déserialisation du message
        val temp = Packet.deserialize(json);

        if(temp instanceof HeartBeatPacket){
            val serverInfo = new Server();
            serverInfo.setHostName(ProxyServer.getInstance().getName());
            // TODO Avoir l'ip et le port du bungee
            serverInfo.setHostAddress("");
            serverInfo.setHostPort(0);
            serverInfo.setServerType(Server.ServerType.BUNGEECORD);
            serverInfo.setStartingMethod(Server.ServerStartingMethod.STATIC);
            serverInfo.setPlayerCount(ProxyServer.getInstance().getPlayers().size());
            serverInfo.setPlayerMaximum(ProxyServer.getInstance().getConfig().getPlayerLimit());

            HeartBeatEventHandler.sendHeartBeatResponse(serverInfo);
        }
    }
}
