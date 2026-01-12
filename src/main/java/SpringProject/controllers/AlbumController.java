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

    // Get albums by artist
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable int artistId) {
        return ResponseEntity.ok(albumServices.getAlbumsByArtist(artistId));
    }

    // Get album by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAlbumById(@PathVariable int id) {
        Album album = albumServices.getAlbumById(id);
        if (album == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(album);
    }
}
