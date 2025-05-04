CREATE TABLE payment
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    created_at      datetime              NULL,
    updated_at      datetime              NULL,
    payment_link    VARCHAR(255)          NULL,
    order_id        BIGINT                NULL,
    payment_status  SMALLINT              NULL,
    payment_method  SMALLINT              NULL,
    payment_gateway SMALLINT              NULL,
    CONSTRAINT pk_payment PRIMARY KEY (id)
);

CREATE TABLE stripe_mapping
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    created_at          datetime              NULL,
    updated_at          datetime              NULL,
    product_id          BIGINT                NULL,
    stripe_product_id   VARCHAR(255)          NULL,
    stripe_price_id     VARCHAR(255)          NULL,
    product_name        VARCHAR(255)          NULL,
    product_description VARCHAR(255)          NULL,
    CONSTRAINT pk_stripemapping PRIMARY KEY (id)
);