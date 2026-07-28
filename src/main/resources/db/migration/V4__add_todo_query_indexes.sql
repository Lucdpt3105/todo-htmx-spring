CREATE INDEX idx_todos_user_id_created_at
    ON todos (user_id, created_at DESC);

CREATE INDEX idx_todos_user_id_completed_priority_created_at
    ON todos (user_id, completed, priority DESC, created_at DESC);
