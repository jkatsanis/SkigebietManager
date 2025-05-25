-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

-- Create sequences for all entities
CREATE SEQUENCE IF NOT EXISTS user_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS ski_lift_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS piste_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS ticket_seq START WITH 1;

-- Insert default users
INSERT INTO users (id, name, email) VALUES 
(next value for user_seq, 'Max Mustermann', 'max@example.com');

-- Insert default ski lifts
INSERT INTO ski_lifts (id, name, typ, kapazitaet) VALUES 
(next value for ski_lift_seq, 'Gipfelbahn', 'Sessellift', 1200),
(next value for ski_lift_seq, 'Talbahn', 'Schlepplift', 800);

-- Insert default pistes
INSERT INTO pisten (id, name, schwierigkeitsgrad, laenge, ski_lift_id) VALUES 
(next value for piste_seq, 'Gipfelabfahrt', 'Schwer', 3.5, 1),
(next value for piste_seq, 'Talabfahrt', 'Leicht', 2.0, 2);

-- Insert default tickets
INSERT INTO tickets (id, user_id, ticketType, date, validFrom, validUntil) VALUES 
(next value for ticket_seq, 1, 'Ganztages', '2024-03-20', '08:00', '16:00'),
(next value for ticket_seq, 1, 'Halbtages', '2024-03-20', '12:00', '16:00');