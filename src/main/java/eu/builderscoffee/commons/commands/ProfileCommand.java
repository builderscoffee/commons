package eu.builderscoffee.commons.commands;

import eu.builderscoffee.commons.Main;
import eu.builderscoffee.commons.inventory.ProfileInventory;
import eu.builderscoffee.commons.utils.Cache;
import lombok.Getter;
import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.Deque;
import java.util.Map;

public class ProfileCommand  implements CommandExecutor {

    @Getter
    private static Cache<Player, String> requestProfile = new Cache<>();

    public static boolean argLength0(Player player) {
        requestProfile.put(player, player.getName());
        ProfileInventory.INVENTORY.open(player);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            boolean ret = false;
            switch (args.length) {
                case 0:
                    ret = argLength0(player);
                    break;
                default:
                    break;
            }

            if (!ret) {
                player.sendMessage(Main.getInstance().getMessages().getPrefix() + Main.getInstance().getMessages().getCommandBadSyntaxe());
            }

            return ret;
        }

        sender.sendMessage(Main.getInstance().getMessages().getCommandMustBePlayer());
        return true;
    }
}