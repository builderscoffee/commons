package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.ToString;

/***
 * {@link CupRound} est l'objet utilisé pour stocker des notes des manches de cups.
 */
@Entity
@Table(name = "cup_notes")
@ToString
public abstract class CupNote {

    /* Columns */

    @Key
    @Generated
    int id;

    @Column(name = "id_round", nullable = false)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne
    CupRoundEntity round;

    @Column(name = "id_jury", nullable = false)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne
    ProfilEntity jury;

    @Column(nullable = false)
    int beaute, creativite, amenagement, folklore, fun;

    /* Links to other entity */

    @ManyToMany
    MutableResult<CupTeam> teams;
}
