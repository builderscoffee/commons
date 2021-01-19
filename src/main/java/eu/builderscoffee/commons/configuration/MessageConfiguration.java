package eu.builderscoffee.commons.configuration;

import com.google.common.collect.Lists;
import eu.builderscoffee.api.configuration.annotation.Configuration;
import eu.builderscoffee.commons.utils.packets.BookUtil;
import lombok.Data;
import lombok.Getter;

import java.beans.Transient;
import java.util.*;

@Data
@Configuration("messages")
public final class MessageConfiguration {

    //Global
    String prefix = "[BC]";

    // Command
    String commandMustBePlayer = "Vous devez être un joueur";
    String commandBadSyntaxe = "bad syntaxe";

    // Event
    String onJoinMessage = "&8[&2+&8] &7%player% ";
    String onQuitMessage = "&8[&2-&8] &7%player% ";

    // ChatFormat
    String chatFormatMessage = "&7%prefix%%player%%suffix% &8>> &f%message%";

    // Rules Book
    @Getter
    //List<BookUtil.Page> Bookpages = Lists.newArrayList(new BookUtil.Page(Arrays.asList("Hello","Builders Coffee")));
    List<List<String >> pages = Arrays.asList(new ArrayList<>(Collections.singletonList("Pages 1")), new ArrayList<>(Collections.singletonList("Pages 2")));

}
