package eu.builderscoffee.commons.common.data;

import eu.builderscoffee.commons.bungeecord.annotations.Addable;
import eu.builderscoffee.commons.bungeecord.annotations.EntityRefference;
import eu.builderscoffee.commons.bungeecord.annotations.Listable;
import eu.builderscoffee.commons.bungeecord.annotations.Updatable;
import io.requery.*;
import lombok.ToString;

import java.sql.Timestamp;

/***
 * {@link Ban} est l'objet utilisé pour stocker les joueurs bannis.
 */
@Entity
@Table(name = "bans")
@ToString
@EntityRefference(entityClass = BanEntity.class)
@Listable(defaultVariableName = {"id_profil", "reason", "date_end"})
@Addable(defaultVariableName = {"id_profil", "reason"})
@Updatable(defaultVariableName = {"reason", "date_end"})
public class Ban {

    @Column(name = "id_profil", unique = true)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @OneToOne @Key
    ProfilEntity profile;

    @Column(length = 255)
    String reason;

    @Column(name = "date_end", nullable = false)
    Timestamp dateEnd;
}
