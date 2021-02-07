package eu.builderscoffee.commons.data;

import io.requery.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
}
