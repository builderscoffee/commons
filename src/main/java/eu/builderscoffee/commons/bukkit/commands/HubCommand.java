package eu.builderscoffee.commons.bukkit.commands;

import eu.builderscoffee.commons.bukkit.CommonsBukkit;
import eu.builderscoffee.commons.bukkit.utils.BungeeUtils;
import eu.builderscoffee.commons.bukkit.utils.MessageUtils;
import eu.builderscoffee.commons.bungeecord.CommonsBungeeCord;
import eu.builderscoffee.commons.common.configuration.SettingsConfig;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
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
            // Get any hub server as backup server
            val anyHubServer = ProxyServer.getInstance().getServers().values().stream()
                    .filter(s -> s.getName().contains("hub"))
                    .findFirst().orElse(null);

            // Get the correct hub server depends on de server mode
            val server = ProxyServer.getInstance().getServers().values().stream()
                    .filter(s -> {
                        if(CommonsBungeeCord.getInstance().getSettings().getPluginMode().equals(SettingsConfig.PluginMode.DEVELOPMENT))
                            return s.getName().contains("hub") && s.getName().contains("dev");
                        return s.getName().contains("hub") && !s.getName().contains("dev");
                    })
                    .findFirst().orElse(anyHubServer);

            BungeeUtils.sendPlayerToServer(CommonsBukkit.getInstance(), (Player) sender, server.getName());
            return true;
        }

        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getMustBePlayer());
        return true;
    }
}
