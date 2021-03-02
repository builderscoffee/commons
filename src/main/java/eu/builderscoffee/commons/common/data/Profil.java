package eu.builderscoffee.commons.common.data;

import eu.builderscoffee.commons.bukkit.Main;
import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import java.sql.Timestamp;
import java.util.Date;

/**
 * {@link Profil} est l'objet utilisé pour stocker le profil d'un joueur.
 */
@Entity
@Table(name = "profils")
@ToString
public abstract class Profil {

    @Column(nullable = false, unique = true, length = 11)
    @Key @Generated @Getter
    int id;

    /**
     * L'UUID du joueur permettant de la reconnaitre
     */
    @Column(name = "uuid", nullable = false, unique = true, length = 36)
    @Getter
    String uniqueId;

    /**
     * Le pseudo du joueur pouvant changer
     */
    @Column(nullable = false, length = 16)
    @Getter @Setter
    String name = "";

    /**
     * La date de creation ne pouvant changer
     */
    @Column(name = "creation_date", nullable = false, value = "CURRENT_TIMESTAMP")
    @Getter @Setter
    Timestamp creationDate;

    /**
     * La date de creation ne pouvant changer
     */
    @Column(name = "update_date", nullable = false, value = "CURRENT_TIMESTAMP")
    @Getter @Setter
    Timestamp updateDate;

    @OneToMany(mappedBy = "id_profil")
    @Getter
    MutableResult<NoteEntity> notes;

    @OneToMany(mappedBy = "id_profil")
    @Getter
    MutableResult<Cosmetique> cosmetiques;

    @OneToOne @Getter
    BanEntity ban;

    @PreUpdate
    protected void onPreUpdate(){ setUpdateDate(new Timestamp(new Date().getTime())); }

    /**
     * Créer une nouvelle entité pour cette table
     *
     * @param uniqueId UUID du joueur
     * @return Entité
     */
    public static ProfilEntity getOrCreate(String uniqueId) {
        val cached = Main.getInstance().getProfilCache().get(uniqueId);
        if(cached == null) {
            val profil = new ProfilEntity();
            profil.setUniqueId(uniqueId);
            return profil;
        }
        return cached;
    }
}
