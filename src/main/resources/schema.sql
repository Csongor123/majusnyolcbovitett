DROP TABLE IF EXISTS participant;
DROP TABLE IF EXISTS event;

CREATE TABLE event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    date DATE,
    category VARCHAR(255),
    favorite BOOLEAN DEFAULT FALSE
);

CREATE TABLE participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT CHECK (age >= 0 AND age <= 99),
    email VARCHAR(255),
    activity_date DATE NOT NULL,
    event_id BIGINT,
    FOREIGN KEY (event_id) REFERENCES event(id)
);
