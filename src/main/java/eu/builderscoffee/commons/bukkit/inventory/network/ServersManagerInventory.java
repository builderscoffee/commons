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
import java.util.ArrayList;
import java.util.Locale;
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
                        // Créer l'item permettant de click
                        serverItems.add(ClickableItem.of(new ItemBuilder(Material.OBSERVER)
                                        .setName(s.getHostName())
                                        .addLoreLine("§bLast heartbeat at §a" + new SimpleDateFormat("EEE dd MMM yyyy à hh:mm:ss", Locale.FRANCE).format(s.getLastHeartbeat()))
                                        .addLoreLine("§bServerType: §a" + s.getServerType())
                                        .addLoreLine("§bStarting method: §a" + s.getStartingMethod())
                                        .addLoreLine("§bServer status: §a" + s.getServerStatus())
                                        .addLoreLine("§bPlayers: §a" + s.getPlayerCount())
                                        .addLoreLine("§bMaximum players: §a" + s.getPlayerMaximum())
                                        .addLoreLine(s.getProperties().entrySet().stream()
                                                .map(entry -> "§b" + entry.getKey() + ": §a" + entry.getValue())
                                                .sorted(String::compareTo)
                                                .collect(Collectors.toList()))
                                        .addLoreLine("")
                                        .addLoreLine("§aClic gauche pour gérer")
                                        .build(),
                                e -> new ServerManagerInventory(s).INVENTORY.open(player)));
                    });
        // Ajouter les items dans l'inventaire
        contents.pagination().setItems(serverItems.toArray(new ClickableItem[0]));
        contents.pagination().setItemsPerPage(27);

        // Définit comment l'inventaire doit afficher les items
        contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 0)));
    }
}
