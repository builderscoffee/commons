package eu.builderscoffee.commons.bungeecord.listeners.redisson;

import eu.builderscoffee.api.common.redisson.events.HearBeatEventHandler;
import eu.builderscoffee.api.common.redisson.listeners.PubSubListener;
import eu.builderscoffee.api.common.redisson.packets.Packet;
import eu.builderscoffee.api.common.redisson.packets.types.common.HeartBeatPacket;
import eu.builderscoffee.api.common.redisson.serverinfos.ServerInfo;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;

public class HearBeatListener implements PubSubListener {
    @Override
    public void onMessage(String json) {
        // Déserialisation du message
        val temp = Packet.deserialize(json);

        if(temp instanceof HeartBeatPacket){
            val serverInfo = new ServerInfo();
            serverInfo.setHostName(ProxyServer.getInstance().getName());
            // TODO Avoir l'ip et le port du bungee
            serverInfo.setHostAddress("");
            serverInfo.setHostPort(0);
            serverInfo.setServerType(ServerInfo.ServerType.BUNGEECORD);
            serverInfo.setStartingMethod(ServerInfo.ServerStartingMethod.STATIC);

            HearBeatEventHandler.sendHeartBeatResponse(serverInfo);
        }
    }
}
