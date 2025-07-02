CREATE TABLE vendor_balance (
    id SERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL UNIQUE,
    available_balance BIGINT DEFAULT 0,
    pending_withdrawal BIGINT DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_vendor_company FOREIGN KEY (company_id) REFERENCES companies(id)
);