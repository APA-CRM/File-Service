INSERT INTO file_metadata(
    id, name, file_type, parent_file_id,
    created_at, updated_at
) VALUES
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c1', 'Documents', 'DIRECTORY', NULL,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c2', 'Images', 'DIRECTORY', NULL,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c3', 'Employee`s Document', 'FILE', '5682d1e7-3eb4-4e41-923a-7b7abc0239c1',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c4', 'Employee`s Photo', 'FILE', '5682d1e7-3eb4-4e41-923a-7b7abc0239c2',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);


INSERT INTO file_content (
    file_metadata_id, content,
    created_at, updated_at
) VALUES
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c3', FILE_READ('classpath:image/file.png'),
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c4', FILE_READ('classpath:image/file.png'),
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);