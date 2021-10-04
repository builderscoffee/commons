package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import lombok.ToString;

/***
 * {@link Note} est l'objet utilisé pour stocker la note d'un joueur.
 */
@Entity
@Table(name = "notes")
@ToString
public abstract class Note {

    /* Columns */

    @Column(name = "id_buildbattle")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key
    @ManyToOne
    BuildbattleEntity buildbattle;

    @Column(name = "id_profil")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key
    @ManyToOne
    ProfilEntity profil;

    @Column(name = "id_jury")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key
    @ManyToOne
    ProfilEntity jury;

    @Column(nullable = false)
    int beaute, creativite, amenagement, folklore, fun;
}
