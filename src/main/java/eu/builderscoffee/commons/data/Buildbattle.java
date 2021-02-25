package eu.builderscoffee.commons.data;

import io.requery.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/***
 * {@link Buildbattle} est l'objet utilisé pour stocker les saisons.
 */
@Entity
@Table(name = "buildbattles")
@ToString
public class Buildbattle {

    @Key @Generated @Getter
    int id;

    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne @Key
    protected SaisonEntity id_saison;

    public SaisonEntity getSaison() { return id_saison; }

    @Column(name = "id_type", nullable = false)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne @Getter
    BuildbattleTypeEntity type;

    @Column(name = "id_theme", nullable = false)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne @Getter
    BuildbattleThemeEntity theme;

    @Column(nullable = false)
    @Getter @Setter
    Timestamp date;

    @Column(nullable = false)
    @Getter @Setter
    boolean step;
}
