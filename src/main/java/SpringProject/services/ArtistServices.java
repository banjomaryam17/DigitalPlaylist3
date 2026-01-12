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

    public List<Artist> getAllArtists() {
        return artistDao.getAllArtists();
    }

    public Artist getArtistById(int id) {
        return artistDao.getArtistById(id);
    }

    public List<Album> getAlbumsByArtist(int artistId) {
        return artistDao.getAlbumsByArtist(artistId);
    }
}
