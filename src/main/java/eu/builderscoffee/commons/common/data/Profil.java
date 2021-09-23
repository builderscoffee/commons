package eu.builderscoffee.commons.common.data;

import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bungeecord.annotations.EntityRefference;
import eu.builderscoffee.commons.bungeecord.annotations.Listable;
import io.requery.*;
import io.requery.query.MutableResult;
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
@EntityRefference(entityClass = ProfilEntity.class)
@Listable(defaultVariableName = {"id", "name"})
public abstract class Profil {

    @Key @Generated
    int id;

    /**
     * L'UUID du joueur permettant de la reconnaitre
     */
    @Column(name = "uuid", nullable = false, unique = true, length = 36)
    String uniqueId;

    /**
     * Le pseudo du joueur pouvant changer
     */
    @Column(length = 16)
    String name = "";

    /**
     * La date de creation ne pouvant changer
     */
    @Column(name = "creation_date", value = "CURRENT_TIMESTAMP")
    @Setter
    Timestamp creationDate;

    /**
     * La date de creation ne pouvant changer
     */
    @Column(name = "update_date", value = "CURRENT_TIMESTAMP")
    @Setter
    Timestamp updateDate;

    @OneToMany(mappedBy = "id_profil")
    MutableResult<NoteEntity> notes;

    @OneToMany(mappedBy = "id_profil")
    MutableResult<Cosmetique> cosmetiques;

    @OneToOne
    BanEntity ban;

    @PreInsert
    protected void onPreInsert(){ setCreationDate(new Timestamp(new Date().getTime())); }

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
            profil.setUpdateDate(new Timestamp(new Date().getTime()));
            profil.setCreationDate(new Timestamp(new Date().getTime()));
            return profil;
        }
        return cached;
    }
}
