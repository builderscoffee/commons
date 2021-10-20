package eu.builderscoffee.commons.common.configuration.settings;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import lombok.val;

@Data
public class MySQLConfig {

    private String host, username, password, database = "";
    private int poolSize = 5;
    private int port = 3306;
    /**
     * @return Une version configuré de {@link HikariConfig} utilisant les informations
     */
    public HikariConfig toHikari() {
        val config = new HikariConfig();

        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(poolSize);
        config.setPoolName("Commons pool");

        return config;
    }
}
