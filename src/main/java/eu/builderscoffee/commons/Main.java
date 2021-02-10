package eu.builderscoffee.commons;

import com.zaxxer.hikari.HikariDataSource;
import eu.builderscoffee.api.gui.InventoryManager;
import eu.builderscoffee.api.utils.Plugins;
import eu.builderscoffee.commons.commands.HubCommand;
import eu.builderscoffee.commons.commands.NetworkCommands;
import eu.builderscoffee.commons.commands.ProfileCommand;
import eu.builderscoffee.commons.configuration.MessageConfiguration;
import eu.builderscoffee.commons.configuration.SQLCredentials;
import eu.builderscoffee.commons.data.*;
import eu.builderscoffee.commons.listeners.ConnexionListener;
import eu.builderscoffee.commons.listeners.PlayerListener;
import eu.builderscoffee.commons.utils.Cache;
import io.requery.sql.EntityDataStore;
import io.requery.sql.SchemaModifier;
import io.requery.sql.TableCreationMode;
import lombok.Getter;
import lombok.val;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static eu.builderscoffee.api.configuration.Configurations.readOrCreateConfiguration;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    //Configuration
    @Getter
    private MessageConfiguration messages;

    @Getter
    private SQLCredentials sqlCredentials;

    @Getter
    private LuckPerms luckyPerms;

    @Getter
    private InventoryManager inventoryManager;

    @Getter
    private HikariDataSource hikari;

    @Getter
    private EntityDataStore<Note> notesStore;

    @Getter
    private EntityDataStore<BuildbattleTheme> buildbattleThemeStore;

    @Getter
    private EntityDataStore<ExpressoType> expressoTypseStore;

    @Getter
    private EntityDataStore<Buildbattle> buildbattlesStore;

    @Getter
    private EntityDataStore<Saison> saisonsStore;

    @Getter
    private EntityDataStore<Profil> profilStore;

    @Getter
    private Cache<String, ProfilEntity> profilCache = new Cache<>();

    @Override
    public void onEnable() {
        // Instance
        instance = this;

        // Service Provider
        val provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) luckyPerms = provider.getProvider();

        // Configuration
        messages = readOrCreateConfiguration(this, MessageConfiguration.class);
        sqlCredentials = readOrCreateConfiguration(this, SQLCredentials.class);

        // Inventory Api
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        // Listeners
        Plugins.registerListeners(this, new PlayerListener());
        Plugins.registerListeners(this, new ConnexionListener());

        // Commands
        this.getCommand("network").setExecutor(new NetworkCommands());
        this.getCommand("menu").setExecutor(new NetworkCommands());
        this.getCommand("hub").setExecutor(new HubCommand());
        this.getCommand("lobby").setExecutor(new HubCommand());
        this.getCommand("profil").setExecutor(new ProfileCommand());

        // Database
        getLogger().info("Connexion à la base de donnée...");
        hikari = new HikariDataSource(sqlCredentials.toHikari());
        notesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        buildbattleThemeStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        expressoTypseStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        buildbattlesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        saisonsStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        profilStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        new SchemaModifier(hikari, Models.DEFAULT).createTables(TableCreationMode.CREATE_NOT_EXISTS);
    }

    @Override
    public void onDisable() {
        hikari.close();
    }
}