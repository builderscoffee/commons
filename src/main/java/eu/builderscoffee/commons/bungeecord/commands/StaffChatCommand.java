package eu.builderscoffee.commons.bungeecord.commands;

import eu.builderscoffee.commons.bungeecord.Main;
import eu.builderscoffee.commons.bungeecord.utils.TextComponentUtil;
import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffChatCommand extends Command {

    public StaffChatCommand() {
        super("staffchat", Main.getInstance().getPermissions().getStaffChatPermission(), "sc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission(Main.getInstance().getPermissions().getStaffChatPermission())
                || sender.hasPermission(Main.getInstance().getPermissions().getGlobalPermission())){
            if(args.length > 0){
                // Get message to String
                String message = "";
                for (String l : args) {
                    message += l + " ";
                }

                // Replace chatFormat
                String line = Main.getInstance().getMessages().getStaffChatFormat()
                        .replace("%player%", sender.getName())
                        .replace("%message%", message)
                        .replace("&", "§");

                // Send message to staff & Console
                ProxyServer.getInstance().getPlayers().stream()
                        .filter(player -> player.hasPermission(Main.getInstance().getPermissions().getStaffChatPermission())
                                || player.hasPermission(Main.getInstance().getPermissions().getGlobalPermission()))
                        .forEach(player -> {
                            player.sendMessage(TextComponentUtil.decodeColor(line));
                        });
                System.out.println(line);
            }
            else {
                if(!(sender instanceof ProxiedPlayer)){
                    sender.sendMessage(TextComponentUtil.decodeColor("§cVous devez être un joueur pour activer le staffchat.\n" +
                            "§cAlternative: /staffchat <message>"));
                    return;
                }

                ProxiedPlayer player = (ProxiedPlayer) sender;
                val list = Main.getInstance().getStaffChatPlayers();

                if(list.contains(player.getUniqueId())){
                    // Remove player
                    list.remove(player.getUniqueId());
                    sender.sendMessage(TextComponentUtil.decodeColor(Main.getInstance().getMessages().getStaffChatActive()));
                }
                else{
                    // Add player
                    list.add(player.getUniqueId());
                    sender.sendMessage(TextComponentUtil.decodeColor(Main.getInstance().getMessages().getStaffChatDesactive()));
                }
            }
        }
        else{
            sender.sendMessage(TextComponentUtil.decodeColor(Main.getInstance().getMessages().getNoPermission().replace("&", "§")));
        }
    }
}
