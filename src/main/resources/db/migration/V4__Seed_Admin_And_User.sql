-- insere usuario admin
INSERT INTO users
(id, created_at, updated_at, created_by, updated_by, name, email, password, role, is_account_non_expired,
 is_account_non_locked, is_credentials_non_expired, is_enabled)
VALUES (0x01BFEF75B5DA4060BA437017E5668982,
        NOW(), NOW(),
        NULL, NULL,
        'Usuario Admin',
        'admin@gmail.com',
        '$2a$10$38sKWIL4DRnPeX9CA8PleeEjkx/mIgztaVvAmgvHew0GvU0buTo1W',
        'ADMIN',
        1, 1, 1, 1);
-- insere usuario comum
INSERT INTO users
(id, created_at, updated_at, created_by, updated_by, name, email, password, role, is_account_non_expired,
 is_account_non_locked, is_credentials_non_expired, is_enabled)
VALUES (0x9880A5209EB54FBFB4118462B2E914C0,
        NOW(), NOW(),
        NULL, NULL,
        'Usuario Comum',
        'user@gmail.com',
        '$2a$10$kmCsAL8UXQ2sAzUkOw.s0.Q4o7Jg4fioNXiNDPd211/9hwMGxdidG',
        'USER',
        1, 1, 1, 1);