package eu.builderscoffee.commons.bukkit.inventory.network;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.bukkit.CommonsBukkit;
import eu.builderscoffee.commons.bukkit.inventory.templates.DefaultAdminTemplateInventory;
import eu.builderscoffee.commons.common.data.DataManager;
import eu.builderscoffee.commons.common.data.tables.Profil;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * This inventory allows players to create a tournament
 */
public class LanguageInventory extends DefaultAdminTemplateInventory {

    public LanguageInventory() {
        super("Langue", NetworkInventory.INVENTORY);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        for (int i = 0; i < Profil.Languages.values().length; i++) {
            val value = Profil.Languages.values()[i];
            contents.set(2, i, ClickableItem.of(new ItemBuilder(Material.PAINTING).setName(value.name).build(),
                    e ->{
                        val profil = CommonsBukkit.getInstance().getProfilCache().get(player.getUniqueId().toString());
                        profil.setLang(value);
                        DataManager.getProfilStore().update(profil);
                        NetworkInventory.INVENTORY.open(player);
                    }));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        super.update(player, contents);
    }
}
