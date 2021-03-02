package eu.builderscoffee.commons.bungeecord;

import com.zaxxer.hikari.HikariDataSource;
import eu.builderscoffee.commons.bungeecord.commands.PBanCommand;
import eu.builderscoffee.commons.bungeecord.commands.PPardonCommand;
import eu.builderscoffee.commons.bungeecord.commands.StaffChatCommand;
import eu.builderscoffee.commons.bungeecord.configuration.MessageConfiguration;
import eu.builderscoffee.commons.bungeecord.listeners.ConnexionListener;
import eu.builderscoffee.commons.bungeecord.listeners.PlayerListener;
import eu.builderscoffee.commons.common.utils.Cache;
import eu.builderscoffee.commons.common.Models;
import eu.builderscoffee.commons.common.configuration.SQLCredentials;
import eu.builderscoffee.commons.common.data.*;
import io.requery.sql.EntityDataStore;
import io.requery.sql.SchemaModifier;
import io.requery.sql.TableCreationMode;
import lombok.Getter;
import lombok.val;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.UUID;

import static eu.builderscoffee.api.bungeecord.configuration.Configurations.readOrCreateConfiguration;
import static eu.builderscoffee.api.bungeecord.configuration.Configurations.writeConfiguration;

public class Main extends Plugin {

    @Getter
    private static Main instance;

    //Configuration
    @Getter
    private MessageConfiguration messages;

    @Getter
    private SQLCredentials sqlCredentials;

    // LuckPerms API
    @Getter
    private LuckPerms luckyPerms;

    // Database
    @Getter
    private HikariDataSource hikari;
    @Getter
    private EntityDataStore<Note> notesStore;
    @Getter
    private EntityDataStore<Ban> banStore;
    @Getter
    private EntityDataStore<BuildbattleTheme> buildbattleThemeStore;
    @Getter
    private EntityDataStore<BuildbattleType> expressoTypseStore;
    @Getter
    private EntityDataStore<Buildbattle> buildbattlesStore;
    @Getter
    private EntityDataStore<Saison> saisonsStore;
    @Getter
    private EntityDataStore<Profil> profilStore;
    @Getter
    private EntityDataStore<Cosmetique> cosmetiquesStore;

    // Cache des profils
    @Getter
    private Cache<String, ProfilEntity> profilCache = new Cache<>();

    // Liste des joueurs en mode staffchat
    @Getter
    private ArrayList<UUID> staffChatPlayers = new ArrayList<>();

    @Override
    public void onEnable() {

        // Instance
        instance = this;

        // Service Provider
        luckyPerms = LuckPermsProvider.get();

        // Configuration
        messages = readOrCreateConfiguration(this, MessageConfiguration.class);
        sqlCredentials = readOrCreateConfiguration(this, SQLCredentials.class);

        // Check redirection server exist
        val server = ProxyServer.getInstance().getServerInfo(messages.getServerRedirectName());
        if(server == null){
            val newServer = (ServerInfo) getProxy().getServers().values().toArray()[0];
            Main.getInstance().getMessages().setServerRedirectName(newServer.getName());
            writeConfiguration(this, messages);
        }

        // Commands
        getProxy().getPluginManager().registerCommand(this, new PBanCommand());
        getProxy().getPluginManager().registerCommand(this, new PPardonCommand());
        getProxy().getPluginManager().registerCommand(this, new StaffChatCommand());

        // Listeners
        getProxy().getPluginManager().registerListener(this, new ConnexionListener());
        getProxy().getPluginManager().registerListener(this, new PlayerListener());

        // Database
        getLogger().info("Connexion à la base de donnée...");
        hikari = new HikariDataSource(sqlCredentials.toHikari());
        saisonsStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        buildbattleThemeStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        expressoTypseStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        buildbattlesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        profilStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        notesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        cosmetiquesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        banStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        new SchemaModifier(hikari, Models.DEFAULT).createTables(TableCreationMode.CREATE_NOT_EXISTS);
    }

    @Override
    public void onDisable() {
        hikari.close();
    }
}
