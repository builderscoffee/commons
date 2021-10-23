package eu.builderscoffee.commons.bukkit.inventory.templates;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.SmartInventory;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.gui.content.InventoryProvider;
import eu.builderscoffee.api.bukkit.gui.content.SlotPos;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bukkit.configuration.MessageConfiguration;
import eu.builderscoffee.commons.bukkit.inventory.network.NetworkInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DefaultAdminTemplateInventory extends DefaultTemplateInventory {

    public DefaultAdminTemplateInventory(String title, SmartInventory previousInventory) {
        super(title, previousInventory);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);
        // Version
        contents.set(5, 8, ClickableItem.empty(new ItemBuilder(Material.HOPPER).setName("Version").addLoreLine(Main.getInstance().getSettings().getPluginMode().toString()).build()));
    }
}
