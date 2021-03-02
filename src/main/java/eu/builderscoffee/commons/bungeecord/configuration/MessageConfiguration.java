package eu.builderscoffee.commons.bungeecord.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Data
@Configuration("messages")
public class MessageConfiguration {

    // Permissions
    String globalPermission = "builderscoffee.bungeecord.*";
    String pbanPremission = "builderscoffee.bungeecord.pban";
    String ppardonPremission = "builderscoffee.bungeecord.ppardon";
    String staffChatPermission = "builderscoffee.bungeecord.staffchat";
    String serverDefaultPermission = "builderscoffee.bungeecord.server.default";

    // Global Message
    String noPermission = "&cVous n'avez pas la permission";
    String staffChatFormat = "&9[StaffChat] %player% &7>> &f%message%";
    String staffChatActive = "§aVous venez de désactiver le staffchat.";
    String staffChatDesactive = "§aVous venez d'activer le staffchat.";
    @Getter
    List<String> banMessage = Arrays.asList("&6BuildersCoffee &8- &7Ban", "&6Raison &8>> &7%reason%", "",  "&6Temps &8>> &7%time%");
    String banBroadcastMessage = "&6BuildersCoffee &7>> &6%author% &7a banni &6%player% &7pendent &6%time% &7pour: &f%reason%";
    @Getter
    List<String> whitelistRedirectMessagesKeywords = Arrays.asList("fermé");
    String serverRedirectName = "hub";
    String serverRedirectionMessage = "&cLe serveur sur lequel vous étiez est maintenant en fermé, vous êtes désormais connecté au hub.";
}
