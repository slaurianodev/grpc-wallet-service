USE wallet_service_db;

start transaction;

CREATE TABLE users(
    user_id INT NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(100),
    PRIMARY KEY (user_id)
);

CREATE TABLE currencies(
    currency_id INT NOT NULL AUTO_INCREMENT,
    currency_name VARCHAR(50) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    PRIMARY KEY (currency_id)
);

CREATE TABLE wallets(
    wallet_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    currency_id INT NOT NULL,
    PRIMARY KEY (wallet_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (currency_id) REFERENCES currencies (currency_id),
    CONSTRAINT U_wallet_currency UNIQUE (user_id,currency_id)
);

commit