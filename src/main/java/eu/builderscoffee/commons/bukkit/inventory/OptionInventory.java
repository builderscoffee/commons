package eu.builderscoffee.commons.bukkit.inventory;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.SmartInventory;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.gui.content.SlotPos;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bukkit.configuration.MessageConfiguration;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class OptionInventory extends DefaultAdminTemplateInventory {

    private final MessageConfiguration messages = Main.getInstance().getMessages();
    private final BiConsumer<Player, InventoryClickEvent> acceptedAction;
    private final BiConsumer<Player, InventoryClickEvent> rejectedAction;
    private final String option;

    public OptionInventory(@NonNull String title, @NonNull String option, SmartInventory previousInventory, @NonNull BiConsumer<Player, InventoryClickEvent> acceptedAction, @NonNull BiConsumer<Player, InventoryClickEvent> rejectedAction) {
        super(title, previousInventory, 5, 9);
        this.option = option;
        this.acceptedAction = acceptedAction;
        this.rejectedAction = rejectedAction;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        // Fill Grey panes
        contents.fillRect(SlotPos.of(1, 0), SlotPos.of(1, 8), greyGlasses);
        contents.fillRect(SlotPos.of(2, 0), SlotPos.of(2, 2), greyGlasses);
        contents.fillRect(SlotPos.of(2, 6), SlotPos.of(2, 8), greyGlasses);
        contents.fillRect(SlotPos.of(3, 0), SlotPos.of(3, 8), greyGlasses);

        val item = new ItemBuilder(Material.SIGN);
        if(option.contains("\n")){
            val split = option.split("\n");
            item.setName("§c" + split[0]);
            for(int i = 1; i < split.length; i++){
                item.addLoreLine("§c" + split[i]);
            }
        }
        else{
            item.setName("§c" + option);
        }

        contents.set(0, 4, ClickableItem.empty(item.build()));

        contents.set(2, 3, ClickableItem.of(new ItemBuilder(Material.WOOL, 1, (short) 13).setName("§aOui").build(),
                e -> acceptedAction.accept(player, e)));

        contents.set(2, 5, ClickableItem.of(new ItemBuilder(Material.WOOL, 1, (short) 14).setName("§cNon").build(),
                e -> rejectedAction.accept(player, e)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public enum OptionState{
        NONE,
        ACCEPTED,
        REFUSED
    }
}
