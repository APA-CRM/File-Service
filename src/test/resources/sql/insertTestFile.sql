INSERT INTO file_metadata(
    id, name, file_type, parent_file_id,
    file_extension, full_name, created_at, updated_at
) VALUES
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c1', 'Documents', 'DIRECTORY', NULL,
    NULL, 'Documents', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c2', 'Images', 'DIRECTORY', NULL,
    NULL, 'Images', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c3', 'Employee`s Document', 'FILE', '5682d1e7-3eb4-4e41-923a-7b7abc0239c1',
    'DOCX', 'Employee`s_Document.docx', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c4', 'Employee`s Photo', 'FILE', '5682d1e7-3eb4-4e41-923a-7b7abc0239c2',
    'JPEG', 'Employee`s_Photo.jpeg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
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