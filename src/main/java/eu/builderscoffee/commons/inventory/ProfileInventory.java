package eu.builderscoffee.commons.inventory;

import eu.builderscoffee.api.gui.ClickableItem;
import eu.builderscoffee.api.gui.SmartInventory;
import eu.builderscoffee.api.gui.content.InventoryContents;
import eu.builderscoffee.api.gui.content.InventoryProvider;
import eu.builderscoffee.api.gui.content.SlotPos;
import eu.builderscoffee.api.utils.ItemBuilder;
import eu.builderscoffee.commons.Main;
import eu.builderscoffee.commons.commands.ProfileCommand;
import eu.builderscoffee.commons.configuration.MessageConfiguration;
import eu.builderscoffee.commons.data.NoteEntity;
import eu.builderscoffee.commons.data.ProfilEntity;
import eu.builderscoffee.commons.utils.BungeeUtils;
import eu.builderscoffee.commons.utils.packets.BookUtil;
import lombok.val;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        val cache = ProfileCommand.getRequestProfile();
        val targetName = cache.get(player);

        cache.remove(player);

        if(targetName == null) {
            contents.inventory().close(player);
            player.sendMessage("§cCe joueur n'existe pas");
            return;
        }

        val storeProfil = main.getProfilStore();
        val profilEntity = storeProfil.select(ProfilEntity.class)
                .where(ProfilEntity.NAME.eq(targetName))
                .get().firstOrNull();

        profilEntity.getNotes().forEach(noteEntity -> {
            player.sendMessage(noteEntity.getSaison().getName() + " " + noteEntity.getProfil().getName() + " " + noteEntity.getJury().getName());
        });


        //Fill Black borders
        contents.fillRect(SlotPos.of(0, 0), SlotPos.of(5, 0), blackGlasses);
        contents.fillRect(SlotPos.of(0, 8), SlotPos.of(5, 8), blackGlasses);
        //Fill Grey borders
        contents.fillRect(SlotPos.of(0, 1), SlotPos.of(5, 1), greyGlasses);
        contents.fillRect(SlotPos.of(0, 7), SlotPos.of(5, 7), greyGlasses);
        // Fill Light Grey line
        contents.fillRect(SlotPos.of(0, 3), SlotPos.of(0, 5), greyGlasses);
        contents.fillRect(SlotPos.of(5, 3), SlotPos.of(5, 5), greyGlasses);

        // Fill isolate grey glasses
        contents.set(SlotPos.of(0, 2), greyGlasses);
        contents.set(SlotPos.of(0, 6), greyGlasses);
        contents.set(SlotPos.of(5, 2), greyGlasses);
        contents.set(SlotPos.of(5, 6), greyGlasses);

        // Profil Skull
        final ItemStack skull = new ItemBuilder(Material.SKULL_ITEM).setName(targetName).build();
        final SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        final UUID uuid = UUID.fromString(profilEntity.getUniqueId());
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));

        /*val t = profilEntity.getNotes().stream()
                .collect(Collectors.groupingBy(NoteEntity::getSaison))
                .size();

        player.sendMessage("l " + t);*/
        skull.setItemMeta(skullMeta);
        contents.set(SlotPos.of(1, 4), ClickableItem.empty(skull));


        // Quitter
        contents.set(5, 4, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName(messages.getCloseItem().replace("&", "§")).build(),
                e -> contents.inventory().close(player)));


    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Nothing to do here
    }
}
