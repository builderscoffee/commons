package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import lombok.ToString;

/***
 * {@link Cosmetique} est l'objet utilisé pour stocker les cosmétiques.
 */
@Entity
@Table(name = "cosmetiques")
@ToString
public abstract class Cosmetique {

    /* Columns */

    @Column(name = "id_profil")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    ProfilEntity profil;

    @Column(length = 32)
    @Key
    String name;
}
