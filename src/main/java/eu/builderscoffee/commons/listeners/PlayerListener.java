package eu.builderscoffee.commons.listeners;

import eu.builderscoffee.api.utils.HeaderAndFooter;
import eu.builderscoffee.commons.Main;
import lombok.val;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        String prefix = "";
        String suffix = "";

        if (Main.getInstance().getLuckyPerms() != null) {
            QueryOptions queryOptions = Main.getInstance().getLuckyPerms().getContextManager().getQueryOptions(player);
            val primaryGroup = Objects.requireNonNull(Main.getInstance().getLuckyPerms().getUserManager().getUser(player.getName())).getPrimaryGroup();
            val cachedMetaData = Objects.requireNonNull(Main.getInstance().getLuckyPerms().getGroupManager().getGroup(primaryGroup)).getCachedData().getMetaData(queryOptions);
            int weight = Objects.requireNonNull(Main.getInstance().getLuckyPerms().getGroupManager().getGroup(primaryGroup)).getWeight().getAsInt();
            prefix = cachedMetaData.getPrefix() != null ? cachedMetaData.getPrefix() : "";
            suffix = cachedMetaData.getSuffix() != null ? cachedMetaData.getSuffix() : "";

            String teamName = player.getName().length() > 13? player.getName().substring(0, 13) : player.getName();
            for(int i = 0; i < 3 - Math.ceil(Math.log(weight <= 1? 2 : weight)); i++) {
                teamName = 0 + teamName;
            }
            teamName = weight + teamName;

            player.setPlayerListName(prefix.replace("&", "§") + player.getName() + suffix.replace("&", "§"));

            for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
                Scoreboard scoreboard = loopPlayer.getScoreboard();

                val team = scoreboard.getTeam(teamName) == null ? scoreboard.registerNewTeam(teamName) : scoreboard.getTeam(teamName);

                team.addPlayer(player);
            }
        }

        event.setJoinMessage(Main.getInstance().getMessages().getOnJoinMessage().replace("%player%", player.getName())
                                                                                .replace("%prefix%", prefix)
                                                                                .replace("%suffix%", suffix)
                                                                                .replace("&", "§"));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        event.setQuitMessage(Main.getInstance().getMessages().getOnQuitMessage().replace("&", "§")
                .replace("%player%", player.getName()));

        player.getScoreboard().getTeams().forEach(team -> {
            team.unregister();
        });
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage();
        String prefix = "";
        String suffix = "";

        if (Main.getInstance().getLuckyPerms() != null) {
            QueryOptions queryOptions = Main.getInstance().getLuckyPerms().getContextManager().getQueryOptions(player);
            val primaryGroup = Objects.requireNonNull(Main.getInstance().getLuckyPerms().getUserManager().getUser(player.getName())).getPrimaryGroup();
            val cachedMetaData = Objects.requireNonNull(Main.getInstance().getLuckyPerms().getGroupManager().getGroup(primaryGroup)).getCachedData().getMetaData(queryOptions);
            prefix = cachedMetaData.getPrefix() != null ? cachedMetaData.getPrefix() : "";
            suffix = cachedMetaData.getSuffix() != null ? cachedMetaData.getSuffix() : "";
        }

        event.setCancelled(true);

        Bukkit.broadcastMessage(Main.getInstance().getMessages().getChatFormatMessage().replace("%player%", player.getName())
                                                                                        .replace("%prefix%", prefix)
                                                                                        .replace("%suffix%", suffix)
                                                                                        .replace("%message%", message)
                                                                                        .replace("&", "§"));
    }
}
