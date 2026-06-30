CREATE EXTENSION IF NOT EXISTS citext;
CREATE DOMAIN email_address AS citext
CHECK (
    VALUE ~ '^[^@\s]+@[^@\s]+\.[^@\s]+$'
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(18) NOT NULL UNIQUE,
    email email_address NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);