package eu.builderscoffee.commons.bungeecord;

import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.RedisCredentials;
import eu.builderscoffee.commons.bungeecord.commands.PBanCommand;
import eu.builderscoffee.commons.bungeecord.commands.PPardonCommand;
import eu.builderscoffee.commons.bungeecord.configuration.MessageConfiguration;
import eu.builderscoffee.commons.bungeecord.configuration.PermissionConfiguration;
import eu.builderscoffee.commons.bungeecord.listeners.ConnexionListener;
import eu.builderscoffee.commons.bungeecord.listeners.PlayerListener;
import eu.builderscoffee.commons.common.configuration.RedisConfig;
import eu.builderscoffee.commons.common.configuration.SQLCredentials;
import eu.builderscoffee.commons.common.data.DataManager;
import eu.builderscoffee.commons.common.data.tables.ProfilEntity;
import eu.builderscoffee.commons.common.utils.Cache;
import eu.builderscoffee.commons.common.utils.LuckPermsUtils;
import lombok.Getter;
import lombok.val;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import static eu.builderscoffee.api.common.configuration.Configuration.readOrCreateConfiguration;
import static eu.builderscoffee.api.common.configuration.Configuration.writeConfiguration;

@Getter
public class Main extends Plugin {

    @Getter
    private static Main instance;

    //Configuration
    private MessageConfiguration messages;
    private PermissionConfiguration permissions;

    private SQLCredentials sqlCredentials;
    private RedisConfig redissonConfig;

    // Cache des profils
    private Cache<String, ProfilEntity> profilCache = new Cache<>();

    @Override
    public void onEnable() {

        // Instance
        instance = this;

        // Service Provider
        LuckPermsUtils.init(LuckPermsProvider.get());

        // Configuration
        messages = readOrCreateConfiguration(this.getDescription().getName(), MessageConfiguration.class);
        permissions = readOrCreateConfiguration(this.getDescription().getName(), PermissionConfiguration.class);
        sqlCredentials = readOrCreateConfiguration(this.getDescription().getName(), SQLCredentials.class);
        redissonConfig = readOrCreateConfiguration(this.getDescription().getName(), RedisConfig.class);

        // Initialize Redisson
        val redisCredentials = new RedisCredentials()
                .setClientName(redissonConfig.getClientName())
                .setIp(redissonConfig.getIp())
                .setPassword(redissonConfig.getPassword())
                .setPort(redissonConfig.getPort());

        Redis.Initialize(ProxyServer.getInstance().getName(), redisCredentials, 0, 0);

        // Database
        getLogger().info("Connexion à la base de donnée...");
        DataManager.init(sqlCredentials.toHikari());

        // Check redirection server exist
        val server = ProxyServer.getInstance().getServerInfo(messages.getServerRedirectName());
        if (server == null) {
            val newServer = (ServerInfo) getProxy().getServers().values().toArray()[0];
            Main.getInstance().getMessages().setServerRedirectName(newServer.getName());
            writeConfiguration(this.getDescription().getName(), messages);
        }

        // Commands
        getProxy().getPluginManager().registerCommand(this, new PBanCommand());
        getProxy().getPluginManager().registerCommand(this, new PPardonCommand());

        // Listeners
        getProxy().getPluginManager().registerListener(this, new ConnexionListener());
        getProxy().getPluginManager().registerListener(this, new PlayerListener());
    }

    @Override
    public void onDisable() {
        DataManager.getHikari().close();
    }
}
