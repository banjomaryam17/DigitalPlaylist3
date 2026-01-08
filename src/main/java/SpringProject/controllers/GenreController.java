package SpringProject.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import SpringProject.entities.Genre;
import SpringProject.services.GenreServices;

import jakarta.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/genres")
public class GenreController {


    private static final int DUPLICATE_KEY_ERROR_CODE = 1062;
    private static final int FOREIGN_KEY_CONSTRAINT_FAILS = 1452;

    private final GenreServices genreService;

    public GenreController(GenreServices genreService) {
        this.genreService = genreService;
    }

    /**
     * Get a genre by its ID
     * @param id The genre ID
     * @return The genre object
     */
    @GetMapping(path = "/{id}", produces = "application/json")
    public Genre getGenre(@PathVariable int id) {
        try {
            Genre genre = genreService.getGenreById(id);
            if (genre == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Genre not found"
                );
            }
            return genre;
        } catch (SQLException e) {
            log.error("Genre with ID \"{}\" could not be retrieved. Database error occurred: {}",
                    id, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error"
            );
        }
    }

    /**
     * Get all genres
     * @return List of all genres
     */
    @GetMapping(path = "/all", produces = "application/json")
    public List<Genre> getAllGenres() {
        try {
            return genreService.getAllGenres();
        } catch (SQLException e) {
            log.error("Genre list could not be retrieved. Database error occurred: {}",
                    e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error"
            );
        }
    }

    /**
     * Create a new genre
     * @param genre The genre to create
     * @return The created genre
     */
    @PostMapping(path = "/add", produces = "application/json")
    public Genre createGenre(@Valid @RequestBody Genre genre) {
        try {
            return genreService.createGenre(genre);
        } catch (IllegalArgumentException e) {
            log.error("Failed to create genre. Validation error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Failed to add genre with name \"{}\". Database error occurred: {}",
                    genre.getName(), e.getMessage());
            if (e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "Genre already exists"
                );
            } else if (e.getErrorCode() == FOREIGN_KEY_CONSTRAINT_FAILS) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY, "Foreign key constraint failed"
                );
            } else {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred"
                );
            }
        }
    }

    /**
     * Update an existing genre
     * @param id The genre ID
     * @param genre The updated genre data
     * @return true if updated successfully
     */
    @PutMapping(path = "/{id}", produces = "application/json")
    public boolean updateGenre(@PathVariable int id, @Valid @RequestBody Genre genre) {
        try {
            // Ensure the ID from the path matches the genre object
            genre.setId(id);

            boolean updated = genreService.updateGenre(genre);
            if (!updated) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Genre not found"
                );
            }
            return true;
        } catch (IllegalArgumentException e) {
            log.error("Failed to update genre. Validation error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Failed to update genre with ID \"{}\". Database error occurred: {}",
                    id, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred"
            );
        }
    }

    /**
     * Delete a genre by ID
     * @param id The genre ID to delete
     * @return true if deleted successfully
     */
    @DeleteMapping(path = "/{id}", produces = "application/json")
    public boolean deleteGenre(@PathVariable int id) {
        try {
            boolean deleted = genreService.deleteGenre(id);
            if (!deleted) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Genre not found"
                );
            }
            return true;
        } catch (IllegalArgumentException e) {
            log.error("Failed to delete genre. Validation error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Failed to delete genre with ID \"{}\". Database error occurred: {}",
                    id, e.getMessage());
            if (e.getErrorCode() == FOREIGN_KEY_CONSTRAINT_FAILS ||
                    e.getMessage().contains("foreign key constraint")) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Cannot delete genre - it is referenced by songs/albums"
                );
            } else {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred"
                );
            }
        }
    }
}