CREATE TABLE publication (
  id              BIGINT PRIMARY KEY NOT NULL,
  link            VARCHAR(300),
  photoshoot_date DATE,
  location        VARCHAR,
  country         VARCHAR,
  title           VARCHAR,
  about           VARCHAR(1500),
  hashtags        VARCHAR(500),
  additional_phs  VARCHAR,
  additional_mds  VARCHAR,
  ph_techincal    VARCHAR,
  created_on      BIGINT,
  vk_post_id      BIGINT REFERENCES vk_post (id)
);

CREATE TABLE publication_pictures (
  id             BIGINT PRIMARY KEY     NOT NULL,
  file_id        VARCHAR(400)           NOT NULL,
  file_name      VARCHAR(255)           NOT NULL,
  friendly_link  VARCHAR(500)           NOT NULL,
  native_link    VARCHAR(500)           NOT NULL,
  publication_id BIGINT REFERENCES publication (id)

);


CREATE TABLE publication_user (
  id             BIGINT PRIMARY KEY                 NOT NULL,
  publication_id BIGINT REFERENCES publication (id) NOT NULL,
  user_id        BIGINT REFERENCES user_yf (id),
  type           VARCHAR                            NOT NULL
);


CREATE TABLE ph_profile (
  id         BIGINT PRIMARY KEY NOT NULL,
  instagram  VARCHAR,
  vk         BIGINT,
  facebook   VARCHAR,
  location   VARCHAR,
  country    VARCHAR,
  twitter    VARCHAR,
  about      VARCHAR(1000),
  website    VARCHAR,
  created_on DATE,
  user_id    BIGINT REFERENCES user_yf (id)
);

CREATE TABLE md_profile (
  id         BIGINT PRIMARY KEY NOT NULL,
  instagram  VARCHAR,
  vk         BIGINT,
  facebook   VARCHAR,
  location   VARCHAR,
  country    VARCHAR,
  twitter    VARCHAR,
  about      VARCHAR(1000),
  website    VARCHAR,
  created_on DATE,
  user_id    BIGINT REFERENCES user_yf (id)
);

ALTER TABLE user_saved_posts ADD COLUMN publication_id BIGINT;
ALTER TABLE user_saved_posts ADD FOREIGN KEY (publication_id)  REFERENCES publication (id);
UPDATE user_saved_posts SET publication_id = publication.id FROM publication WHERE publication.vk_post_id = user_saved_posts.post_id;

ALTER TABLE user_saved_posts ADD COLUMN created_on BIGINT;
ALTER TABLE user_saved_posts DROP COLUMN date;
UPDATE user_saved_posts SET created_on = '1533915264000';