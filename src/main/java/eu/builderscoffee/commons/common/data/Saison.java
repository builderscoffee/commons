package eu.builderscoffee.commons.common.data;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

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

    @Column(name = "begin_date", nullable = false, value = "CURRENT_TIMESTAMP")
    @Getter @Setter
    Timestamp beginDate;

    @Column(name = "end_date", nullable = false, value = "CURRENT_TIMESTAMP")
    @Getter @Setter
    Timestamp endDate;

    @OneToMany(mappedBy = "id_saison")
    @Getter
    MutableResult<BuildbattleEntity> buildbattles;
}
