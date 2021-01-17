package eu.builderscoffee.commons.inventory;

import eu.builderscoffee.api.gui.ClickableItem;
import eu.builderscoffee.api.gui.SmartInventory;
import eu.builderscoffee.api.gui.content.InventoryContents;
import eu.builderscoffee.api.gui.content.InventoryProvider;
import eu.builderscoffee.api.gui.content.SlotPos;
import eu.builderscoffee.api.utils.ItemBuilder;
import eu.builderscoffee.commons.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class NetworkInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("network")
            .provider(new NetworkInventory())
            .size(6, 9)
            .title(ChatColor.WHITE + "Menu Builders Coffee")
            .manager(Main.getInstance().getInventoryManager())
            .build();

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
        contents.fillRect(SlotPos.of(0,3),SlotPos.of(0,5), lightgreyGlasses);
        contents.fillRect(SlotPos.of(5,3),SlotPos.of(5,5), lightgreyGlasses);

        // Fill isolate grey glasses
        contents.set(SlotPos.of(0, 2), greyGlasses);
        contents.set(SlotPos.of(0, 6), greyGlasses);
        contents.set(SlotPos.of(5, 2), greyGlasses);
        contents.set(SlotPos.of(5, 6), greyGlasses);


        // Serveur hub
        contents.set(1, 3, ClickableItem.of(new ItemStack(Material.NETHER_STAR),
                e -> player.sendMessage(ChatColor.GOLD + "LATTE")));
        // Serveur BuildBattle
        contents.set(1, 5, ClickableItem.of(new ItemStack(Material.ENCHANTMENT_TABLE),
                e -> player.sendMessage(ChatColor.GOLD + "CAPUCINO")));
        // Régles du serveur
        contents.set(3, 3, ClickableItem.of(new ItemStack(Material.BOOK_AND_QUILL),
                e -> player.sendMessage(ChatColor.GOLD + "CAPUCINO")));
        // Nous soutenirg.HIDE_ENCHANTS);
        ItemStack diamond = new ItemBuilder(Material.DIAMOND).setName("Nous soutenir").build();
        diamond.addUnsafeEnchantment(Enchantment.LUCK, 1);
        diamond.getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        contents.set(3, 4, ClickableItem.of(diamond, e -> player.closeInventory()));
        // Expresso
        contents.set(3, 5, ClickableItem.of(new ItemStack(Material.FLOWER_POT_ITEM),
                e -> player.sendMessage(ChatColor.GOLD + "CAPUCINO")));
        // Quitter
        contents.set(5, 0, ClickableItem.of(new ItemStack(Material.BARRIER),
                e -> player.sendMessage(ChatColor.GOLD + "CAPUCINO")));
        // Cosmétiques
        contents.set(5, 8, ClickableItem.of(new ItemStack(Material.CHEST),
                e -> player.sendMessage(ChatColor.GOLD + "CAPUCINO")));


    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
