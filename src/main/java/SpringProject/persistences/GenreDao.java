package SpringProject.persistences;
import SpringProject.entities.Genre;

import java.sql.SQLException;
import java.util.List;


    public interface GenreDao {
        void closeConnection();
        /**
         * Creates a new genre in the database
         *
         * @param genre The genre object containing genre details (name, description)
         * @return The created Genre object with the generated ID, or null if creation failed
         * @throws SQLException if a database access error occurs, the SQL statement fails,
         *  or a genre with the same name already exists (UNIQUE constraint violation)
         */
        public Genre create(Genre genre) throws SQLException;

        /**
         * Retrieves a genre by its unique ID
         *
         * @param id The ID of the genre to retrieve
         * @return The Genre object if found, null if no genre exists with the specified ID
         * @throws SQLException if a database access error occurs or the SQL query fails
         */
        public Genre findById(int id) throws SQLException;

        /**
         * Retrieves all genres from the database
         *
         * @return List of all Genre objects in the database, empty list if no genres exist
         * @throws SQLException if a database access error occurs or the SQL query fails
         */
        public List<Genre> findAll() throws SQLException;

        /**
         * Updates an existing genre's information
         * Can update genre name and description
         *
         * @param genre The genre object containing updated information with a valid ID
         * @return true if the genre was successfully updated, false if genre not found or update failed
         * @throws SQLException if a database access error occurs, the SQL update statement fails,
         */
        public boolean update(Genre genre) throws SQLException;

        /**
         * Deletes a genre from the database
         *
         *
         * @param id The ID of the genre to delete
         * @return true if the genre was successfully deleted, false if genre not found or deletion failed
         * @throws SQLException if a database access error occurs, the SQL delete statement fails,
         */
        public boolean delete(int id) throws SQLException;
    }


