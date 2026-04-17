-- Performance indexes

CREATE INDEX idx_contributors_total_score ON contributors(total_score DESC);
CREATE INDEX idx_contributors_org_score ON contributors(organization_id, total_score DESC);
CREATE INDEX idx_contributors_last_synced ON contributors(last_synced_at);
CREATE INDEX idx_contribution_events_contributor ON contribution_events(contributor_id);
CREATE INDEX idx_contribution_events_occurred_at ON contribution_events(occurred_at);
CREATE INDEX idx_contribution_events_type ON contribution_events(event_type);
CREATE INDEX idx_contribution_events_contributor_date ON contribution_events(contributor_id, occurred_at);
CREATE INDEX idx_users_github_username ON users(github_username);
CREATE INDEX idx_users_is_active ON users(is_active);