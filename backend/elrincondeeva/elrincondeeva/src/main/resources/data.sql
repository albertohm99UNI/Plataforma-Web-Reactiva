CREATE TABLE IF NOT EXISTS persistent_logins ( 
	username VARCHAR(100) NOT NULL, 
	series VARCHAR(64) PRIMARY KEY, 
	token VARCHAR(64) NOT NULL, 
	last_used TIMESTAMP NOT NULL);
  


INSERT INTO roles (id, name) VALUES 
(1, 'ROLE_ADMIN'),
(2, 'ROLE_CLIENTE'),
(3, 'ROLE_USER');



