CREATE TABLE nm_field (
	id 					BIGINT(19) NOT NULL AUTO_INCREMENT,
	name 				VARCHAR(1000) NOT NULL,
	description 		VARCHAR(10000) NOT NULL,
	field_type 			INT(9) NOT NULL,
	options				VARCHAR(10000) NULL,
	default_template_id	BIGINT(19) NOT NULL,
	creator_id			BIGINT(19) NOT NULL,
	create_ymdt			DATETIME NOT NULL,
	PRIMARY KEY(id)
);
alter table nm_field auto_increment=1;

CREATE TABLE nm_frame (
	id 					BIGINT(19) NOT NULL AUTO_INCREMENT,
	name 				VARCHAR(1000) NOT NULL,
	description 		VARCHAR(10000) NOT NULL,
	default_template_id	BIGINT(19) NOT NULL,
	creator_id			BIGINT(19) NOT NULL,
	create_ymdt			DATETIME NOT NULL,
	PRIMARY KEY(id)
); 
alter table nm_frame auto_increment=1000000001;

CREATE TABLE nm_frame_hierarchy (
	frame_id 		BIGINT(19) NOT NULL,
	parent_frame_id	BIGINT(19) NOT NULL,
	creator_id		BIGINT(19) NOT NULL,
	create_ymdt		DATETIME NOT NULL,
	PRIMARY KEY(frame_id, parent_frame_id)
);
ALTER TABLE nm_frame_hierarchy ADD INDEX idx_01(parent_frame_id, frame_id);

CREATE TABLE nm_frame_field (
	frame_id 		BIGINT(19) NOT NULL,
	field_id		BIGINT(19) NOT NULL,
	creator_id		BIGINT(19) NOT NULL,
	create_ymdt		DATETIME NOT NULL,
	PRIMARY KEY(frame_id, field_id)
);
ALTER TABLE nm_frame_field ADD INDEX idx_01(field_id, frame_id);

CREATE TABLE nm_template (
	id 				BIGINT(19) NOT NULL AUTO_INCREMENT,
	name 			VARCHAR(1000) NOT NULL,
	description 	VARCHAR(10000) NOT NULL,
	resource_id		BIGINT(19) NOT NULL,
	creator_id		BIGINT(19) NOT NULL,
	create_ymdt		DATETIME NOT NULL,
	value 			LONGTEXT NOT NULL,
	PRIMARY KEY(id)
);
alter table nm_template auto_increment=2000000001;
ALTER TABLE nm_template ADD INDEX idx_01(resource_id);

CREATE TABLE nm_content (
	id 				BIGINT(19) NOT NULL AUTO_INCREMENT,
	name 			VARCHAR(1000) NOT NULL,
	description 	VARCHAR(10000) NOT NULL,
	creator_id		BIGINT(19) NOT NULL,
	create_ymdt		DATETIME NOT NULL,
	frame_id 		BIGINT(19) NOT NULL,
	value 			LONGTEXT NOT NULL,
	PRIMARY KEY(id)
);
alter table nm_content auto_increment=10000000001;
ALTER TABLE nm_content ADD INDEX idx_01(frame_id);

