package SpringProject.persistences;
import SpringProject.entities.Genre;

import java.sql.SQLException;
import java.util.List;


    public interface GenreDao {
        void closeConnection();
        public Genre create(Genre genre) throws SQLException;
        public Genre findById(int id) throws SQLException;
        public List<Genre> findAll() throws SQLException;
        public boolean update(Genre genre) throws SQLException;
        public boolean delete(int id) throws SQLException;

    }


