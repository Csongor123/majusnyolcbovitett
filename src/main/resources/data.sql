-- Sample events
INSERT INTO event (name, location, date, category, favorite) VALUES
-- Running
('Félmaraton', 'Városi Park', '2025-06-15', 'Futás', false),
('Jótákonysági Futás', 'Erdő Park', '2026-03-01', 'Futás',false),

-- Swimming
('Bajnokság', 'Városi Uszoda', '2025-07-10', 'Úszás',false),
('Általános Tesnevelés', 'Egyetem Uszoda', '2023-05-22', 'Úszás', false),

-- Basketball
('felkészülés', 'Sporterem', '2025-08-05', 'Kosárlabda', false),

-- Football
('Kupa', 'Középiskola Focipálya', '2025-09-01', 'Labdarúgás', false),
('Járási edzés', 'Helyi Focipálya', '2025-09-15', 'Labdarúgás', false),

-- Tennis
('Open', 'Iskolai Teniszpálya', '2025-10-05', 'Tenisz', false),

-- Röplabda
('Városi bajnokság', 'Helyi Tornaterem', '2025-08-20', 'Röplabda', false),

-- Kézilabda
('Barátságos meccs', 'Városi Tornacsarnok', '2025-11-12', 'Kézilabda', false),

-- Jégkorong
('Téli Bajnokság', 'Korcsolyapálya', '2025-12-01', 'Jégkorong', false),

-- Kerékpározás
('Kerékpártúra', 'Település utcái', '2025-05-18', 'Kerékpározás', false),

-- Vívás
('Nyári bajnokság', 'Kistornaterem', '2025-06-05', 'Vívás', false),

-- Asztalitenisz
('Edző meccs', 'Szabadidőpark', '2025-07-01', 'Asztalitenisz', false);


-- Sample participants
INSERT INTO participant (name, age, email, activity_date, event_id) VALUES
-- Running
('Kiss Ferenc', 35, 'feri@example.com', '2025-04-25', 1),
('Nagy Helga', 28, 'helga@example.com', '2025-04-25', 1),
('Kovács Bence', 19, 'bence@example.com', '2025-04-25', 2),

-- Swimming
('Tóth Mihály', 30, 'toth@example.com', '2025-04-25', 3),
('Fazekas Dóra', 26, 'dóra@example.com', '2025-04-25', 4),

-- Basketball
('Szabó Kornél', 22, 'kornél@example.com', '2025-04-25', 5),

-- Football
('Varga Dominik', 45, 'dom@example.com', '2025-04-25', 6),
('Kecskés Balázs', 39, 'bzs@example.com', '2025-04-25', 7),

-- Tennis
('Babos Boglárka', 42, 'bogi@example.com', '2025-04-25', 8),

-- Röplabda
('Horváth Lili', 24, 'lili@example.com', '2025-04-25', 9),
('Nagy Ádám', 27, 'adam@example.com', '2025-04-25', 9),

-- Kézilabda
('Kovács Péter', 29, 'peter@example.com', '2025-04-25', 10),
('Tóth Judit', 31, 'judit@example.com', '2025-04-25', 10),

-- Jégkorong
('Kiss Martin', 32, 'martin@example.com', '2025-04-25', 11),
('Major Dániel', 34, 'daniel@example.com', '2025-04-25', 11),

-- Kerékpározás
('Varga Norbert', 36, 'norbert@example.com', '2025-04-25', 12),
('Szabó Krisztina', 25, 'krisztina@example.com', '2025-04-25', 12),

-- Vívás
('Farkas Gergő', 21, 'gergo@example.com', '2025-04-25', 13),

-- Asztalitenisz
('Németh Tamás', 23, 'tamas@example.com', '2025-04-25', 14);
