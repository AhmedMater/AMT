INSERT INTO role (role, description) VALUES ('Ad','Admin');
INSERT INTO role (role, description) VALUES ('St','Student');
INSERT INTO role (role, description) VALUES ('Tu','Tutor');

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
  (1, 'Ahmed_Mater', 'e10adc3949ba59abbe56e057f20f883e', 'ahmedmotair@gmail.com', 'Ahmed', 'Mater', 'Ad', NOW());

