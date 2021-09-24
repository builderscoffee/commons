package eu.builderscoffee.commons.common.data;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.ToString;

import java.sql.Timestamp;

/***
 * {@link CupRound} est l'objet utilisé pour stocker des manches des cups.
 */
@Entity
@Table(name = "cup_rounds")
@ToString
public abstract class CupRound {

    /* Columns */

    @Key
    @Generated
    int id;

    @Column(name = "id_cup", nullable = false)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne
    CupEntity cup;

    @Column(nullable = false)
    Timestamp date;

    @Column(name = "id_type", nullable = false)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne
    BuildbattleTypeEntity type;

    @Column(name = "id_theme", nullable = false)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne
    BuildbattleThemeEntity theme;

    @Column
    int stage;

    /* Links to other entity */

    @OneToMany(mappedBy = "id_round")
    MutableResult<CupNoteEntity> notes;
}
