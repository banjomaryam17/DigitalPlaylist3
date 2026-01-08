package SpringProject.persistences;

import org.springframework.stereotype.Component;

import java.sql.Connection;
@Component
public interface Connector {
    public Connection getConnection();
    public void freeConnection();
}
