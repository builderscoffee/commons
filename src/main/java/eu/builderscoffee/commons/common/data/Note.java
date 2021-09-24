package eu.builderscoffee.commons.common.data;

import eu.builderscoffee.commons.bungeecord.annotations.EntityRefference;
import eu.builderscoffee.commons.bungeecord.annotations.Listable;
import io.requery.*;
import lombok.ToString;

/***
 * {@link Note} est l'objet utilisé pour stocker la note d'un joueur.
 */
@Entity
@Table(name = "notes")
@ToString
@EntityRefference(entityClass = NoteEntity.class)
@Listable(defaultVariableName = {"id_buildbattle", "id_profil", "id_jury", "beaute", "creativite", "amenagement", "folklore", "fun"})
public class Note {

    @Column(name = "id_buildbattle")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    BuildbattleEntity buildbattle;

    @Column(name = "id_profil")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    ProfilEntity profil;

    @Column(name = "id_jury")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    ProfilEntity jury;

    @Column(nullable = false)
    int beaute;

    @Column(nullable = false)
    int creativite;

    @Column(nullable = false)
    int amenagement;

    @Column(nullable = false)
    int folklore;

    @Column(nullable = false)
    int fun;
}
