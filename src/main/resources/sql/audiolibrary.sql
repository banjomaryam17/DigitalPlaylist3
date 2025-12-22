DROP DATABASE IF EXISTS audiolibrary;
CREATE DATABASE audiolibrary;
USE audiolibrary;

CREATE TABLE genres
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);
CREATE TABLE playlists
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT          NOT NULL,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    is_public    BOOLEAN  DEFAULT FALSE,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE TABLE playlist_songs
(
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    playlist_id          INT NOT NULL,
    song_id              INT NOT NULL,
    added_date           DATETIME DEFAULT CURRENT_TIMESTAMP,
    position_in_playlist INT,
    FOREIGN KEY (playlist_id) REFERENCES playlists (id),
    FOREIGN KEY (song_id) REFERENCES songs (id),
    UNIQUE (playlist_id, song_id)
);
