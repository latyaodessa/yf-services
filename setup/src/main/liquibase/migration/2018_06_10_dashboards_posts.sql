UPDATE user_saved_posts SET user_id = user_vk.user_id FROM user_vk WHERE user_vk.id = user_saved_posts.user_id;
UPDATE user_saved_posts SET user_id = user_fb.user_id FROM user_fb WHERE user_fb.id = user_saved_posts.user_id;
