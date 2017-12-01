
INSERT INTO role (role, description, isAdmin) VALUES ('Ow','Owner', 1);
INSERT INTO role (role, description, isAdmin) VALUES ('Ad','Admin', 1);
INSERT INTO role (role, description, isAdmin) VALUES ('St','Student', 0);
INSERT INTO role (role, description, isAdmin) VALUES ('Tu','Tutor', 0);

INSERT INTO content_status(status, description) VALUES ('Fu', 'Future');
INSERT INTO content_status(status, description) VALUES ('Pr', 'In Progress');
INSERT INTO content_status(status, description) VALUES ('Do', 'Done');

INSERT INTO course_type(type, description) VALUES ('Ac', 'Academic');
INSERT INTO course_type(type, description) VALUES ('Pr', 'Practical');

INSERT INTO course_level(level, description) VALUES ('Be', 'Beginner');
INSERT INTO course_level(level, description) VALUES ('In', 'Intermediate');
INSERT INTO course_level(level, description) VALUES ('Ad', 'Advanced');

INSERT INTO material_type(type, description) VALUES ('Co', 'Course');
INSERT INTO material_type(type, description) VALUES ('Bo', 'Book');

INSERT INTO data_type(type, description, regex) VALUES ('Nu', 'Number', '[0-9]+');

INSERT INTO system_parameter(param_name, param_type, param_value) VALUES ('Total Course Number', 'Nu', '0');

INSERT INTO users(user_id, user_name, password, email, first_name, last_name, role, creation_date) VALUES
  (1, 'Owner_User', 'e10adc3949ba59abbe56e057f20f883e', 'owner.user@amt.com', 'Owner', 'User', 'Ow', NOW());

INSERT INTO users(user_id, user_name, password, email, first_name, last_name, role, creation_date) VALUES
  (2, 'Admin_User', 'e10adc3949ba59abbe56e057f20f883e', 'admin.user@amt.com', 'Admin', 'User', 'Ad', NOW());

