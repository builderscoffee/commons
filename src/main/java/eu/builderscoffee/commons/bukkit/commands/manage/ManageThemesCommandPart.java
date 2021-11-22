package eu.builderscoffee.commons.bukkit.commands.manage;

import eu.builderscoffee.api.common.data.DataManager;
import eu.builderscoffee.api.common.data.tables.BuildbattleThemeEntity;
import eu.builderscoffee.commons.bukkit.CommonsBukkit;
import eu.builderscoffee.commons.bukkit.commands.ManageCommand;
import eu.builderscoffee.commons.common.utils.CommandUtils;
import eu.builderscoffee.commons.bukkit.utils.MessageUtils;
import lombok.val;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
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
        data.stream().forEach(theme -> {
            sender.sendMessage(messages.getManage().getThemes().getListFormat()
                    .replace("&", "§")
                    .replace("%id%", String.valueOf(theme.getId())));
            theme.getNames().forEach(translation -> {
                sender.sendMessage(messages.getManage().getThemes().getListFormatNames()
                        .replace("%lang%", translation.getLanguage().name())
                        .replace("%name%", translation.getName()));
            });
        });
        return false;
    }

    public boolean add(CommandSender sender, String[] args){
        /*val messages = MessageUtils.getMessageConfig(sender).getCommand();
        if(!sender.hasPermission(CommonsBukkit.getInstance().getPermissions().getCommandManageThemeChange())){
            sender.sendMessage(messages.getNoPremission().replace("&", "§"));
            return false;
        }

        if(CommandUtils.getArgument(args, 2).isEmpty()){
            sender.sendMessage(messages.getManage().getThemes().getNameNotEmpty().replace("&", "§"));
            return true;
        }

        val arguments = new ArrayList<String>();
        for(int i = 2; i < args.length; i++){
            arguments.add(args[i]);
        }

        val split = String.join(" ", arguments).split(",");
        val entities = new ArrayList<BuildbattleThemeEntity>();

        for (String value : split) {
            val name = value.trim();

            if(DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.eq(name)).get().stream().count() > 0){
                sender.sendMessage(messages.getManage().getThemes().getNameAlreadyExist()
                        .replace("&", "§")
                        .replace("%name%", name));
                return true;
            }

            val entity = new BuildbattleThemeEntity();
            entity.setName(name);

            entities.add(entity);
        }

        entities.forEach(DataManager.getBuildbattleThemeStore()::insert);

        sender.sendMessage(messages.getManage().getThemes().getAdded().replace("&", "§"));*/

        System.out.println("§6Work in progress");

        return false;
    }

    public boolean update(CommandSender sender, String[] args){
        /*val messages = MessageUtils.getMessageConfig(sender).getCommand();
        if(!sender.hasPermission(CommonsBukkit.getInstance().getPermissions().getCommandManageThemeChange())){
            sender.sendMessage(messages.getNoPremission().replace("&", "§"));
            return false;
        }

        val arguments = new ArrayList<String>();
        for(int i = 2; i < args.length; i++){
            arguments.add(args[i]);
        }

        val split = String.join(" ", arguments).split(",");

        if(split.length != 2){
            sender.sendMessage(messages.getManage().getThemes().getNamesUpdateNotCorrectFilled().replace("&", "§"));
            sender.sendMessage("§cExample: /manage themes update oldname, newname");
            return false;
        }

        val oldName = split[0].trim();
        val newName = split[1].trim();

        if(oldName.isEmpty()){
            sender.sendMessage(messages.getManage().getThemes().getNameNotEmpty().replace("&", "§"));
            return false;
        }

        if(newName.isEmpty()){
            sender.sendMessage(messages.getManage().getThemes().getNewNameNotEmpty().replace("&", "§"));
            return false;
        }

        val entity = DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.lower().eq(oldName.toLowerCase())).get().firstOrNull();
        if(Objects.isNull(entity)){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemes().getNameNotExist().replace("&", "§"));
            return false;
        }

        if(DataManager.getBuildbattleThemeStore().select(BuildbattleThemeEntity.class).where(BuildbattleThemeEntity.NAME.lower().eq(newName.toLowerCase())).get().stream().count() > 0){
            sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemes().getNameAlreadyExist().replace("&", "§"));
            return false;
        }

        entity.setName(newName);

        DataManager.getBuildbattleThemeStore().update(entity);

        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getManage().getThemes().getUpdated().replace("&", "§"));
*/

        System.out.println("§6Work in progress");

        return false;
    }

    public boolean delete(CommandSender sender, String[] args){
        /*val messages = MessageUtils.getMessageConfig(sender).getCommand();
        if(!sender.hasPermission(CommonsBukkit.getInstance().getPermissions().getCommandManageThemeChange())){
            sender.sendMessage(messages.getNoPremission().replace("&", "§"));
            return false;
        }

        val arguments = new ArrayList<String>();
        for(int i = 2; i < args.length; i++){
            arguments.add(args[i]);
        }

        val name = String.join(" ", arguments).trim();

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
         */

        System.out.println("§6Work in progress");

        return false;
    }
}
