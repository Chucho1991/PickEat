ALTER TABLE discount_item
    ADD COLUMN IF NOT EXISTS apply_scope VARCHAR(20) NOT NULL DEFAULT 'ORDER',
    ADD COLUMN IF NOT EXISTS exclusive BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS apply_over_discount BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS auto_apply BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS generates_coupon BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS coupon_rule_type VARCHAR(30),
    ADD COLUMN IF NOT EXISTS coupon_min_total NUMERIC(10,2),
    ADD COLUMN IF NOT EXISTS coupon_dish_type VARCHAR(30),
    ADD COLUMN IF NOT EXISTS coupon_min_item_qty INTEGER,
    ADD COLUMN IF NOT EXISTS coupon_validity_days INTEGER,
    ADD COLUMN IF NOT EXISTS coupon_require_no_discount BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS coupon_active BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE IF NOT EXISTS discount_item_menu_item (
    discount_item_id UUID NOT NULL,
    menu_item_id UUID NOT NULL,
    PRIMARY KEY (discount_item_id, menu_item_id),
    CONSTRAINT fk_discount_item_menu_discount FOREIGN KEY (discount_item_id) REFERENCES discount_item(id) ON DELETE CASCADE,
    CONSTRAINT fk_discount_item_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_item(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS discount_coupon (
    id UUID PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    discount_item_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    generated_order_id UUID,
    redeemed_order_id UUID,
    redeemed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_coupon_discount_item FOREIGN KEY (discount_item_id) REFERENCES discount_item(id)
);

CREATE INDEX IF NOT EXISTS idx_discount_coupon_code ON discount_coupon(code);
CREATE INDEX IF NOT EXISTS idx_discount_coupon_status ON discount_coupon(status);
