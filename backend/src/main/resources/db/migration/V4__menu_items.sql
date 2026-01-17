CREATE TABLE IF NOT EXISTS menu_item (
    id UUID PRIMARY KEY,
    long_description TEXT NOT NULL,
    short_description VARCHAR(255) NOT NULL,
    nickname VARCHAR(80) UNIQUE NOT NULL,
    dish_type VARCHAR(20) NOT NULL,
    status VARCHAR(10) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    image_path VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
