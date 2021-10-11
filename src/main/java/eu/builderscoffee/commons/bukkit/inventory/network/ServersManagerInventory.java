package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.SmartInventory;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.gui.content.InventoryProvider;
import eu.builderscoffee.api.bukkit.gui.content.SlotIterator;
import eu.builderscoffee.api.bukkit.gui.content.SlotPos;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.serverinfos.Server;
import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bukkit.configuration.MessageConfiguration;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.redisson.api.RSortedSet;

import java.text.SimpleDateFormat;
import java.util.*;

public class ServersManagerInventory implements InventoryProvider {

    public final SmartInventory INVENTORY;
    private static final ClickableItem blackGlasses = ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15)).setName("§a").build());
    private static final ClickableItem greyGlasses = ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setName("§a").build());

    private final Main main = Main.getInstance();
    private final MessageConfiguration messages = main.getMessages();

    public ServersManagerInventory() {
        this.INVENTORY = SmartInventory.builder()
                .id("server_manager")
                .provider(this)
                .size(6, 9)
                .title(ChatColor.WHITE + "Server Manager")
                .manager(Main.getInstance().getInventoryManager())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        //Fill Black borders
        contents.fillRect(SlotPos.of(0, 0), SlotPos.of(5, 0), blackGlasses);
        contents.fillRect(SlotPos.of(0, 8), SlotPos.of(5, 8), blackGlasses);
        //Fill Grey borders
        contents.fillRect(SlotPos.of(0, 1), SlotPos.of(0, 7), greyGlasses);
        contents.fillRect(SlotPos.of(5, 1), SlotPos.of(5, 7), greyGlasses);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        val serverItems = new ArrayList<ClickableItem>();
        // Récupère les données de serveurs
        final RSortedSet<Server> servers = Redis.getRedissonClient().getSortedSet("servers");

        // Vérifie que la liste existe
        if(servers == null) return;

        // Boucle de tous les serveurs
        servers.stream()
                .sorted()
                .forEach(s -> {
                    // Creer une description selon les données du serveur
                    val lore = new TreeSet<String>();
                    Arrays.stream(s.getClass().getMethods())
                            .filter(m -> m.getName().startsWith("get") &&
                                    m.getParameterTypes().length == 0 &&
                                    !m.getName().equalsIgnoreCase("getHostAddress") &&
                                    !m.getName().equalsIgnoreCase("getHostPort") &&
                                    !m.getName().equalsIgnoreCase("getHostName") &&
                                    !m.getName().equalsIgnoreCase("getClass"))
                            .forEach(m -> {
                                try {
                                    Object result = m.invoke(s);
                                    if(result instanceof Date)
                                        result = new SimpleDateFormat("EEE dd MMM yyyy à hh:mm:ss", Locale.FRANCE).format((Date) result);

                                    String name = camelToPhrase(m.getName().substring(3));
                                    if(result instanceof Collection){
                                        val collection = (Collection) result;
                                        lore.add("§b" + name + ":");
                                        collection.forEach(o1 -> o1.toString());
                                    }
                                    else {
                                        lore.add("§b" + name + ": §a" + result);
                                    }
                                } catch (Exception e) {
                                }
                            });
                    lore.add("");
                    lore.add("§aClic gauche pour gérer");

                    // Créer l'item permettant de click
                    serverItems.add(ClickableItem.of(new ItemBuilder(Material.PAPER)
                            .setName(s.getHostName())
                            .addLoreLine(new ArrayList<>(lore))
                            .build(),
                            e -> new ServerManagerInventory(s).INVENTORY.open(player)));
                });
        // Ajouter les items dans l'inventaire
        contents.pagination().setItems(serverItems.toArray(new ClickableItem[0]));
        contents.pagination().setItemsPerPage(7);

        // Définit comment l'inventaire doit afficher les items
        contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 1)));

        // Retour
        contents.set(5, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).setName(messages.getRetourItem().replace("&", "§")).build(),
                e -> NetworkInventory.INVENTORY.open(player)));
    }

    public static String camelToPhrase(String str)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.chars().count(); i++){
            val ch = str.charAt(i);
            sb.append(Character.isUpperCase(ch) && i != 0? " " + Character.toLowerCase(ch): ch);
        }
        return sb.toString();
    }
}
