\set ON_ERROR_STOP on

-- Expected after running the first two scripts: 4 users and 7 initial marines.
SELECT
    (SELECT COUNT(*) FROM lab7_users) AS users_count,
    (SELECT COUNT(*) FROM lab7_space_marines) AS marines_count,
    (SELECT last_value FROM lab7_space_marines_id_seq) AS last_generated_id,
    (SELECT last_value + 1 FROM lab7_space_marines_id_seq) AS expected_next_id;

SELECT
    marine.id,
    marine.name,
    marine.creation_date,
    marine.health,
    marine.category,
    marine.melee_weapon,
    COALESCE(marine.chapter_name || ' (' || marine.chapter_world || ')', 'no chapter') AS chapter,
    owner.login AS creator
FROM lab7_space_marines AS marine
JOIN lab7_users AS owner ON owner.id = marine.creator_id
ORDER BY marine.id;

-- Display accounts separately; password hashes are intentionally not selected.
SELECT id, login, created_at
FROM lab7_users
ORDER BY id;

SELECT indexname, indexdef
FROM pg_indexes
WHERE schemaname = 'public'
  AND tablename = 'lab7_space_marines'
ORDER BY indexname;
