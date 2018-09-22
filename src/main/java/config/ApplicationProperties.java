package config;

import lombok.Getter;

import java.io.IOException;
import java.util.Properties;

public class ApplicationProperties {

    private static ApplicationProperties instance = null;
    private Properties properties;
    @Getter
    private DataSourceProperties dataSourceProperties;

    private ApplicationProperties() throws IOException {
        properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));

        dataSourceProperties = new DataSourceProperties();
    }

    public static ApplicationProperties getInstance() {
        if (instance == null) {
            try {
                instance = new ApplicationProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    @Getter
    public class DataSourceProperties {

        private static final String PROPERTY_PREFIX = "datasource";

        private String url;
        private String driver;
        private String username;
        private String password;

        public DataSourceProperties() {
            url = properties.getProperty(PROPERTY_PREFIX + ".url");
            driver = properties.getProperty(PROPERTY_PREFIX + ".driver");
            username = properties.getProperty(PROPERTY_PREFIX + ".username");
            password = properties.getProperty(PROPERTY_PREFIX + ".password");
        }
    }

}
