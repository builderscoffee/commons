package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
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
        super(server.getHostName(), new ServersManagerInventory().INVENTORY);
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
                        server.stop();
                }));

        // Freeze
        contents.set(0, 7, ClickableItem.of(new ItemBuilder(Material.PACKED_ICE).setName("Freeze").build(),
                e -> player.sendMessage("§cnot yet implemented")));

        // état
        val lore = new TreeSet<String>();
        Arrays.stream(server.getClass().getMethods())
            .filter(m -> m.getName().startsWith("get") &&
                    m.getParameterTypes().length == 0 &&
                    !m.getName().equalsIgnoreCase("getHostAddress") &&
                    !m.getName().equalsIgnoreCase("getHostPort") &&
                    !m.getName().equalsIgnoreCase("getHostName") &&
                    !m.getName().equalsIgnoreCase("getClass"))
            .forEach(m -> {
                try {
                    Object result = m.invoke(server);
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
        contents.set(0, 4, ClickableItem.empty(new ItemBuilder(Material.PAPER)
                .setName("État")
                .addLoreLine(new ArrayList<>(lore))
                .build()));

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
    }

    @Override
    public void update(Player player, InventoryContents contents) {

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
