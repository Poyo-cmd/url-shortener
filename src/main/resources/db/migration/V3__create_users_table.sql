CREATE TABLE usuarios (
                          id          BIGSERIAL PRIMARY KEY,
                          email       VARCHAR(255) NOT NULL UNIQUE,
                          password    VARCHAR(255) NOT NULL,
                          created_at  TIMESTAMP NOT NULL
);

ALTER TABLE short_urls ADD COLUMN usuario_id BIGINT REFERENCES usuarios(id);