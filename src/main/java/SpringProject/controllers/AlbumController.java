package SpringProject.controllers;

import SpringProject.entities.Album;
import SpringProject.services.AlbumServices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumServices albumServices;

    public AlbumController(AlbumServices albumServices) {
        this.albumServices = albumServices;
    }

    /** get album vis artist unique id
     * @param artistId unique id belonging to each artist
     * @return all albums relating to the artist
     */
    // Get albums by artist
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable int artistId) {
        return ResponseEntity.ok(albumServices.getAlbumsByArtist(artistId));
    }

    /**
     * get album by album unique id
     * @param id corresponding to the individual album
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAlbumById(@PathVariable int id) {
        Album album = albumServices.getAlbumById(id);
        if (album == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(album);
    }
}