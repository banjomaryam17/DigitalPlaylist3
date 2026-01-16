package SpringProject.services;

import SpringProject.entities.Song;
import SpringProject.persistences.Connector;
import SpringProject.persistences.MySqlConnector;
import SpringProject.persistences.SongDao;
import SpringProject.persistences.SongDaoImpl;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;
@Service
public class SongServices {

    private final SongDao songDao;

    public SongServices(SongDao songDao) {
        this.songDao = songDao;
    }

    /**
     * searches for songs using its specific id
     * @param id unique toi each song
     * @return all info relating to the song
     */
    public Song getSongById(int id) {
        return songDao.getSongById(id);
    }

    /**
     * searches for all songs within the database
     * @return a list of all songs in database
     */
    public List<Song> getAllSongs() {
        return songDao.findAll();
    }

    /**
     * searches for song by title
     * @param title of the desired song
     * @return all information on the chosen song
     */
    public List<Song> searchByTitle(String title) {
        return songDao.getSongsByTitle(title);
    }

    /**
     * searches for songs by a given artist
     * @param artistName name of artist
     * @return all songs by chosen artist
     */
    public List<Song> searchByArtist(String artistName) {
        return songDao.getSongsByArtist(artistName);
    }

    /**
     * searches for songs in a specific album
     * @param albumTitle the album you want to search
     * @return a list of songs within the album
     */
    public List<Song> searchByAlbum(String albumTitle) {
        return songDao.getSongsByAlbum(albumTitle);
    }

    /**
     * searches for songs with a specific genre
     * @param genreName the chosen genre that you want to find
     * @return a list of songs in that specific genre
     */
    public List<Song> searchByGenre(String genreName) {
        return songDao.getSongsByGenre(genreName);
    }
}
