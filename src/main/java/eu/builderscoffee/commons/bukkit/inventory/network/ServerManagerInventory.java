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
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.commons.common.redisson.topics.CommonTopics;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.redisson.api.RSortedSet;

import java.text.SimpleDateFormat;
import java.util.*;

public class ServerManagerInventory implements InventoryProvider {

    public final SmartInventory INVENTORY;
    private static final ClickableItem blackGlasses = ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15)).setName("§a").build());
    private static final ClickableItem greyGlasses = ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setName("§a").build());

    private final Main main = Main.getInstance();
    private final MessageConfiguration messages = main.getMessages();
    private final Server server;

    public ServerManagerInventory(Server server) {
        this.server = server;
        this.INVENTORY = SmartInventory.builder()
                .id("server_manager_" + server.getHostName())
                .provider(this)
                .size(6, 9)
                .title(ChatColor.WHITE + server.getHostName())
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

        // Stop item
        val stopItem = new ItemBuilder(Material.WOOL, 1, (short) 14).setName("Stopper le serveur");
        if(!server.getStartingMethod().equals(Server.ServerStartingMethod.DYNAMIC))
            stopItem.addLoreLine("§cImpossible de stopper ce type de serveur pour le moment.");
        contents.set(3, 3, ClickableItem.of(stopItem.build(),
                e -> {
                    if(server.getStartingMethod().equals(Server.ServerStartingMethod.DYNAMIC))
                        server.stop();
                }));

        // Demander au serveur si une configuration est possible ou néscessaire
        val configPacket = new ServerManagerRequest();
        configPacket.setTargetServerName(server.getHostName());
        configPacket.setAction("requestConfig");
        configPacket.onResponse = responsePacket -> {
            val response = (ServerManagerResponse) responsePacket;
            response.getItems().forEach(item -> {
                int i1 = item.getT1();
                int i2 = item.getT2();
                // Check si l'emplacement des items est choisis
                if(i1 == -1 || i2 == -1){
                    // TODO if correct => Choose any place where item can be put
                }
                contents.set(i1, i2, ClickableItem.of(item.getT3(), e -> {
                    // Creer une action de la config custom
                    val actionPacket = new ServerManagerRequest();
                    actionPacket.setTargetServerName(server.getHostName());
                    actionPacket.setAction(item.getT4());
                    // Envoyer la réponse
                    Redis.publish(CommonTopics.SERVER_MANAGER, actionPacket);
                }));
            });
        };

        // Envoyer la demande de config
        Redis.publish(CommonTopics.SERVER_MANAGER, configPacket);

        // Retour
        contents.set(5, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).setName(messages.getRetourItem().replace("&", "§")).build(),
                e -> new ServersManagerInventory().INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
