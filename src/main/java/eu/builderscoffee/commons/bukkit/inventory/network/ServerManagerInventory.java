package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.gui.content.SlotIterator;
import eu.builderscoffee.api.bukkit.gui.content.SlotPos;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.bukkit.utils.serializations.SingleItemSerialization;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.commons.common.redisson.topics.CommonTopics;
import eu.builderscoffee.commons.common.utils.Triplet;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.redisson.api.RSortedSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This inventory allows players to manager a specific server
 */
public class ServerManagerInventory extends DefaultAdminTemplateInventory {

    @Getter
    private static ArrayList<Triplet<Player, ServerManagerInventory, String>> chatRequests = new ArrayList<>();

    private final Server server;
    @Getter
    private InventoryContents contents;
    @Setter
    private boolean requestConfigOnOpen = true;

    public ServerManagerInventory(Server server) {
        super(server.getHostName(), new ServersManagerInventory().INVENTORY, 5, 9);
        this.server = server;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        // Stop item
        val stopItem = new ItemBuilder(Material.CONCRETE, 1, (short) 14).setName("Stopper le serveur");
        if (!server.getStartingMethod().equals(Server.ServerStartingMethod.DYNAMIC))
            stopItem.addLoreLine("§cImpossible de stopper ce type de serveur pour le moment.");
        contents.set(0, 8, ClickableItem.of(stopItem.build(),
                e -> {
                    if (server.getStartingMethod().equals(Server.ServerStartingMethod.DYNAMIC)) {
                        server.stop();
                        new ServersManagerInventory().INVENTORY.open(player);
                    }
                }));

        // Freeze
        if (server.getStartingMethod().equals(Server.ServerStartingMethod.DYNAMIC))
            contents.set(0, 7, ClickableItem.of(new ItemBuilder(Material.PACKED_ICE).setName("Freeze").build(),
                    e -> {
                        server.freeze();
                        new ServersManagerInventory().INVENTORY.open(player);
                    }));

        // Demander au serveur si une configuration est possible ou néscessaire
        if(requestConfigOnOpen) sendConfigRequest(player, "request_config", "", contents);
        requestConfigOnOpen = true;

        this.contents = contents;
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        final RSortedSet<Server> servers = Redis.getRedissonClient().getSortedSet("servers");

        // Vérifie que la liste existe
        if (servers == null) return;

        //if (servers.stream().filter(s -> s.getHostName().equals(server.getHostName())).count() == 0)
        //    new ServersManagerInventory().INVENTORY.open(player);
        //else
        servers.stream().filter(s -> s.getHostName().equals(server.getHostName())).forEach(s -> {
            // État
            contents.set(0, 4, ClickableItem.empty(new ItemBuilder(Material.OBSERVER)
                    .setName("État")
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
                    .build()));
        });

        this.contents = contents;
    }

    public void sendConfigRequest(@NonNull Player player, @NonNull String type, @NonNull String data, @NonNull InventoryContents contents) {
        // Create request
        val configPacket = new ServerManagerRequest();

        System.out.println("Send " + type);

        // Define target server & action
        configPacket.setTargetServerName(server.getHostName());
        configPacket.setType(type);
        configPacket.setData(data);

        // Show items on response
        configPacket.onResponse = response -> {
            // create list to temporary store items
            val configItems = new ArrayList<ClickableItem>();

            // loop all items
            response.getActions().forEach(action -> {
                if (action instanceof ServerManagerResponse.Items) {
                    val itemsAction = (ServerManagerResponse.Items) action;

                    itemsAction.getItems().forEach(itemInfo -> {
                        val i1 = itemInfo.getFirst();
                        val i2 = itemInfo.getSecond();
                        val item = ClickableItem.of(SingleItemSerialization.getItem(itemInfo.getThird()), e -> {
                            if (!response.isFinished()) sendConfigRequest(player, itemsAction.getType(), itemInfo.getFourth(), contents);
                        });

                        // slot hasn't been chosen
                        if (i1 == -1 || i2 == -1)
                            configItems.add(item);
                        // slot has been chosen
                        else
                            contents.set(i1, i2, item);
                    });
                } else if (action instanceof ServerManagerResponse.ChatRequest) {
                    val chatRequestAction = (ServerManagerResponse.ChatRequest) action;

                    if (Objects.nonNull(chatRequestAction.getMessage()))
                        player.sendMessage(chatRequestAction.getMessage());

                    chatRequests.add(new Triplet<>(player, this, chatRequestAction.getType()));
                    player.closeInventory();
                    return;
                }
            });
            if (response.isFinished()) {
                new ServersManagerInventory().INVENTORY.open(player);
            }


            // Set items in pagination system
            contents.pagination().setItems(configItems.toArray(new ClickableItem[0]));
            contents.pagination().setItemsPerPage(configItems.size() > 27? 27 : configItems.size());

            // Define how items are placed in inv
            contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 0)));
        };

        // Send request
        Redis.publish(CommonTopics.SERVER_MANAGER, configPacket);
    }
}
