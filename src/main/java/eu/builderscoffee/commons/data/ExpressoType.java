package eu.builderscoffee.commons.data;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import lombok.ToString;

/***
 * {@link ExpressoType} est l'objet utilisé pour stocker types d'Expresso.
 */
@Entity
@Table(name = "expresso_types")
@ToString
public class ExpressoType {

    @Column(nullable = false, unique = true, length = 11)
    @Key @Generated @Getter
    int id;

    @Column(nullable = false, unique = true, length = 32)
    @Getter
    String name;

    @OneToMany(mappedBy = "id_expresso")
    @Getter
    MutableResult<BuildbattleEntity> buildbattles;
}
