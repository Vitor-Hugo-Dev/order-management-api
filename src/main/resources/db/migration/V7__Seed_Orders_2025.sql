/*
  V7: Popula 'orders' e 'order_items'
*/

SET @admin_id = 0x01BFEF75B5DA4060BA437017E5668982;
SET @user_id = 0x9880A5209EB54FBFB4118462B2E914C0;

SET @prod1 = UUID_TO_BIN('a0a0a0a0-0001-0001-0001-000000000001'); -- Notebook
SET @prod2 = UUID_TO_BIN('a0a0a0a0-0001-0001-0001-000000000002'); -- Smartphone
SET @prod3 = UUID_TO_BIN('a0a0a0a0-0001-0001-0001-000000000003'); -- Fone
SET @prod4 = UUID_TO_BIN('a0a0a0a0-0002-0002-0002-000000000004'); -- Teclado
SET @prod5 = UUID_TO_BIN('a0a0a0a0-0002-0002-0002-000000000005'); -- Mouse (Estoque 0)
SET @prod6 = UUID_TO_BIN('a0a0a0a0-0003-0003-0003-000000000006'); -- Café
SET @prod7 = UUID_TO_BIN('a0a0a0a0-0003-0003-0003-000000000007'); -- Azeite
SET @prod8 = UUID_TO_BIN('a0a0a0a0-0004-0004-0004-000000000008'); -- Livro Código Limpo
SET @prod9 = UUID_TO_BIN('a0a0a0a0-0004-0004-0004-000000000009'); -- Livro Pragmático
SET @prod10 = UUID_TO_BIN('a0a0a0a0-0005-0005-0005-000000000010'); -- Luminária

SET @price1 = 9500.00;
SET @price2 = 6200.50;
SET @price3 = 850.00;
SET @price4 = 450.00;
SET @price5 = 320.00;
SET @price6 = 75.80;
SET @price7 = 95.00;
SET @price8 = 120.00;
SET @price9 = 135.50;
SET @price10 = 180.00;

SET @order1_total = (@price1 * 1.000);
SET @order2_total = (@price6 * 5.500);
INSERT IGNORE INTO orders (id, created_at, updated_at, created_by, updated_by, user_id, status, total_amount) VALUES
(UUID_TO_BIN('b0b0b0b0-0001-0001-0001-000000000001'), '2025-01-15 10:00:00', '2025-01-15 10:00:00', @user_id, @user_id, @user_id, 'PAID', @order1_total),
(UUID_TO_BIN('b0b0b0b0-0001-0001-0001-000000000002'), '2025-01-20 14:00:00', '2025-01-20 14:00:00', @admin_id, @admin_id, @admin_id, 'PAID', @order2_total);

SET @order3_total = (@price8 * 2.000) + (@price9 * 2.000);
INSERT IGNORE INTO orders (id, created_at, updated_at, created_by, updated_by, user_id, status, total_amount) VALUES
(UUID_TO_BIN('b0b0b0b0-0002-0002-0002-000000000003'), '2025-02-10 09:00:00', '2025-02-10 09:00:00', @user_id, @user_id, @user_id, 'PAID', @order3_total);

SET @order4_total = (@price2 * 1.000) + (@price3 * 1.000);
INSERT IGNORE INTO orders (id, created_at, updated_at, created_by, updated_by, user_id, status, total_amount) VALUES
(UUID_TO_BIN('b0b0b0b0-0003-0003-0003-000000000004'), '2025-03-05 11:00:00', '2025-03-05 11:00:00', @user_id, @user_id, @user_id, 'PAID', @order4_total);

SET @order5_total = (@price10 * 3.000);
INSERT IGNORE INTO orders (id, created_at, updated_at, created_by, updated_by, user_id, status, total_amount) VALUES
(UUID_TO_BIN('b0b0b0b0-0004-0004-0004-000000000005'), '2025-04-18 16:00:00', '2025-04-18 16:00:00', @admin_id, @admin_id, @admin_id, 'PAID', @order5_total);


SET @order6_total = (@price7 * 10.000);
SET @order7_total = (@price4 * 1.000);
INSERT IGNORE INTO orders (id, created_at, updated_at, created_by, updated_by, user_id, status, total_amount) VALUES
(UUID_TO_BIN('b0b0b0b0-0005-0005-0005-000000000006'), '2025-05-12 13:00:00', '2025-05-12 13:00:00', @user_id, @user_id, @user_id, 'PAID', @order6_total),
(UUID_TO_BIN('b0b0b0b0-0005-0005-0005-000000000007'), '2025-05-25 08:00:00', '2025-05-26 09:00:00', @user_id, @user_id, @user_id, 'CANCELLED', @order7_total);

