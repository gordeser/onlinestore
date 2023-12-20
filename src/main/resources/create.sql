-- Remove conflicting tables
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS product_orders CASCADE;
-- End of removing

CREATE TABLE comment (
                         id_product BIGINT NOT NULL,
                         id_users BIGINT NOT NULL,
                         id_comment BIGINT NOT NULL,
                         text TEXT NOT NULL,
                         date TIMESTAMP NOT NULL
);
ALTER TABLE comment ADD CONSTRAINT pk_comment PRIMARY KEY (id_product, id_users);

CREATE TABLE orders (
                        id_orders BIGINT NOT NULL,
                        id_users BIGINT NOT NULL,
                        date TIMESTAMP NOT NULL,
                        status VARCHAR(256) NOT NULL
);
ALTER TABLE orders ADD CONSTRAINT pk_orders PRIMARY KEY (id_orders);

CREATE TABLE product (
                         id_product BIGINT NOT NULL,
                         name VARCHAR(256) NOT NULL,
                         description VARCHAR(256) NOT NULL,
                         price DOUBLE PRECISION NOT NULL,
                         category VARCHAR(256) NOT NULL
);
ALTER TABLE product ADD CONSTRAINT pk_product PRIMARY KEY (id_product);

CREATE TABLE users (
                       id_users BIGINT NOT NULL,
                       name VARCHAR(256) NOT NULL,
                       surname VARCHAR(256) NOT NULL,
                       address VARCHAR(256) NOT NULL,
                       password VARCHAR(256) NOT NULL,
                       email VARCHAR(256) NOT NULL
);
ALTER TABLE users ADD CONSTRAINT pk_users PRIMARY KEY (id_users);

CREATE TABLE product_orders (
                                id_product BIGINT NOT NULL,
                                id_orders BIGINT NOT NULL
);
ALTER TABLE product_orders ADD CONSTRAINT pk_product_orders PRIMARY KEY (id_product, id_orders);

ALTER TABLE comment ADD CONSTRAINT fk_comment_product FOREIGN KEY (id_product) REFERENCES product (id_product) ON DELETE CASCADE;
ALTER TABLE comment ADD CONSTRAINT fk_comment_users FOREIGN KEY (id_users) REFERENCES users (id_users) ON DELETE CASCADE;

ALTER TABLE orders ADD CONSTRAINT fk_orders_users FOREIGN KEY (id_users) REFERENCES users (id_users) ON DELETE CASCADE;

ALTER TABLE product_orders ADD CONSTRAINT fk_product_orders_product FOREIGN KEY (id_product) REFERENCES product (id_product) ON DELETE CASCADE;
ALTER TABLE product_orders ADD CONSTRAINT fk_product_orders_orders FOREIGN KEY (id_orders) REFERENCES orders (id_orders) ON DELETE CASCADE;