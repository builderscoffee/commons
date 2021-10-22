package eu.builderscoffee.commons.common.data.tables;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.ToString;

import java.util.UUID;

/***
 * {@link Schematics} est l'objet utilisé pour stocker le token du schematic lier à un joueur
 */
@Entity
@Table(name = "schematics")
@ToString
public abstract class Schematics {

    /* Columns */
    @Key
    @Column
    UUID token;

    @Column(name = "id_cup_round", nullable = false)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne
    CupRound cupRound;

    @Column(name = "id_buildbattle", nullable = false)
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @ManyToOne
    Buildbattle buildbattle;

    /* Links to other entity */

    @JunctionTable(type = Profil.class)
    @ManyToMany
    MutableResult<Profil> profils;
}
