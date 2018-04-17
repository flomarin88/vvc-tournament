ALTER TABLE TOURNAMENT ADD COLUMN subscriptions_opening_date TIMESTAMP;
ALTER TABLE TOURNAMENT ADD COLUMN subscriptions_closing_date TIMESTAMP;

UPDATE TOURNAMENT SET subscriptions_opening_date = '2018-05-01 18:00:00';
UPDATE TOURNAMENT SET subscriptions_closing_date = '2018-06-10 00:00:00';