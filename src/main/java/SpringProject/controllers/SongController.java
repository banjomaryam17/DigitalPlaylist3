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

    /**
     * getAll songs request, obtains a list with all songs in the database
     * @return a list of all songs
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songServices.getAllSongs();
        if (songs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(songs);
    }

    /**
     * get the song related to the current song id
     * @param id inputted will be validated and if there is a song with the same Id, it will be returned
     * @return a sing with the same id as the param id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable int id) {
        Song song = songServices.getSongById(id);
        if (song == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Song not found");
        }
        return ResponseEntity.ok(song);
    }


    /**
     * get the song related to the current song id
     * @param title inputted will be validated and if there is a song with that name, the song entity will be returned
     * @return a song with the same id as the param id
     */
    @GetMapping("/search/{title}")
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

    /**
     * search for songs related to the inputted artist
     * @param artist inputted will be validated and if there is a song with the artist name, it will be returned
     * @return a song with the same artist name as the param artist
     */
    @GetMapping("/search/artist/{artist}")
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

    /**
     * get all songs in a given album, if album doesnt exist returns bedRequest
     * @param album
     * @return
     */
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

    /**
     * To search for songs with a specific genre within a list of Songs, if it doesnt exist or is empty/null it returns badRequest.
     * @param genre the genre you want to search with
     * @return list of songs relating to that genre
     */
    @GetMapping("/search/genre/{genre}")
    public ResponseEntity<List<Song>> searchByGenre(@RequestParam String genre) {
        System.out.println(genre);
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
