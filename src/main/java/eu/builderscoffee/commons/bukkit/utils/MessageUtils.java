package eu.builderscoffee.commons.bukkit.utils;

import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bukkit.configuration.MessageConfiguration;
import eu.builderscoffee.commons.common.data.tables.Profil;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@UtilityClass
public class MessageUtils {

    public static Profil.Lanugages getLang(Player player) {
        val profil = Main.getInstance().getProfilCache().get(player.getUniqueId().toString());
        return profil.getLang();
    }

    public static Profil.Lanugages getLang(UUID uuid) {
        val profil = Main.getInstance().getProfilCache().get(uuid.toString());
        return profil.getLang();
    }

    public static MessageConfiguration getMessageConfig(Player player) {
        return Main.getInstance().getMessages().get(getLang(player));
    }

    public static MessageConfiguration getMessageConfig(UUID uuid) {
        return Main.getInstance().getMessages().get(getLang(uuid));
    }

    public static MessageConfiguration getMessageConfig(CommandSender sender) {
        if (sender instanceof Player) {
            return getMessageConfig((Player) sender);
        }
        else{
            return getDefaultMessageConfig();
        }
    }

    public static MessageConfiguration getDefaultMessageConfig() {
        return Main.getInstance().getMessages().get(Profil.Lanugages.FR);
    }
}
