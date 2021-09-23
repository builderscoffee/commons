package eu.builderscoffee.commons.bukkit.listeners.redisson;

import eu.builderscoffee.api.common.redisson.listeners.PubSubListener;
import eu.builderscoffee.api.common.redisson.packets.Packet;
import eu.builderscoffee.commons.common.redisson.packets.StaffChatPacket;
import lombok.val;
import org.bukkit.Bukkit;

public class StaffChatListener implements PubSubListener {
    @Override
    public void onMessage(String s) {
        val packet = Packet.deserialize(s, StaffChatPacket.class);
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(player.hasPermission("builderscoffee.staffchat")){
                player.sendMessage(packet.getMessage());
                Bukkit.getConsoleSender().sendMessage(packet.getMessage());
            }
        });
    }
}
