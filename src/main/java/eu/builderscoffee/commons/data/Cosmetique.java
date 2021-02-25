package eu.builderscoffee.commons.data;

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

    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    ProfilEntity id_profil;

    public ProfilEntity getProfil(){ return id_profil; }

    @Column(length = 32)
    @Key @Getter
    String name;
}
