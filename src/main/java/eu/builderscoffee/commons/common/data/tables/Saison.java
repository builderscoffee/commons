package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

/***
 * {@link Saison} est l'objet utilisé pour stocker les saisons.
 */
@Entity
@Table(name = "saisons")
@ToString
public abstract class Saison {

    /* Columns */

    @Column(nullable = false, unique = true, length = 11)
    @Key
    @Generated
    int id;

    @Column(name = "begin_date", nullable = false, value = "CURRENT_TIMESTAMP")
    Timestamp beginDate;

    @Column(name = "end_date", nullable = false, value = "CURRENT_TIMESTAMP")
    Timestamp endDate;

    /* Links to other entity */

    @OneToMany(mappedBy = "id_saison")
    MutableResult<BuildbattleEntity> buildbattles;
}
