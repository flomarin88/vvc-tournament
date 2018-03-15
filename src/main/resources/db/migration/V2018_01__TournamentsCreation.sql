ALTER TABLE TOURNAMENT ADD COLUMN gender VARCHAR(10);
ALTER TABLE TOURNAMENT ADD COLUMN year INT;

UPDATE TOURNAMENT SET name = '3x3', gender = 'WOMEN', year = 2017 WHERE id = 1;
UPDATE TOURNAMENT SET name = '3x3', gender = 'MEN', year = 2017 WHERE id = 2;

INSERT INTO TOURNAMENT (id, name, gender, year, team_limit) VALUES (3, '3x3', 'WOMEN', 2018, 33);
INSERT INTO TOURNAMENT (id, name, gender, year, team_limit) VALUES (4, '3x3', 'MEN', 2018, 48);
