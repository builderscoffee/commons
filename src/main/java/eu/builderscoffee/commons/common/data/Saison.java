package eu.builderscoffee.commons.common.data;

import eu.builderscoffee.commons.bungeecord.annotations.EntityRefference;
import eu.builderscoffee.commons.bungeecord.annotations.Listable;
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
@EntityRefference(entityClass = SaisonEntity.class)
@Listable(defaultVariableName = {"id"})
public class Saison {

    @Column(nullable = false, unique = true, length = 11)
    @Key @Generated
    int id;

    @Column(name = "begin_date", nullable = false, value = "CURRENT_TIMESTAMP")
    Timestamp beginDate;

    @Column(name = "end_date", nullable = false, value = "CURRENT_TIMESTAMP")
    Timestamp endDate;

    @OneToMany(mappedBy = "id_saison")
    @Getter
    MutableResult<BuildbattleEntity> buildbattles;
}
