package eu.builderscoffee.commons.common.data;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Key;
import io.requery.Table;
import lombok.ToString;

/***
 * {@link CupRound} est l'objet utilisé pour stocker des notes des manches de cups.
 */
@Entity
@Table(name = "cup_teams-profils")
@ToString
public abstract class CupTeam_Profil {

    /* Columns */

    @ForeignKey(references = CupTeam.class)
    @Key
    Integer teamId;

    @ForeignKey( references = Profil.class  )
    @Key
    Integer profilId;
}
