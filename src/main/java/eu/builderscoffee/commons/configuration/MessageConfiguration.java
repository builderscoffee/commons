package eu.builderscoffee.commons.configuration;

import eu.builderscoffee.api.configuration.annotation.Configuration;
import lombok.Data;

@Data
@Configuration("messages")
public class MessageConfiguration {

    //Global
    private String prefix = "[BC]";

    // Command
    private String commandMustBePlayer = "Vous devez être un joueur";
    private String commandBadSyntaxe = "bad syntaxe";


    private String onJoinMessage = "[+] %player% ";
    private String onQuitMessage = "[-] %player% ";

}
