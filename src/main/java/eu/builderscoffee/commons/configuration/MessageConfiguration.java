package eu.builderscoffee.commons.configuration;

import eu.builderscoffee.api.configuration.annotation.Configuration;
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

    // Event
    String onJoinMessage = "&8[&2+&8] &7%prefix%%player%%suffix% ";
    int showJoinMessageWeight = 750;
    String onQuitMessage = "&8[&2-&8] &7%player% ";

    // ChatFormat
    String chatFormatMessage = "&7%prefix%%player%%suffix% &8>> &f%message%";
}
