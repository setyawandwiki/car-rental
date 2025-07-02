CREATE TABLE transaction_ledgers(
    id SERIAL PRIMARY KEY,
    company_id INT NOT NULL,
    amount BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    reference_id VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_company_ledger FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE platform_profits(
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    company_id INT NOT NULL,
    profit_amount INT NOT NULL,
    percentage INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_order_profit FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_company_profit FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE payments(
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL UNIQUE,
    invoice_id VARCHAR(100) UNIQUE NOT NULL,
    external_id VARCHAR(100) UNIQUE NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    amount INT NOT NULL,
    paid_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_order_payment FOREIGN KEY (order_id) REFERENCES orders(id)
);