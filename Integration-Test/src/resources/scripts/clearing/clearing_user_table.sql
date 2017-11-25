SET SQL_SAFE_UPDATES=0;
DELETE FROM amt.user_ip_deactive where user_id > 0;
DELETE FROM amt.user_ip_failure where user_ip_failure_id > 0;
DELETE FROM amt.user_login_log where user_id > 0;
DELETE FROM amt.users WHERE user_id > 0;