CREATE TABLE ROUND
(
  id                BIGSERIAL PRIMARY KEY,
  name              VARCHAR(255) NOT NULL,
  tournament_id     BIGINT REFERENCES TOURNAMENT,
  previous_round_id BIGINT REFERENCES ROUND,
  tournament_branch VARCHAR(255) NOT NULL,
  status            VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX ROUND_ID_UINDEX
  ON ROUND (id);

CREATE TABLE ROUND_TEAM
(
  round_id BIGINT REFERENCES ROUND,
  team_id  BIGINT REFERENCES TEAM
);