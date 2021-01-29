package eu.builderscoffee.commons.utils;

import eu.builderscoffee.commons.Main;
import lombok.val;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

public class LuckPermsUtils {

    public static QueryOptions getQueryOptions(Player player){
        if (Main.getInstance().getLuckyPerms() != null) {
            return Main.getInstance().getLuckyPerms().getContextManager().getQueryOptions(player);
        }
        return null;
    }

    public static String getPrimaryGroup(Player player){
        if (Main.getInstance().getLuckyPerms() != null) {
            val queryOptions = Main.getInstance().getLuckyPerms().getContextManager().getQueryOptions(player);
            return Objects.requireNonNull(Main.getInstance().getLuckyPerms().getUserManager().getUser(player.getName())).getPrimaryGroup();
        }
        return null;
    }

    public static String getPrefix(Player player){
        if (Main.getInstance().getLuckyPerms() != null) {
            final String primaryGroup = getPrimaryGroup(player);
            final QueryOptions queryOptions = getQueryOptions(player);
            val cachedMetaData = Objects.requireNonNull(Main.getInstance().getLuckyPerms().getGroupManager().getGroup(primaryGroup)).getCachedData().getMetaData(queryOptions);
            return cachedMetaData.getPrefix() != null ? cachedMetaData.getPrefix() : "";
        }
        return null;
    }

    public static String getSuffix(Player player){
        if (Main.getInstance().getLuckyPerms() != null) {
            final String primaryGroup = getPrimaryGroup(player);
            final QueryOptions queryOptions = getQueryOptions(player);
            val cachedMetaData = Objects.requireNonNull(Main.getInstance().getLuckyPerms().getGroupManager().getGroup(primaryGroup)).getCachedData().getMetaData(queryOptions);
            return cachedMetaData.getSuffix() != null ? cachedMetaData.getSuffix() : "";
        }
        return null;
    }

    public static int getWeight(Player player){
        if (Main.getInstance().getLuckyPerms() != null) {
            final String primaryGroup = getPrimaryGroup(player);
            final QueryOptions queryOptions = getQueryOptions(player);
            val cachedMetaData = Objects.requireNonNull(Main.getInstance().getLuckyPerms().getGroupManager().getGroup(primaryGroup)).getCachedData().getMetaData(queryOptions);
            return Objects.requireNonNull(Main.getInstance().getLuckyPerms().getGroupManager().getGroup(primaryGroup)).getWeight().getAsInt();
        }
        return -1;
    }
}
