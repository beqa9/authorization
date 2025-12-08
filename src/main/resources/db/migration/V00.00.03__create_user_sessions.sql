CREATE TABLE user_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    ip VARCHAR(255),
    user_agent TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    last_seen TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    expires_at TIMESTAMP WITH TIME ZONE,
    revoked BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT fk_user_sessions_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

ALTER TABLE refresh_tokens
  ADD COLUMN session_id BIGINT;

ALTER TABLE refresh_tokens
  ADD CONSTRAINT fk_refresh_token_session FOREIGN KEY (session_id) REFERENCES user_sessions (id) ON DELETE SET NULL;