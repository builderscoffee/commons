package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import lombok.ToString;

import java.util.UUID;

/***
 * {@link Schematics_Profils} est l'objet utilisé pour stocker le token du schematic lier à un joueur
 */
@Entity
@Table(name = "schematics_profils")
@ToString
public abstract class Schematics_Profils {

    /* Columns */

    @Column(name = "id_profil")
    @ForeignKey(references = Profil.class, referencedColumn = "id")
    @Key
    int profilId;

    @Column(name = "token_schematic")
    @ForeignKey(references = Schematics.class, referencedColumn = "token")
    @Key
    UUID schematicsToken;
}
