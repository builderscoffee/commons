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

    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    BuildbattleEntity id_buildbattle;

    public BuildbattleEntity getBuildbattle() { return id_buildbattle; }

    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    SaisonEntity id_saison;

    public SaisonEntity getSaison() { return id_saison; }

    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    ProfilEntity id_profil;

    public ProfilEntity getProfil() { return id_profil; }

    @ForeignKey(update = ReferentialAction.CASCADE, referencedColumn = "id")
    @Key @ManyToOne
    ProfilEntity id_jury;

    public ProfilEntity getJury() { return id_jury; }

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
