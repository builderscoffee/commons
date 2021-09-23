package eu.builderscoffee.commons.common.data;

import eu.builderscoffee.commons.bungeecord.annotations.EntityRefference;
import eu.builderscoffee.commons.bungeecord.annotations.Listable;
import io.requery.*;
import io.requery.query.MutableResult;
import lombok.ToString;

/***
 * {@link BuildbattleType} est l'objet utilisé pour stocker types d'Expresso.
 */
@Entity
@Table(name = "buildbattle_types")
@ToString
@EntityRefference(entityClass = BuildbattleTypeEntity.class)
@Listable(defaultVariableName = {"id", "name"})
public class BuildbattleType {

    @Key @Generated
    int id;

    @Column(nullable = false, unique = true, length = 32)
    String name;

    @OneToMany(mappedBy = "id_type")
    MutableResult<BuildbattleEntity> buildbattles;
}
