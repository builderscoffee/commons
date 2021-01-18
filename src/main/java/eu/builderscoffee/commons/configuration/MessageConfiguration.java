package eu.builderscoffee.commons.configuration;

import eu.builderscoffee.api.configuration.annotation.Configuration;
import lombok.Data;

@Data
@Configuration("messages")
public final class MessageConfiguration {

    //Global
    String prefix = "[BC]";

    // Command
    String commandMustBePlayer = "Vous devez être un joueur";
    String commandBadSyntaxe = "bad syntaxe";

    // Event
    String onJoinMessage = "[+] %player% ";
    String onQuitMessage = "[-] %player% ";

}
