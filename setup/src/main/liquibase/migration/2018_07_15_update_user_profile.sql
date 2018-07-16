CREATE TABLE profile_picture (
   id BIGINT PRIMARY KEY     NOT NULL,
   file_id VARCHAR(400) NOT NULL,
   file_name VARCHAR(255) NOT NULL,
   friendly_link VARCHAR(500) NOT NULL,
   native_link VARCHAR(500) NOT NULL
   );


ALTER TABLE user_yf DROP COLUMN avatar;

ALTER TABLE user_yf ADD COLUMN profil_picture BIGINT;
ALTER TABLE user_yf ADD FOREIGN KEY (profil_picture)  REFERENCES profile_picture (id);
