package eu.builderscoffee.commons.bukkit.commands;

import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bukkit.inventory.NetworkInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NetworkCommands implements CommandExecutor {

    public static boolean argLength0(Player player) {
        NetworkInventory.INVENTORY.open(player);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            boolean ret = false;
            switch (args.length) {
                case 0:
                    ret = argLength0((Player) sender);
                    break;
                default:
                    break;
            }

            if (!ret) {
                sender.sendMessage(Main.getInstance().getMessages().getPrefix() + Main.getInstance().getMessages().getCommandBadSyntaxe());
            }

            return ret;
        }

        sender.sendMessage(Main.getInstance().getMessages().getCommandMustBePlayer());
        return true;
    }
}
