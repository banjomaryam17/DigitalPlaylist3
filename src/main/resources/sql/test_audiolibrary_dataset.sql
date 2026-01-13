USE test_audiolibrary;

INSERT INTO genres (name, description)
VALUES ('Pop', 'Popular mainstream music with catchy melodies'),
       ('Rock', 'Guitar-driven music with strong beats'),
       ('Hip Hop', 'Rhythmic music featuring rap and beats'),
       ('Electronic', 'Music produced using electronic instruments and technology'),
       ('Jazz', 'Improvisational music with complex harmonies'),
       ('Classical', 'Traditional orchestral and chamber music'),
       ('R&B', 'Rhythm and Blues, soulful vocals'),
       ('Country', 'American roots music with storytelling lyrics');

INSERT INTO users (username, email, passwordHash, subscriptionStatus, subscriptionExpiry)
VALUES ('athena', 'athena@email', '$2a$12$MfrThbMXfkuVoaUKFc0gWO99v7iUGc/KmEhh0nOgbl5CjBu5719by', 'active',
        '2026-12-31 23:59:59'),
       ('helo', 'helo@email', '$2a$12$a/5xFCTok30DeFrLmBfP.OrqaQuRFMOxfCpM9Afh7tBI75.XTD1Nu', 'canceled',
        '2024-08-10 23:59:59'),
       ('lee', 'lee@email', '$2a$12$Z/KqOM412KpQFULhBf4HO.lToA5h.JfEt3XVHDG39TvbdmiLI4lJa', 'expired',
        '2024-10-15 23:59:59');

INSERT INTO artists (name, bio, country, formedYear)
VALUES ('The Midnight Riders', 'Legendary rock band known for their powerful guitar riffs', 'USA', 1985),
       ('Luna Sky', 'Pop sensation with chart-topping hits', 'UK', 2015),
       ('DJ Pulse', 'Electronic music producer and DJ', 'Germany', 2010),
       ('The Jazz Collective', 'Instrumental jazz ensemble', 'USA', 1998),
       ('Bella Harmony', 'R&B vocalist with soulful voice', 'USA', 2018),
       ('Urban Poets', 'Hip hop group with socially conscious lyrics', 'USA', 2012),
       ('Symphony Orchestra', 'Classical music ensemble', 'Austria', 1950),
       ('Country Roads Band', 'Traditional country music group', 'USA', 2005),
       ('Electric Dreams', 'Electronic pop duo', 'Sweden', 2016),
       ('Rock Legends', 'Classic rock revival band', 'USA', 2008),
       ('Smooth Vibes', 'Contemporary R&B artist', 'Canada', 2019),
       ('Beat Masters', 'Hip hop production collective', 'USA', 2013),
       ('Neon Nights', 'Synthwave electronic artist', 'France', 2017),
       ('Soul Sisters', 'Female R&B vocal group', 'USA', 2014),
       ('Mountain Echo', 'Folk-country singer-songwriter', 'USA', 2020);

INSERT INTO albums (title, artistId, genreId, releaseDate)
VALUES ('Electric Highway', 1, 2, '1990-05-15'),
       ('Midnight Chronicles', 1, 2, '1995-08-20'),
       ('Starlight', 2, 1, '2018-03-10'),
       ('Dreams & Reality', 2, 1, '2020-11-05'),
       ('Pulse Wave', 3, 4, '2015-07-22'),
       ('Digital Horizon', 3, 4, '2019-09-14'),
       ('Jazz After Dark', 4, 5, '2005-02-28'),
       ('Soul Sessions', 5, 7, '2021-06-12'),
       ('Urban Stories', 6, 3, '2016-04-18'),
       ('Street Philosophy', 6, 3, '2019-12-01'),
       ('Classical Masters Vol. 1', 7, 6, '2010-01-10'),
       ('Backroads', 8, 8, '2008-10-30'),
       ('Country Heart', 8, 8, '2012-05-25'),
       ('Synthetic Dreams', 9, 4, '2018-08-15'),
       ('Rock Revival', 10, 2, '2011-03-20'),
       ('Velvet Voice', 11, 7, '2022-02-14'),
       ('Beat Laboratory', 12, 3, '2017-11-08'),
       ('Neon Memories', 13, 4, '2020-06-30'),
       ('Harmony & Soul', 14, 7, '2019-09-22'),
       ('Mountain Tales', 15, 8, '2023-04-10');

