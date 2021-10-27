package eu.builderscoffee.commons.common.data.tables;

import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.bukkit.configuration.MessageConfiguration;
import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * {@link Profil} est l'objet utilisé pour stocker le profil d'un joueur.
 */
@Entity
@Table(name = "profils")
@ToString
public abstract class Profil {

    /* Columns */

    @Key
    @Generated
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

    @Column
    Lanugages lang;

    /* Links to other entity */

    @OneToMany(mappedBy = "id_profil")
    MutableResult<NoteEntity> notes;

    @OneToMany(mappedBy = "id_jury")
    MutableResult<NoteEntity> notesRegistered;

    @OneToMany(mappedBy = "id_jury")
    MutableResult<CupNoteEntity> cupNotesRegistered;

    @ManyToMany(mappedBy = "id_team")
    MutableResult<CupTeamEntity> cupTeams;

    @OneToMany(mappedBy = "id_profil")
    MutableResult<Cosmetique> cosmetiques;

    @OneToOne
    BanEntity ban;

    @ManyToMany
    MutableResult<SchematicsEntity> schematics;

    /**
     * Créer une nouvelle entité pour cette table
     * @param uniqueId UUID du joueur
     * @return Entité
     */
    public static ProfilEntity getOrCreate(String uniqueId) {
        val cached = Main.getInstance().getProfilCache().get(uniqueId);
        if (cached == null) {
            val profil = new ProfilEntity();
            profil.setUniqueId(uniqueId);
            profil.setLang(Lanugages.FR);
            profil.setUpdateDate(new Timestamp(new Date().getTime()));
            profil.setCreationDate(new Timestamp(new Date().getTime()));
            return profil;
        }
        return cached;
    }

    @PreInsert
    protected void onPreInsert() {
        setCreationDate(new Timestamp(new Date().getTime()));
    }

    @PreUpdate
    protected void onPreUpdate() {
        setUpdateDate(new Timestamp(new Date().getTime()));
    }

    public enum Lanugages{
        FR("Français"),
        EN("English");

        public String name;

        Lanugages(String name) {
            this.name = name;
        }
    }
}
