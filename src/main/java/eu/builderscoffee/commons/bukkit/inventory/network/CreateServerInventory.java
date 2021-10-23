package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.RedisTopic;
import eu.builderscoffee.api.common.redisson.infos.Server;
import eu.builderscoffee.api.common.redisson.packets.types.playpen.actions.DeprovisionServerPacket;
import eu.builderscoffee.api.common.redisson.packets.types.playpen.actions.ProvisionServerPacket;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
import eu.builderscoffee.commons.common.configuration.SettingsConfig;
import lombok.val;
import lombok.var;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.redisson.api.RSortedSet;

public class CreateServerInventory extends DefaultAdminTemplateInventory {

    public CreateServerInventory() {
        super("Create Server", new ServersManagerInventory().INVENTORY);
    }

    private SettingsConfig.PluginMode newServerMode = SettingsConfig.PluginMode.PRODUCTION;

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        // Proxy
        contents.set(2, 2, ClickableItem.of(new ItemBuilder(Material.WOOL, 1, (short) 14).setName("Proxy").addLoreLine("§bServer mode: " + newServerMode).addLoreLine("").addLoreLine("§bShift click pour changer de mode").build(),
                e -> {
                    if(e.isShiftClick()){
                        newServerMode = newServerMode.equals(SettingsConfig.PluginMode.PRODUCTION)? SettingsConfig.PluginMode.DEVELOPMENT : SettingsConfig.PluginMode.PRODUCTION;
                        init(player, contents);
                    }
                    else{
                        final RSortedSet<Server> servers = Redis.getRedissonClient().getSortedSet("servers");
                        val name = "proxy" + (newServerMode.equals(SettingsConfig.PluginMode.DEVELOPMENT)? "-dev" : "") + "-";
                        var exist = true;
                        for(var i = 1; exist; i++){
                            int finalI = i;
                            exist = servers.stream().anyMatch(server -> server.getHostName().equalsIgnoreCase(name + finalI));
                            if(!exist){
                                val packet = new ProvisionServerPacket();
                                packet.setNewServerName(name + i);
                                packet.setNewServerPacketId(newServerMode.equals(SettingsConfig.PluginMode.DEVELOPMENT)? "proxy-dev" : "proxy-prod");
                                packet.setNewServerVersion("promoted");

                                Redis.publish(RedisTopic.PLAYPEN, packet);
                                new ServersManagerInventory().INVENTORY.open(player);
                            }
                        }
                    }
                }));

        // Hub
        contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.WOOL, 1, (short) 2).setName("Hub").addLoreLine("§bServer mode: " + newServerMode).addLoreLine("").addLoreLine("§bShift click pour changer de mode").build(),
                e -> {
                    if(e.isShiftClick()){
                        newServerMode = newServerMode.equals(SettingsConfig.PluginMode.PRODUCTION)? SettingsConfig.PluginMode.DEVELOPMENT : SettingsConfig.PluginMode.PRODUCTION;
                        init(player, contents);
                    }
                    else{
                        final RSortedSet<Server> servers = Redis.getRedissonClient().getSortedSet("servers");
                        val name = "hub" + (newServerMode.equals(SettingsConfig.PluginMode.DEVELOPMENT)? "-dev" : "") + "-";
                        var exist = true;
                        for(var i = 1; exist; i++){
                            int finalI = i;
                            exist = servers.stream().anyMatch(server -> server.getHostName().equalsIgnoreCase(name + finalI));
                            if(!exist){
                                val packet = new ProvisionServerPacket();
                                packet.setNewServerName(name + i);
                                packet.setNewServerPacketId(newServerMode.equals(SettingsConfig.PluginMode.DEVELOPMENT)? "hub-dev" : "hub-prod");
                                packet.setNewServerVersion("promoted");

                                Redis.publish(RedisTopic.PLAYPEN, packet);
                                new ServersManagerInventory().INVENTORY.open(player);
                            }
                        }
                    }
                }));

        // Plot
        contents.set(2, 6, ClickableItem.of(new ItemBuilder(Material.WOOL, 1, (short) 7).setName("Plot").build(),
                e -> new CreatePlotServerInventory().INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        super.update(player, contents);
    }
}
