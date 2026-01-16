package SpringProject.controllers;

import SpringProject.entities.Album;
import SpringProject.entities.Artist;
import SpringProject.services.ArtistServices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistServices artistService;

    public ArtistController(ArtistServices artistService) {
        this.artistService = artistService;
    }

    /**
     * @return a list of all artists
     */
    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    /**
     * search by id to find an artist
     * @param id the id of the artist you want to find
     * @return artist entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getArtist(@PathVariable int id) {
        try {
            return ResponseEntity.ok(artistService.getArtistById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    /**
     *get album by artist id
     * @param id corresponding to the artist
     * @return returns all albums by artist
     */
    @GetMapping("/{id}/albums")
    public ResponseEntity<?> getAlbumsByArtist(@PathVariable int id) {
        return ResponseEntity.ok(artistService.getAlbumsByArtist(id));
    }
}
