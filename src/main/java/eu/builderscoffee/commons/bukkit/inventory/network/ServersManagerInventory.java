package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.gui.content.SlotIterator;
import eu.builderscoffee.api.bukkit.gui.content.SlotPos;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.redisson.api.RSortedSet;

import java.text.SimpleDateFormat;
import java.util.*;

public class ServersManagerInventory extends DefaultAdminTemplateInventory {

    public ServersManagerInventory() {
        super("Server Manager", NetworkInventory.INVENTORY);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        // Creer un serveur
        contents.set(5, 3, ClickableItem.of(new ItemBuilder(Material.NETHER_STAR).setName("Creer un serveur").build(),
                e -> new CreateServerInventory().INVENTORY.open(player)));

        // Tournois
        contents.set(5, 5, ClickableItem.of(new ItemBuilder(Material.BANNER).setName("Gérer les tournois").build(),
                e -> new TournamentInventory().INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        val serverItems = new ArrayList<ClickableItem>();
        // Récupère les données de serveurs
        final RSortedSet<Server> servers = Redis.getRedissonClient().getSortedSet("servers");

        // Vérifie que la liste existe
        if(servers == null) return;

        // Boucle de tous les serveurs
        if(servers.stream().count() > 0)
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

                        // Créer l'item permettant de click
                        serverItems.add(ClickableItem.of(new ItemBuilder(Material.OBSERVER)
                                .setName(s.getHostName())
                                .addLoreLine(new ArrayList<>(lore))
                                .addLoreLine("")
                                .addLoreLine("§aClic gauche pour gérer")
                                .build(),
                                e -> new ServerManagerInventory(s).INVENTORY.open(player)));
                    });
        // Ajouter les items dans l'inventaire
        contents.pagination().setItems(serverItems.toArray(new ClickableItem[0]));
        contents.pagination().setItemsPerPage(28);

        // Définit comment l'inventaire doit afficher les items
        contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 0)));
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
