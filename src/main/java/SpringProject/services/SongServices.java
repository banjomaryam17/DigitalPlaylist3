package SpringProject.services;

import SpringProject.entities.Song;
import SpringProject.persistences.SongDao;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SongServices {

    private final SongDao songDao;

    public SongServices(SongDao songDao) {
        this.songDao = songDao;
    }

    public Song getSongById(int id) {
        return songDao.getSongById(id);
    }

    public List<Song> getAllSongs() {
        return songDao.findAll();
    }

    public List<Song> searchByTitle(String title) {
        return songDao.getSongsByTitle(title);
    }

    public List<Song> searchByArtist(String artistName) {
        return songDao.getSongsByArtist(artistName);
    }

    public List<Song> searchByAlbum(String albumTitle) {
        return songDao.getSongsByAlbum(albumTitle);
    }

    public List<Song> searchByGenre(String genreName) {
        return songDao.getSongsByGenre(genreName);
    }
}
