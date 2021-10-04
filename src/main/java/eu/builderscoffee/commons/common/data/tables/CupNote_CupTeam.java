package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import lombok.ToString;

/***
 * {@link CupRound} est l'objet utilisé pour stocker des notes des manches de cups.
 */
@Entity
@Table(name = "cup_notes_teams")
@ToString
public class CupNote_CupTeam {

    /* Columns */

    @Column(name = "id_team")
    @ForeignKey(references = CupTeam.class, referencedColumn = "id")
    @Key
    int teamId;

    @Column(name = "id_note")
    @ForeignKey(references = CupNote.class, referencedColumn = "id")
    @Key
    int noteId;
}
