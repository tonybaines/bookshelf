# --- !Ups

CREATE TABLE books (
  id INTEGER NOT NULL AUTO_INCREMENT,
  title varchar(80),
  PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE if exists books;