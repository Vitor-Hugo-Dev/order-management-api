/*
  Cria a tabela 'users' inicial
*/
CREATE TABLE users (
    id BINARY(16) PRIMARY KEY,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BINARY(16) NULL,
    updated_by BINARY(16) NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,

    is_account_non_expired BOOLEAN DEFAULT TRUE NOT NULL,
    is_account_non_locked BOOLEAN DEFAULT TRUE NOT NULL,
    is_credentials_non_expired BOOLEAN DEFAULT TRUE NOT NULL,
    is_enabled BOOLEAN DEFAULT TRUE NOT NULL,

    CONSTRAINT UK_users_email UNIQUE (email)
);