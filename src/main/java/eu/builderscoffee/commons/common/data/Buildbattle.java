package eu.builderscoffee.commons.common.data;

import eu.builderscoffee.commons.bungeecord.annotations.EntityRefference;
import eu.builderscoffee.commons.bungeecord.annotations.Listable;
import io.requery.*;
import lombok.ToString;

import java.sql.Timestamp;

/***
 * {@link Buildbattle} est l'objet utilisé pour stocker les saisons.
 */
@Entity
@Table(name = "buildbattles")
@ToString
@EntityRefference(entityClass = BuildbattleEntity.class)
@Listable(defaultVariableName = {"id", "id_saison"})
public class Buildbattle {

    @Key @Generated
    int id;

    @Column(nullable = false)
    int num;

    @Column(name = "id_saison")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne
    protected SaisonEntity saison;

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
}
