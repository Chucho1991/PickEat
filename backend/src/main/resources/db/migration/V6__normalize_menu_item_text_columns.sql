ALTER TABLE menu_item
    ALTER COLUMN long_description TYPE TEXT
    USING CASE
        WHEN pg_typeof(long_description) = 'bytea'::regtype THEN convert_from(long_description, 'UTF8')
        ELSE long_description::text
    END;

ALTER TABLE menu_item
    ALTER COLUMN short_description TYPE VARCHAR(255)
    USING CASE
        WHEN pg_typeof(short_description) = 'bytea'::regtype THEN convert_from(short_description, 'UTF8')
        ELSE short_description::varchar(255)
    END;

ALTER TABLE menu_item
    ALTER COLUMN nickname TYPE VARCHAR(80)
    USING CASE
        WHEN pg_typeof(nickname) = 'bytea'::regtype THEN convert_from(nickname, 'UTF8')
        ELSE nickname::varchar(80)
    END;
