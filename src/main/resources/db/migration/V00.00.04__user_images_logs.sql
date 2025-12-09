CREATE TABLE user_images (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    object_name VARCHAR(255) NOT NULL,
    bucket VARCHAR(255) NOT NULL,
    uploaded_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    CONSTRAINT fk_user_images_session FOREIGN KEY (session_id)
        REFERENCES user_sessions (id) ON DELETE CASCADE
);