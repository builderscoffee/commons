package eu.builderscoffee.commons.bukkit;

import com.zaxxer.hikari.HikariDataSource;
import eu.builderscoffee.api.bukkit.gui.InventoryManager;
import eu.builderscoffee.api.bukkit.utils.Plugins;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.RedisCredentials;
import eu.builderscoffee.commons.bukkit.commands.*;
import eu.builderscoffee.commons.bukkit.listeners.redisson.StaffChatListener;
import eu.builderscoffee.commons.common.Models;
import eu.builderscoffee.commons.common.configuration.RedisConfig;
import eu.builderscoffee.commons.common.data.*;
import eu.builderscoffee.commons.bukkit.listeners.ConnexionListener;
import eu.builderscoffee.commons.bukkit.configuration.MessageConfiguration;
import eu.builderscoffee.commons.common.configuration.SQLCredentials;
import eu.builderscoffee.commons.bukkit.listeners.PlayerListener;
import eu.builderscoffee.commons.common.redisson.topics.CommonTopics;
import eu.builderscoffee.commons.common.utils.Cache;
import eu.builderscoffee.commons.common.utils.LuckPermsUtils;
import io.requery.sql.EntityDataStore;
import io.requery.sql.SchemaModifier;
import io.requery.sql.TableCreationMode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

import static eu.builderscoffee.api.bukkit.configuration.Configurations.readOrCreateConfiguration;
import static eu.builderscoffee.commons.bukkit.commands.HelpCommand.registerCommand;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    //Configuration
    @Getter
    private MessageConfiguration messages;

    @Getter
    private SQLCredentials sqlCredentials;

    @Getter
    private RedisConfig redissonConfig;

    @Getter
    private InventoryManager inventoryManager;

    @Getter
    private HikariDataSource hikari;

    @Getter
    private EntityDataStore<Note> notesStore;

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

    @Getter
    private Cache<String, ProfilEntity> profilCache = new Cache<>();

    @Getter
    private ArrayList<UUID> staffchatPlayers = new ArrayList<>();

    @SneakyThrows
    @Override
    public void onEnable() {
        // Instance
        instance = this;

        // Service Provider LuckPerms
        val provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) LuckPermsUtils.init(provider.getProvider());

        // Configuration
        messages = readOrCreateConfiguration(this, MessageConfiguration.class);
        sqlCredentials = readOrCreateConfiguration(this, SQLCredentials.class);
        redissonConfig = readOrCreateConfiguration(this, RedisConfig.class);

        // Initialize Redisson
        val redisCredentials = new RedisCredentials()
                .setClientName(redissonConfig.getClientName())
                .setIp(redissonConfig.getIp())
                .setPassword(redissonConfig.getPassword())
                .setPort(redissonConfig.getPort());

        Redis.Initialize(redisCredentials, 0, 0);

        // Redisson Listeners
        Redis.subscribe(CommonTopics.STAFFCHAT, new StaffChatListener());

        // Inventory Api
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

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
        new SchemaModifier(hikari, Models.DEFAULT).createTables(TableCreationMode.CREATE_NOT_EXISTS);

        // Listeners
        Plugins.registerListeners(this, new PlayerListener());
        Plugins.registerListeners(this, new ConnexionListener());

        // Commands
        this.getCommand("network").setExecutor(new NetworkCommands());
        this.getCommand("menu").setExecutor(new NetworkCommands());
        this.getCommand("hub").setExecutor(new HubCommand());
        this.getCommand("lobby").setExecutor(new HubCommand());
        this.getCommand("profil").setExecutor(new ProfileCommand());
        this.getCommand("help").setExecutor(new HelpCommand());
        this.getCommand("broadcast").setExecutor(new BroadcastCommand());
        this.getCommand("staffchat").setExecutor(new StaffChatCommand());

        registerCommand(HelpCommand.class);
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        hikari.close();
        Redis.close();
    }
}