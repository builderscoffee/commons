package eu.builderscoffee.commons.common.data;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.ToString;

/***
 * {@link BuildbattleType} est l'objet utilisé pour stocker types d'Expresso.
 */
@Entity
@Table(name = "buildbattle_types")
@ToString
public abstract class BuildbattleType {

    /* Columns */

    @Key @Generated
    int id;

    @Column(nullable = false, unique = true, length = 32)
    String name;

    /* Links to other entity */

    @OneToMany(mappedBy = "id_type")
    MutableResult<BuildbattleEntity> buildbattles;

    @OneToMany(mappedBy = "id_type")
    MutableResult<CupRoundEntity> cupRounds;
}
