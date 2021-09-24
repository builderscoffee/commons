package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.ToString;

/***
 * {@link BuildbattleTheme} est l'objet utilisé pour stocker thèmes de Buildbattles.
 */
@Entity
@Table(name = "buildbattles_themes")
@ToString
public abstract class BuildbattleTheme {

    /* Columns */

    @Key @Generated
    int id;

    @Column(nullable = false, unique = true, length = 32)
    String name;

    /* Links to other entity */

    @OneToMany(mappedBy = "id_theme")
    MutableResult<BuildbattleEntity> buildbattles;

    @OneToMany(mappedBy = "id_theme")
    MutableResult<CupRoundEntity> cupRounds;
}
