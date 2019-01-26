
CREATE TABLE submission (
  id           BIGINT PRIMARY KEY NOT NULL,
  created_on   BIGINT             NOT NULL,
  uuid         VARCHAR(255)       NOT NULL,
  about         TEXT,
  title        VARCHAR(255),
  country      VARCHAR(100),
  city         VARCHAR(100),
  submitter_id BIGINT REFERENCES user_yf (id),
  event_date   BIGINT,
  status       VARCHAR(255),
  comment      VARCHAR(255),
  equipment    VARCHAR (255)
);


CREATE TABLE submission_participant (
  id            BIGINT PRIMARY KEY NOT NULL,
  number            INT,
  created_on    BIGINT             NOT NULL,
  first_name    VARCHAR(255),
  last_name     VARCHAR(255),
  me            BOOLEAN,
  type          VARCHAR(255),
  instagram     VARCHAR(255),
  vk            VARCHAR(255),
  facebook      VARCHAR(255),
  website       VARCHAR(255),
  agency        VARCHAR(255),
  city          VARCHAR(100),
  country       VARCHAR(100),
  submission_fk BIGINT REFERENCES submission (id)

);


CREATE TABLE publication_participant (
  id            BIGINT PRIMARY KEY NOT NULL,
  number            INT,
  created_on    BIGINT             NOT NULL,
  first_name    VARCHAR(255),
  last_name     VARCHAR(255),
  me            BOOLEAN,
  type          VARCHAR(255),
  instagram     VARCHAR(255),
  vk            VARCHAR(255),
  facebook      VARCHAR(255),
  website       VARCHAR(255),
  agency        VARCHAR(255),
  city          VARCHAR(100),
  country       VARCHAR(100),
  publication_fk BIGINT REFERENCES publication (id)

);

ALTER TABLE publication RENAME location TO city;
ALTER TABLE publication DROP COLUMN photoshoot_date;
ALTER TABLE publication ADD COLUMN event_date BIGINT;
ALTER TABLE publication DROP COLUMN title;
ALTER TABLE publication DROP COLUMN about;
ALTER TABLE publication ADD COLUMN about TEXT;
ALTER TABLE publication ADD COLUMN title VARCHAR(255);
ALTER TABLE publication RENAME ph_techincal TO equipment;
ALTER TABLE publication ADD COLUMN type VARCHAR(255);

ALTER TABLE publication_pictures add COLUMN created_on BIGINT NOT NULL;
ALTER TABLE publication_pictures DROP COLUMN native_link;
ALTER TABLE publication_pictures add COLUMN sha1 VARCHAR(500) NOT NULL;

ALTER TABLE publication
  ADD COLUMN submission_id BIGINT;
ALTER TABLE publication
  ADD CONSTRAINT submission_fk FOREIGN KEY (submission_id) REFERENCES submission (id) ON DELETE CASCADE;

ALTER TABLE user_vk DROP COLUMN verified;
ALTER TABLE user_vk ADD COLUMN verified BOOLEAN;
ALTER TABLE public.user_yf ALTER COLUMN id TYPE BIGINT USING id::BIGINT;

ALTER TABLE user_saved_posts ALTER COLUMN post_id DROP NOT NULL;