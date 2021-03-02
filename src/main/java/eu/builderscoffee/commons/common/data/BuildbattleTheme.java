package eu.builderscoffee.commons.common.data;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import lombok.ToString;

/***
 * {@link BuildbattleTheme} est l'objet utilisé pour stocker thèmes de Buildbattles.
 */
@Entity
@Table(name = "buildbattles_themes")
@ToString
public class BuildbattleTheme {

    @Key @Generated
    int id;

    @Column(nullable = false, unique = true, length = 32)
    String name;

    @OneToMany(mappedBy = "id_theme")
    MutableResult<BuildbattleEntity> buildbattles;
}
