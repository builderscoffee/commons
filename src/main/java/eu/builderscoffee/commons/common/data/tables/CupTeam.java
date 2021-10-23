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

    @JunctionTable(type = CupTeam_Profil.class)
    @ManyToMany(mappedBy = "id_team")
    MutableResult<Profil> members;

    @JunctionTable(type = CupNote_CupTeam.class)
    @ManyToMany(mappedBy = "id_note")
    MutableResult<CupNoteEntity> notes;
}
