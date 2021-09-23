package eu.builderscoffee.commons.bukkit.commands;

import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bukkit.utils.BungeeUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            BungeeUtils.sendPlayerToServer(Main.getInstance(), (Player) sender, "hub");
            return true;
        }

        sender.sendMessage(Main.getInstance().getMessages().getCommandMustBePlayer());
        return true;
    }
}
