package eu.builderscoffee.commons.common.data;

import eu.builderscoffee.commons.bungeecord.annotations.EntityRefference;
import eu.builderscoffee.commons.bungeecord.annotations.Listable;
import io.requery.*;
import lombok.ToString;

/***
 * {@link Cosmetique} est l'objet utilisé pour stocker les cosmétiques.
 */
@Entity
@Table(name = "cosmetiques")
@ToString
@EntityRefference(entityClass = CosmetiqueEntity.class)
@Listable(defaultVariableName = {"id_profil", "name"})
public class Cosmetique {

    @Column(name = "id_profil")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    ProfilEntity profil;

    @Column(length = 32)
    @Key
    String name;
}
