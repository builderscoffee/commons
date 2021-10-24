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
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * This class is a template inventory for admin panels
 */
public class DefaultAdminTemplateInventory extends DefaultTemplateInventory {

    /***
     * @param title Title of inventory
     * @param previousInventory If not null, will show the back arrow at the bottom
     */
    public DefaultAdminTemplateInventory(@NonNull String title, SmartInventory previousInventory) {
        super(title, previousInventory);
    }

    /***
     * @param title Title of inventory
     * @param previousInventory If not null, will show the back arrow at the bottom
     * @param rows Define amounts of rows
     * @param columns Define the amounts of columns
     */
    public DefaultAdminTemplateInventory(@NonNull String title, SmartInventory previousInventory, int rows, int columns) {
        super(title, previousInventory, rows, columns);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);
        // Version
        if(rows > 2)
            contents.set(rows - 1, columns - 1, ClickableItem.empty(new ItemBuilder(Material.HOPPER).setName("Version").addLoreLine(Main.getInstance().getSettings().getPluginMode().toString()).build()));
    }
}
