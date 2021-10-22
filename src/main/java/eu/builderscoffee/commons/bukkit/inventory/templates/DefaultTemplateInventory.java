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
import eu.builderscoffee.commons.bukkit.inventory.network.ServersManagerInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DefaultTemplateInventory implements InventoryProvider {

    public final SmartInventory INVENTORY;
    protected static final ClickableItem blackGlasses = ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15)).setName("§a").build());

    protected final MessageConfiguration messages = Main.getInstance().getMessages();
    protected final SmartInventory previousInventory;

    public DefaultTemplateInventory(String title, SmartInventory previousInventory) {
        this.INVENTORY = SmartInventory.builder()
                .id(this.getClass().getName())
                .provider(this)
                .size(6, 9)
                .title(ChatColor.WHITE + title)
                .manager(Main.getInstance().getInventoryManager())
                .build();
        this.previousInventory = previousInventory;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        //Fill Grey borders
        contents.fillRect(SlotPos.of(0, 0), SlotPos.of(0, 8), blackGlasses);
        contents.fillRect(SlotPos.of(5, 0), SlotPos.of(5, 8), blackGlasses);

        if(previousInventory != null){
            // Retour
            contents.set(5, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).setName(messages.getRetourItem().replace("&", "§")).build(),
                    e -> previousInventory.open(player)));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Nothing to do
    }
}
