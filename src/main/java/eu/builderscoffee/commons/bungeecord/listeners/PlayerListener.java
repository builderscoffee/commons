package eu.builderscoffee.commons.bungeecord.listeners;

import com.google.common.collect.Iterables;
import eu.builderscoffee.commons.bungeecord.Main;
import eu.builderscoffee.commons.bungeecord.utils.DateUtil;
import eu.builderscoffee.commons.bungeecord.utils.TextComponentUtil;
import eu.builderscoffee.commons.common.data.BanEntity;
import lombok.val;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Date;

import static eu.builderscoffee.api.bungeecord.configuration.Configurations.writeConfiguration;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        // Update Profil
        val profil = Main.getInstance().getProfilCache().get(player.getUniqueId().toString());
        if(profil == null) {
            player.disconnect(TextComponentUtil.decodeColor("Proxy: §cUne erreur est survenue lors du chargement de données.\n§cVeuillez vous reconnecter"));
            return;
        }

        val banStore = Main.getInstance().getBanStore();
        val ban = banStore.select(BanEntity.class)
                .where(BanEntity.PROFILE.eq(profil))
                .get().firstOrNull();

        if(ban != null){
            if(new Date().after(ban.getDateEnd()))
            {
                banStore.delete(ban);
            }
            else{
                String message = "";
                for (String s : Main.getInstance().getMessages().getBanMessage()) {
                    String line = s.replace("%reason%", ban.getReason())
                            .replace("%time%", DateUtil.formatDateDiff(ban.getDateEnd().getTime()))
                            .replace("&", "§");
                    message += line + "\n";
                }
                player.disconnect(TextComponentUtil.decodeColor(message));
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        val player = event.getPlayer();
        if(Main.getInstance().getStaffChatPlayers().contains(player.getUniqueId())){
            Main.getInstance().getStaffChatPlayers().remove(player.getUniqueId());
        }
    }



    @EventHandler
    public void onChat(ChatEvent event){
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if(!event.isCommand() && Main.getInstance().getStaffChatPlayers().contains(player.getUniqueId())){
            event.setCancelled(true);
            String message = event.getMessage();
            String line = Main.getInstance().getMessages().getStaffChatFormat()
                    .replace("%player%", player.getName())
                    .replace("%message%", message)
                    .replace("&", "§");
            ProxyServer.getInstance().getPlayers().stream()
                    .filter(playerLoop -> playerLoop.hasPermission(Main.getInstance().getMessages().getStaffChatPermission())
                            || playerLoop.hasPermission(Main.getInstance().getMessages().getGlobalPermission()))
                    .forEach(playerLoop -> {
                        playerLoop.sendMessage(TextComponentUtil.decodeColor(line));
                    });
            Main.getInstance().getLogger().info(line);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerKick(final ServerKickEvent event){
        // When running in single-server mode, we can't kick people to the hub if they are on the hub.
        if(ProxyServer.getInstance().getServers().size() <= 1 && Iterables.getOnlyElement(ProxyServer.getInstance().getServers().values()).equals(event.getKickedFrom())){
            return;
        }

        if (event.getPlayer().getServer() == null) {
            // Not connected before
            return;
        }

        if (!event.getPlayer().getServer().getInfo().equals(event.getKickedFrom())) {
            // We aren't even on that server, so ignore it.
            return;
        }

        boolean match = false;

        for (BaseComponent baseComponent : event.getKickReasonComponent()) {
            for (String keyword : Main.getInstance().getMessages().getWhitelistRedirectMessagesKeywords()) {
                if(baseComponent.toLegacyText().contains(keyword)){
                    match = true;
                }
            }
        }

        if(!match){
            return;
        }

        val server = ProxyServer.getInstance().getServerInfo(Main.getInstance().getMessages().getServerRedirectName());

        if (server == null) {
            return;
        }

        if(event.getPlayer().getServer().equals(server)){
            return;
        }

        event.setCancelled(true);
        event.setCancelServer(server);

        event.getPlayer().sendMessage(TextComponentUtil.decodeColor(Main.getInstance().getMessages().getServerRedirectionMessage()));
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (event.getPlayer().getServer() != null){
            return;
        }

        val server = ProxyServer.getInstance().getServerInfo(Main.getInstance().getMessages().getServerRedirectName());

        if (server == null) {
            return;
        }

        event.setTarget(server);
    }

    @EventHandler
    public void onCommand(ChatEvent event){
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if(event.isCommand() && event.getMessage().toLowerCase().startsWith("/server")){
            val args = event.getMessage().split(" ");
            ServerInfo server = null;
            if(args.length > 2){
                for(int i = 0; i < args.length; i++){
                    if(i == 1){
                        server = ProxyServer.getInstance().getServerInfo(args[i]);
                        if(server == null){
                            return;
                        }
                    }
                    else if(i == 2 && args[i].toLowerCase().equals("default")){
                        if(player.hasPermission(Main.getInstance().getMessages().getServerDefaultPermission()))
                        {
                            event.setCancelled(true);
                            Main.getInstance().getMessages().setServerRedirectName(server.getName());
                            writeConfiguration(Main.getInstance(), Main.getInstance().getMessages());
                            event.setCancelled(true);
                            player.sendMessage(TextComponentUtil.decodeColor("§aLe serveur " + server.getName() + " est désormais le serveur par default"));
                        }
                        else{
                            player.sendMessage(TextComponentUtil.decodeColor(Main.getInstance().getMessages().getNoPermission().replace("&", "§")));
                        }
                    }
                }
            }
        }
    }
}
