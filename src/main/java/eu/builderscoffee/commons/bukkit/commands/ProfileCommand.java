package eu.builderscoffee.commons.bukkit.commands;

import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bukkit.inventory.profile.ProfilInventory;
import eu.builderscoffee.commons.common.data.DataManager;
import eu.builderscoffee.commons.common.data.tables.ProfilEntity;
import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand implements CommandExecutor {

    private static boolean openProfile(Player player, String targetName) {
        val profilEntity = DataManager.getProfilStore()
                .select(ProfilEntity.class)
                .where(ProfilEntity.NAME.lower().like(targetName.toLowerCase() + "%"))
                .get().firstOrNull();
        if (profilEntity != null) {
            new ProfilInventory(profilEntity).INVENTORY.open(player);
            return true;
        }
        player.sendMessage("§cCe joueur n'existe pas");
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            openProfile(player, args.length == 1 ? args[0] : player.getName());
            return true;
        }

        sender.sendMessage(Main.getInstance().getMessages().getCommandMustBePlayer());
        return true;
    }
}