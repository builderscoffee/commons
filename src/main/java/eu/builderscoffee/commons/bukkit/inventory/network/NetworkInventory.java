package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.SmartInventory;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.gui.content.InventoryProvider;
import eu.builderscoffee.api.bukkit.gui.content.SlotPos;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.bukkit.inventory.servermanager.ServersManagerInventory;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultTemplateInventory;
import eu.builderscoffee.commons.bukkit.utils.BookUtil;
import eu.builderscoffee.commons.bukkit.CommonsBukkit;
import eu.builderscoffee.commons.bukkit.utils.BungeeUtils;
import eu.builderscoffee.commons.bukkit.utils.MessageUtils;
import lombok.NonNull;
import lombok.val;
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

/**
 * This inventory allows players to navigate through servers
 */
public class NetworkInventory extends DefaultTemplateInventory {

    public NetworkInventory(Player player) {
        // Todo Title being translated
        super("Menu de Builders Coffee", null, 5, 9);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        val messages = MessageUtils.getMessageConfig(player);
        //Fill Grey borders
        contents.fill(greyGlasses);

        // WIP
        contents.set(2, 4, ClickableItem.empty(new ItemBuilder(Material.BARRIER).setName("§6Work in progress").build()));

        if(player.hasPermission(CommonsBukkit.getInstance().getPermissions().getServerManagerSee())){
            // ServerManager
            contents.set(4, 1, ClickableItem.of(new ItemBuilder(Material.PAPER).setName(messages.getNetwork().getServerManagerItem().replace("&", "§")).build(),
                    e -> new ServersManagerInventory(player).INVENTORY.open(player)));
        }

        // Quitter
        contents.set(4, 0, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName(messages.getNetwork().getCloseItem().replace("&", "§")).build(),
                e -> contents.inventory().close(player)));

        // Langues
        contents.set(4, 7, ClickableItem.of(new ItemBuilder(Material.PAINTING).setName(messages.getNetwork().getLanguageItem().replace("&", "§")).build(),
                e -> new LanguageInventory(player).INVENTORY.open(player)));

        // Cosmétiques
        contents.set(4, 8, ClickableItem.of(new ItemBuilder(Material.CHEST).setName(messages.getNetwork().getCosmeticsItem().replace("&", "§")).build(),
                e -> player.sendMessage("§cIl n'y a pas de grains de café en stock")));
    }
}
