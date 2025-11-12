/*
  V6: Popula a tabela 'products'
*/

SET @admin_id = 0x01BFEF75B5DA4060BA437017E5668982;

INSERT IGNORE INTO products (id, created_at, updated_at, created_by, updated_by, name, price, category, stock) VALUES
(UUID_TO_BIN('a0a0a0a0-0001-0001-0001-000000000001'), NOW(), NOW(), @admin_id, @admin_id, 'Notebook Gamer Ultra', 9500.00, 'Eletrônicos', 50.000),
(UUID_TO_BIN('a0a0a0a0-0001-0001-0001-000000000002'), NOW(), NOW(), @admin_id, @admin_id, 'Smartphone Pro Max', 6200.50, 'Eletrônicos', 120.000),
(UUID_TO_BIN('a0a0a0a0-0001-0001-0001-000000000003'), NOW(), NOW(), @admin_id, @admin_id, 'Fone de Ouvido Bluetooth', 850.00, 'Eletrônicos', 200.000);

INSERT IGNORE INTO products (id, created_at, updated_at, created_by, updated_by, name, price, category, stock) VALUES
(UUID_TO_BIN('a0a0a0a0-0002-0002-0002-000000000004'), NOW(), NOW(), @admin_id, @admin_id, 'Teclado Mecânico RGB', 450.00, 'Periféricos', 150.000),
(UUID_TO_BIN('a0a0a0a0-0002-0002-0002-000000000005'), NOW(), NOW(), @admin_id, @admin_id, 'Mouse Gamer Sem Fio', 320.00, 'Periféricos', 0.000);

INSERT IGNORE INTO products (id, created_at, updated_at, created_by, updated_by, name, price, category, stock) VALUES
(UUID_TO_BIN('a0a0a0a0-0003-0003-0003-000000000006'), NOW(), NOW(), @admin_id, @admin_id, 'Café Especial em Grãos (Kg)', 75.80, 'Alimentos', 150.500),
(UUID_TO_BIN('a0a0a0a0-0003-0003-0003-000000000007'), NOW(), NOW(), @admin_id, @admin_id, 'Azeite de Oliva Extra Virgem (L)', 95.00, 'Alimentos', 80.750);

INSERT IGNORE INTO products (id, created_at, updated_at, created_by, updated_by, name, price, category, stock) VALUES
(UUID_TO_BIN('a0a0a0a0-0004-0004-0004-000000000008'), NOW(), NOW(), @admin_id, @admin_id, 'Livro: Código Limpo', 120.00, 'Livros', 300.000),
(UUID_TO_BIN('a0a0a0a0-0004-0004-0004-000000000009'), NOW(), NOW(), @admin_id, @admin_id, 'Livro: O Programador Pragmático', 135.50, 'Livros', 250.000);

INSERT IGNORE INTO products (id, created_at, updated_at, created_by, updated_by, name, price, category, stock) VALUES
(UUID_TO_BIN('a0a0a0a0-0005-0005-0005-000000000010'), NOW(), NOW(), @admin_id, @admin_id, 'Luminária de Mesa LED', 180.00, 'Casa', 90.000);