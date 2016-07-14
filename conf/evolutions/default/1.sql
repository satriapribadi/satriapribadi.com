# --- !Ups
CREATE TABLE users (
  id                  BIGINT                  NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_login          VARCHAR(60)             NOT NULL,
  user_pass           VARCHAR(64)             NOT NULL,
  user_nicename       VARCHAR(50)             NULL,
  user_email          VARCHAR(100)            NOT NULL,
  user_url            VARCHAR(100)            NULL,
  user_registered     TIMESTAMP DEFAULT now() NOT NULL,
  user_activation_key VARCHAR(60)             NULL,
  user_status         INT(11) DEFAULT 0       NULL,
  display_name        VARCHAR(250)            NOT NULL,
  created_at          TIMESTAMP               NULL,
  updated_at          TIMESTAMP               NULL
);
CREATE UNIQUE INDEX user_login_key ON users (user_login);
CREATE UNIQUE INDEX user_nicename ON users (user_nicename);
CREATE UNIQUE INDEX user_email ON users (user_email);

INSERT INTO users (id, user_login, user_pass, user_nicename, user_email, user_activation_key, display_name, created_at, updated_at)
VALUES
  (1, 'satriapribadi', 'f7adb2f599fb7e1f32cd0a61bf4d61ac2a38a295', 'satriapribadi', 'satria.pribadi19@gmail.com',
   'f7adb2f599fb7e1f32cd0a61bf4d61ac2a38a295', 'Satria Pribadi', NOW(), NOW());

# --- !Downs
DROP TABLE users;