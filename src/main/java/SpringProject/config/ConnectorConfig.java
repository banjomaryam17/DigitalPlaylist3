package SpringProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import SpringProject.persistences.Connector;
import SpringProject.persistences.MySqlConnector;

import java.io.IOException;

@Configuration
public class ConnectorConfig {

    private Environment env;

    public ConnectorConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public Connector connector() throws IOException {
        String path = env.getProperty("connector.properties.path");
        return new MySqlConnector(path);
    }
}