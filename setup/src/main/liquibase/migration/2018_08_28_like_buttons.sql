ALTER TABLE publication ADD COLUMN likes INTEGER DEFAULT 0;

CREATE TABLE system_settings (
  id         BIGINT PRIMARY KEY     NOT NULL,
  created_on BIGINT                 NOT NULL,
  key        VARCHAR UNIQUE         NOT NULL,
  value      VARCHAR
);

INSERT INTO system_settings (id, created_on, key, value)
VALUES
  (1, 1536177616537, 'vk_scheduler_enabled', 'false');