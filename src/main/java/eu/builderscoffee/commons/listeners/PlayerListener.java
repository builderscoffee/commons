package eu.builderscoffee.commons.listeners;

import eu.builderscoffee.commons.Main;
import eu.builderscoffee.commons.utils.LuckPermsUtils;
import lombok.val;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        // Creating teams from LuckyPerms
        Bukkit.getOnlinePlayers().forEach(loopPlayer -> {
            loopPlayer.getScoreboard().getTeams().forEach(team -> team.unregister());
        });

        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            final String primaryGroup = LuckPermsUtils.getPrimaryGroup(loopPlayer);
            final int weight = Math.abs(1000 - LuckPermsUtils.getWeight(loopPlayer));

            String teamName = primaryGroup.length() > 13 ? weight + primaryGroup.substring(0, 13) : weight + primaryGroup;
            for (int i = 0; i < 3 - String.valueOf(weight).length(); i++) {
                teamName = "0" + teamName;
            }

            loopPlayer.setPlayerListName(LuckPermsUtils.getPrefix(loopPlayer).replace("&", "§") + loopPlayer.getName() + LuckPermsUtils.getSuffix(loopPlayer).replace("&", "§"));

            for(Player loopPlayer2: Bukkit.getOnlinePlayers()) {
                Scoreboard scoreboard = loopPlayer2.getScoreboard();
                val team = scoreboard.getTeam(teamName) == null ? scoreboard.registerNewTeam(teamName) : scoreboard.getTeam(teamName);

                team.addPlayer(loopPlayer);
            }
        }

        // Join message
        if(LuckPermsUtils.getWeight(player) > Main.getInstance().getMessages().getShowJoinMessageWeight()){
            event.setJoinMessage(Main.getInstance().getMessages().getOnJoinMessage().replace("%player%", player.getName())
                    .replace("%prefix%", LuckPermsUtils.getPrefix(player))
                    .replace("%suffix%", LuckPermsUtils.getSuffix(player))
                    .replace("&", "§"));
        }
        else{
            event.setJoinMessage(null);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if(LuckPermsUtils.getWeight(player) > Main.getInstance().getMessages().getShowQuitMessageWeight()){
            event.setQuitMessage(Main.getInstance().getMessages().getOnQuitMessage().replace("&", "§")
                    .replace("%player%", player.getName()));
        }
        else{
            event.setQuitMessage(null);
        }

        player.getScoreboard().getTeams().forEach(team -> {
            boolean online = false;
            for (OfflinePlayer offlinePlayer : team.getPlayers()) {
                if(offlinePlayer.isOnline()) {
                    online = true;
                }
            }
            if(!online)
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

        event.setFormat(Main.getInstance().getMessages().getChatFormatMessage().replace("%player%", player.getName())
                .replace("%prefix%", prefix)
                .replace("%suffix%", suffix)
                .replace("%message%", message)
                .replace("&", "§"));
    }


}
