package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import lombok.ToString;

import java.sql.Timestamp;

/***
 * {@link Ban} est l'objet utilisé pour stocker les joueurs bannis.
 */
@Entity
@Table(name = "bans")
@ToString
public abstract class Ban {

    /* Columns */

    @Column(name = "id_profil", unique = true)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @OneToOne @Key
    ProfilEntity profile;

    @Column(length = 255)
    String reason;

    @Column(name = "date_end", nullable = false)
    Timestamp dateEnd;
}
