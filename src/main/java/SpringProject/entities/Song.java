package SpringProject.entities;

import java.util.Objects;

public class Song {

    private int songID;
    private String title;
    private String artist;

    // No-args constructor
    public Song() {
    }

    // All-args constructor
    public Song(int songID, String title, String artist) {
        this.songID = songID;
        this.title = title;
        this.artist = artist;
    }

    // Getters
    public int getSongID() {
        return songID;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    // Setters
    public void setSongID(int songID) {
        this.songID = songID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songID=" + songID +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }

    // Equality based on songID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return songID == song.songID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(songID);
    }
}
