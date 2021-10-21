package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import lombok.ToString;

import java.util.UUID;

/***
 * {@link Schematics} est l'objet utilisé pour stocker le token du schematic lier à un joueur
 */
@Entity
@Table(name = "schematics")
@ToString
public abstract class Schematics {

    /* Columns */
    @Key
    @Column
    UUID token;

    @Column(name = "player_uuid")
    UUID playerUuid;
}
