package eu.builderscoffee.commons.bungeecord.listeners;

import eu.builderscoffee.commons.bungeecord.Main;
import eu.builderscoffee.commons.bungeecord.listeners.event.DataStatueEvent;
import eu.builderscoffee.commons.common.data.Profil;
import eu.builderscoffee.commons.common.data.ProfilEntity;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ConnexionListener implements Listener {

    @EventHandler
    public void onAsyncPreLogin(PreLoginEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getConnection();
        ProxyServer.getInstance().getPluginManager().callEvent(new DataStatueEvent.Load(player.getUniqueId().toString()));
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(),
                () -> ProxyServer.getInstance().getPluginManager().callEvent(new DataStatueEvent.Save(player.getUniqueId().toString())));
    }

    /*@EventHandler(priority = EventPriority.MONITOR)
    public void onKick(PlayerKickEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),
                () -> Bukkit.getServer().getPluginManager().callEvent(new DataStatueEvent.Save(event.getPlayer().getUniqueId().toString())));
    }*/

    @EventHandler
    public void onLoad(DataStatueEvent.Load event) {
        val instance = Main.getInstance();
        val store = instance.getProfilStore();
        val uniqueId = event.getUniqueId();
        // Récupère ou créer une nouvelle entité
        ProfilEntity entity =  store.select(ProfilEntity.class).where(ProfilEntity.UNIQUE_ID.eq(uniqueId))
                .get().firstOrNull();
        if(entity == null) {
            entity = Profil.getOrCreate(uniqueId);
            entity = store.insert(entity);
        }
        instance.getProfilCache().put(uniqueId, entity);
    }

    @EventHandler
    public void onSave(DataStatueEvent.Save event) {
        val instance = Main.getInstance();
        val uniqueId = event.getUniqueId();
        val entity = instance.getProfilCache().get(uniqueId);
        if(entity == null) {
            int currentLine = new Throwable().getStackTrace()[0].getLineNumber() + 1;
            Main.getInstance().getLogger().warning("§cLe joueur n'avait pas de donnée (" + this.getClass().getName() + ".java:" + currentLine + ")");
            return;
        }
        val store = instance.getProfilStore();
        store.update(entity);
    }
}
