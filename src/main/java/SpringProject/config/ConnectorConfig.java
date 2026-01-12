package SpringProject.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import SpringProject.persistences.*;
import SpringProject.services.*;

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
    @Bean
    public ArtistDao artistDao(Connector connector) {
        return new ArtistDaoImpl(connector);
    }

    @Bean
    public AlbumDao albumDao(Connector connector) {
        return new AlbumDaoImpl(connector);
    }

    @Bean
    public SongDao songDao(Connector connector) {
        return new SongDaoImpl(connector);
    }
}