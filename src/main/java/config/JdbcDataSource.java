package config;

import exceptions.ApplicationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class JdbcDataSource {

    private static JdbcDataSource instance = null;

    @Getter
    private Connection connection;

    private JdbcDataSource() {
        ApplicationProperties.DataSourceProperties dataSourceProperties
                = ApplicationProperties.getInstance().getDataSourceProperties();
        try {
            Class.forName(dataSourceProperties.getDriver());
            connection = DriverManager.getConnection(
                    dataSourceProperties.getUrl(),
                    dataSourceProperties.getUsername(),
                    dataSourceProperties.getPassword()
            );
        } catch (ClassNotFoundException e) {
            log.error("Could not found driver for name: {}", dataSourceProperties.getDriver(), e);
            throw new ApplicationException();
        } catch (SQLException e) {
            log.error("Could not connect to database", e);
            throw new ApplicationException();
        }
    }

    public static JdbcDataSource getInstance() {
        if (instance == null) {
            instance = new JdbcDataSource();
        }
        return instance;
    }


}
