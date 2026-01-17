DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'menu_item'
          AND column_name = 'long_description'
          AND data_type = 'bytea'
    ) THEN
        ALTER TABLE menu_item
            ALTER COLUMN long_description TYPE TEXT
            USING convert_from(long_description, 'UTF8');
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'menu_item'
          AND column_name = 'short_description'
          AND data_type = 'bytea'
    ) THEN
        ALTER TABLE menu_item
            ALTER COLUMN short_description TYPE VARCHAR(255)
            USING convert_from(short_description, 'UTF8');
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'menu_item'
          AND column_name = 'nickname'
          AND data_type = 'bytea'
    ) THEN
        ALTER TABLE menu_item
            ALTER COLUMN nickname TYPE VARCHAR(80)
            USING convert_from(nickname, 'UTF8');
    END IF;
END $$;
