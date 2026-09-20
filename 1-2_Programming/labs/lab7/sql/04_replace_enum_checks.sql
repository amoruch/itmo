\set ON_ERROR_STOP on

BEGIN;

-- Add reference tables and fill them with the values accepted by the model.
CREATE TABLE IF NOT EXISTS lab7_astartes_categories (
    name VARCHAR(32) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS lab7_melee_weapons (
    name VARCHAR(32) PRIMARY KEY
);

INSERT INTO lab7_astartes_categories (name)
VALUES ('ASSAULT'), ('SUPPRESSOR'), ('TACTICAL'), ('TERMINATOR'), ('APOTHECARY')
ON CONFLICT (name) DO NOTHING;

INSERT INTO lab7_melee_weapons (name)
VALUES ('POWER_SWORD'), ('MANREAPER'), ('LIGHTING_CLAW'), ('POWER_FIST')
ON CONFLICT (name) DO NOTHING;

-- The old CHECK constraints are replaced by foreign keys to the reference tables.
ALTER TABLE lab7_space_marines
    DROP CONSTRAINT IF EXISTS lab7_marines_category_valid,
    DROP CONSTRAINT IF EXISTS lab7_marines_melee_weapon_valid;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'lab7_marines_category_fk'
    ) THEN
        ALTER TABLE lab7_space_marines
            ADD CONSTRAINT lab7_marines_category_fk
            FOREIGN KEY (category) REFERENCES lab7_astartes_categories (name);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'lab7_marines_melee_weapon_fk'
    ) THEN
        ALTER TABLE lab7_space_marines
            ADD CONSTRAINT lab7_marines_melee_weapon_fk
            FOREIGN KEY (melee_weapon) REFERENCES lab7_melee_weapons (name);
    END IF;
END $$;

COMMIT;
