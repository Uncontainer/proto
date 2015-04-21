CREATE TABLE resource (
	id				VARCHAR(255) 	NOT NULL,
	type_class_id	VARCHAR(255) 	NOT NULL,
	last_updated 	DATETIME 		NOT NULL,
	PRIMARY KEY(id)
); 

CREATE TABLE class (
	id				VARCHAR(255) 	NOT NULL,
	last_updated 	DATETIME 		NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE property (
	id				VARCHAR(255) 	NOT NULL,
	domain_class_id	VARCHAR(255) 	NOT NULL,
	range_class_id	VARCHAR(255) 	NOT NULL,
	last_updated 	DATETIME		NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE resource_hierarchy (
	id				VARCHAR(255) 	NOT NULL,
	parent_id		VARCHAR(255) 	NOT NULL,
	order_no		INT				NOT NULL,
	last_updated 	DATETIME 		NOT NULL,
	PRIMARY KEY(id, parent_id, order_no)
);

CREATE TABLE triple (
	id 					BIGINT(20) 		NOT NULL AUTO_INCREMENT,
	subject_resource_id	VARCHAR(255) 	NOT NULL,
	property_id			VARCHAR(255) 	NOT NULL,
	object_value		VARCHAR(10000) 	NOT NULL,
	last_updated 		DATETIME 		NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE local_value_large (
	id 					BIGINT(20) 		NOT NULL AUTO_INCREMENT,
	resource_type		CHAR(1) 		NOT NULL,
	resource_id 		VARCHAR(200) 	NOT NULL,
	locale 				VARCHAR(3) 		NOT NULL,
  	value_cont 			LONGTEXT 		NOT NULL,
	last_updated 		DATETIME 		NOT NULL,
  	PRIMARY KEY (id)
);

CREATE TABLE local_value_medium (
	id 					BIGINT(20) 		NOT NULL AUTO_INCREMENT,
	resource_type		CHAR(1) 		NOT NULL,
	resource_id 		VARCHAR(200) 	NOT NULL,
	locale 				VARCHAR(3) 		NOT NULL,
  	value_cont 			VARCHAR(20000)	NOT NULL,
	last_updated 		DATETIME 		NOT NULL,
  	PRIMARY KEY (id)
);

CREATE TABLE local_value_small (
	id 					BIGINT(20) 		NOT NULL AUTO_INCREMENT,
	resource_type		CHAR(1) 		NOT NULL,
	resource_id 		VARCHAR(200) 	NOT NULL,
	locale 				VARCHAR(3) 		NOT NULL,
  	value_cont 			VARCHAR(1000)	NOT NULL,
	last_updated 		DATETIME 		NOT NULL,
  	PRIMARY KEY (id)
);
