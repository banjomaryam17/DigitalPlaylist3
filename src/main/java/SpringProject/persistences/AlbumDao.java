package SpringProject.persistences;

import SpringProject.entities.Album;
import java.util.List;

public interface AlbumDao {
    List<Album> getAlbumsByArtistId(int artistId);
    Album getAlbumById(int id);
}
