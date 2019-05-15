DROP TABLE IF EXISTS posts;

CREATE TABLE posts (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  title VARCHAR(250) NOT NULL UNIQUE,
  body VARCHAR(250) NOT NULL
);

DROP TABLE IF EXISTS comments;

CREATE TABLE comments (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  post_id INT,
  name VARCHAR(50) NOT NULL,
  email VARCHAR(50) NOT NULL,
  body VARCHAR(250) NOT NULL
);

ALTER TABLE comments
    ADD FOREIGN KEY (post_id)
    REFERENCES posts(id);

INSERT INTO posts (id, title, body) VALUES (2, 'Title 2', 'Body Text Description 2');
INSERT INTO posts (id, title, body) VALUES (3, 'Title 3', 'Body Text Description 3');

INSERT INTO comments (id, post_id, name, email, body) VALUES (4, 3, 'Name 1', 'Email1@email.tn', 'Body Comment Text 1');
INSERT INTO comments (id, post_id, name, email, body) VALUES (5, 3, 'Name 3', 'Email3@email.tn', 'Body Comment Text 3');