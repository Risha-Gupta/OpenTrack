-- OpenTrack initial schema

CREATE TABLE IF NOT EXISTS organizations (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    github_org_name VARCHAR(100) NOT NULL UNIQUE,
    display_name    VARCHAR(200),
    description     TEXT,
    avatar_url      VARCHAR(500),
    is_active       TINYINT(1) NOT NULL DEFAULT 1,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS contributors (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    github_username     VARCHAR(50) NOT NULL UNIQUE,
    display_name        VARCHAR(100),
    avatar_url          VARCHAR(500),
    github_profile_url  VARCHAR(500),
    total_score         INT NOT NULL DEFAULT 0,
    commit_count        INT NOT NULL DEFAULT 0,
    pr_count            INT NOT NULL DEFAULT 0,
    issue_count         INT NOT NULL DEFAULT 0,
    review_count        INT NOT NULL DEFAULT 0,
    organization_id     BIGINT,
    last_synced_at      DATETIME,
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_contributor_org FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS users (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    username         VARCHAR(50) NOT NULL UNIQUE,
    email            VARCHAR(100) NOT NULL UNIQUE,
    password         VARCHAR(255) NOT NULL,
    github_username  VARCHAR(50),
    github_token     VARCHAR(500),
    role             ENUM('USER','ADMIN','ORG_ADMIN') NOT NULL DEFAULT 'USER',
    is_active        TINYINT(1) NOT NULL DEFAULT 1,
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS contribution_events (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    contributor_id   BIGINT NOT NULL,
    event_type       ENUM('COMMIT','PULL_REQUEST_OPENED','PULL_REQUEST_MERGED',
                         'ISSUE_OPENED','ISSUE_CLOSED','CODE_REVIEW','FORK','STAR','RELEASE') NOT NULL,
    repo_name        VARCHAR(200),
    github_event_id  VARCHAR(100) UNIQUE,
    event_url        VARCHAR(500),
    description      TEXT,
    score_awarded    INT NOT NULL DEFAULT 0,
    occurred_at      DATETIME,
    recorded_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_event_contributor FOREIGN KEY (contributor_id)
        REFERENCES contributors(id) ON DELETE CASCADE
);