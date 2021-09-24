package eu.builderscoffee.commons.bukkit.commands;

import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.RedisTopic;
import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.common.redisson.packets.StaffChatPacket;
import eu.builderscoffee.commons.common.redisson.topics.CommonTopics;
import eu.builderscoffee.commons.common.utils.LuckPermsUtils;
import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if((sender instanceof Player && sender.hasPermission("builderscoffee.staffchat"))
                || sender instanceof ConsoleCommandSender){
            if(args.length > 0){
                String message = "";
                for (String arg : args) {
                    message += (message.isEmpty()? "" : " ") + arg;
                }
                val prefix = (sender instanceof Player)? LuckPermsUtils.getPrefixOrEmpty(((Player)sender).getUniqueId()) : "";
                val suffix = (sender instanceof Player)? LuckPermsUtils.getSuffixOrEmpty(((Player)sender).getUniqueId()) : "";
                val packet = new StaffChatPacket()
                        .setServerName(Main.getInstance().getRedissonConfig().getClientName())
                        .setPlayerName(sender.getName())
                        .setMessage(Main.getInstance().getMessages().getStaffChatFormatMessage()
                                .replace("%player%", sender.getName())
                                .replace("%prefix%", prefix)
                                .replace("%suffix%", suffix)
                                .replace("%message%", message)
                                .replace("&", "§"));
                Redis.getTopic(CommonTopics.STAFFCHAT).publish(packet.serialize());
            }
            else {
                if(sender instanceof Player){
                    val player = (Player) sender;
                    if(Main.getInstance().getStaffchatPlayers().contains(player.getUniqueId())){
                        player.sendMessage("§bVous avez désactivé le staffchat");
                        Main.getInstance().getStaffchatPlayers().remove(player.getUniqueId());
                    }
                    else {
                        player.sendMessage("§bVous avez activé le staffchat");
                        Main.getInstance().getStaffchatPlayers().add(player.getUniqueId());
                    }
                }
                else {
                    sender.sendMessage("§cUsage: /staffchat <message>");
                }
            }
            return true;
        }
        sender.sendMessage("§cVous n'avez pas la permission !");
        return true;
    }
}
