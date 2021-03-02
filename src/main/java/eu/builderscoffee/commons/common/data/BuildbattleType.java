package eu.builderscoffee.commons.common.data;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import lombok.ToString;

/***
 * {@link BuildbattleType} est l'objet utilisé pour stocker types d'Expresso.
 */
@Entity
@Table(name = "buildbattle_types")
@ToString
public class BuildbattleType {

    @Key @Generated @Getter
    int id;

    @Column(nullable = false, unique = true, length = 32)
    @Getter
    String name;

    @OneToMany(mappedBy = "id_type")
    @Getter
    MutableResult<BuildbattleEntity> buildbattles;
}