INSERT INTO songs (title, artistId, albumId, genreId, durationSeconds, releaseYear)
VALUES ('Thunder Road', 1, 1, 2, 245, 1990),
       ('Highway Dreams', 1, 1, 2, 298, 1990),
       ('Midnight Rain', 1, 2, 2, 312, 1995),
       ('Rebel Heart', 1, 2, 2, 276, 1995),
       ('Dancing Stars', 2, 3, 1, 195, 2018),
       ('Golden Hour', 2, 3, 1, 210, 2018),
       ('Dream On', 2, 4, 1, 223, 2020),
       ('Reality Check', 2, 4, 1, 189, 2020),
       ('Electric Pulse', 3, 5, 4, 267, 2015),
       ('Wave Rider', 3, 5, 4, 301, 2015),
       ('Digital Sky', 3, 6, 4, 285, 2019),
       ('Horizon', 3, 6, 4, 312, 2019),
       ('Late Night Jazz', 4, 7, 5, 421, 2005),
       ('Smooth Sax', 4, 7, 5, 389, 2005),
       ('Velvet Soul', 5, 8, 7, 234, 2021),
       ('Heartbeat', 5, 8, 7, 256, 2021),
       ('City Lights', 6, 9, 3, 198, 2016),
       ('Street Wisdom', 6, 9, 3, 213, 2016),
       ('Urban Legend', 6, 10, 3, 227, 2019),
       ('Truth Spoken', 6, 10, 3, 241, 2019),
       ('Symphony No. 5', 7, 11, 6, 1820, 2010),
       ('Concerto in D', 7, 11, 6, 1456, 2010),
       ('Country Morning', 8, 12, 8, 203, 2008),
       ('Backroad Melody', 8, 12, 8, 218, 2008),
       ('Hometown Glory', 8, 13, 8, 195, 2012),
       ('Simple Life', 8, 13, 8, 207, 2012),
       ('Neon Love', 9, 14, 4, 243, 2018),
       ('Synthetic Heart', 9, 14, 4, 267, 2018),
       ('Rock Anthem', 10, 15, 2, 289, 2011),
       ('Guitar Hero', 10, 15, 2, 312, 2011),
       ('Smooth Operator', 11, 16, 7, 221, 2022),
       ('Velvet Touch', 11, 16, 7, 234, 2022),
       ('Beat Drop', 12, 17, 3, 192, 2017),
       ('Bass Line', 12, 17, 3, 206, 2017),
       ('Neon Nights', 13, 18, 4, 278, 2020),
       ('Retro Wave', 13, 18, 4, 295, 2020),
       ('Sisters Unite', 14, 19, 7, 245, 2019),
       ('Harmony Rising', 14, 19, 7, 267, 2019),
       ('Mountain View', 15, 20, 8, 189, 2023),
       ('Echo Valley', 15, 20, 8, 201, 2023);

INSERT INTO ratings (userId, songId, rating)
VALUES
    (1, 1, 5), (1, 5, 4), (1, 9, 5), (1, 15, 3), (1, 20, 4),
    (1, 4, 5), (1, 8, 5), (1, 12, 4), (1, 18, 5), (1, 23, 4),
    (1, 3, 4), (1, 11, 5), (1, 25, 3), (1, 21, 4), (1, 26, 5),
    (1, 6, 4), (1, 14, 5), (1, 28, 4), (1, 24, 3), (1, 29, 5),
    (2, 2, 4), (2, 6, 5), (2, 10, 4), (2, 16, 5), (2, 21, 3),
    (2, 1, 4), (2, 9, 5), (2, 13, 5), (2, 19, 3), (2, 24, 4),
    (2, 4, 5), (2, 12, 4), (2, 30, 5), (2, 22, 4), (2, 27, 3),
    (2, 7, 3), (2, 15, 4), (2, 25, 5), (2, 31, 4), (2, 33, 5),
    (3, 3, 3), (3, 7, 4), (3, 11, 3), (3, 17, 4), (3, 22, 5),
    (3, 2, 5), (3, 10, 4), (3, 14, 4), (3, 20, 5), (3, 35, 3),
    (3, 5, 3), (3, 13, 4), (3, 32, 5), (3, 23, 5), (3, 28, 4),
    (3, 8, 4), (3, 16, 3), (3, 26, 5), (3, 34, 4), (3, 36, 5);



INSERT INTO playlists (userId, name, description, isPublic)
VALUES
    (1, 'My Workout Mix', 'High energy songs for gym sessions', TRUE),
    (1, 'Chill Evening', 'Relaxing tunes for winding down', FALSE),
    (1, 'Favorites 2024', 'My top songs this year', TRUE),
    (1, 'Morning Motivation', 'Upbeat songs to start the day', FALSE),
    (2, 'Road Trip Anthems', 'Perfect songs for long drives', TRUE),
    (2, 'Late Night Vibes', 'Smooth music for late hours', TRUE),
    (2, 'Hip Hop Favorites', 'Top hip hop tracks', TRUE),
    (2, 'Party Hits', 'Dance floor favorites', TRUE),
    (3, 'Study Focus', 'Instrumental music for concentration', TRUE),
    (3, 'Rock Classics', 'Best rock songs of all time', TRUE),
    (3, 'Jazz Standards', 'Essential jazz recordings', FALSE),
    (3, 'Country Roads', 'Country music collection', FALSE);

INSERT INTO playlistSongs(playlistId, songId, positionInPlaylist)
VALUES (1, 1, 1),
       (1, 9, 2),
       (1, 17, 3),
       (1, 29, 4),
       (2, 13, 1),
       (2, 15, 2),
       (2, 31, 3),
       (2, 35, 4),
       (3, 2, 1),
       (3, 5, 2),
       (3, 23, 3),
       (3, 39, 4),
       (4, 21, 1),
       (4, 22, 2),
       (4, 13, 3),
       (4, 14, 4),
       (5, 5, 1),
       (5, 7, 2),
       (5, 27, 3),
       (5, 33, 4),
       (6, 15, 1),
       (6, 16, 2),
       (6, 31, 3),
       (6, 37, 4),
       (7, 35, 1),
       (7, 13, 2),
       (7, 27, 3),
       (7, 11, 4),
       (8, 1, 1),
       (8, 3, 2),
       (8, 29, 3),
       (8, 4, 4),
       (9, 13, 1),
       (9, 14, 2),
       (9, 21, 3),
       (9, 22, 4),
       (10, 17, 1),
       (10, 19, 2),
       (10, 33, 3),
       (10, 34, 4),
       (11, 23, 1),
       (11, 25, 2),
       (11, 39, 3),
       (11, 40, 4),
       (12, 5, 1),
       (12, 9, 2),
       (12, 15, 3),
       (12, 27, 4);

INSERT INTO subscription (`username`, `sub_startDate`, `sub_endDate`)
VALUES ('Aisha', '2025-11-01 00:00:00', '2026-11-01 00:00:00'),
       ('Liam', '2025-03-10 00:00:00', '2026-03-10 00:00:00'),
       ('Ciara', '2024-12-15 00:00:00', '2025-12-15 00:00:00');






