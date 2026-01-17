package SpringProject.controllers;

import SpringProject.entities.Rating;
import SpringProject.entities.Song;
import SpringProject.persistences.Connector;
import SpringProject.persistences.MySqlConnector;
import SpringProject.persistences.RatingDao;
import SpringProject.persistences.RatingDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingDao ratingDao;

    public RatingController() {
        Connector connector = new MySqlConnector("database.properties");
        this.ratingDao = new RatingDaoImpl(connector) {
            @Override
            public Song getLowestRatedSong() {
                return null;
            }

            @Override
            public Song getMostPopularSong() {
                return null;
            }

            @Override
            public Song getTopRatedSong() {
                return null;
            }
        };
    }
    /**
     * Get all ratings in the system.
     *
     * @return list of ratings
     */
    @GetMapping(path = "/all", produces = "application/json")
    public java.util.List<Rating> getAllRatings() {
        try {
            return ratingDao.getAllRatings();
        } catch (Exception e) {
            log.error("Get all ratings failed: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error");
        }
    }
    /**
     * Get all ratings for a given user
     * @param userId the username to search
     * @return ratings belonging to that user
     */
    @GetMapping(path = "/user/{username}", produces = "application/json")
    public java.util.List<Rating> getRatingsForUser(@PathVariable String userId) {
        try {
            return ratingDao.getUserRatingFromUsername(userId);
        } catch (Exception e) {
            log.error("Get ratings for user '{}' failed: {}", userId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error");
        }
    }


    /**
     * Adds a new rating
     * The request body must include: username, songID and userRating.
     *
     * @param rating rating details from JSON
     * @return true if the rating was added
     */
    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    public boolean addRating(@RequestBody Rating rating) {
        if (rating == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating required");
        }

        try {
            int added = ratingDao.addRating(rating);
            if (added == -1) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Rating not added");
            }
            return true;
        } catch (Exception e) {
            log.error("Add rating failed: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    /**
     * Get the song with the highest average rating.
     *
     * @return top-rated song
     */
    @GetMapping(path = "/songs/top-rated", produces = "application/json")
    public Song topRatedSong() {
        try {
            Song s = ratingDao.getTopRatedSong();
            if (s == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No song found");
            return s;
        } catch (Exception e) {
            log.error("Top rated song failed: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    /**
     * Get the song with the lowest average rating
     * @return lowest rated song
     */
    @GetMapping(path = "/songs/lowest-rated", produces = "application/json")
    public Song lowestRatedSong() {
        try {
            Song s = ratingDao.getLowestRatedSong();
            if (s == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No song found");
            return s;
        } catch (Exception e) {
            log.error("Lowest rated song failed: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error");
        }
    }


    /**
     * Get the song that has been rated the most times
     * @return most popular song
     */
    @GetMapping(path = "/songs/most-popular", produces = "application/json")
    public Song mostPopularSong() {
        try {
            Song s = ratingDao.getMostPopularSong();
            if (s == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No song found");
            return s;
        } catch (Exception e) {
            log.error("Most popular song failed: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}
