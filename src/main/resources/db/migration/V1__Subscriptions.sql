CREATE TABLE TOURNAMENT
(
  id         BIGSERIAL PRIMARY KEY,
  name       VARCHAR(255) NOT NULL,
  team_limit INT          NOT NULL
);
CREATE UNIQUE INDEX TOURNAMENT_ID_UINDEX
  ON TOURNAMENT (id);

INSERT INTO TOURNAMENT (id, name, team_limit) VALUES (1, '3x3 Féminin', 33);
INSERT INTO TOURNAMENT (id, name, team_limit) VALUES (2, '3x3 Masculin', 48);

CREATE TABLE TEAM
(
  id             BIGSERIAL PRIMARY KEY,
  tournament_id  BIGINT REFERENCES TOURNAMENT,
  name           VARCHAR(255) NOT NULL,
  level          INT          NOT NULL,
  captain_name   VARCHAR(255) NOT NULL,
  captain_email  VARCHAR(255) NOT NULL,
  captain_phone  VARCHAR(255) NOT NULL,
  player_2_name  VARCHAR(255) NOT NULL,
  player_2_email VARCHAR(255) NOT NULL,
  player_3_name  VARCHAR(255) NOT NULL,
  player_3_email VARCHAR(255) NOT NULL,
  UNIQUE (tournament_id, name)
);
CREATE UNIQUE INDEX TEAM_ID_UINDEX
  ON TEAM (id);
