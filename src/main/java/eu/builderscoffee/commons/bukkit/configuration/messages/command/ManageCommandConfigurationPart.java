package eu.builderscoffee.commons.bukkit.configuration.messages.command;

import eu.builderscoffee.commons.bukkit.configuration.messages.command.themes.ManageSeasonsCommandConfigurationPart;
import eu.builderscoffee.commons.bukkit.configuration.messages.command.themes.ManageThemesCommandConfigurationPart;
import lombok.Data;

@Data
public class ManageCommandConfigurationPart {

    String subPartsOptions = "&6Options possibles:";
    String subParts = "&6/manage &e%name%";

    ManageThemesCommandConfigurationPart themes = new ManageThemesCommandConfigurationPart();
    ManageSeasonsCommandConfigurationPart seasons = new ManageSeasonsCommandConfigurationPart();
}
