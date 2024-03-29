package eu.builderscoffee.commons.bungeecord.commands;

import eu.builderscoffee.commons.bungeecord.Main;
import eu.builderscoffee.commons.bungeecord.utils.DateUtil;
import eu.builderscoffee.commons.bungeecord.utils.TextComponentUtil;
import eu.builderscoffee.commons.common.data.BanEntity;
import eu.builderscoffee.commons.common.data.ProfilEntity;
import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PBanCommand extends Command {

    public PBanCommand() {
        super("pban", Main.getInstance().getMessages().getPbanPremission());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(TextComponentUtil.decodeColor("§c/pban <player> [time] [reason]"));
            return;
        }

        val profileStore = Main.getInstance().getProfilStore();
        ProfilEntity profil = profileStore.select(ProfilEntity.class)
                .where(ProfilEntity.NAME.lower().eq(args[0].toLowerCase()))
                .get().firstOrNull();
        if(profil == null){
            sender.sendMessage(TextComponentUtil.decodeColor("§cCe joueur n'existe pas"));
            return;
        }

        val banStore = Main.getInstance().getBanStore();
        val ban = banStore.select(BanEntity.class)
                .where(BanEntity.PROFILE.eq(profil))
                .get().firstOrNull();

        if(ban != null){
            sender.sendMessage(TextComponentUtil.decodeColor("§cCe joueur est déja banni"));
            return;
        }

        long banTimestamp = 0;
        final String time;
        String banReason = null;

        if (args.length > 1) {
            time = args[1];
            try {
                banTimestamp = DateUtil.parseDateDiff(time, true);
                banReason = getFinalArg(args, 2);
            } catch (final Exception e) {
                banReason = getFinalArg(args, 1);
            }
        }

        if(banTimestamp <= 0){
            final Calendar c = new GregorianCalendar();
            c.add(Calendar.YEAR, 10);
            banTimestamp = c.getTimeInMillis();
        }

        if(banReason == null){
            banReason = "The Ban Hammer has spoken!";
        }

        Timestamp timestamp = new Timestamp(banTimestamp);

        System.out.println(timestamp.getTime());

        val banEntity = new BanEntity();
        banEntity.setProfile(profil);
        banEntity.setReason(banReason);
        banEntity.setDateEnd(timestamp);

        banStore.insert(banEntity);

        String message = "";
        for (String s : Main.getInstance().getMessages().getBanMessage()) {
            String line = s.replace("%reason%", banReason)
                    .replace("%time%", DateUtil.formatDateDiff(banTimestamp))
                    .replace("&", "§");
            message += line + "\n";
        }

        final String finalMessage = message;
        final long finalBanTimestamp = banTimestamp;
        final String finalBanReason = banReason;

        ProxyServer.getInstance().getPlayers().stream()
                .filter(player -> player.getUniqueId().toString().equals(profil.getUniqueId()))
                .forEach(player -> player.disconnect(TextComponentUtil.decodeColor(finalMessage)));

        ProxyServer.getInstance().getPlayers().stream()
                .forEach(player -> player.sendMessage(TextComponentUtil.decodeColor(Main.getInstance().getMessages().getBanBroadcastMessage().replace("%author%", sender.getName())
                        .replace("%player%", profil.getName())
                        .replace("%time%", DateUtil.formatDateDiff(finalBanTimestamp))
                        .replace("%reason%", finalBanReason)
                        .replace("&", "§"))));

        System.out.println(Main.getInstance().getMessages().getBanBroadcastMessage().replace("%author%", sender.getName())
                .replace("%player%", profil.getName())
                .replace("%time%", DateUtil.formatDateDiff(banTimestamp))
                .replace("%reason%", banReason)
                .replace("&", "§"));
    }

    private String getFinalArg(final String[] args, final int start) {
        final StringBuilder bldr = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i != start) {
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }
        return bldr.toString();
    }
}
