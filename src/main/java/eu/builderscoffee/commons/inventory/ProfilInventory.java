package eu.builderscoffee.commons.inventory;

import eu.builderscoffee.api.gui.ClickableItem;
import eu.builderscoffee.api.gui.SmartInventory;
import eu.builderscoffee.api.gui.content.InventoryContents;
import eu.builderscoffee.api.gui.content.InventoryProvider;
import eu.builderscoffee.api.gui.content.SlotPos;
import eu.builderscoffee.api.utils.ItemBuilder;
import eu.builderscoffee.commons.Main;
import eu.builderscoffee.commons.configuration.MessageConfiguration;
import eu.builderscoffee.commons.data.*;
import eu.builderscoffee.commons.utils.LuckPermsUtils;
import eu.builderscoffee.commons.utils.SkullCreator;
import io.requery.sql.EntityDataStore;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProfilInventory implements InventoryProvider {

    public final SmartInventory INVENTORY;

    private final Main main = Main.getInstance();
    private final MessageConfiguration messages = main.getMessages();

    private final ProfilEntity profilEntity;

    private static final ClickableItem blackGlasses = ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
    private static final ClickableItem greyGlasses = ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
    private static final ClickableItem lightgreyGlasses = ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8));

    private static final String GLOBE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEwNzdlODcxZDkxYjdmZWEyNGIxZjY4MDhlMDg4ZDdiODQyZGE1MTZmNjM1NmNlOTE2MTM0OTQ0MzFhZThhMCJ9fX0=";


    public ProfilInventory(ProfilEntity profilEntity){
        if(profilEntity == null)
            throw new NullPointerException("Profile can't be null");

        this.profilEntity = profilEntity;

        INVENTORY = SmartInventory.builder()
                .id("profile")
                .provider(this)
                .size(6, 9)
                .title(ChatColor.WHITE + "Profil >> " + profilEntity.getName())
                .manager(Main.getInstance().getInventoryManager())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        //Fill Black borders
        contents.fillRect(SlotPos.of(0, 0), SlotPos.of(5, 0), blackGlasses);
        contents.fillRect(SlotPos.of(0, 8), SlotPos.of(5, 8), blackGlasses);
        //Fill Grey borders
        contents.fillRect(SlotPos.of(0, 1), SlotPos.of(5, 1), greyGlasses);
        contents.fillRect(SlotPos.of(0, 7), SlotPos.of(5, 7), greyGlasses);
        // Fill Light Grey line
        contents.fillRect(SlotPos.of(0, 3), SlotPos.of(0, 5), lightgreyGlasses);
        contents.fillRect(SlotPos.of(5, 3), SlotPos.of(5, 5), greyGlasses);

        // Fill isolate grey glasses
        contents.set(SlotPos.of(0, 2), greyGlasses);
        contents.set(SlotPos.of(0, 6), greyGlasses);
        contents.set(SlotPos.of(5, 2), greyGlasses);
        contents.set(SlotPos.of(5, 6), greyGlasses);

        // Skull lore information
        val participations = profilEntity.getNotes().stream()
                .filter(distinctByKeys(NoteEntity::getSaison, NoteEntity::getBuildbattle))
                .count();
        val primaryGroup = LuckPermsUtils.getPrimaryGroup(player).substring(0, 1).toUpperCase() + LuckPermsUtils.getPrimaryGroup(player).substring(1);

        // Profile skull builder
        val ibSkull = new ItemBuilder(SkullCreator.itemFromUuid(UUID.fromString(profilEntity.getUniqueId())))
                .setName(messages.getProfilSkullItem()
                        .replace("%player%", profilEntity.getName())
                        .replace("&", "§"));

        // Add replaced lore to skull
        messages.getProfilSkullLore().forEach(s -> ibSkull.addLoreLine(s.replace("%participations%", participations + "")
                .replace("%grade%", primaryGroup)
                .replace("&", "§")));

        // Add skul to inventory
        val skull = ibSkull.build();
        contents.set(SlotPos.of(1, 4), ClickableItem.empty(skull));

        // Saisons
        contents.set(3, 3, ClickableItem.of(new ItemBuilder(Material.PAINTING).setName(messages.getProfilResultatItem().replace("&", "§")).build(),
                e -> {
                    List<BuildbattleEntity> buildbattles = new ArrayList<>();

                    // Store played saisons
                    profilEntity.getNotes().stream()
                            .filter(distinctByKeys(NoteEntity::getSaison, NoteEntity::getBuildbattle))
                            .forEach(note -> buildbattles.add(note.getBuildbattle()));


                    if(!buildbattles.isEmpty()){
                        // Sort saisons per date
                        Collections.sort(buildbattles, new Comparator<BuildbattleEntity>() {
                            public int compare(BuildbattleEntity o1, BuildbattleEntity o2) {
                                return o1.getDate().compareTo(o2.getDate());
                            }
                        });

                        // Open invetory of the last saison played
                        new NoteInventory(profilEntity, buildbattles.get(0)).INVENTORY.open(player);
                    }
                    else{
                        player.sendMessage(messages.getNotPlayedAnyBuildbattle().replace("&", "§"));
                    }
                }));

        // Derniers Résultats
        val saisons = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setName(messages.getProfilSaisons().replace("&", "§")).build();
        SkullCreator.itemWithBase64(saisons, GLOBE);
        contents.set(3, 5, ClickableItem.of(saisons,
                e -> {
                    new SaisonsInventory(profilEntity).INVENTORY.open(player);
                }));

        // Quitter
        contents.set(5, 4, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName(messages.getCloseItem().replace("&", "§")).build(),
                e -> contents.inventory().close(player)));


    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Nothing to do here
    }

    private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors)
    {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t ->
        {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());

            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }
}
