-- Seed data for local dev and testing

INSERT INTO organizations (github_org_name, display_name, description) VALUES
('OSDC-JIIT', 'OSDC JIIT', 'Open Source Developer Community at JIIT Noida'),
('spring-projects', 'Spring Projects', 'Spring framework and ecosystem'),
('apache', 'Apache Software Foundation', 'Apache open source projects');

INSERT INTO contributors (github_username, display_name, total_score, commit_count, pr_count, issue_count, review_count, organization_id) VALUES
('dev-alice',     'Alice Dev',    340, 45, 12,  8, 15, 1),
('bob-codes',     'Bob Codes',    285, 38,  9, 12, 10, 1),
('charlie-oss',   'Charlie OSS',  220, 28,  7,  6,  8, 1),
('diana-pr',      'Diana PR',     195, 20, 14,  5,  6, 1),
('evan-issues',   'Evan Issues',  165, 15,  5, 18,  4, 1),
('frank-reviews', 'Frank Reviews',150, 10,  4,  3, 18, 2),
('grace-merge',   'Grace Merge',  130, 18,  6,  7,  3, 2),
('henry-fork',    'Henry Fork',    95, 22,  2,  4,  2, 3);

INSERT INTO users (username, email, password, github_username, role) VALUES
('admin', 'admin@opentrack.dev',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh',
 'dev-alice', 'ADMIN'),
('testuser', 'test@opentrack.dev',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh',
 'bob-codes', 'USER');
-- Default password for all seed users: admin123