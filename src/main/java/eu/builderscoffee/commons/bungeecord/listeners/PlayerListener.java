package eu.builderscoffee.commons.bungeecord.listeners;

import eu.builderscoffee.commons.bungeecord.Main;
import lombok.val;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        // Update Profil
        val profil = Main.getInstance().getProfilCache().get(player.getUniqueId().toString());
        if(profil == null) {
            player.disconnect(new TextComponent("Proxy: §cUne erreur est survenue lors du chargement de données.\n§cVeuillez vous reconnecter"));
            return;
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {

    }
}
