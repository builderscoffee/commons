package eu.builderscoffee.commons.bukkit.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import eu.builderscoffee.commons.bukkit.configuration.messages.*;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * This class stores messages configuration
 */
@Data
@Configuration("messages")
public final class MessageConfiguration {

    /* Global */
    String prefix = "[BC]";
    String serverCloseMessage = "§cLe serveur s'est fermé. Vous avez été déplacé dans le hub.";
    String retourItem = "§7Retour";

    private Chat chat = new Chat();
    private Command command = new Command();
    private Network network = new Network();
    private Profil profil = new Profil();
    private Join join = new Join();
    private Quit quit = new Quit();


}
