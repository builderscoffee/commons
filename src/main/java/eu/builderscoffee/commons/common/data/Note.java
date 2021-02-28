package eu.builderscoffee.commons.common.data;

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
    @Key @ManyToOne
    BuildbattleEntity buildbattle;

    @Column(name = "id_saison")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    SaisonEntity saison;

    @Column(name = "id_profil")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    ProfilEntity profil;

    @Column(name = "id_jury")
    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    ProfilEntity jury;

    @Column(nullable = false)
    @Getter @Setter
    int beaute;

    @Column(nullable = false)
    @Getter @Setter
    int creativite;

    @Column(nullable = false)
    @Getter @Setter
    int amenagement;

    @Column(nullable = false)
    @Getter @Setter
    int folklore;

    @Column(nullable = false)
    @Getter @Setter
    int fun;
}
