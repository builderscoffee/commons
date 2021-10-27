package eu.builderscoffee.commons.bukkit.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import eu.builderscoffee.commons.bukkit.configuration.messages.*;
import lombok.Data;

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

    private ChatConfigurationPart chat = new ChatConfigurationPart();
    private CommandConfigurationPart command = new CommandConfigurationPart();
    private NetworkConfigurationPart network = new NetworkConfigurationPart();
    private ProfilConfigurationPart profil = new ProfilConfigurationPart();
    private JoinConfigurationPart join = new JoinConfigurationPart();
    private QuitConfigurationPart quit = new QuitConfigurationPart();
}
