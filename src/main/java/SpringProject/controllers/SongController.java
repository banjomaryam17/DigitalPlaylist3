package SpringProject.controllers;

import SpringProject.entities.Song;
import SpringProject.services.SongServices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongServices songServices;

    public SongController(SongServices songServices) {
        this.songServices = songServices;
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songServices.getAllSongs();
        if (songs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable int id) {
        Song song = songServices.getSongById(id);
        if (song == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Song not found");
        }
        return ResponseEntity.ok(song);
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Song>> searchByTitle(@RequestParam String title) {
        if (title == null || title.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        List<Song> results = songServices.searchByTitle(title);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/artist")
    public ResponseEntity<List<Song>> searchByArtist(@RequestParam String artist) {
        if (artist == null || artist.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        List<Song> results = songServices.searchByArtist(artist);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/album")
    public ResponseEntity<List<Song>> searchByAlbum(@RequestParam String album) {
        if (album == null || album.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        List<Song> results = songServices.searchByAlbum(album);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }


    @GetMapping("/search/genre")
    public ResponseEntity<List<Song>> searchByGenre(@RequestParam String genre) {
        if (genre == null || genre.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        List<Song> results = songServices.searchByGenre(genre);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }
}
