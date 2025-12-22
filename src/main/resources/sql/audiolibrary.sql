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


CREATE TABLE subscription (
    subID INT AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    sub_startDate DATETIME NOT NULL,
    sub_endDate DATETIME NOT NULL,
    PRIMARY KEY (subID),
    UNIQUE (username),
    FOREIGN KEY (username) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE users (
    username VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    userType TINYINT NOT NULL DEFAULT 1 COMMENT '1 for general user, 2 for admin',
    PRIMARY KEY (username)
);


CREATE TABLE rating (
     username VARCHAR(50) NOT NULL,
     songID INT NOT NULL,
     userRating DECIMAL(2,1) NOT NULL,
     PRIMARY KEY (username, songID),
     FOREIGN KEY (username) REFERENCES users(username)
     ON UPDATE CASCADE ON DELETE CASCADE,
     FOREIGN KEY (songID) REFERENCES songs(songID)
     ON UPDATE CASCADE ON DELETE CASCADE
);
