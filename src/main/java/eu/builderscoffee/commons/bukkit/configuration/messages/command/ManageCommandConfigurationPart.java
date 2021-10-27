package eu.builderscoffee.commons.bukkit.configuration.messages.command;

import lombok.Data;

@Data
public class ManageCommandConfigurationPart {

    String themeNameNotEmpty = "§cLe nom du theme ne doit pas être vide";
    String themeNewNameNotEmpty = "§cVous devez aussi entrer le nouveau nom";
    String themeNameAlreadyExist = "§cLe nom du theme existe déja !";
    String themeNameNotExist = "§cLe nom du theme n'existe pas !";
    String themesList = "§6Themes list:";
    String themeAdded = "§aTheme ajouté";
    String themeUpdated = "§aTheme modifié";
    String themeDeleted = "§aTheme supprimé";
    String commandAvailbableOption = "§6Options possibles:";
}
