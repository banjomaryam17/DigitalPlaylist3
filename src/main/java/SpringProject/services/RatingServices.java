package SpringProject.services;

import lombok.extern.slf4j.Slf4j;
import SpringProject.entities.Rating;
import SpringProject.entities.Song;
import SpringProject.persistences.RatingDao;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
@Service
public class RatingServices {

    private RatingDao ratingDao;

    public RatingServices(RatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }


    /**
     * Add or update a rating 
     */
    public int addRating(Rating rating) throws SQLException {
        if (rating == null) {
            throw new IllegalArgumentException("Rating cannot be null");
        }
        if (rating.getUsername() == null || rating.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username must be provided");
        }
        if (rating.getSongID() <= 0) {
            throw new IllegalArgumentException("Song ID must be positive");
        }

        log.info("Adding rating: user='{}', songId={}, rating={}",
                rating.getUsername(), rating.getSongID(), rating.getUserRating());

        return ratingDao.addRating(rating);
    }

    /**
     * Get all ratings
     */
    public ArrayList<Rating> getAllRatings() throws SQLException {
        log.info("Retrieving all ratings");
        return ratingDao.getAllRatings();
    }

    /**
     * Get ratings for one user
     */
    public ArrayList<Rating> getUserRatingFromUsername(String username) throws SQLException {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must be provided");
        }
        log.info("Retrieving ratings for username '{}'", username);
        return ratingDao.getUserRatingFromUsername(username);
    }

    /**
     * Find one rating by username + songID
     */
    public Rating findRatingByUsernameAndSongID(String username, int songID) throws SQLException {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must be provided");
        }
        if (songID <= 0) {
            throw new IllegalArgumentException("Song ID must be positive");
        }
        log.info("Finding rating for username='{}' and songID={}", username, songID);
        return ratingDao.findRatingByUsernameAndSongID(username, songID);
    }

    /**
     * Song stats
     */
    public Song getTopRatedSong() {
        log.info("getTopRatedSong(): requesting from DAO");
        return ratingDao.getTopRatedSong();
    }

    public Song getLowestRatedSong() {
        log.info("getLowestRatedSong(): requesting from DAO");
        return ratingDao.getLowestRatedSong();
    }

    public Song getMostPopularSong() {
        log.info("getMostPopularSong(): requesting from DAO");
        return ratingDao.getMostPopularSong();
    }



}
