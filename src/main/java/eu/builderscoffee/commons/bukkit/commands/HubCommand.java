package eu.builderscoffee.commons.bukkit.commands;

import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bukkit.utils.BungeeUtils;
import eu.builderscoffee.commons.bukkit.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This command allows to navigate back to the main hub server
 */
public class HubCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            // TODO Make more generic to dynamic hub servers
            BungeeUtils.sendPlayerToServer(Main.getInstance(), (Player) sender, "hub");
            return true;
        }

        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getMustBePlayer());
        return true;
    }
}
