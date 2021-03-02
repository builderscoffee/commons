package eu.builderscoffee.commons.bungeecord.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextComponentUtil {

    public static BaseComponent decodeColor(String message){
        message = message.replace("&", "§");

        if(!message.contains("§")) {
            return new TextComponent(message);
        }

        String[] split = message.split("§");

        TextComponent textComponent = new TextComponent();

        for (String s : split) {
            if(s.length() > 0){
                TextComponent extra = new TextComponent(s.substring(1));
                ChatColor chatColor = ChatColor.getByChar(s.charAt(0));
                extra.setColor(chatColor != null? chatColor: ChatColor.RESET);
                textComponent.addExtra(extra);
            }
        }

        return textComponent;
    }
}
