--liquibase formatted sql

--changeset gsaini:1
CREATE TABLE player (
	id int(11) NOT NULL AUTO_INCREMENT,
	name VARCHAR ( 100 ) UNIQUE NOT NULL,
	role VARCHAR ( 20 ) NOT NULL,
	short_name VARCHAR ( 50 ),
	PRIMARY KEY (id)
);
--rollback DROP TABLE player;
