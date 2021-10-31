package eu.builderscoffee.commons.bukkit.commands.manage;

import eu.builderscoffee.commons.bukkit.CommonsBukkit;
import eu.builderscoffee.commons.bukkit.commands.ManageCommand;
import eu.builderscoffee.commons.bukkit.utils.CommandUtils;
import eu.builderscoffee.commons.bukkit.utils.MessageUtils;
import eu.builderscoffee.commons.common.data.DataManager;
import eu.builderscoffee.commons.common.data.tables.BuildbattleThemeEntity;
import lombok.val;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class ManageThemesCommandPart extends ManageCommand.ManageCommandPart {

    @Override
    public String getName() {
        return "themes";
    }

    @Override
    public boolean base(CommandSender sender, String[] args){
        val messages = MessageUtils.getMessageConfig(sender).getCommand();
        if(!sender.hasPermission(CommonsBukkit.getInstance().getPermissions().getCommandManageTheme())){
            sender.sendMessage(messages.getNoPremission());
            return false;
        }

        switch (CommandUtils.getArgument(args, 1).toLowerCase()){
            case "l":
            case "list":
                return list(sender, args);
            case "a":
            case "add":
                return add(sender, args);
            case "u":
            case "update":
                return update(sender, args);
            case "d":
            case "delete":
                return delete(sender, args);
        }
        sender.sendMessage(messages.getManage().getSubPartsOptions().replace("&", "§"));
        sender.sendMessage(messages.getManage().getThemes().getCommandList().replace("&", "§"));
        sender.sendMessage(messages.getManage().getThemes().getCommandAdd().replace("&", "§"));
        sender.sendMessage(messages.getManage().getThemes().getCommandUpdate().replace("&", "§"));
        sender.sendMessage(messages.getManage().getThemes().getCommandDelete().replace("&", "§"));
        return true;
    }

    public boolean list(CommandSender sender, String[] args){
        val messages = MessageUtils.getMessageConfig(sender).getCommand();
        if(!sender.hasPermission(CommonsBukkit.getInstance().getPermissions().getCommandManageThemeList())){
            sender.sendMessage(messages.getNoPremission().replace("&", "§"));
            return false;
        }

        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemes().getList().replace("&", "§"));
        val data = DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).get();
        data.stream().forEach(theme -> sender.sendMessage(messages.getManage().getThemes().getListFormat()
                .replace("&", "§")
                .replace("%name%", theme.getName())));
        return false;
    }

    public boolean add(CommandSender sender, String[] args){
        val messages = MessageUtils.getMessageConfig(sender).getCommand();
        if(!sender.hasPermission(CommonsBukkit.getInstance().getPermissions().getCommandManageThemeChange())){
            sender.sendMessage(messages.getNoPremission().replace("&", "§"));
            return false;
        }

        val name = CommandUtils.getArgument(args, 2);

        if(name.isEmpty()){
            sender.sendMessage(messages.getManage().getThemes().getNameNotEmpty().replace("&", "§"));
            return true;
        }

        if(DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.eq(name)).get().stream().count() > 0){
            sender.sendMessage(messages.getManage().getThemes().getNameAlreadyExist().replace("&", "§"));
            return true;
        }

        val entity = new BuildbattleThemeEntity();
        entity.setName(name);

        DataManager.getBuildbattleThemeStore().insert(entity);

        sender.sendMessage(messages.getManage().getThemes().getAdded().replace("&", "§"));

        return false;
    }

    public boolean update(CommandSender sender, String[] args){
        val messages = MessageUtils.getMessageConfig(sender).getCommand();
        if(!sender.hasPermission(CommonsBukkit.getInstance().getPermissions().getCommandManageThemeChange())){
            sender.sendMessage(messages.getNoPremission().replace("&", "§"));
            return false;
        }

        val oldName = CommandUtils.getArgument(args, 2);
        val newName = CommandUtils.getArgument(args, 3);

        if(oldName.isEmpty()){
            sender.sendMessage(messages.getManage().getThemes().getNameNotEmpty().replace("&", "§"));
            return true;
        }

        if(newName.isEmpty()){
            sender.sendMessage(messages.getManage().getThemes().getNewNameNotEmpty().replace("&", "§"));
            return true;
        }

        val entity = DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.lower().eq(oldName.toLowerCase())).get().firstOrNull();
        if(Objects.isNull(entity)){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemes().getNameNotExist().replace("&", "§"));
            return true;
        }

        if(DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.lower().eq(newName.toLowerCase())).get().stream().count() > 0){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemes().getNameAlreadyExist().replace("&", "§"));
            return true;
        }

        entity.setName(newName);

        DataManager.getBuildbattleThemeStore().update(entity);

        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemes().getUpdated().replace("&", "§"));

        return false;
    }

    public boolean delete(CommandSender sender, String[] args){
        val messages = MessageUtils.getMessageConfig(sender).getCommand();
        if(!sender.hasPermission(CommonsBukkit.getInstance().getPermissions().getCommandManageThemeChange())){
            sender.sendMessage(messages.getNoPremission().replace("&", "§"));
            return false;
        }

        val name = CommandUtils.getArgument(args, 2);

        if(name.isEmpty()){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemes().getNameNotEmpty().replace("&", "§"));
            return true;
        }

        val entity = DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.lower().eq(name.toLowerCase())).get().firstOrNull();
        if(Objects.isNull(entity)){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemes().getNameNotExist().replace("&", "§"));
            return true;
        }

        DataManager.getBuildbattleThemeStore().delete(entity);

        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemes().getDeleted().replace("&", "§"));

        return false;
    }
}
