/* ---------------------------------------------------- */
/*  Generated by Enterprise Architect Version 12.1 		*/
/*  Created On : 09-Dec-2017 2:37:50 PM 				*/
/*  DBMS       : MySql 						*/
/* ---------------------------------------------------- */

SET FOREIGN_KEY_CHECKS=0 
;

/* Create Tables */

CREATE TABLE `content_status`
(
	`status` VARCHAR(2) NOT NULL,
	`description` VARCHAR(100) NOT NULL,
	CONSTRAINT `PK_content_status` PRIMARY KEY (`status` ASC)
)

;

CREATE TABLE `course`
(
	`course_id` VARCHAR(22) NOT NULL,
	`course_name` VARCHAR(100) 	,
	`course_level` VARCHAR(2) NOT NULL,
	`course_type` VARCHAR(2) NOT NULL,
	`description` VARCHAR(200) 	,
	`estimated_duration` INT NOT NULL,
	`actual_duration` INT 	,
	`created_by` INT NOT NULL,
	`creation_date` DATETIME NOT NULL,
	`last_update_date` DATETIME 	,
	`course_status` VARCHAR(2) NOT NULL,
	`start_date` DATE 	,
	`due_date` DATE 	,
	`min_per_day` INT NOT NULL,
	CONSTRAINT `PK_course` PRIMARY KEY (`course_id` ASC)
)

;

CREATE TABLE `course_level`
(
	`level` VARCHAR(2) NOT NULL,
	`description` VARCHAR(100) NOT NULL,
	CONSTRAINT `PK_course_level` PRIMARY KEY (`level` ASC)
)

;

CREATE TABLE `course_pre_requisite`
(
	`pre_requisite_id` INT NOT NULL AUTO_INCREMENT,
	`course_id` VARCHAR(22) NOT NULL,
	`pre_requisite_num` INT NOT NULL,
	`pre_requisite_name` VARCHAR(50) NOT NULL,
	`pre_requisite_type` VARCHAR(2) NOT NULL,
	`pre_requisite_url` VARCHAR(200) NOT NULL,
	CONSTRAINT `PK_course_pre_requisite` PRIMARY KEY (`pre_requisite_id` ASC)
)

;

CREATE TABLE `course_reference`
(
	`reference_id` INT NOT NULL AUTO_INCREMENT,
	`course_id` VARCHAR(22) NOT NULL,
	`reference_num` INT NOT NULL,
	`reference_name` VARCHAR(50) NOT NULL,
	`reference_type` VARCHAR(2) NOT NULL,
	`reference_url` VARCHAR(200) NOT NULL,
	CONSTRAINT `PK_course_reference` PRIMARY KEY (`reference_id` ASC)
)

;

CREATE TABLE `course_type`
(
	`type` VARCHAR(2) NOT NULL,
	`description` VARCHAR(100) NOT NULL,
	CONSTRAINT `PK_course_type` PRIMARY KEY (`type` ASC)
)

;

CREATE TABLE `data_type`
(
	`type` VARCHAR(2) NOT NULL,
	`description` VARCHAR(50) NOT NULL,
	`regex` VARCHAR(200) NOT NULL,
	CONSTRAINT `PK_data_type` PRIMARY KEY (`type` ASC)
)

;

CREATE TABLE `material_type`
(
	`type` VARCHAR(2) NOT NULL,
	`description` VARCHAR(100) NOT NULL,
	CONSTRAINT `PK_material_type` PRIMARY KEY (`type` ASC)
)

;

CREATE TABLE `role`
(
	`role` VARCHAR(2) NOT NULL,
	`description` VARCHAR(100) NOT NULL,
	`is_admin` BOOL NOT NULL,
	`rank` INT NOT NULL,
	CONSTRAINT `PK_role` PRIMARY KEY (`role` ASC)
)

;

CREATE TABLE `section`
(
	`section_id` INT NOT NULL AUTO_INCREMENT,
	`course_id` VARCHAR(22) NOT NULL,
	`section_num` INT NOT NULL,
	`section_name` VARCHAR(100) NOT NULL,
	`estimated_duration` INT NOT NULL,
	`actual_duration` INT 	,
	`description` VARCHAR(200) 	,
	`creation_date` DATETIME NOT NULL,
	`section_status` VARCHAR(2) NOT NULL,
	`start_date` DATE 	,
	`due_date` DATE 	,
	CONSTRAINT `PK_chapter` PRIMARY KEY (`section_id` ASC)
)

