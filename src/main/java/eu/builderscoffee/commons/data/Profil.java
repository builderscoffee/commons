package eu.builderscoffee.commons.data;

import eu.builderscoffee.commons.Main;
import io.requery.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import java.util.UUID;

/***
 * {@link Profil} est l'objet utilisé pour stocker le profil d'un joueur.
 */
@Entity
@Table(name = "profil")
@ToString
public abstract class Profil {

    @Column(nullable = false, unique = true)
    @Key @Generated @Getter
    int id;

    /**
     * L'UUID du joueur permettant de la reconnaitre
     */
    @Column(nullable = false, unique = true)
    @Getter
    UUID uniqueId;

    /**
     * Le pseudo du joueur pouvant changer
     */
    @Getter @Setter
    String name = "";

    /**
     * Créer une nouvelle entité pour cette table
     *
     * @param uniqueId UUID du joueur
     * @return Entité
     */
    public static ProfilEntity getOrCreate(UUID uniqueId) {
        val cached = Main.getInstance().getProfilCache().get(uniqueId);
        if(cached == null) {
            val profil = new ProfilEntity();
            profil.setUniqueId(uniqueId);
            return profil;
        }
        return cached;
    }
}
