package eu.builderscoffee.commons.bukkit.listeners;

import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.common.data.Profil;
import eu.builderscoffee.commons.common.data.*;
import eu.builderscoffee.commons.bukkit.listeners.event.DataStatueEvent;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnexionListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(new DataStatueEvent.Load(event.getUniqueId().toString()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),
                () -> Bukkit.getServer().getPluginManager().callEvent(new DataStatueEvent.Save(event.getPlayer().getUniqueId().toString())));
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
