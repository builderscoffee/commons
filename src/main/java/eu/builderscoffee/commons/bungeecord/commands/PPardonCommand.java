package eu.builderscoffee.commons.bungeecord.commands;

import eu.builderscoffee.commons.bungeecord.Main;
import eu.builderscoffee.commons.bungeecord.utils.TextComponentUtil;
import eu.builderscoffee.commons.common.data.BanEntity;
import eu.builderscoffee.commons.common.data.ProfilEntity;
import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class PPardonCommand extends Command {

    public PPardonCommand() {
        super("ppardon", Main.getInstance().getMessages().getPpardonPremission());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission(Main.getInstance().getMessages().getPbanPremission())){
            sender.sendMessage(TextComponentUtil.decodeColor(Main.getInstance().getMessages().getNoPermission()));
            return;
        }

        if(args.length < 1){
            sender.sendMessage(TextComponentUtil.decodeColor("§c/ppardon <player>"));
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

        if(ban == null){
            sender.sendMessage(TextComponentUtil.decodeColor("§cCe joueur n'est pas banni"));
            return;
        }

        banStore.delete(ban);
        sender.sendMessage(TextComponentUtil.decodeColor("§aVous avez débanni " + profil.getName()));
        Main.getInstance().getLogger().info("§7" + sender.getName() + " §8débanni §7" + profil.getName());
    }
}
