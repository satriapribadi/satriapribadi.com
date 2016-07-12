# --- !Ups
CREATE TABLE oauth_clients (
  id            BIGINT                  NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_id       BIGINT                  NOT NULL,
  grant_type    VARCHAR(254)            NOT NULL,
  client_id     VARCHAR(254)            NOT NULL,
  client_secret VARCHAR(254)            NOT NULL,
  redirect_uri  VARCHAR(254),
  created_at    TIMESTAMP DEFAULT now() NOT NULL
);

ALTER TABLE oauth_clients
  ADD CONSTRAINT oauth_client_user_fk FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

CREATE TABLE oauth_authorization_codes (
  id              BIGINT                  NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_id         BIGINT                  NOT NULL,
  oauth_client_id BIGINT                  NOT NULL,
  code            VARCHAR(254)            NOT NULL,
  redirect_uri    VARCHAR(254),
  created_at      TIMESTAMP DEFAULT now() NOT NULL
);

ALTER TABLE oauth_authorization_codes
  ADD CONSTRAINT oauth_authorization_code_user_fk FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;
ALTER TABLE oauth_authorization_codes
  ADD CONSTRAINT oauth_authorization_code_client_fk FOREIGN KEY (oauth_client_id) REFERENCES oauth_clients (id)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

CREATE TABLE oauth_access_tokens (
  id              BIGINT                  NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_id         BIGINT                  NOT NULL,
  oauth_client_id BIGINT                  NOT NULL,
  access_token    VARCHAR(254)            NOT NULL,
  refresh_token   VARCHAR(254)            NOT NULL,
  created_at      TIMESTAMP DEFAULT now() NOT NULL
);

ALTER TABLE oauth_access_tokens
  ADD CONSTRAINT oauth_access_token_user_fk FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;
ALTER TABLE oauth_access_tokens
  ADD CONSTRAINT oauth_access_token_client_fk FOREIGN KEY (oauth_client_id) REFERENCES oauth_clients (id)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

INSERT INTO oauth_clients (id, user_id, grant_type, client_id, client_secret)
VALUES
  (1, 1, 'client_credentials', '50504f2fa6f7d5b9d9b03db5ec5b8457b6e67f9', '154de02d909cf5bc6e3737a38c213cf19f72068'),
  (2, 1, 'password', 'c7587faf51c7575aa722b8c500c92b1bde1e7c8', '190aeff4f071d49b6c63cb221e91aac906e0a9c');

# --- !Downs
DROP TABLE oauth_access_tokens;
DROP TABLE oauth_authorization_codes;
DROP TABLE oauth_clients;