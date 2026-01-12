package SpringProject.persistences;

import SpringProject.entities.*;
import java.util.List;

public interface SongDao {
    List<Song> getSongsByTitle(String title);
    List<Song> getSongsByArtist(String artistName);
    List<Song> getSongsByAlbum(String albumTitle);
    List<Song> getSongsByGenre(String genreName);
    Song getSongById(int id);
    List<Song> findAll();
}