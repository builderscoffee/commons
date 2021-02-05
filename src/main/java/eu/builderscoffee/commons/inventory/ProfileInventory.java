package eu.builderscoffee.commons.inventory;

import eu.builderscoffee.api.gui.ClickableItem;
import eu.builderscoffee.api.gui.SmartInventory;
import eu.builderscoffee.api.gui.content.InventoryContents;
import eu.builderscoffee.api.gui.content.InventoryProvider;
import eu.builderscoffee.api.gui.content.SlotPos;
import eu.builderscoffee.api.utils.ItemBuilder;
import eu.builderscoffee.commons.Main;
import eu.builderscoffee.commons.configuration.MessageConfiguration;
import eu.builderscoffee.commons.utils.BungeeUtils;
import eu.builderscoffee.commons.utils.packets.BookUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ProfileInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("profile")
            .provider(new ProfileInventory())
            .size(6, 9)
            .title(ChatColor.WHITE + "Profil")
            .manager(Main.getInstance().getInventoryManager())
            .build();
    private final Main main = Main.getInstance();
    private final MessageConfiguration messages = main.getMessages();
    ClickableItem blackGlasses = ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
    ClickableItem greyGlasses = ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
    ClickableItem lightgreyGlasses = ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8));

    @Override
    public void init(Player player, InventoryContents contents) {
        //Fill Black borders
        contents.fillRect(SlotPos.of(0, 0), SlotPos.of(4, 0), blackGlasses);
        contents.fillRect(SlotPos.of(0, 8), SlotPos.of(4, 8), blackGlasses);
        //Fill Grey borders
        contents.fillRect(SlotPos.of(0, 1), SlotPos.of(5, 1), greyGlasses);
        contents.fillRect(SlotPos.of(0, 7), SlotPos.of(5, 7), greyGlasses);
        // Fill Light Grey line
        contents.fillRect(SlotPos.of(0, 3), SlotPos.of(0, 5), lightgreyGlasses);
        contents.fillRect(SlotPos.of(5, 3), SlotPos.of(5, 5), lightgreyGlasses);

        // Fill isolate grey glasses
        contents.set(SlotPos.of(0, 2), greyGlasses);
        contents.set(SlotPos.of(0, 6), greyGlasses);
        contents.set(SlotPos.of(5, 2), greyGlasses);
        contents.set(SlotPos.of(5, 6), greyGlasses);


        // Quitter
        contents.set(5, 0, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName(messages.getCloseItem().replace("&", "§")).build(),
                e -> contents.inventory().close(player)));


    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Nothing to do here
    }
}
