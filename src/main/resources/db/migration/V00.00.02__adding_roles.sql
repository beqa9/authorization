-- Seed basic authorities and roles

INSERT INTO authorities (name, description) VALUES
('PERSON_READ', 'Read person'),
('PERSON_WRITE', 'Create/update person'),
('PERSON_DELETE', 'Delete (or soft-delete) person'),
('USER_MANAGE', 'Manage users and roles');

INSERT INTO roles (name, description) VALUES
('ROLE_ADMIN', 'Administrator'),
('ROLE_USER', 'Default user');

-- Map ROLE_ADMIN -> all authorities

INSERT INTO role_authorities (role_id, authority_id)
SELECT r.id, a.id FROM roles r, authorities a WHERE r.name = 'ROLE_ADMIN';

-- Map ROLE_USER -> PERSON_READ

INSERT INTO role_authorities (role_id, authority_id)
SELECT r.id, a.id FROM roles r, authorities a
WHERE r.name = 'ROLE_USER' AND a.name = 'PERSON_READ';