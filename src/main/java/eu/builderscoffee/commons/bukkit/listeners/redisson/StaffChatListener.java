package eu.builderscoffee.commons.bukkit.listeners.redisson;

import eu.builderscoffee.api.common.redisson.listeners.PubSubListener;
import eu.builderscoffee.api.common.redisson.packets.Packet;
import eu.builderscoffee.commons.common.redisson.packets.StaffChatPacket;
import lombok.val;
import org.bukkit.Bukkit;

public class StaffChatListener implements PubSubListener {
    @Override
    public void onMessage(String s) {
        // Déserialisation du message
        val packet = Packet.deserialize(s, StaffChatPacket.class);

        // Envoi du message aux joueurs ayant la permission
        Bukkit.getOnlinePlayers().stream()
            .filter(player -> player.hasPermission("builderscoffee.staffchat"))
            .forEach(player -> {
                player.sendMessage(packet.getMessage());
                Bukkit.getConsoleSender().sendMessage(packet.getMessage());
        });
    }
}
