CREATE TABLE IF NOT EXISTS order_channel (
    id UUID PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    locked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

INSERT INTO order_channel (id, name, active, deleted, is_default, locked, created_at, updated_at)
VALUES
    (uuid_generate_v4(), 'LOCAL', TRUE, FALSE, TRUE, TRUE, NOW(), NOW()),
    (uuid_generate_v4(), 'WEB', TRUE, FALSE, FALSE, FALSE, NOW(), NOW()),
    (uuid_generate_v4(), 'PEDIDOS YA', TRUE, FALSE, FALSE, FALSE, NOW(), NOW()),
    (uuid_generate_v4(), 'UBER EATS', TRUE, FALSE, FALSE, FALSE, NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

ALTER TABLE orden
    ADD COLUMN IF NOT EXISTS channel_id UUID;

UPDATE orden
    SET channel_id = (SELECT id FROM order_channel WHERE name = 'LOCAL' LIMIT 1)
WHERE channel_id IS NULL;

ALTER TABLE orden
    ALTER COLUMN channel_id SET NOT NULL;

ALTER TABLE orden
    ADD CONSTRAINT fk_orden_channel FOREIGN KEY (channel_id) REFERENCES order_channel(id);
