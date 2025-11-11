-- Tabela principal de Pedidos
CREATE TABLE orders (
    id BINARY(16) PRIMARY KEY,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BINARY(16) NULL,
    updated_by BINARY(16) NULL,

    user_id BINARY(16) NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_amount DECIMAL(19, 2) NOT NULL,

    CONSTRAINT FK_orders_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT FK_orders_created_by FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT FK_orders_updated_by FOREIGN KEY (updated_by) REFERENCES users(id)
);


-- Tabela de Vinculo (Itens do Pedido)
CREATE TABLE order_items (
     id BINARY(16) PRIMARY KEY,
     created_at DATETIME NOT NULL,
     updated_at DATETIME NOT NULL,
     created_by BINARY(16) NULL,
     updated_by BINARY(16) NULL,

     order_id BINARY(16) NOT NULL,
     product_id BINARY(16) NOT NULL,
     quantity INT NOT NULL,
     price_at_purchase DECIMAL(19, 2) NOT NULL,

     CONSTRAINT FK_order_items_order_id FOREIGN KEY (order_id) REFERENCES orders(id),
     CONSTRAINT FK_order_items_product_id FOREIGN KEY (product_id) REFERENCES products(id),
     CONSTRAINT FK_order_items_created_by FOREIGN KEY (created_by) REFERENCES users(id),
     CONSTRAINT FK_order_items_updated_by FOREIGN KEY (updated_by) REFERENCES users(id)
);