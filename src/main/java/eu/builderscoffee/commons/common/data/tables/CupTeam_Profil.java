package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import lombok.ToString;

/***
 * {@link CupRound} est l'objet utilisé pour stocker des notes des manches de cups.
 */
@Entity
@Table(name = "cup_teams_profils")
@ToString
public abstract class CupTeam_Profil {

    /* Columns */

    @Column(name = "id_team")
    @ForeignKey(references = CupTeam.class, referencedColumn = "id")
    @Key
    int teamId;

    @Column(name = "id_profil")
    @ForeignKey(references = Profil.class, referencedColumn = "id")
    @Key
    int profilId;
}
