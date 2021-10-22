package eu.builderscoffee.commons.bukkit.configuration.messages;

import lombok.Data;

@Data
public class Command {

    String mustBePlayer = "Vous devez être un joueur";
    String badSyntaxe = "bad syntaxe";
}
