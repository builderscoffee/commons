package eu.builderscoffee.commons;

import eu.builderscoffee.api.gui.InventoryManager;
import eu.builderscoffee.api.utils.Plugins;
import eu.builderscoffee.commons.commands.NetworkCommands;
import eu.builderscoffee.commons.configuration.MessageConfiguration;
import eu.builderscoffee.commons.listeners.PlayerListener;
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
    private LuckPerms luckyPerms;

    @Getter
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        instance = this;

        val provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) luckyPerms = provider.getProvider();

        messages = readOrCreateConfiguration(this, MessageConfiguration.class);

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        Plugins.registerListeners(this, new PlayerListener());

        this.getCommand("network").setExecutor(new NetworkCommands());
    }

    @Override
    public void onDisable() {

    }
}