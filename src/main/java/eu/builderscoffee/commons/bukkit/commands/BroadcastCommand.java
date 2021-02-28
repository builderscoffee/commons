package eu.builderscoffee.commons.spigot.commands;

import eu.builderscoffee.commons.spigot.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("builderscoffee.broadcast")){
            String message = "";
            for (String arg : args) {
                message += arg + " ";
            }
            if(message.length() > 3){
                Bukkit.broadcastMessage(main.getMessages().getBroadcastFormatMessage().replace("%message%", message)
                        .replace("&", "§")
                );
                return false;
            }

            sender.sendMessage("§cLe message doit être plus long que 3 charactères !");
            return true;
        }

        sender.sendMessage("§cVous n'avez pas la permission !");
        return true;
    }
}
