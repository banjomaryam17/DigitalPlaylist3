package SpringProject.services;

import SpringProject.entities.Artist;
import SpringProject.entities.Album;
import SpringProject.persistences.ArtistDao;
import SpringProject.persistences.AlbumDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServices {

    private final ArtistDao artistDao;
    private final AlbumDao albumDao;

    public ArtistServices(ArtistDao artistDao, AlbumDao albumDao) {
        this.artistDao = artistDao;
        this.albumDao = albumDao;
    }

    /**
     * searches for all artist and returns them as a list
     * @return returns a list of artists with every entry\
     */
    public List<Artist> getAllArtists() {
        return artistDao.getAllArtists();
    }

    /**
     * searches by artist with unique id
     * @param id unique to each artist
     * @return all info retaining to artist
     */
    public Artist getArtistById(int id) {
        return artistDao.getArtistById(id);
    }

    /**
     * searches for albums by an artists specific id
     * @param artistId unique to each artist
     * @return all album information relating to the artist
     */
    public List<Album> getAlbumsByArtist(int artistId) {
        return artistDao.getAlbumsByArtist(artistId);
    }
}
