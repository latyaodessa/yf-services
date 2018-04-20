ALTER TABLE user_yf RENAME COLUMN id TO social_id;
ALTER TABLE user_yf ADD COLUMN id SERIAL;
ALTER TABLE user_yf DROP CONSTRAINT user_yf_pkey CASCADE;
ALTER TABLE user_yf ADD PRIMARY KEY (id);
ALTER TABLE user_yf ADD COLUMN created_on DATE NOT NULL DEFAULT '2017-01-01';
ALTER TABLE user_yf ADD COLUMN version int NOT NULL DEFAULT 0;

ALTER TABLE user_vk ADD COLUMN created_on DATE NOT NULL DEFAULT '2017-01-01';
ALTER TABLE user_vk ADD COLUMN version int NOT NULL DEFAULT 0;

ALTER TABLE user_fb ADD COLUMN created_on DATE NOT NULL DEFAULT '2017-01-01';
ALTER TABLE user_fb ADD COLUMN version int NOT NULL DEFAULT 0;

ALTER TABLE user_yf RENAME COLUMN yf_user_type TO social_type;


ALTER TABLE user_vk ADD COLUMN user_id BIGINT;
UPDATE user_vk SET user_id = user_yf.id FROM user_yf WHERE user_yf.social_id = user_vk.id;
ALTER TABLE user_vk ADD FOREIGN KEY (user_id)  REFERENCES user_yf (id);

ALTER TABLE user_fb ADD COLUMN user_id BIGINT;
UPDATE user_fb SET user_id = user_yf.id FROM user_yf WHERE user_yf.social_id = user_fb.id;
ALTER TABLE user_fb ADD FOREIGN KEY (user_id)  REFERENCES user_yf (id);

ALTER TABLE user_yf DROP COLUMN IF EXISTS social_type, DROP COLUMN IF EXISTS social_id, DROP COLUMN IF EXISTS type;
ALTER TABLE user_yf DROP COLUMN isuserauthorized;

ALTER TABLE user_vk DROP COLUMN about, DROP COLUMN activities, DROP COLUMN books, DROP COLUMN domain, DROP COLUMN followers_count, DROP COLUMN games, DROP COLUMN interests, DROP COLUMN maiden_name, DROP COLUMN movies, DROP COLUMN music, DROP COLUMN nickname, DROP COLUMN photo_100, DROP COLUMN photo_200, DROP COLUMN photo_200_orig, DROP COLUMN photo_400_orig, DROP COLUMN photo_50, DROP COLUMN quotes, DROP COLUMN site, DROP COLUMN university_name;


ALTER TABLE user_yf ADD COLUMN type VARCHAR(255) DEFAULT 'BASIC' NOT NUll;
ALTER TABLE user_yf ADD COLUMN status VARCHAR(255) DEFAULT 'ACTIVE' NOT NUll;
ALTER TABLE user_yf ADD COLUMN authorize BOOLEAN DEFAULT false;

GRANT ALL PRIVILEGES ON TABLE user_yf TO yf_system;
GRANT ALL PRIVILEGES ON TABLE user_vk TO yf_system;
GRANT ALL PRIVILEGES ON TABLE user_fb TO yf_system;

grant postgres to yf_system;
