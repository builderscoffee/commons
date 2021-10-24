package eu.builderscoffee.commons.bukkit.configuration.messages;

import lombok.Data;

/**
 * This class stores command messages configuration
 */
@Data
public class Command {

    String mustBePlayer = "Vous devez être un joueur";
    String badSyntaxe = "bad syntaxe";
}
