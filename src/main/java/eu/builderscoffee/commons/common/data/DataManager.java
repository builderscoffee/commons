package eu.builderscoffee.commons.common.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.builderscoffee.commons.common.data.tables.*;
import io.requery.sql.EntityDataStore;
import io.requery.sql.SchemaModifier;
import io.requery.sql.TableCreationMode;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass

public class DataManager {

    @Getter private static HikariDataSource hikari;

    @Getter private static EntityDataStore<Ban> bansStore;
    @Getter private static EntityDataStore<Note> notesStore;
    @Getter private static EntityDataStore<BuildbattleTheme> buildbattleThemeStore;
    @Getter private static EntityDataStore<BuildbattleType> expressoTypseStore;
    @Getter private static EntityDataStore<Buildbattle> buildbattlesStore;
    @Getter private static EntityDataStore<Saison> saisonsStore;
    @Getter private static EntityDataStore<Profil> profilStore;
    @Getter private static EntityDataStore<Cosmetique> cosmetiquesStore;
    @Getter private static EntityDataStore<Cup> cupStore;
    @Getter private static EntityDataStore<CupNote> cupNotesStore;
    @Getter private static EntityDataStore<CupRound> cupRoundsStore;
    @Getter private static EntityDataStore<CupTeam> cupTeamsStore;
    @Getter private static EntityDataStore<Schematics> schematicsStore;

    /***
     * Initialise les tables de la bdd
     * @param config - Configuration Hikari
     */
    public static void init(HikariConfig config){
        hikari = new HikariDataSource(config);
        saisonsStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        bansStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        buildbattleThemeStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        expressoTypseStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        buildbattlesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        profilStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        notesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        cosmetiquesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        cupStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        cupNotesStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        cupRoundsStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        cupTeamsStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        schematicsStore = new EntityDataStore<>(hikari, Models.DEFAULT);
        new SchemaModifier(hikari, Models.DEFAULT).createTables(TableCreationMode.CREATE_NOT_EXISTS);
    }
}
