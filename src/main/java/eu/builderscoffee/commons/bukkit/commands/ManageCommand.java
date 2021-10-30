package eu.builderscoffee.commons.bukkit.commands;

import eu.builderscoffee.commons.bukkit.utils.CommandUtils;
import eu.builderscoffee.commons.bukkit.utils.MessageUtils;
import eu.builderscoffee.commons.common.data.DataManager;
import eu.builderscoffee.commons.common.data.tables.BuildbattleThemeEntity;
import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * This command allows to broadcast a message on the server
 */
public class ManageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //if(!sender.hasPermission())

        switch (CommandUtils.getArgument(args, 0).toLowerCase()){
            case "themes":
                return themes(sender, args);
        }
        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getCommandAvailbableOption());
        sender.sendMessage("§7/manage §6themes");
        return true;
    }

    private boolean themes(CommandSender sender, String[] args){
        switch (CommandUtils.getArgument(args, 1).toLowerCase()){
            case "l":
            case "list":
                return themesList(sender, args);
            case "c":
            case "create":
                return themesCreate(sender, args);
            case "u":
            case "update":
                return themesUpdate(sender, args);
            case "d":
            case "delete":
                return themesDelete(sender, args);
        }
        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getCommandAvailbableOption());
        sender.sendMessage("§7/manage themes §6list");
        sender.sendMessage("§7/manage themes §6create");
        sender.sendMessage("§7/manage themes §6update");
        sender.sendMessage("§7/manage themes §6delete");
        return true;
    }

    private boolean themesList(CommandSender sender, String[] args){
        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemesList());
        val data = DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).get();
        data.stream().forEach(theme -> sender.sendMessage("§7 - " + theme.getName()));
        return false;
    }

    private boolean themesCreate(CommandSender sender, String[] args){
        val name = CommandUtils.getArgument(args, 2);

        if(name.isEmpty()){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemeNameNotEmpty());
            return true;
        }

        if(DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.eq(name)).get().stream().count() > 0){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemeNameAlreadyExist());
            return true;
        }

        val entity = new BuildbattleThemeEntity();
        entity.setName(name);

        DataManager.getBuildbattleThemeStore().insert(entity);

        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemeAdded());

        return false;
    }

    private boolean themesUpdate(CommandSender sender, String[] args){
        val oldName = CommandUtils.getArgument(args, 2);
        val newName = CommandUtils.getArgument(args, 3);

        if(oldName.isEmpty()){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemeNameNotEmpty());
            return true;
        }

        if(newName.isEmpty()){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemeNewNameNotEmpty());
            return true;
        }

        if(DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.lower().eq(oldName.toLowerCase())).get().stream().count() == 0){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemeNameNotExist());
            return true;
        }

        if(DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.lower().eq(newName.toLowerCase())).get().stream().count() > 0){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemeNameAlreadyExist());
            return true;
        }

        val entity = DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.lower().eq(oldName.toLowerCase())).get().firstOrNull();
        entity.setName(newName);

        DataManager.getBuildbattleThemeStore().update(entity);

        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemeUpdated());

        return false;
    }

    private boolean themesDelete(CommandSender sender, String[] args){
        val name = CommandUtils.getArgument(args, 2);

        if(name.isEmpty()){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemeNameNotEmpty());
            return true;
        }

        if(DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.lower().eq(name.toLowerCase())).get().stream().count() == 0){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemeNameNotExist());
            return true;
        }

        DataManager.getBuildbattleThemeStore().delete(DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.lower().eq(name.toLowerCase())).get());

        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemeDeleted());

        return false;
    }
}
