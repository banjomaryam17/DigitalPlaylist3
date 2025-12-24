USE audiolibrary;

INSERT INTO genres (name, description) VALUES
('Pop', 'Popular mainstream music with catchy melodies'),
('Rock', 'Guitar-driven music with strong beats'),
('Hip Hop', 'Rhythmic music featuring rap and beats'),
('Electronic', 'Music produced using electronic instruments and technology'),
('Jazz', 'Improvisational music with complex harmonies'),
('Classical', 'Traditional orchestral and chamber music'),
('R&B', 'Rhythm and Blues, soulful vocals'),
('Country', 'American roots music with storytelling lyrics');


INSERT INTO playlists (user_id, name, description, is_public) VALUES
(1, 'My Workout Mix', 'High energy songs for gym sessions', TRUE),
(1, 'Chill Evening', 'Relaxing tunes for winding down', FALSE),
(2, 'Road Trip Anthems', 'Perfect songs for long drives', TRUE),
(3, 'Study Focus', 'Instrumental music for concentration', TRUE),
(4, 'Party Hits', 'Dance floor favorites', TRUE),
(5, 'Morning Motivation', 'Upbeat songs to start the day', FALSE),
(6, 'Late Night Vibes', 'Smooth music for late hours', TRUE),
(7, 'Rock Classics', 'Best rock songs of all time', TRUE),
(8, 'Jazz Standards', 'Essential jazz recordings', FALSE),
(9, 'Hip Hop Favorites', 'Top hip hop tracks', TRUE),
(10, 'Country Roads', 'Country music collection', FALSE),
(1, 'Favorites 2024', 'My top songs this year', TRUE);

INSERT INTO playlist_songs (playlist_id, song_id, position_in_playlist) VALUES
(1, 1, 1),
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
VALUES
    ('Aisha',  '2025-11-01 00:00:00', '2026-11-01 00:00:00'),
    ('Liam',   '2025-03-10 00:00:00', '2026-03-10 00:00:00'),
    ('Ciara',  '2024-12-15 00:00:00', '2025-12-15 00:00:00');

INSERT INTO users (username, email, password_hash, subscription_status, subscription_expiry) VALUES
  ('athena','athena@email','$2a$12$MfrThbMXfkuVoaUKFc0gWO99v7iUGc/KmEhh0nOgbl5CjBu5719by','active', '2026-12-31 23:59:59'),
  ( 'helo', 'helo@email', '$2a$12$a/5xFCTok30DeFrLmBfP.OrqaQuRFMOxfCpM9Afh7tBI75.XTD1Nu', 'canceled', '2024-08-10 23:59:59'),
  ( 'lee', 'lee@email','$2a$12$Z/KqOM412KpQFULhBf4HO.lToA5h.JfEt3XVHDG39TvbdmiLI4lJa', 'expired', '2024-10-15 23:59:59'),


INSERT INTO ratings (userId, songId, rating) VALUES
     (1, 1, 5), (1, 5, 4), (1, 9, 5), (1, 15, 3), (1, 20, 4),
     (2, 2, 4), (2, 6, 5), (2, 10, 4), (2, 16, 5), (2, 21, 3),
     (3, 3, 3), (3, 7, 4), (3, 11, 3), (3, 17, 4), (3, 22, 5),
     (4, 4, 5), (4, 8, 5), (4, 12, 4), (4, 18, 5), (4, 23, 4),
     (5, 1, 4), (5, 9, 5), (5, 13, 5), (5, 19, 3), (5, 24, 4),
     (6, 2, 5), (6, 10, 4), (6, 14, 4), (6, 20, 5), (6, 25, 3),
     (7, 3, 4), (7, 11, 5), (7, 15, 3), (7, 21, 4), (7, 26, 5),
     (8, 4, 5), (8, 12, 4), (8, 16, 5), (8, 22, 4), (8, 27, 3),
     (9, 5, 3), (9, 13, 4), (9, 17, 5), (9, 23, 5), (9, 28, 4),
     (10, 6, 4), (10, 14, 5), (10, 18, 4), (10, 24, 3), (10, 29, 5);