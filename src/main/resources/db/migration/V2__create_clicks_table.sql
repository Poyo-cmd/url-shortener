CREATE TABLE clicks (
                        id          BIGSERIAL PRIMARY KEY,
                        short_url_id BIGINT NOT NULL REFERENCES short_urls(id),
                        clicked_at  TIMESTAMP NOT NULL,
                        ip_address  VARCHAR(45)
);