package SpringProject.services;

import SpringProject.entities.Album;
import SpringProject.persistences.AlbumDao;
import SpringProject.persistences.ArtistDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumServices {

    private final AlbumDao albumDao;
    private final ArtistDao artistDao;

    public AlbumServices (AlbumDao albumDao, ArtistDao artistDao) {
        this.albumDao = albumDao;
        this.artistDao = artistDao;
    }

    public List<Album> getAlbumsByArtist (int artistId) {
        if (artistDao.getArtistById(artistId) == null) {
            throw new IllegalArgumentException("Artist not found");
        }
        return albumDao.getAlbumsByArtistId(artistId);
    }

    public Album getAlbumById (int albumId) {
        Album album = albumDao.getAlbumById(albumId);
        if (album == null) {
            throw new IllegalArgumentException("Album not found");
        }
        return album;
    }
}
