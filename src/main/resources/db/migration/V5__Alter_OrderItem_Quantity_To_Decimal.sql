-- Altera a coluna quantity para DECIMAL(10, 3)
ALTER TABLE order_items MODIFY quantity DECIMAL(10, 3) NOT NULL;