package eu.builderscoffee.commons.bukkit.inventory.servermanager;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.SmartInventory;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
import eu.builderscoffee.commons.bukkit.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * This inventory allows players to create a plot server
 */
public class CreatePlotServerInventory extends DefaultAdminTemplateInventory {

    public CreatePlotServerInventory(SmartInventory previousInventory, Player player) {
        super(MessageUtils.getMessageConfig(player).getInventory().getCreatePlot().getTitle(), previousInventory, 5, 9);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        // BuildBattle
        contents.set(2, 3, ClickableItem.of(new ItemBuilder(Material.WORKBENCH).setName("BuildBattle").build(),
                e -> player.sendMessage("§cnot yet implemented")));

        // Expresso
        contents.set(2, 5, ClickableItem.of(new ItemBuilder(Material.INK_SACK, 1, (short) 3).setName("Expresso").build(),
                e -> player.sendMessage("§cnot yet implemented")));
    }
}
