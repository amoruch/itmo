\set ON_ERROR_STOP on

BEGIN;

CREATE TABLE IF NOT EXISTS lab7_users (
    -- PostgreSQL creates and advances the underlying sequence automatically.
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login VARCHAR(64) NOT NULL UNIQUE,
    password_hash CHAR(56) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT lab7_users_login_not_blank CHECK (btrim(login) <> ''),
    CONSTRAINT lab7_users_password_hash_sha224
        CHECK (password_hash ~ '^[0-9a-f]{56}$')
);

-- Reference values for the enum-like fields of SpaceMarine.
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

CREATE TABLE IF NOT EXISTS lab7_space_marines (
    -- Java does not provide this value during a normal INSERT.
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    coordinate_x BIGINT NOT NULL,
    coordinate_y BIGINT NOT NULL,
    creation_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    health DOUBLE PRECISION NOT NULL,
    loyal BOOLEAN NOT NULL,
    category VARCHAR(32) NOT NULL
        CONSTRAINT lab7_marines_category_fk REFERENCES lab7_astartes_categories (name),
    melee_weapon VARCHAR(32) NOT NULL
        CONSTRAINT lab7_marines_melee_weapon_fk REFERENCES lab7_melee_weapons (name),
    chapter_name VARCHAR(255),
    chapter_world VARCHAR(255),
    -- An object cannot exist without the account that created it.
    creator_id INTEGER NOT NULL REFERENCES lab7_users (id) ON DELETE RESTRICT,

    CONSTRAINT lab7_marines_name_not_blank CHECK (btrim(name) <> ''),
    CONSTRAINT lab7_marines_coordinate_x_valid CHECK (coordinate_x > -319),
    CONSTRAINT lab7_marines_coordinate_y_valid CHECK (coordinate_y > -601),
    CONSTRAINT lab7_marines_health_positive CHECK (health > 0),
    CONSTRAINT lab7_marines_chapter_complete CHECK (
        (chapter_name IS NULL AND chapter_world IS NULL)
        OR (btrim(chapter_name) <> '' AND chapter_world IS NOT NULL)
    )
);

CREATE INDEX IF NOT EXISTS lab7_marines_creator_id_idx
    ON lab7_space_marines (creator_id);

COMMIT;
