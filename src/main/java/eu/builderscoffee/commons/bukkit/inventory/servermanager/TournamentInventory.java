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
 * This inventory allows players to manager multiple tournaments
 */
public class TournamentInventory extends DefaultAdminTemplateInventory {

    public TournamentInventory(SmartInventory previousInventory, Player player) {
        super(MessageUtils.getMessageConfig(player).getInventory().getTournamentManager().getTitle(), previousInventory, 5, 9);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);
        // Creer un tournoi
        contents.set(rows - 1, 3, ClickableItem.of(new ItemBuilder(Material.NETHER_STAR).setName("Creer un tournoi").build(),
                e -> new CreateTournamentInventory(this.INVENTORY, player).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Nothing to do
    }
}