SET @order8_total = (@price1 * 2.000);
SET @order9_total = (@price6 * 20.500);
INSERT IGNORE INTO orders (id, created_at, updated_at, created_by, updated_by, user_id, status, total_amount) VALUES
(UUID_TO_BIN('b0b0b0b0-0006-0006-0006-000000000008'), '2025-06-02 10:00:00', '2025-06-02 10:00:00', @user_id, @user_id, @user_id, 'PAID', @order8_total),
(UUID_TO_BIN('b0b0b0b0-0006-0006-0006-000000000009'), '2025-06-20 17:00:00', '2025-06-20 17:00:00', @admin_id, @admin_id, @admin_id, 'PAID', @order9_total);

SET @order10_total = (@price8 * 5.000);
INSERT IGNORE INTO orders (id, created_at, updated_at, created_by, updated_by, user_id, status, total_amount) VALUES
(UUID_TO_BIN('b0b0b0b0-0007-0007-0007-000000000010'), '2025-07-07 12:00:00', '2025-07-07 12:00:00', @user_id, @user_id, @user_id, 'PAID', @order10_total);

SET @order11_total = (@price3 * 2.000) + (@price4 * 1.000) + (@price10 * 1.000);
INSERT IGNORE INTO orders (id, created_at, updated_at, created_by, updated_by, user_id, status, total_amount) VALUES
(UUID_TO_BIN('b0b0b0b0-0008-0008-0008-000000000011'), '2025-08-15 14:00:00', '2025-08-15 14:00:00', @user_id, @user_id, @user_id, 'PAID', @order11_total);

SET @order12_total = (@price2 * 1.000);
SET @order13_total = (@price9 * 1.000);
INSERT IGNORE INTO orders (id, created_at, updated_at, created_by, updated_by, user_id, status, total_amount) VALUES
(UUID_TO_BIN('b0b0b0b0-0009-0009-0009-000000000012'), '2025-09-10 10:00:00', '2025-09-10 10:00:00', @admin_id, @admin_id, @admin_id, 'PAID', @order12_total),
(UUID_TO_BIN('b0b0b0b0-0009-0009-0009-000000000013'), '2025-09-22 11:00:00', '2025-09-23 12:00:00', @user_id, @user_id, @user_id, 'CANCELLED', @order13_total);

SET @order14_total = (@price1 * 1.000) + (@price2 * 2.000);
SET @order15_total = (@price7 * 3.500);
SET @order16_total = (@price8 * 10.000);
INSERT IGNORE INTO orders (id, created_at, updated_at, created_by, updated_by, user_id, status, total_amount) VALUES
(UUID_TO_BIN('b0b0b0b0-0010-0010-0010-000000000014'), '2025-10-01 09:00:00', '2025-10-01 09:00:00', @user_id, @user_id, @user_id, 'PAID', @order14_total),
(UUID_TO_BIN('b0b0b0b0-0010-0010-0010-000000000015'), '2025-10-15 15:00:00', '2025-10-15 15:00:00', @admin_id, @admin_id, @admin_id, 'PAID', @order15_total),
(UUID_TO_BIN('b0b0b0b0-0010-0010-0010-000000000016'), '2025-10-28 16:00:00', '2025-10-28 16:00:00', @user_id, @user_id, @user_id, 'PAID', @order16_total);

SET @order17_total = (@price6 * 2.000);
SET @order18_total = (@price4 * 1.000);
SET @order19_total = (@price10 * 1.000);
SET @order20_total = (@price5 * 1.000);
INSERT IGNORE INTO orders (id, created_at, updated_at, created_by, updated_by, user_id, status, total_amount) VALUES
(UUID_TO_BIN('b0b0b0b0-0011-0011-0011-000000000017'), '2025-11-01 10:00:00', '2025-11-01 10:00:00', @user_id, @user_id, @user_id, 'PAID', @order17_total),
(UUID_TO_BIN('b0b0b0b0-0011-0011-0011-000000000018'), '2025-11-05 11:00:00', '2025-11-05 11:00:00', @admin_id, @admin_id, @admin_id, 'PENDING', @order18_total),
(UUID_TO_BIN('b0b0b0b0-0011-0011-0011-000000000019'), '2025-11-10 09:00:00', '2025-11-10 09:00:00', @user_id, @user_id, @user_id, 'PENDING', @order19_total),
(UUID_TO_BIN('b0b0b0b0-0011-0011-0011-000000000020'), '2025-11-11 14:00:00', '2025-11-11 14:00:00', @user_id, @user_id, @user_id, 'PENDING', @order20_total);


INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0001-0001-0001-000000000001'), '2025-01-15 10:00:00', '2025-01-15 10:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0001-0001-0001-000000000001'), @prod1, 1.000, @price1);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0002-0002-0002-000000000002'), '2025-01-20 14:00:00', '2025-01-20 14:00:00', @admin_id, @admin_id, UUID_TO_BIN('b0b0b0b0-0001-0001-0001-000000000002'), @prod6, 5.500, @price6);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0003-0003-0003-000000000003'), '2025-02-10 09:00:00', '2025-02-10 09:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0002-0002-0002-000000000003'), @prod8, 2.000, @price8),
(UUID_TO_BIN('c0c0c0c0-0003-0003-0003-000000000004'), '2025-02-10 09:00:00', '2025-02-10 09:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0002-0002-0002-000000000003'), @prod9, 2.000, @price9);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0004-0004-0004-000000000005'), '2025-03-05 11:00:00', '2025-03-05 11:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0003-0003-0003-000000000004'), @prod2, 1.000, @price2),
(UUID_TO_BIN('c0c0c0c0-0004-0004-0004-000000000006'), '2025-03-05 11:00:00', '2025-03-05 11:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0003-0003-0003-000000000004'), @prod3, 1.000, @price3);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0005-0005-0005-000000000007'), '2025-04-18 16:00:00', '2025-04-18 16:00:00', @admin_id, @admin_id, UUID_TO_BIN('b0b0b0b0-0004-0004-0004-000000000005'), @prod10, 3.000, @price10);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0006-0006-0006-000000000008'), '2025-05-12 13:00:00', '2025-05-12 13:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0005-0005-0005-000000000006'), @prod7, 10.000, @price7);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0007-0007-0007-000000000009'), '2025-05-25 08:00:00', '2025-05-26 09:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0005-0005-0005-000000000007'), @prod4, 1.000, @price4);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0008-0008-0008-000000000010'), '2025-06-02 10:00:00', '2025-06-02 10:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0006-0006-0006-000000000008'), @prod1, 2.000, @price1);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0009-0009-0009-000000000011'), '2025-06-20 17:00:00', '2025-06-20 17:00:00', @admin_id, @admin_id, UUID_TO_BIN('b0b0b0b0-0006-0006-0006-000000000009'), @prod6, 20.500, @price6);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0010-0010-0010-000000000012'), '2025-07-07 12:00:00', '2025-07-07 12:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0007-0007-0007-000000000010'), @prod8, 5.000, @price8);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0011-0011-0011-000000000013'), '2025-08-15 14:00:00', '2025-08-15 14:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0008-0008-0008-000000000011'), @prod3, 2.000, @price3),
(UUID_TO_BIN('c0c0c0c0-0011-0011-0011-000000000014'), '2025-08-15 14:00:00', '2025-08-15 14:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0008-0008-0008-000000000011'), @prod4, 1.000, @price4),
(UUID_TO_BIN('c0c0c0c0-0011-0011-0011-000000000015'), '2025-08-15 14:00:00', '2025-08-15 14:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0008-0008-0008-000000000011'), @prod10, 1.000, @price10);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0012-0012-0012-000000000016'), '2025-09-10 10:00:00', '2025-09-10 10:00:00', @admin_id, @admin_id, UUID_TO_BIN('b0b0b0b0-0009-0009-0009-000000000012'), @prod2, 1.000, @price2);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0013-0013-0013-000000000017'), '2025-09-22 11:00:00', '2025-09-23 12:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0009-0009-0009-000000000013'), @prod9, 1.000, @price9);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0014-0014-0014-000000000018'), '2025-10-01 09:00:00', '2025-10-01 09:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0010-0010-0010-000000000014'), @prod1, 1.000, @price1),
(UUID_TO_BIN('c0c0c0c0-0014-0014-0014-000000000019'), '2025-10-01 09:00:00', '2025-10-01 09:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0010-0010-0010-000000000014'), @prod2, 2.000, @price2);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0015-0015-0015-000000000020'), '2025-10-15 15:00:00', '2025-10-15 15:00:00', @admin_id, @admin_id, UUID_TO_BIN('b0b0b0b0-0010-0010-0010-000000000015'), @prod7, 3.500, @price7);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0016-0016-0016-000000000021'), '2025-10-28 16:00:00', '2025-10-28 16:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0010-0010-0010-000000000016'), @prod8, 10.000, @price8);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0017-0017-0017-000000000022'), '2025-11-01 10:00:00', '2025-11-01 10:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0011-0011-0011-000000000017'), @prod6, 2.000, @price6);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0018-0018-0018-000000000023'), '2025-11-05 11:00:00', '2025-11-05 11:00:00', @admin_id, @admin_id, UUID_TO_BIN('b0b0b0b0-0011-0011-0011-000000000018'), @prod4, 1.000, @price4);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0019-0019-0019-000000000024'), '2025-11-10 09:00:00', '2025-11-10 09:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0011-0011-0011-000000000019'), @prod10, 1.000, @price10);

INSERT IGNORE INTO order_items (id, created_at, updated_at, created_by, updated_by, order_id, product_id, quantity, price_at_purchase) VALUES
(UUID_TO_BIN('c0c0c0c0-0020-0020-0020-000000000025'), '2025-11-11 14:00:00', '2025-11-11 14:00:00', @user_id, @user_id, UUID_TO_BIN('b0b0b0b0-0011-0011-0011-000000000020'), @prod5, 1.000, @price5);