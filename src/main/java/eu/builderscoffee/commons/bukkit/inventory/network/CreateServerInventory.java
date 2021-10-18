package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CreateServerInventory extends DefaultAdminTemplateInventory {

    public CreateServerInventory() {
        super("Create Server", new ServersManagerInventory().INVENTORY);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        // Proxy
        contents.set(2, 2, ClickableItem.of(new ItemBuilder(Material.WOOL, 1, (short) 14).setName("Proxy").build(),
                e -> player.sendMessage("§cnot yet implemented")));

        // Hub
        contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.WOOL, 1, (short) 2).setName("Hub").build(),
                e -> player.sendMessage("§cnot yet implemented")));

        // Plot
        contents.set(2, 6, ClickableItem.of(new ItemBuilder(Material.WOOL, 1, (short) 7).setName("Plot").build(),
                e -> new CreatePlotServerInventory().INVENTORY.open(player)));
    }
}
