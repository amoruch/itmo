\set ON_ERROR_STOP on

BEGIN;

-- Owner of the seven objects copied from collection.json.
-- Password: initial-import
INSERT INTO lab7_users (login, password_hash)
VALUES ('initial_import', 'e5cc1365bd4c41a96effae40e180900022d9d95bd0f222fe9b6adcb2');

-- Test accounts for checking access restrictions.
-- Passwords: captain-pass, scout-pass, medic-pass respectively.
INSERT INTO lab7_users (login, password_hash)
VALUES
    ('captain', 'd563bcaeb71a136e8aa76e32119fa8c12f39297b821ecafcf9cc31dd'),
    ('scout',   '08c276fe709a2f367413a91684dc51c95958e2a064b99a77830e9251'),
    ('medic',   '68107e90c0717494428436f8a55f3371b9a002768fcac3773a9e7df7');

-- Run this initial-fill script once on an empty lab7 database.
-- PostgreSQL generates the id values; the final column links every object
-- to the initial_import account.
INSERT INTO lab7_space_marines (
    name, coordinate_x, coordinate_y, creation_date, health, loyal,
    category, melee_weapon, chapter_name, chapter_world, creator_id
)
VALUES
    ('1', 1, 1, '2026-09-04T15:37:41.314550100+03:00'::TIMESTAMPTZ, 1.0, TRUE,
        'ASSAULT', 'POWER_SWORD', '1', '1',
        (SELECT id FROM lab7_users WHERE login = 'initial_import')),
    ('1', 1, 1, '2026-09-13T23:37:45.174874700+03:00'::TIMESTAMPTZ, 1.0, TRUE,
        'ASSAULT', 'POWER_SWORD', NULL, NULL,
        (SELECT id FROM lab7_users WHERE login = 'initial_import')),
    ('aboba', 0, 0, '2026-03-24T19:26:36.174458510+03:00'::TIMESTAMPTZ, 2.0, TRUE,
        'TACTICAL', 'POWER_SWORD', 'MilkiWay', 'Viltrum',
        (SELECT id FROM lab7_users WHERE login = 'initial_import')),
    ('aboba', 0, 0, '2026-03-24T19:28:16.981533679+03:00'::TIMESTAMPTZ, 2.0, FALSE,
        'TACTICAL', 'POWER_SWORD', NULL, NULL,
        (SELECT id FROM lab7_users WHERE login = 'initial_import')),
    ('aboba', 0, 0, '2026-03-24T19:30:13.045383720+03:00'::TIMESTAMPTZ, 2.0, FALSE,
        'TACTICAL', 'POWER_SWORD', 'MilkyWay', 'Viltrum',
        (SELECT id FROM lab7_users WHERE login = 'initial_import')),
    ('aboba', 0, 0, '2026-03-24T19:31:27.467313138+03:00'::TIMESTAMPTZ, 2.0, TRUE,
        'TACTICAL', 'POWER_SWORD', 'MilkyWay', 'Viltrum',
        (SELECT id FROM lab7_users WHERE login = 'initial_import')),
    ('Roman', 1, 1, '2006-11-22T18:38:20.486432862+03:00'::TIMESTAMPTZ, 1.1, TRUE,
        'ASSAULT', 'MANREAPER', 'Astartes', 'Earth',
        (SELECT id FROM lab7_users WHERE login = 'initial_import'));

COMMIT;
