ALTER TABLE user_yf ADD COLUMN nick_name VARCHAR;
ALTER TABLE user_yf ADD COLUMN last_name VARCHAR;
ALTER TABLE user_yf ADD COLUMN first_name VARCHAR;
ALTER TABLE user_yf ADD COLUMN avatar VARCHAR;
ALTER TABLE user_yf ADD COLUMN phone VARCHAR;
ALTER TABLE user_yf ADD COLUMN passport VARCHAR;
ALTER TABLE user_yf DROP COLUMN password;
ALTER TABLE user_yf ADD COLUMN password bytea;
CREATE EXTENSION pgcrypto;


CREATE TABLE email_template (
   id BIGINT PRIMARY KEY     NOT NULL,
   name           VARCHAR   NOT NULL,
   language       VARCHAR  NOT NULL,
   sender           VARCHAR   NOT NULL,
   subject        VARCHAR   NOT NULL,
   text           TEXT        NOT NULL,
   html           TEXT        NOT NULL
);

INSERT INTO email_template (id,name,language,sender,subject,text,html) VALUES (1, 'VERIFICATION', 'RU','"Young Folks" <noreply@youngfolks.ru>', 'Верификация аккаунта', 'Пожалуйста, перейдите по указанной ссылке, чтобы верифицировать свой аккаунт ${link}"', '<b>Пожалуйста, перейдите по указанной ссылке, чтобы верифицировать свой аккаунт <a href=${link}>${link}</a></b>');
INSERT INTO email_template (id,name,language,sender,subject,text,html) VALUES (2, 'RESET_PASSWORD', 'RU','"Young Folks" <noreply@youngfolks.ru>', 'Смена пароля', 'Пожалуйста, перейдите по указанной ссылке, чтобы изменить свой пароль ${link} "', '<b>Пожалуйста, перейдите по указанной ссылке, чтобы изменить свой пароль <a href=${link}>${link}</a></b>');


CREATE TABLE email_logs (
   id BIGINT PRIMARY KEY     NOT NULL,
   created_on DATE NOT NULL,
   content TEXT
);


CREATE TABLE user_verifications (
   id BIGINT PRIMARY KEY NOT NULL,
   email  BOOLEAN DEFAULT false,
   phone  BOOLEAN DEFAULT false,
   passport  BOOLEAN DEFAULT false,
   company BOOLEAN DEFAULT false,
   created_on DATE
);

ALTER TABLE user_yf ADD COLUMN verification_id BIGINT;
ALTER TABLE user_yf ADD FOREIGN KEY (verification_id)  REFERENCES user_verifications (id);


CREATE TABLE verifications (
   id BIGINT PRIMARY KEY     NOT NULL,
   type VARCHAR NOT NULL,
   created_on DATE NOT NULL,
   verification VARCHAR(300) NOT NULL,
   verified BOOLEAN NOT NULL
);

