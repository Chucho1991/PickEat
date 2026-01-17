DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'menu_item'
          AND column_name = 'status'
    ) THEN
        ALTER TABLE menu_item
            ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE,
            ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE;
        UPDATE menu_item
            SET active = (status = 'ACTIVO');
        ALTER TABLE menu_item
            DROP COLUMN status;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'mesa'
          AND column_name = 'status'
    ) THEN
        ALTER TABLE mesa
            ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE,
            ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE;
        UPDATE mesa
            SET active = (status = 'ACTIVO');
        ALTER TABLE mesa
            DROP COLUMN status;
    END IF;
END $$;
