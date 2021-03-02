package eu.builderscoffee.commons.common.data;

import io.requery.*;
import lombok.Getter;
import lombok.ToString;

/***
 * {@link Cosmetique} est l'objet utilisé pour stocker les cosmétiques.
 */
@Entity
@Table(name = "cosmetiques")
@ToString
public class Cosmetique {

    @Column(name = "id_profil")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    ProfilEntity profil;

    @Column(length = 32)
    @Key @Getter
    String name;
}
