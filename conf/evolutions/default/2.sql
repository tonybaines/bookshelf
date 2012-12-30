# --- !Ups

ALTER TABLE books ADD COLUMN (author VARCHAR(80));

# --- !Downs
ALTER TABLE books DROP COLUMN author;