package eu.builderscoffee.commons.bukkit.listeners;

import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.common.data.DataManager;
import eu.builderscoffee.commons.common.data.tables.Profil;
import eu.builderscoffee.commons.common.data.tables.*;
import eu.builderscoffee.commons.bukkit.listeners.event.DataStatueEvent;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnexionListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(new DataStatueEvent.Load(event.getUniqueId().toString()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(final AsyncPlayerPreLoginEvent e) {
        val whitelist = Main.getInstance().getWhitelist();
        for (final Player player : Main.getInstance().getServer().getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(e.getName()) ||
                    player.getUniqueId().equals(e.getUniqueId()) ||
                    player.getUniqueId().toString().toLowerCase().replaceAll("-", "").equalsIgnoreCase(e.getUniqueId().toString().toLowerCase().replaceAll("-", ""))) {
                e.setKickMessage(whitelist.getKick_message());
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
            }
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e){
        val whitelist = Main.getInstance().getWhitelist();
        if(!whitelist.getIp_whitelist().contains(e.getRealAddress().getHostAddress())){
            e.setKickMessage(whitelist.getKick_message());
            e.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> Bukkit.getServer().getPluginManager().callEvent(new DataStatueEvent.Save(event.getPlayer().getUniqueId().toString())));
    }

    @EventHandler
    public void onLoad(DataStatueEvent.Load event) {
        val instance = Main.getInstance();
        val store = DataManager.getProfilStore();
        val uniqueId = event.getUniqueId();
        // Récupère ou créer une nouvelle entité
        try(val query =  store.select(ProfilEntity.class).where(ProfilEntity.UNIQUE_ID.eq(uniqueId))
                .get()) {
            ProfilEntity entity = query.firstOrNull();
            if (entity == null) {
                entity = Profil.getOrCreate(uniqueId);
                store.insert(entity);
            }
            instance.getProfilCache().put(uniqueId, entity);
        }
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
        val store = DataManager.getProfilStore();
        try{
            val query = store.update(entity);
        } catch (Exception e){
            e.printStackTrace();
        }
        instance.getProfilCache().remove(uniqueId);
    }
}
