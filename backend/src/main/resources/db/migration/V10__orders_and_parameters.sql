CREATE SEQUENCE IF NOT EXISTS order_number_seq START 1;

CREATE TABLE IF NOT EXISTS app_parameter (
    param_key VARCHAR(60) PRIMARY KEY,
    numeric_value NUMERIC(10,2),
    text_value VARCHAR(80),
    boolean_value BOOLEAN,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS orden (
    id UUID PRIMARY KEY,
    order_number BIGINT NOT NULL DEFAULT nextval('order_number_seq'),
    mesa_id UUID NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL,
    tax_amount NUMERIC(10,2) NOT NULL,
    tip_amount NUMERIC(10,2) NOT NULL,
    discount_amount NUMERIC(10,2) NOT NULL,
    total_amount NUMERIC(10,2) NOT NULL,
    currency_code VARCHAR(10) NOT NULL,
    currency_symbol VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_orden_mesa FOREIGN KEY (mesa_id) REFERENCES mesa(id)
);

CREATE TABLE IF NOT EXISTS orden_item (
    id UUID PRIMARY KEY,
    orden_id UUID NOT NULL,
    menu_item_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    total_price NUMERIC(10,2) NOT NULL,
    CONSTRAINT fk_orden_item_orden FOREIGN KEY (orden_id) REFERENCES orden(id) ON DELETE CASCADE,
    CONSTRAINT fk_orden_item_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_item(id)
);

CREATE INDEX IF NOT EXISTS idx_orden_item_orden_id ON orden_item(orden_id);

INSERT INTO app_parameter (param_key, numeric_value, text_value, boolean_value, updated_at)
VALUES
    ('CURRENCY_CODE', NULL, 'USD', NULL, NOW()),
    ('CURRENCY_SYMBOL', NULL, '$', NULL, NOW()),
    ('TAX_RATE', 0.00, NULL, NULL, NOW()),
    ('TIP_TYPE', NULL, 'PERCENTAGE', NULL, NOW()),
    ('TIP_VALUE', 0.00, NULL, NULL, NOW())
ON CONFLICT (param_key) DO NOTHING;
