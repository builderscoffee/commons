package eu.builderscoffee.commons.listeners;

import eu.builderscoffee.commons.Main;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(Main.getInstance().getMessages().getOnJoinMessage().replace("&", "§")
                .replace("%player%", player.getName()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(Main.getInstance().getMessages().getOnQuitMessage().replace("&", "§")
                .replace("%player%", player.getName()));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String prefix = "";
        String suffix = "";

        if (Main.getInstance().getLuckyPerms() != null) {
            QueryOptions queryOptions = Main.getInstance().getLuckyPerms().getContextManager().getQueryOptions(player);
            String primaryGroup = Main.getInstance().getLuckyPerms().getUserManager().getUser(player.getName()).getPrimaryGroup();
            CachedMetaData cachedMetaData = Main.getInstance().getLuckyPerms().getGroupManager().getGroup(primaryGroup).getCachedData().getMetaData(queryOptions);
            prefix = cachedMetaData.getPrefix() != null ? cachedMetaData.getPrefix() : "";
            suffix = cachedMetaData.getSuffix() != null ? cachedMetaData.getSuffix() : "";
        }

        event.setCancelled(true);

        Bukkit.broadcastMessage(Main.getInstance().getMessages().getChatFormatMessage().replace("&", "§")
                .replace("%player%", player.getName())
                .replace("%prefix%", prefix)
                .replace("%suffix%", suffix)
                .replace("%message%", message));
    }
}
