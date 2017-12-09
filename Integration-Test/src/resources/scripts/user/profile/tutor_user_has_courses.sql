INSERT INTO users(user_id, user_name, password, email, first_name, last_name, role, creation_date) VALUES
  (9, 'Test_User_9', 'e10adc3949ba59abbe56e057f20f883e', 'test.user.9@amt.com', 'Test', 'User', 'Tu', NOW());

INSERT INTO course(course_name, course_level, course_type, description, estimated_duration, actual_duration, created_by, creation_date, course_id, last_update_date, course_status, start_date, due_date, min_per_day) VALUES
  ('Test-Course', 'Be', 'Pr', 'Course Description', 30, 35, 9, NOW(), 'Cor0009', NOW(), 'Fu', NOW(), NOW(), 15);