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

    /**
     * get album by artist unique id
     * @param artistId unique id belonging to an artist
     * @return albums retaining to the artist
     */
    public List<Album> getAlbumsByArtist (int artistId) {
        if (artistDao.getArtistById(artistId) == null) {
            throw new IllegalArgumentException("Artist not found");
        }
        return albumDao.getAlbumsByArtistId(artistId);
    }

    /**
     * get album by its unique id
     * @param albumId unique to each album
     * @return all album information
     */
    public Album getAlbumById (int albumId) {
        Album album = albumDao.getAlbumById(albumId);
        if (album == null) {
            throw new IllegalArgumentException("Album not found");
        }
        return album;
    }
}
