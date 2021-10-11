package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.SmartInventory;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.gui.content.InventoryProvider;
import eu.builderscoffee.api.bukkit.gui.content.SlotPos;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.bukkit.packets.BookUtil;
import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bukkit.configuration.MessageConfiguration;
import eu.builderscoffee.commons.bukkit.utils.BungeeUtils;
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

public class NetworkInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("network")
            .provider(new NetworkInventory())
            .size(6, 9)
            .title(ChatColor.WHITE + "Menu Builders Coffee")
            .manager(Main.getInstance().getInventoryManager())
            .build();
    private final Main main = Main.getInstance();
    private final MessageConfiguration messages = main.getMessages();
    private static final ClickableItem blackGlasses = ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15)).setName("§a").build());
    private static final ClickableItem greyGlasses = ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setName("§a").build());
    private static final ClickableItem lightgreyGlasses = ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8)).setName("§a").build());

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


        // Serveur hub
        contents.set(1, 3, ClickableItem.of(new ItemBuilder(Material.NETHER_STAR).setName(messages.getHubItem().replace("&", "§")).build(),
                e -> BungeeUtils.sendPlayerToServer(main, player, "hub")));
        // Serveur BuildBattle
        contents.set(1, 5, ClickableItem.of(new ItemBuilder(Material.ENCHANTMENT_TABLE).setName(messages.getBuildBattleItem().replace("&", "§")).build(),
                e -> BungeeUtils.sendPlayerToServer(main, player, "plot")));
        // Régles du serveur
        contents.set(3, 3, ClickableItem.of(new ItemBuilder(Material.BOOK_AND_QUILL).setName(messages.getRulesBookItem().replace("&", "§")).build(),
                e -> {
                    List<String> pages = new ArrayList<>();
                    messages.getPages().forEach(s -> {
                        TextComponent page0 = new TextComponent(s.replace("&", "§"));
                        page0.addExtra("\n");
                        pages.add(ComponentSerializer.toString(page0));
                    });
                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                    BookMeta meta = (BookMeta) book.getItemMeta();
                    meta.setTitle("");
                    meta.setAuthor("");
                    BookUtil.setPages(meta, pages);
                    book.setItemMeta(meta);
                    BookUtil.openBook(book, player);
                }));
        // Nous soutenir
        ItemStack diamond = new ItemBuilder(Material.DIAMOND).setName(messages.getSupportUsItem().replace("&", "§")).build();
        ItemMeta diamondMeta = diamond.getItemMeta();
        diamondMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        diamondMeta.addEnchant(Enchantment.LUCK, 1, false);
        diamond.setItemMeta(diamondMeta);
        contents.set(3, 4, ClickableItem.of(diamond, e -> {
                    TextComponent message = new TextComponent(messages.getSupportChatMessage());
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, messages.getSupportLink()));
                    player.spigot().sendMessage(message);
                    player.closeInventory();
                }));
        // Expresso
        contents.set(3, 5, ClickableItem.of(new ItemBuilder(Material.FLOWER_POT_ITEM).setName(messages.getExpressoItem().replace("&", "§")).build(),
                e -> {
                    TextComponent message = new TextComponent(messages.getExpressoChatMessage());
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, messages.getExpressoLink()));
                    player.spigot().sendMessage(message);
                    player.closeInventory();
                }));

        if(player.hasPermission(main.getPermissions().getServerManagerSee())){
            // ServerManager
            contents.set(5, 1, ClickableItem.of(new ItemBuilder(Material.PAPER).setName(messages.getServerManagerItem().replace("&", "§")).build(),
                    e -> new ServersManagerInventory().INVENTORY.open(player)));
        }

        // Quitter
        contents.set(5, 0, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName(messages.getCloseItem().replace("&", "§")).build(),
                e -> contents.inventory().close(player)));
        // Cosmétiques
        contents.set(5, 8, ClickableItem.of(new ItemBuilder(Material.CHEST).setName(messages.getCosmeticsItem().replace("&", "§")).build(),
                e -> player.sendMessage("§cIl n'y a pas de grains de café en stock")));


    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Nothing to do here
    }
}