;

CREATE TABLE `system_parameter`
(
	`param_id` INT NOT NULL AUTO_INCREMENT,
	`param_name` VARCHAR(100) NOT NULL,
	`param_value` VARCHAR(200) NOT NULL,
	`param_type` VARCHAR(2) NOT NULL,
	CONSTRAINT `PK_system_parameter` PRIMARY KEY (`param_id` ASC)
)

;

CREATE TABLE `user_ip_deactive`
(
	`user_ip_deactive_id` INT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
	`ip` VARCHAR(50) NOT NULL,
	`trail_num` INT NOT NULL,
	`last_trail_date` DATETIME NOT NULL,
	`is_active` BOOL NOT NULL,
	CONSTRAINT `PK_user_ip_deactive` PRIMARY KEY (`user_ip_deactive_id` ASC)
)

;

CREATE TABLE `user_ip_failure`
(
	`user_ip_failure_id` INT NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(50) NOT NULL,
	`ip` VARCHAR(50) NOT NULL,
	`login_date` DATETIME NOT NULL,
	CONSTRAINT `PK_user_ip_failure` PRIMARY KEY (`user_ip_failure_id` ASC)
)

;

CREATE TABLE `user_login_log`
(
	`user_login_log_id` INT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
	`ip` VARCHAR(50) NOT NULL,
	`login_date` DATETIME NOT NULL,
	`is_success` BOOL NOT NULL,
	`error_code` VARCHAR(10) 	,
	`error_msg` VARCHAR(150) 	,
	CONSTRAINT `PK_user_login_log` PRIMARY KEY (`user_login_log_id` ASC)
)

;

CREATE TABLE `users`
(
	`user_id` INT NOT NULL AUTO_INCREMENT,
	`first_name` VARCHAR(15) 	,
	`last_name` VARCHAR(15) 	,
	`user_name` VARCHAR(50) NOT NULL,
	`password` VARCHAR(50) NOT NULL,
	`email` VARCHAR(60) NOT NULL,
	`role` VARCHAR(2) NOT NULL,
	`creation_date` DATETIME NOT NULL,
	CONSTRAINT `PK_Table1` PRIMARY KEY (`user_id` ASC)
)

;

/* Create Foreign Key Constraints */

ALTER TABLE `course` 
 ADD CONSTRAINT `FK_course_content_status`
	FOREIGN KEY (`course_status`) REFERENCES `content_status` (`status`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `course` 
 ADD CONSTRAINT `FK_course_course_level`
	FOREIGN KEY (`course_level`) REFERENCES `course_level` (`level`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `course` 
 ADD CONSTRAINT `FK_course_course_type`
	FOREIGN KEY (`course_type`) REFERENCES `course_type` (`type`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `course` 
 ADD CONSTRAINT `FK_course_users`
	FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `course_pre_requisite` 
 ADD CONSTRAINT `FK_course_pre_requisite_course`
	FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `course_pre_requisite` 
 ADD CONSTRAINT `FK_course_pre_requisite_material_type`
	FOREIGN KEY (`pre_requisite_type`) REFERENCES `material_type` (`type`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `course_reference` 
 ADD CONSTRAINT `FK_course_reference_course`
	FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `course_reference` 
 ADD CONSTRAINT `FK_course_reference_material_type`
	FOREIGN KEY (`reference_type`) REFERENCES `material_type` (`type`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `section` 
 ADD CONSTRAINT `FK_section_content_status`
	FOREIGN KEY (`section_status`) REFERENCES `content_status` (`status`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `section` 
 ADD CONSTRAINT `FK_section_course`
	FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `system_parameter` 
 ADD CONSTRAINT `FK_system_parameters_data_type`
	FOREIGN KEY (`param_type`) REFERENCES `data_type` (`type`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `user_ip_deactive` 
 ADD CONSTRAINT `FK_user_ip_deactive_users`
	FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `user_login_log` 
 ADD CONSTRAINT `FK_user_login_log_users`
	FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `users` 
 ADD CONSTRAINT `FK_users_role`
	FOREIGN KEY (`role`) REFERENCES `role` (`role`) ON DELETE Restrict ON UPDATE Restrict
;

SET FOREIGN_KEY_CHECKS=1 
;
