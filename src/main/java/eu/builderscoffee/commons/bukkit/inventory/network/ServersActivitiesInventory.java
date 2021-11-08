package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.gui.content.SlotIterator;
import eu.builderscoffee.api.bukkit.gui.content.SlotPos;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.common.data.DataManager;
import eu.builderscoffee.api.common.data.tables.ServerActivityEntity;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * This inventory allows players to manage multiple servers
 */
public class ServersActivitiesInventory extends DefaultAdminTemplateInventory {

    public ServersActivitiesInventory() {
        super("Server Activities", new ServersManagerInventory().INVENTORY, 5, 9);
    }

    private int state;

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);
    }

    @SneakyThrows
    @Override
    public void update(Player player, InventoryContents contents) {
        if(state++ % 20 == 0) return;

        val results = DataManager.getServerActivityStore().select(ServerActivityEntity.class).orderBy(ServerActivityEntity.DATE.desc()).get();
        if(contents.pagination().getItems().length == results.stream().count())
            return;

        val items = new ArrayList<ClickableItem>();

        results.forEach(activity -> {
            val lore = new ArrayList<String>();
            val split = ("§b" + activity.getMessage().replaceAll(activity.getServerName(), "§f" + activity.getServerName() + "§b")).trim().split(" ");

            String line = "";
            for(int i = 0; i < split.length; i++){
                val word = split[i];

                if(word.contains("''"))
                    line += (line.isEmpty()? "" : " ") + "§f" + word.replaceAll("''", "") + "§b";
                else
                    line += (line.isEmpty()? "" : " ") + word;

                if(line.length() > 30 && i != split.length - 1){
                    lore.add("§b" + line);
                    line = "";
                }
            }

            lore.add("§b" + line);

            items.add(ClickableItem.empty(new ItemBuilder(Material.PAPER).setName(activity.getServerName()).addLoreLine(lore).build()));
        });

        // Ajouter les items dans l'inventaire
        contents.pagination().setItems(items.toArray(new ClickableItem[0]));
        contents.pagination().setItemsPerPage(27);

        // Définit comment l'inventaire doit afficher les items
        contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 0)));
    }
}
