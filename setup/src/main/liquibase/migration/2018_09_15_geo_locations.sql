-- CREATE TABLE _countries (
--   country_id BIGINT PRIMARY KEY     NOT NULL,
--   title_ru   VARCHAR,
--   title_ua   VARCHAR,
--   title_be   VARCHAR,
--   title_en   VARCHAR,
--   title_es   VARCHAR,
--   title_pt   VARCHAR,
--   title_de   VARCHAR,
--   title_fr   VARCHAR,
--   title_it   VARCHAR,
--   title_pl   VARCHAR,
--   title_ja   VARCHAR,
--   title_lt   VARCHAR,
--   title_lv   VARCHAR,
--   title_cz   VARCHAR
-- );
--
--
-- CREATE TABLE _cities (
--   city_id    BIGINT PRIMARY KEY     NOT NULL,
--   country_id BIGINT,
--   important  BOOLEAN,
--   region_id  BIGINT,
--   title_ru   VARCHAR,
--   area_ru    VARCHAR,
--   region_ru  VARCHAR,
--   title_ua   VARCHAR,
--   area_ua    VARCHAR,
--   region_ua  VARCHAR,
--   title_be   VARCHAR,
--   area_be    VARCHAR,
--   region_be  VARCHAR,
--   title_en   VARCHAR,
--   area_en    VARCHAR,
--   region_en  VARCHAR,
--   title_es   VARCHAR,
--   area_es    VARCHAR,
--   region_es  VARCHAR,
--   title_pt   VARCHAR,
--   area_pt    VARCHAR,
--   region_pt  VARCHAR,
--   title_de   VARCHAR,
--   area_de    VARCHAR,
--   region_de  VARCHAR,
--   title_fr   VARCHAR,
--   area_fr    VARCHAR,
--   region_fr  VARCHAR,
--   title_it   VARCHAR,
--   area_it    VARCHAR,
--   region_it  VARCHAR,
--   title_pl   VARCHAR,
--   area_pl    VARCHAR,
--   region_pl  VARCHAR,
--   title_ja   VARCHAR,
--   area_ja    VARCHAR,
--   region_ja  VARCHAR,
--   title_lt   VARCHAR,
--   area_lt    VARCHAR,
--   region_lt  VARCHAR,
--   title_lv   VARCHAR,
--   area_lv    VARCHAR,
--   region_lv  VARCHAR,
--   title_cz   VARCHAR,
--   area_cz    VARCHAR,
--   region_cz  VARCHAR
-- );


CREATE TABLE submission (
  id           BIGINT PRIMARY KEY NOT NULL,
  created_on   BIGINT             NOT NULL,
  uuid         VARCHAR(255)       NOT NULL,
  text         TEXT,
  country      VARCHAR(100),
  city         VARCHAR(100),
  submitter_id BIGINT REFERENCES user_yf (id),
  event_date   BIGINT,
  status       VARCHAR(255),
  comment      VARCHAR(255),
  equipment    VARCHAR (255)
);

-- CREATE TABLE submission_picture (
--   id            BIGINT PRIMARY KEY NOT NULL,
--   url           VARCHAR(255),
--   pic_order     integer,
--   created_on    BIGINT             NOT NULL,
--   submission_fk BIGINT REFERENCES submission (id)
-- );


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




