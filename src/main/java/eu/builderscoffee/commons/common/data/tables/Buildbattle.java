package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.ToString;

import java.sql.Timestamp;

/***
 * {@link Buildbattle} est l'objet utilisé pour stocker les saisons.
 */
@Entity
@Table(name = "buildbattles")
@ToString
public abstract class Buildbattle {

    /* Columns */

    @Key @Generated
    int id;

    @Column(nullable = false)
    int num;

    @Column(name = "id_saison")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne
    SaisonEntity saison;

    @Column(name = "id_type", nullable = false)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne
    BuildbattleTypeEntity type;

    @Column(name = "id_theme", nullable = false)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne
    BuildbattleThemeEntity theme;

    @Column(nullable = false)
    Timestamp date;

    @Column(nullable = false)
    boolean step;

    /* Links to other entity */

    @OneToMany(mappedBy = "id_buildbattle")
    MutableResult<Schematics> schematics;
}
