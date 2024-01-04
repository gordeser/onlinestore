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
                         id_comment SERIAL NOT NULL ,
                         text TEXT NOT NULL,
                         date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE comment ADD CONSTRAINT pk_comment PRIMARY KEY (id_comment);

CREATE TABLE orders (
                        id_orders SERIAL NOT NULL,
                        id_users BIGINT NOT NULL,
                        date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        status VARCHAR(256) NOT NULL DEFAULT 'PENDING' check (status IN ('PENDING', 'DELIVERING', 'DELIVERED', 'CANCELLED')),
                        products_quantities TEXT NOT NULL DEFAULT '{}'
);
ALTER TABLE orders ADD CONSTRAINT pk_orders PRIMARY KEY (id_orders);

CREATE TABLE product (
                         id_product SERIAL NOT NULL,
                         name VARCHAR(256) NOT NULL,
                         description VARCHAR(256) NOT NULL,
                         price DOUBLE PRECISION NOT NULL check (price >= 0.0),
                         category VARCHAR(256) NOT NULL,
                         image VARCHAR(256) NOT NULL
);
ALTER TABLE product ADD CONSTRAINT pk_product PRIMARY KEY (id_product);

CREATE TABLE users (
                       id_users SERIAL NOT NULL,
                       name VARCHAR(256) NOT NULL,
                       surname VARCHAR(256) NOT NULL,
                       address VARCHAR(256) NOT NULL,
                       password VARCHAR(256) NOT NULL,
                       email VARCHAR(256) NOT NULL
);
ALTER TABLE users ADD CONSTRAINT pk_users PRIMARY KEY (id_users);

CREATE TABLE product_orders (
                                products_id_product BIGINT NOT NULL,
                                orders_id_orders BIGINT NOT NULL
);
ALTER TABLE product_orders ADD CONSTRAINT pk_product_orders PRIMARY KEY (products_id_product, orders_id_orders);

ALTER TABLE comment ADD CONSTRAINT fk_comment_product FOREIGN KEY (id_product) REFERENCES product (id_product) ON DELETE CASCADE;
ALTER TABLE comment ADD CONSTRAINT fk_comment_users FOREIGN KEY (id_users) REFERENCES users (id_users) ON DELETE CASCADE;

ALTER TABLE orders ADD CONSTRAINT fk_orders_users FOREIGN KEY (id_users) REFERENCES users (id_users) ON DELETE CASCADE;

ALTER TABLE product_orders ADD CONSTRAINT fk_product_orders_product FOREIGN KEY (products_id_product) REFERENCES product (id_product) ON DELETE CASCADE;
ALTER TABLE product_orders ADD CONSTRAINT fk_product_orders_orders FOREIGN KEY (orders_id_orders) REFERENCES orders (id_orders) ON DELETE CASCADE;