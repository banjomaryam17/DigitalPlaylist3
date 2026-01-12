package SpringProject.persistences;

import SpringProject.entities.Album;
import SpringProject.entities.Artist;

import java.util.List;
import java.util.Optional;

public interface ArtistDao {
    List<Artist> getAllArtists();
    Artist getArtistById(int id);
    List<Album> getAlbumsByArtist(int artistId);
    List<Artist> findAll();
    Optional<Artist> findById(int id);
}
