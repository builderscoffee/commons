package eu.builderscoffee.commons.spigot.commands;

import eu.builderscoffee.commons.spigot.Main;
import eu.builderscoffee.commons.common.data.*;
import eu.builderscoffee.commons.spigot.inventory.ProfilInventory;
import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand  implements CommandExecutor {

    public static boolean argLength0(Player player) {
        openProfile(player, player.getName());
        return true;
    }

    public static boolean argLength1(Player player, String arg0) {
        openProfile(player, arg0);
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
                case 1:
                    ret = argLength1(player, args[0]);
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

    private static boolean openProfile(Player player, String targetName){
        val storeProfil = Main.getInstance().getProfilStore();
        val profilEntity = storeProfil.select(ProfilEntity.class)
                .where(ProfilEntity.NAME.lower().like(targetName.toLowerCase() + "%"))
                .get().firstOrNull();
        if(profilEntity != null)
        {
            new ProfilInventory(profilEntity).INVENTORY.open(player);
            return true;
        }
        player.sendMessage("§cCe joueur n'existe pas");
        return false;
    }
}