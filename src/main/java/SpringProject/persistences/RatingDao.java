package SpringProject.persistences;
import java.util.ArrayList;


import SpringProject.entities.Rating;

public  interface RatingDao {


     public   int addRating (RatingDao rating );  // the public boolen implement RatingSong


    public ArrayList<Rating> getAllRatings();

      public Song getTopRatedSong();
      public Song getMostPopularSong();


      public Song getLowestRatedSong();
      public Rating findRatingByUsernameAndSongID(String username, int songID);

    public ArrayList<Rating> getUserRatingFromUsername(String username);

}
