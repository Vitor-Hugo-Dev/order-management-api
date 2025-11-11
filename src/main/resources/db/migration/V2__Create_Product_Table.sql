/*
  Cria a tabela 'products'
*/
CREATE TABLE products (
    id BINARY(16) PRIMARY KEY,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BINARY(16) NULL,
    updated_by BINARY(16) NULL,

    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    category VARCHAR(255) NOT NULL,
    stock DECIMAL(10, 0) NOT NULL,

    CONSTRAINT FK_products_created_by FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT FK_products_updated_by FOREIGN KEY (updated_by) REFERENCES users(id)
);