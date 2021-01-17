package eu.builderscoffee.commons;

import eu.builderscoffee.api.gui.InventoryManager;
import eu.builderscoffee.api.utils.Plugins;
import eu.builderscoffee.commons.commands.NetworkCommands;
import eu.builderscoffee.commons.configuration.MessageConfiguration;
import eu.builderscoffee.commons.inventory.NetworkInventory;
import eu.builderscoffee.commons.listeners.PlayerListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import static eu.builderscoffee.api.configuration.Configurations.readOrCreateConfiguration;

public class Main  extends JavaPlugin {

    @Getter
    private static Main instance;
    //Configuration
    @Getter
    private MessageConfiguration messageConfiguration;

    @Getter
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        instance = this;

        //messageConfiguration = readOrCreateConfiguration(this, MessageConfiguration.class);

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        Plugins.registerListeners(this,new PlayerListener());

        this.getCommand("network").setExecutor(new NetworkCommands());
    }

    @Override
    public void onDisable() {

    }
}