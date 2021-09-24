package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.ToString;

/***
 * {@link CupRound} est l'objet utilisé pour stocker des notes des manches de cups.
 */
@Entity
@Table(name = "cup_teams")
@ToString
public abstract class CupTeam {

    /* Columns */

    @Key
    @Generated
    int id;

    @Column(nullable = false)
    String name;

    /* Links to other entity */

    @JunctionTable(type = Profil.class)
    @ManyToMany
    MutableResult<Profil> members;

    @JunctionTable(type = Profil.class)
    @ManyToMany
    MutableResult<CupNoteEntity> notes;
}
