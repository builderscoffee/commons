package eu.builderscoffee.commons.bukkit.listeners.redisson;

import eu.builderscoffee.api.common.redisson.listeners.PacketListener;
import eu.builderscoffee.api.common.redisson.listeners.ProcessPacket;
import eu.builderscoffee.commons.common.redisson.packets.StaffChatPacket;
import org.bukkit.Bukkit;

public class StaffChatListener implements PacketListener {

    @ProcessPacket
    public void onStaffChatPacket(StaffChatPacket packet){
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("builderscoffee.staffchat"))
                .forEach(player -> {
                    player.sendMessage(packet.getMessage());
                    Bukkit.getConsoleSender().sendMessage(packet.getMessage());
                });
    }
}
