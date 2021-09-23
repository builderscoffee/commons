package eu.builderscoffee.commons.common.data;

import eu.builderscoffee.commons.bungeecord.annotations.EntityRefference;
import eu.builderscoffee.commons.bungeecord.annotations.Listable;
import io.requery.*;
import io.requery.query.MutableResult;
import lombok.ToString;

/***
 * {@link BuildbattleTheme} est l'objet utilisé pour stocker thèmes de Buildbattles.
 */
@Entity
@Table(name = "buildbattles_themes")
@ToString
@EntityRefference(entityClass = BuildbattleThemeEntity.class)
@Listable(defaultVariableName = {"id", "name"})
public class BuildbattleTheme {

    @Key @Generated
    int id;

    @Column(nullable = false, unique = true, length = 32)
    String name;

    @OneToMany(mappedBy = "id_theme")
    MutableResult<BuildbattleEntity> buildbattles;
}
