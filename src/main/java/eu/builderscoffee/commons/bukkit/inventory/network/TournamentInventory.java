package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TournamentInventory extends DefaultAdminTemplateInventory {

    public TournamentInventory() {
        super("Tournament Manager", new ServersManagerInventory().INVENTORY);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);
        // Creer un tournoi
        contents.set(5, 3, ClickableItem.of(new ItemBuilder(Material.NETHER_STAR).setName("Creer un tournoi").build(),
                e -> new CreateTournamentInventory().INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Nothing to do
    }
}