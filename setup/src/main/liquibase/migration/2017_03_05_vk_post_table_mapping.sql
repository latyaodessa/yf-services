ALTER TABLE vk_post RENAME post_id TO id;
ALTER TABLE user_saved_posts ADD FOREIGN KEY(post_id) REFERENCES vk_post(id);