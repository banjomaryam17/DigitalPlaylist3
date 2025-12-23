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
    userId      INT          NOT NULL,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    isPublic    BOOLEAN  DEFAULT FALSE,
    createdDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users (id) ON DELETE CASCADE
);
CREATE TABLE playlist_songs
(
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    playlistId          INT NOT NULL,
    songId              INT NOT NULL,
    addedDate           DATETIME DEFAULT CURRENT_TIMESTAMP,
    positionInPlaylist INT,
    FOREIGN KEY (playlistId) REFERENCES playlists (id),
    FOREIGN KEY (songId) REFERENCES songs (id),
    UNIQUE (playlistId, songId)
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
       id INT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(50) NOT NULL UNIQUE,
       email VARCHAR(100) NOT NULL UNIQUE,
       passwordHash VARCHAR(255) NOT NULL,
      subscriptionStatus VARCHAR(20) NOT NULL DEFAULT 'expired',
      subscriptionExpiry DATETIME,
      createdDate DATETIME DEFAULT CURRENT_TIMESTAMP

);


CREATE TABLE ratings (
     id INT AUTO_INCREMENT PRIMARY KEY,
     userId INT NOT NULL,
     songId INT NOT NULL,
     rating INT NOT NULL,
     ratedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY (userId) REFERENCES users(id),
     FOREIGN KEY (songId) REFERENCES songs(id),
     UNIQUE (userId, songId)

);
