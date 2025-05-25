-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

-- Insert default users
INSERT INTO users (id, name, email) VALUES 
(1, 'Max Mustermann', 'max@example.com');

-- Insert default ski lifts
INSERT INTO ski_lift (id, name, typ, kapazitaet) VALUES 
(1, 'Gipfelbahn', 'Sessellift', 1200),
(2, 'Talbahn', 'Schlepplift', 800);

-- Insert default pistes
INSERT INTO piste (id, name, schwierigkeitsgrad, laenge, ski_lift_id) VALUES 
(1, 'Gipfelabfahrt', 'Schwer', 3.5, 1),
(2, 'Talabfahrt', 'Leicht', 2.0, 2);

-- Insert default tickets
INSERT INTO ticket (id, user_id, ticket_type, date, valid_from, valid_until) VALUES 
(1, 1, 'Ganztages', '2024-03-20', '08:00', '16:00'),
(2, 1, 'Halbtages', '2024-03-20', '12:00', '16:00');