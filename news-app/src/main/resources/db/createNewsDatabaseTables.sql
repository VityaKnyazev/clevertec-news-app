CREATE TABLE IF NOT EXISTS news
(
    id              BIGSERIAL,
    uuid            UUID                   NOT NULL,
    journalist_uuid UUID                   NOT NULL,
    title           CHARACTER VARYING(150) NOT NULL,
    text            TEXT                   NOT NULL,
    create_date     TIMESTAMPTZ            NOT NULL,
    update_date     TIMESTAMPTZ,

    CHECK (length(title) >= 3),
    CHECK (length(text) >= 3),
    CHECK (length(text) <= 200000),

    UNIQUE (uuid),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comment
(
    id              BIGSERIAL,
    uuid            UUID                   NOT NULL,
    subscriber_uuid UUID                   NOT NULL,
    text            CHARACTER VARYING(800) NOT NULL,
    news_id         BIGSERIAL              NOT NULL,
    create_date     TIMESTAMPTZ            NOT NULL,
    update_date     TIMESTAMPTZ,

    CHECK (length(text) >= 3),

    UNIQUE (uuid),
    PRIMARY KEY (id),

    CONSTRAINT fk_news
        FOREIGN KEY (news_id)
            REFERENCES news (id)
            ON DELETE NO ACTION ON UPDATE NO ACTION
);
