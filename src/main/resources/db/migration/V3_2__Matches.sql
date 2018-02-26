CREATE TABLE MATCH
(
  id          BIGSERIAL PRIMARY KEY,
  pool_id     BIGINT REFERENCES POOL,
  team1_id    BIGINT REFERENCES TEAM,
  team2_id    BIGINT REFERENCES TEAM,
  score_team1 INT,
  score_team2 INT
);
CREATE UNIQUE INDEX MATCH_ID_UINDEX
  ON MATCH (id);