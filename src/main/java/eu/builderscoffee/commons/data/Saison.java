package eu.builderscoffee.commons.data;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/***
 * {@link Saison} est l'objet utilisé pour stocker les saisons.
 */
@Entity
@Table(name = "saisons")
@ToString
public class Saison {

    @Column(nullable = false, unique = true, length = 11)
    @Key @Generated @Getter
    int id;

    @Column(nullable = false, unique = true, length = 32)
    @Getter @Setter
    String name;

    @OneToMany(mappedBy = "id_saison")
    @Getter
    MutableResult<BuildbattleEntity> buildbattles;
}
