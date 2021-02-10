package eu.builderscoffee.commons.data;

import io.requery.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.AssociationOverride;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

/***
 * {@link Buildbattle} est l'objet utilisé pour stocker les saisons.
 */
@Entity
@Table(name = "buildbattles")
@ToString
public class Buildbattle {

    @Column(nullable = false, unique = true, length = 11)
    @Key @Generated @Getter
    int id;

    @Column(name = "id_saison", nullable = false, unique = true, length = 11)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne @Key @Getter
    SaisonEntity saison;

    @Column(name = "id_expresso", nullable = false, length = 11)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne @Getter @Setter
    ExpressoTypeEntity expressoType;

    @Column(name = "id_theme", nullable = false, length = 11)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne @Getter @Setter
    BuildbattleThemeEntity buildbattleTheme;
}
