package eu.builderscoffee.commons.bukkit.configuration.messages.command.themes;

import lombok.Data;

@Data
public class ManageThemesCommandConfigurationPart {

    String nameNotEmpty = "&cLe nom du theme ne doit pas être vide";
    String newNameNotEmpty = "&cVous devez aussi entrer le nouveau nom";
    String nameAlreadyExist = "&cLe nom du theme existe déja !";
    String nameNotExist = "&cLe nom du theme n'existe pas !";
    String list = "&6List des themes:";
    String listFormat = "&7 - %name%";
    String added = "&aTheme ajouté";
    String updated = "&aTheme modifié";
    String deleted = "&aTheme supprimé";

    String commandList = "&6/manage themes &elist &7: Voir la liste des themes";
    String commandAdd = "&6/manage themes &eadd <name> &7: Ajouter une theme";
    String commandUpdate = "&6/manage themes &eupdate <name> &7: Modifier une theme";
    String commandDelete = "&6/manage themes &edelete <name> &7: Suprimmer une theme";
}