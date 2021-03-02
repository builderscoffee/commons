package eu.builderscoffee.commons.bukkit.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Data
@Configuration("messages")
public final class MessageConfiguration {

    //Global
    String prefix = "[BC]";

    // Command
    String commandMustBePlayer = "Vous devez être un joueur";
    String commandBadSyntaxe = "bad syntaxe";
    //Network
    // - Hub Item
    String hubItem = "&aHub";
    // - BuildBattle Item
    String buildBattleItem = "&aBuildBattle";
    // - Rules Book
    String rulesBookItem = "&aRégles du serveur";
    @Getter
    List<String> pages = Arrays.asList("Hello", "Builders Coffee");
    // - SupportUs Item
    String supportUsItem = "&fNous soutenir";
    // - SupportUs ChatMessage
    String supportChatMessage = "\n\n§fhttps://fr.tipeee.com/builders-coffee\n\n";
    // - SupportUs Link
    String supportLink = "https://fr.tipeee.com/builders-coffee";
    // - Expresso Item
    String expressoItem = "&6Expresso";
    // - Expresso ChatMessage
    String expressoChatMessage = "\n\n§fhttps://builderscoffee.eu/portfolio/les-expressos/\n\n";
    // - Expresso Link
    String expressoLink = "https://builderscoffee.eu/portfolio/les-expressos/";
    // - CloseMenu Item
    String closeItem = "&cQuitter le menu";
    // - Cosmetics Item
    String CosmeticsItem = "&aCosmétiques";
    // - Retour Item
    String retourItem = "§7Retour";

    // Event§
    String onJoinMessage = "&8[&2+&8] &7%prefix%%player%%suffix% ";
    int showJoinMessageWeight = 750;
    String onQuitMessage = "&8[&2-&8] &7%player% ";
    int showQuitMessageWeight = 750;

    // ChatFormat
    String chatFormatMessage = "&7%prefix%%player%%suffix% &8>> &f%message%";

    // BroadcastFormat
    String broadcastFormatMessage = "&8&m----------&8&m------\n\n&6&lBuilders Coffee &8>> &e%message%\n\n&8&m----------&8&m------";

    String serverCloseMessage = "§cLe serveur s'est fermé. Vous avez été déplacé dans le hub.";

    //Profil
    // - Skull Item
    String profilSkullItem = "&b%player%";
    @Getter
    List<String> profilSkullLore = Arrays.asList("&9Grade: &f%grade%", "&9Participations: &f%participations%", "&9Victoires: &f%victoires%");
    // - Resultat Item
    String profilResultatItem = "Derniers résultat";
    // - Not played any buildbattle
    String notPlayedAnyBuildbattle = "&cVous n'avez pas encore joué une seule partie !";
    // - Saisons Item
    String profilSaisons = "Saisons";
    // - Historique Item
    String profilhistorique = "Historique";
    // - Global Result Item
    String profilGlobalResult = "Résultat Général";

}
