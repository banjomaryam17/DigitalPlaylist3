package SpringProject.services;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import SpringProject.entities.Genre;
import SpringProject.persistences.GenreDao;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class GenreServices {
    private GenreDao genreDao;

    public GenreServices(GenreDao dao){
        this.genreDao = dao;
    }
    public void shutdownService(){
        genreDao.closeConnection();
    }

    /**
     * @return A list of all genres
     * @throws SQLException if a database error occurs
     */
    public List<Genre> getAllGenres() throws SQLException{
        log.info("Retrieving all genres");
        return genreDao.findAll();
    }

    /**
     * @param id The ID of the genre to be retrieved
     * @return Genre object if found, null if not found
     * @throws SQLException if database error occurs
     */
    public Genre getGenreById(int id)throws SQLException{
        log.info("Genre retrieval: {}", id);
        return genreDao.findById(id);
    }

    /**
     * @param genre The genre to create
     * @return The created genre
     * @throws SQLException if database access error occurs
     * @throws IllegalArgumentException if genre data is invalid
     */
    public Genre createGenre(Genre genre) throws SQLException{
        if(genre == null){
            throw new IllegalArgumentException("Cannot create null genre");
        }
        if(genre.getName() == null || genre.getName().isBlank()){
            throw new IllegalArgumentException("Genre name has to be provided");
        }
        log.info("Creating a new genre: {}", genre.getName());
        return genreDao.create(genre);
    }

    /**
     * @param genre THE GENRE TO BE UPDATED
     * @return TRUE OR FALSE IF THE GENRE WAS UPDATED
     * @throws SQLException IF DATABASE ERROR OCCURS
     * @throws IllegalArgumentException if genre data provided is invalid
     */
    public boolean updateGenre(Genre genre) throws SQLException{
        if(genre == null){
            throw new IllegalArgumentException("Cannot update null genre");
        }
        if(genre.getName() == null || genre.getName().isBlank()){
            throw new IllegalArgumentException("Genre name has to be provided");
        }
        if (genre.getId() <= 0){
            throw new IllegalArgumentException("Genre ID must be provided");
        }
        log.info("Updating genre ID: {}", genre.getId());
        return genreDao.update(genre);
    }

    public boolean deleteGenre(int id) throws SQLException{
        if(id <= 0){
            throw new IllegalArgumentException("Genre ID must be positive");
        }
        log.info("Deleting genre ID: {}", id);

        try{
            return genreDao.delete(id);
        } catch (SQLException e) {
            if(e.getMessage().contains("foreign key constriant")){
                log.error("Cannot delet genre {} - it is being used by songs and/or albums", id);
                throw new SQLException("Cannot delete a genre - it is referenced by albums and songs. Remove all song/albums first.");
            }
            throw e;
        }




    }



}
