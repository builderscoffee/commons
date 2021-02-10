package eu.builderscoffee.commons.data;

import io.requery.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/***
 * {@link Note} est l'objet utilisé pour stocker la note d'un joueur.
 */
@Entity
@Table(name = "notes")
@ToString
public class Note {
    @Column(name = "id_buildbattle")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne @Getter
    BuildbattleEntity buildbattle;

    @Column(name = "id_saison")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne @Getter
    SaisonEntity saison;

    @Column(name = "id_profil")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne @Getter
    ProfilEntity profil;

    @Column(name = "id_jury")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne @Getter
    ProfilEntity jury;

    @Column(name = "note", nullable = false)
    @Getter @Setter
    int note;
}
