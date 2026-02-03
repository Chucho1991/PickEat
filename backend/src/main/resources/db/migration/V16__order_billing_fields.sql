CREATE TABLE IF NOT EXISTS order_billing_field (
    id UUID PRIMARY KEY,
    label VARCHAR(120) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

ALTER TABLE orden
    ADD COLUMN IF NOT EXISTS billing_data JSONB NOT NULL DEFAULT '{}'::jsonb;

INSERT INTO order_billing_field (id, label, active, deleted, sort_order, created_at, updated_at)
SELECT uuid_generate_v4(), 'Nombres', TRUE, FALSE, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM order_billing_field WHERE label = 'Nombres');

INSERT INTO order_billing_field (id, label, active, deleted, sort_order, created_at, updated_at)
SELECT uuid_generate_v4(), 'Documento', TRUE, FALSE, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM order_billing_field WHERE label = 'Documento');

INSERT INTO order_billing_field (id, label, active, deleted, sort_order, created_at, updated_at)
SELECT uuid_generate_v4(), 'Correo', TRUE, FALSE, 3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM order_billing_field WHERE label = 'Correo');

INSERT INTO order_billing_field (id, label, active, deleted, sort_order, created_at, updated_at)
SELECT uuid_generate_v4(), 'Direccion', TRUE, FALSE, 4, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM order_billing_field WHERE label = 'Direccion');

INSERT INTO order_billing_field (id, label, active, deleted, sort_order, created_at, updated_at)
SELECT uuid_generate_v4(), 'Celular', TRUE, FALSE, 5, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM order_billing_field WHERE label = 'Celular');
