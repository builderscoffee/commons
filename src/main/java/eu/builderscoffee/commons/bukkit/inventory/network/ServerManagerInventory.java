package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.RedisTopic;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.api.common.redisson.packets.types.playpen.actions.FreezeServerPacket;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
import eu.builderscoffee.commons.common.configuration.SettingsConfig;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.commons.common.redisson.topics.CommonTopics;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class ServerManagerInventory extends DefaultAdminTemplateInventory {

    private final Server server;

    public ServerManagerInventory(Server server) {
        super(server.getHostName(), new ServersManagerInventory().INVENTORY, 5, 9);
        this.server = server;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        // Stop item
        val stopItem = new ItemBuilder(Material.CONCRETE, 1, (short) 14).setName("Stopper le serveur");
        if(!server.getStartingMethod().equals(Server.ServerStartingMethod.DYNAMIC))
            stopItem.addLoreLine("§cImpossible de stopper ce type de serveur pour le moment.");
        contents.set(0, 8, ClickableItem.of(stopItem.build(),
                e -> {
                    if(server.getStartingMethod().equals(Server.ServerStartingMethod.DYNAMIC))
                    {
                        server.stop();
                        new ServersManagerInventory().INVENTORY.open(player);
                    }
                }));

        // Freeze
        if(server.getStartingMethod().equals(Server.ServerStartingMethod.DYNAMIC))
            contents.set(0, 7, ClickableItem.of(new ItemBuilder(Material.PACKED_ICE).setName("Freeze").build(),
                    e -> {
                        server.freeze();
                        new ServersManagerInventory().INVENTORY.open(player);
                    }));

        // État
        val lore = new TreeSet<String>();
        lore.add("§bStarting method: §a" + server.getStartingMethod());
        lore.add("§bServer status: §a" + server.getServerStatus());
        lore.add("§bServerType: §a" + server.getServerType());
        lore.add("§bLast heartbeat at §a" + new SimpleDateFormat("EEE dd MMM yyyy à hh:mm:ss", Locale.FRANCE).format(server.getLastHeartbeat()));
        lore.add("§bPlayers: §a" + server.getPlayerCount());
        lore.add("§bMaximum players: §a" + server.getPlayerMaximum());
        server.getProperties().forEach((key, value)->{
            if(value instanceof Date)
                value = new SimpleDateFormat("EEE dd MMM yyyy à hh:mm:ss", Locale.FRANCE).format((Date) value);
            lore.add("§b" + key + ": §a" + value);
        });
        contents.set(0, 4, ClickableItem.empty(new ItemBuilder(Material.OBSERVER)
                .setName("État")
                .addLoreLine(new ArrayList<>(lore))
                .build()));

        // Demander au serveur si une configuration est possible ou néscessaire
        val configPacket = new ServerManagerRequest();
        configPacket.setTargetServerName(server.getHostName());
        configPacket.setAction("requestConfig");
        configPacket.onResponse = response -> response.getItems().forEach(item -> {
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

        // Envoyer la demande de config
        Redis.publish(CommonTopics.SERVER_MANAGER, configPacket);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
