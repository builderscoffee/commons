package eu.builderscoffee.commons.bungeecord.listeners;

import eu.builderscoffee.commons.bungeecord.Main;
import eu.builderscoffee.commons.bungeecord.listeners.event.DataStatueEvent;
import eu.builderscoffee.commons.common.data.Profil;
import eu.builderscoffee.commons.common.data.ProfilEntity;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ConnexionListener implements Listener {

    @EventHandler
    public void onPreLogin(LoginEvent event) {
        ProxyServer.getInstance().getPluginManager().callEvent(new DataStatueEvent.Load(event.getConnection().getUniqueId().toString()));
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(),
                () -> ProxyServer.getInstance().getPluginManager().callEvent(new DataStatueEvent.Save(player.getUniqueId().toString())));
    }

    @EventHandler
    public void onLoad(DataStatueEvent.Load event) {
        val instance = Main.getInstance();
        val store = instance.getProfilStore();
        val uniqueId = event.getUniqueId();
        // Récupère ou créer une nouvelle entité
        try(val query =  store.select(ProfilEntity.class).where(ProfilEntity.UNIQUE_ID.eq(uniqueId))
                .get()) {
            ProfilEntity entity = query.firstOrNull();
            if (entity == null) {
                entity = Profil.getOrCreate(uniqueId);
                entity = store.insert(entity);
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
        val store = instance.getProfilStore();
        try{
            val query = store.update(entity);
        } catch (Exception e){
            e.printStackTrace();
        }
        instance.getProfilCache().remove(uniqueId);
    }
}
