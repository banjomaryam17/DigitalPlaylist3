package SpringProject.persistences;

import SpringProject.entities.Rating;
import SpringProject.entities.Song;

import java.sql.SQLException;
import java.util.ArrayList;

public interface RatingDao {

    int addRating(Rating rating) throws SQLException;

    ArrayList<Rating> getAllRatings() throws SQLException;

    Rating findRatingByUsernameAndSongID(String username, int songID) throws SQLException;

    ArrayList<Rating> getUserRatingFromUsername(String username) throws SQLException;

    Song getLowestRatedSong();

    Song getMostPopularSong();

    Song getTopRatedSong();
}
