package eu.builderscoffee.commons.bukkit.listeners;

import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.common.redisson.packets.StaffChatPacket;
import eu.builderscoffee.commons.common.redisson.topics.CommonTopics;
import eu.builderscoffee.commons.common.utils.LuckPermsUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        val player = event.getPlayer();
        // Update Profil
        val profil = Main.getInstance().getProfilCache().get(player.getUniqueId().toString());
        if (profil == null) {
            player.kickPlayer("§6§lBuilders Coffee Server \n§cUne erreur est survenue lors du chargement de données.\n§cVeuillez vous reconnecter");
            return;
        }

        if (!player.getName().equalsIgnoreCase(profil.getName()))
            profil.setName(player.getName());

        // Creating teams from LuckyPerms
        Bukkit.getOnlinePlayers().forEach(loopPlayer -> {
            loopPlayer.getScoreboard().getTeams().forEach(team -> team.unregister());
        });

        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            final String primaryGroup = LuckPermsUtils.getPrimaryGroup(loopPlayer.getUniqueId());
            final int weight = Math.abs(1000 - LuckPermsUtils.getWeight(loopPlayer.getUniqueId()));

            String teamName = primaryGroup.length() > 13 ? weight + primaryGroup.substring(0, 13) : weight + primaryGroup;
            for (int i = 0; i < 3 - String.valueOf(weight).length(); i++) {
                teamName = "0" + teamName;
            }

            val prefix = (LuckPermsUtils.getPrefix(loopPlayer.getUniqueId()) != null? LuckPermsUtils.getPrefix(loopPlayer.getUniqueId()) : "");
            val suffix = (LuckPermsUtils.getSuffix(loopPlayer.getUniqueId()) != null? LuckPermsUtils.getSuffix(loopPlayer.getUniqueId()) : "");
            loopPlayer.setPlayerListName(prefix.replace("&", "§") + loopPlayer.getName() + suffix.replace("&", "§"));

            for (Player loopPlayer2 : Bukkit.getOnlinePlayers()) {
                Scoreboard scoreboard = loopPlayer2.getScoreboard();
                val team = scoreboard.getTeam(teamName) == null ? scoreboard.registerNewTeam(teamName) : scoreboard.getTeam(teamName);

                team.addPlayer(loopPlayer);
            }
        }

        // Join message
        if (LuckPermsUtils.getWeight(player.getUniqueId()) > Main.getInstance().getMessages().getShowJoinMessageWeight()) {
            event.setJoinMessage(Main.getInstance().getMessages().getOnJoinMessage().replace("%player%", player.getName())
                    .replace("%prefix%", (LuckPermsUtils.getPrefix(player.getUniqueId()) != null? LuckPermsUtils.getPrefix(player.getUniqueId()) : ""))
                    .replace("%suffix%", (LuckPermsUtils.getSuffix(player.getUniqueId()) != null? LuckPermsUtils.getSuffix(player.getUniqueId()) : ""))
                    .replace("&", "§"));
        } else {
            event.setJoinMessage(null);
        }


    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        val player = event.getPlayer();

        if (LuckPermsUtils.getWeight(player.getUniqueId()) > Main.getInstance().getMessages().getShowQuitMessageWeight()) {
            event.setQuitMessage(Main.getInstance().getMessages().getOnQuitMessage().replace("&", "§")
                    .replace("%player%", player.getName()));
        } else {
            event.setQuitMessage(null);
        }

        player.getScoreboard().getTeams().forEach(team -> {
            boolean online = false;
            for (OfflinePlayer offlinePlayer : team.getPlayers()) {
                if (offlinePlayer.isOnline()) {
                    online = true;
                }
            }
            if (!online)
                team.unregister();
        });
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        val player = event.getPlayer();
        val message = event.getMessage();
        val tempPrefix = LuckPermsUtils.getPrefix(player.getUniqueId());
        val tempSuffix = LuckPermsUtils.getSuffix(player.getUniqueId());
        val prefix = tempPrefix != null ? tempPrefix : "";
        val suffix = tempSuffix != null ? tempSuffix : "";

        // StaffChat
        if(Main.getInstance().getStaffchatPlayers().contains(event.getPlayer().getUniqueId())){
            val packet = new StaffChatPacket()
                    .setServerName(Main.getInstance().getRedissonConfig().getClientName())
                    .setPlayerName(player.getName())
                    .setMessage(Main.getInstance().getMessages().getStaffChatFormatMessage()
                            .replace("%player%", player.getName())
                            .replace("%prefix%", prefix)
                            .replace("%suffix%", suffix)
                            .replace("%message%", message)
                            .replace("&", "§"));
            Redis.getTopic(CommonTopics.STAFFCHAT).publish(packet.serialize());
            event.setCancelled(true);
        }
        // Normal chat
        else {
            event.setFormat(Main.getInstance().getMessages().getChatFormatMessage()
                    .replace("%player%", player.getName())
                    .replace("%prefix%", prefix)
                    .replace("%suffix%", suffix)
                    .replace("%message%", message)
                    .replace("&", "§"));
        }
    }
}
