package eu.builderscoffee.commons.bukkit.commands;

import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bukkit.utils.BungeeUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            BungeeUtils.sendPlayerToServer(main, player, "hub");

            return true;
        }

        sender.sendMessage(Main.getInstance().getMessages().getCommandMustBePlayer());
        return true;
    }
}
