package eu.builderscoffee.commons.bungeecord.listeners;

import com.google.common.collect.Iterables;
import eu.builderscoffee.commons.bungeecord.Main;
import eu.builderscoffee.commons.bungeecord.utils.TextComponentUtil;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import static eu.builderscoffee.api.common.configuration.Configuration.writeConfiguration;

public class PlayerListener implements Listener {

    /*@EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        // Update Profil
        val profil = Main.getInstance().getProfilCache().get(player.getUniqueId().toString());
        if (profil == null) {
            player.disconnect(TextComponentUtil.decodeColor("§6§lBuilders Coffee Proxy \n§cUne erreur est survenue lors du chargement de données.\n§cVeuillez vous reconnecter"));
            return;
        }

        val banStore = DataManager.getBansStore();
        try (val query = banStore.select(BanEntity.class)
                .where(BanEntity.PROFILE.eq(profil))
                .get()) {

            val ban = query.firstOrNull();

            if (ban != null) {
                if (new Date().after(ban.getDateEnd())) {
                    banStore.delete(ban);
                } else {
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
    }*/

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerKick(final ServerKickEvent event) {
        // When running in single-server mode, we can't kick people to the hub if they are on the hub.
        if (ProxyServer.getInstance().getServers().size() <= 1 && Iterables.getOnlyElement(ProxyServer.getInstance().getServers().values()).equals(event.getKickedFrom())) {
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
                if (baseComponent.toLegacyText().contains(keyword)) {
                    match = true;
                }
            }
        }

        if (!match) {
            return;
        }

        val server = ProxyServer.getInstance().getServerInfo(Main.getInstance().getMessages().getServerRedirectName());

        if (server == null) {
            return;
        }

        if (event.getPlayer().getServer().getInfo().equals(server)) {
            return;
        }

        event.setCancelled(true);
        event.setCancelServer(server);

        event.getPlayer().sendMessage(TextComponentUtil.decodeColor(Main.getInstance().getMessages().getServerRedirectionMessage()));
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (event.getPlayer().getServer() != null) {
            return;
        }

        val server = ProxyServer.getInstance().getServerInfo(Main.getInstance().getMessages().getServerRedirectName());

        if (server == null) {
            return;
        }

        event.setTarget(server);
    }

    @EventHandler
    public void onCommand(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (event.isCommand() && event.getMessage().toLowerCase().startsWith("/server")) {
            val args = event.getMessage().split(" ");
            ServerInfo server = null;
            if (args.length > 2) {
                for (int i = 0; i < args.length; i++) {
                    if (i == 1) {
                        server = ProxyServer.getInstance().getServerInfo(args[i]);
                        if (server == null) {
                            return;
                        }
                    } else if (i == 2 && args[i].toLowerCase().equals("default")) {
                        if (player.hasPermission(Main.getInstance().getPermissions().getServerDefaultPermission())) {
                            event.setCancelled(true);
                            Main.getInstance().getMessages().setServerRedirectName(server.getName());
                            writeConfiguration(Main.getInstance().getDescription().getName(), Main.getInstance().getMessages());
                            event.setCancelled(true);
                            player.sendMessage(TextComponentUtil.decodeColor("§aLe serveur " + server.getName() + " est désormais le serveur par default"));
                        } else {
                            player.sendMessage(TextComponentUtil.decodeColor(Main.getInstance().getMessages().getNoPermission().replace("&", "§")));
                        }
                    }
                }
            }
        }
    }
}
