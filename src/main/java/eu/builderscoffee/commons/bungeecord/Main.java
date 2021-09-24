package eu.builderscoffee.commons.bungeecord;

import com.zaxxer.hikari.HikariDataSource;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.RedisCredentials;
import eu.builderscoffee.commons.bungeecord.commands.PBanCommand;
import eu.builderscoffee.commons.bungeecord.commands.PPardonCommand;
import eu.builderscoffee.commons.bungeecord.configuration.MessageConfiguration;
import eu.builderscoffee.commons.bungeecord.configuration.PermissionConfiguration;
import eu.builderscoffee.commons.bungeecord.listeners.ConnexionListener;
import eu.builderscoffee.commons.bungeecord.listeners.PlayerListener;
import eu.builderscoffee.commons.common.configuration.RedisConfig;
import eu.builderscoffee.commons.common.utils.Cache;
import eu.builderscoffee.commons.common.Models;
import eu.builderscoffee.commons.common.configuration.SQLCredentials;
import eu.builderscoffee.commons.common.data.*;
import eu.builderscoffee.commons.common.utils.LuckPermsUtils;
import io.requery.sql.EntityDataStore;
import io.requery.sql.SchemaModifier;
import io.requery.sql.TableCreationMode;
import lombok.Getter;
import lombok.val;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import static eu.builderscoffee.api.bungeecord.configuration.Configurations.readOrCreateConfiguration;
import static eu.builderscoffee.api.bungeecord.configuration.Configurations.writeConfiguration;

@Getter
public class Main extends Plugin {

    @Getter
    private static Main instance;

    //Configuration
    private MessageConfiguration messages;
    private PermissionConfiguration permissions;

    private SQLCredentials sqlCredentials;
    private RedisConfig redissonConfig;

    // Database
    private HikariDataSource hikari;
    private EntityDataStore<Note> notesStore;
    private EntityDataStore<Ban> banStore;
    private EntityDataStore<BuildbattleTheme> buildbattleThemeStore;
    private EntityDataStore<BuildbattleType> buildbattleTypeStore;
    private EntityDataStore<Buildbattle> buildbattlesStore;
    private EntityDataStore<Saison> saisonsStore;
    private EntityDataStore<Profil> profilStore;
    private EntityDataStore<Cosmetique> cosmetiquesStore;

    // Cache des profils
    private Cache<String, ProfilEntity> profilCache = new Cache<>();

    @Override
    public void onEnable() {

        // Instance
        instance = this;

        // Service Provider
        LuckPermsUtils.init(LuckPermsProvider.get());

        // Configuration
        messages = readOrCreateConfiguration(this, MessageConfiguration.class);
        permissions = readOrCreateConfiguration(this, PermissionConfiguration.class);
        sqlCredentials = readOrCreateConfiguration(this, SQLCredentials.class);
        redissonConfig = readOrCreateConfiguration(this, RedisConfig.class);

        // Initialize Redisson
        val redisCredentials = new RedisCredentials()
                .setClientName(redissonConfig.getClientName())
                .setIp(redissonConfig.getIp())
                .setPassword(redissonConfig.getPassword())
                .setPort(redissonConfig.getPort());

        Redis.Initialize(redisCredentials, 0, 0);

        // Database
        getLogger().info("Connexion à la base de donnée...");
        hikari = new HikariDataSource(sqlCredentials.toHikari());
        saisonsStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        buildbattleThemeStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        buildbattleTypeStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        buildbattlesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        profilStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        notesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        cosmetiquesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        banStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        new SchemaModifier(hikari, Models.DEFAULT).createTables(TableCreationMode.CREATE_NOT_EXISTS);

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

        // Listeners
        getProxy().getPluginManager().registerListener(this, new ConnexionListener());
        getProxy().getPluginManager().registerListener(this, new PlayerListener());
    }

    @Override
    public void onDisable() {
        hikari.close();
    }
}
