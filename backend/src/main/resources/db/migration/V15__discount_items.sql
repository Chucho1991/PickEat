CREATE TABLE IF NOT EXISTS discount_item (
    id UUID PRIMARY KEY,
    long_description TEXT NOT NULL,
    short_description VARCHAR(255) NOT NULL,
    nickname VARCHAR(80) UNIQUE NOT NULL,
    discount_type VARCHAR(20) NOT NULL,
    value NUMERIC(10,2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    deleted BOOLEAN NOT NULL DEFAULT false,
    image_path VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS orden_discount_item (
    id UUID PRIMARY KEY,
    orden_id UUID NOT NULL,
    discount_item_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    discount_type VARCHAR(20) NOT NULL,
    unit_value NUMERIC(10,2) NOT NULL,
    total_value NUMERIC(10,2) NOT NULL,
    CONSTRAINT fk_orden_discount_item_orden FOREIGN KEY (orden_id) REFERENCES orden(id) ON DELETE CASCADE,
    CONSTRAINT fk_orden_discount_item_discount FOREIGN KEY (discount_item_id) REFERENCES discount_item(id)
);

CREATE INDEX IF NOT EXISTS idx_orden_discount_item_orden_id ON orden_discount_item(orden_id);
