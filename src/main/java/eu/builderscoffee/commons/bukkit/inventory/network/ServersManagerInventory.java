package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.gui.content.SlotIterator;
import eu.builderscoffee.api.bukkit.gui.content.SlotPos;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.commons.bukkit.CommonsBukkit;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
import eu.builderscoffee.commons.bukkit.utils.BungeeUtils;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.redisson.api.RSortedSet;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This inventory allows players to manage multiple servers
 */
public class ServersManagerInventory extends DefaultAdminTemplateInventory {

    public ServersManagerInventory() {
        super("Server Manager", NetworkInventory.INVENTORY, 5, 9);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        // Creer un serveur
        contents.set(rows - 1, 3, ClickableItem.of(new ItemBuilder(Material.NETHER_STAR).setName("Creer un serveur").build(),
                e -> new CreateServerInventory().INVENTORY.open(player)));

        // Tournois
        contents.set(rows - 1, 5, ClickableItem.of(new ItemBuilder(Material.BANNER).setName("Gérer les tournois").build(),
                e -> new TournamentInventory().INVENTORY.open(player)));

        // Server Activities
        contents.set(rows - 1, 7, ClickableItem.of(new ItemBuilder(Material.SIGN).setName("Activités des serveurs").build(),
                e -> new ServersActivitiesInventory().INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        val serverItems = new ArrayList<ClickableItem>();
        // Récupère les données de serveurs
        final RSortedSet<Server> servers = Redis.getRedissonClient().getSortedSet("servers");

        // Vérifie que la liste existe
        if (servers == null) return;

        // Creating temporary servers list to avoid changes
        val tempServers = servers.stream().collect(Collectors.toList());

        // Boucle de tous les serveurs
        if (tempServers.stream().count() > 0)
            tempServers.stream()
                    .sorted()
                    .forEach(s -> {
                        val itemB = new ItemBuilder(Material.OBSERVER)
                                .setName(s.getHostName() + " §7(" + s.getServerStatus().name().toLowerCase() + ")")
                                .addLoreLine("§f" + capitalizeFirstLetter(s.getStartingMethod().name()) + " " + s.getServerType().name().toLowerCase() + " §bserver")
                                .addLoreLine("§f" + s.getPlayerCount() + "§b/§f" + s.getPlayerMaximum() + " §bjoueurs");

                        if (s.getProperties().entrySet().size() > 0) {
                            itemB.addLoreLine("§bDonnées supplémentaires: ");
                            itemB.addLoreLine(s.getProperties().entrySet().stream()
                                    .map(entry -> "  §b" + entry.getKey() + ": §a" + entry.getValue())
                                    .sorted(String::compareTo)
                                    .collect(Collectors.toList()));
                        }
                        serverItems.add(ClickableItem.of(itemB
                                        .addLoreLine("")
                                        .addLoreLine("§aClic gauche pour gérer")
                                        .addLoreLine("§aClic droit pour y aller")
                                        .build(),
                                e -> {
                                    if (e.isLeftClick())
                                        new ServerManagerInventory(s).INVENTORY.open(player);
                                    else if (e.isRightClick())
                                        BungeeUtils.sendPlayerToServer(CommonsBukkit.getInstance(), player, s.getHostName());
                                }));
                    });
        // Ajouter les items dans l'inventaire
        contents.pagination().setItems(serverItems.toArray(new ClickableItem[0]));
        contents.pagination().setItemsPerPage(27);

        // Définit comment l'inventaire doit afficher les items
        contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 0)));
    }

    private String capitalizeFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
