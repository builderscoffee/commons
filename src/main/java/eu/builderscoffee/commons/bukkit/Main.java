package eu.builderscoffee.commons.bukkit;

import eu.builderscoffee.api.bukkit.gui.InventoryManager;
import eu.builderscoffee.api.bukkit.utils.Plugins;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.RedisCredentials;
import eu.builderscoffee.api.common.redisson.RedisTopic;
import eu.builderscoffee.commons.bukkit.commands.*;
import eu.builderscoffee.commons.bukkit.configuration.MessageConfiguration;
import eu.builderscoffee.commons.bukkit.configuration.PermissionsConfiguration;
import eu.builderscoffee.commons.bukkit.listeners.ConnexionListener;
import eu.builderscoffee.commons.bukkit.listeners.PlayerListener;
import eu.builderscoffee.commons.bukkit.listeners.redisson.HeartBeatListener;
import eu.builderscoffee.commons.bukkit.listeners.redisson.StaffChatListener;
import eu.builderscoffee.commons.common.configuration.SettingsConfig;
import eu.builderscoffee.commons.common.data.DataManager;
import eu.builderscoffee.commons.common.data.tables.ProfilEntity;
import eu.builderscoffee.commons.common.redisson.topics.CommonTopics;
import eu.builderscoffee.commons.common.utils.Cache;
import eu.builderscoffee.commons.common.utils.LuckPermsUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

import static eu.builderscoffee.api.common.configuration.Configuration.readOrCreateConfiguration;
import static eu.builderscoffee.commons.bukkit.commands.HelpCommand.registerCommand;

@Getter
public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    //Configuration
    private MessageConfiguration messages;
    private PermissionsConfiguration permissions;
    private SettingsConfig settings;

    private InventoryManager inventoryManager;

    private Cache<String, ProfilEntity> profilCache = new Cache<>();
    private ArrayList<UUID> staffchatPlayers = new ArrayList<>();

    @SneakyThrows
    @Override
    public void onEnable() {
        // Instance
        instance = this;

        // Configuration
        messages = readOrCreateConfiguration(this.getName(), MessageConfiguration.class);
        permissions = readOrCreateConfiguration(this.getName(), PermissionsConfiguration.class);
        settings = readOrCreateConfiguration(this.getName(), SettingsConfig.class);

        // Initialize Redisson
        val redisCredentials = new RedisCredentials()
                .setClientName(settings.getRedis().getClientName())
                .setIp(settings.getRedis().getIp())
                .setPassword(settings.getRedis().getPassword())
                .setPort(settings.getRedis().getPort());

        Redis.Initialize(Bukkit.getServerName(), redisCredentials, 0, 0);

        // Redisson Listeners
        Redis.subscribe(CommonTopics.STAFFCHAT, new StaffChatListener());
        Redis.subscribe(RedisTopic.HEARTBEATS, new HeartBeatListener());

        // Database
        getLogger().info("Connexion à la base de donnée...");
        DataManager.init(settings.getMySQL().toHikari());

        if(!settings.getLoadMode().equals(SettingsConfig.LoadMode.LAZY)){
            // Inventory Api
            inventoryManager = new InventoryManager(this);
            inventoryManager.init();

            // Service Provider LuckPerms
            // TODO Test with only LuckPermsProvider.get()
            val provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) LuckPermsUtils.init(provider.getProvider());

            // Commands
            this.getCommand("network").setExecutor(new NetworkCommands());
            this.getCommand("menu").setExecutor(new NetworkCommands());
            this.getCommand("hub").setExecutor(new HubCommand());
            this.getCommand("lobby").setExecutor(new HubCommand());
            this.getCommand("profil").setExecutor(new ProfileCommand());
            this.getCommand("help").setExecutor(new HelpCommand());
            this.getCommand("broadcast").setExecutor(new BroadcastCommand());
            this.getCommand("staffchat").setExecutor(new StaffChatCommand());

            // Listeners
            Plugins.registerListeners(this, new PlayerListener());
            Plugins.registerListeners(this, new ConnexionListener());

            registerCommand(HelpCommand.class);
        }
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        DataManager.getHikari().close();
        Redis.close();
    }
}