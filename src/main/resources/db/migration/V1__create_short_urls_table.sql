CREATE TABLE short_urls (
                            id          BIGSERIAL PRIMARY KEY,
                            code        VARCHAR(10)  NOT NULL UNIQUE,
                            original_url TEXT        NOT NULL,
                            created_at  TIMESTAMP    NOT NULL,
                            expires_at  TIMESTAMP,
                            active      BOOLEAN      NOT NULL DEFAULT TRUE
);