CREATE DATABASE me2event CHARACTER SET 'UTF8';
INSERT INTO db VALUES('%', 'event', 'me2day', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
CREATE DATABASE me2event_log CHARACTER SET 'UTF8';
INSERT INTO db VALUES('%', 'event_log', 'me2day', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
flush privileges;

-- me2day event 저장 테이블
CREATE TABLE me2_event_$tableNo$ (
  	event_id				BIGINT NOT NULL AUTO_INCREMENT,	-- 이벤트 일련 번호
  	event_time				TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,	-- 이벤트 발생 시간
  	event_target			VARCHAR(20) NOT NULL,	-- event 대상 (post, comment, ...)
  	event_operation			VARCHAR(20) NOT NULL,	-- event 종류 (create, delete, ...)
	event_fire_user_id		VARCHAR(255) NOT NULL,	-- event 발생 사용자 ID
  	event_options			VARCHAR(10000),			-- event 옵션
  	heavy_yn				enum('Y','N') NOT NULL DEFAULT 'N',	-- 처리 부하 높은 event 여부
  	
  	scheduled_process_time	TIMESTAMP NULL DEFAULT NULL,  -- 예상 처리 시간. NOT NULL로 선언할 방법이 없어 NULLABLE로 선언
  	process_status 			VARCHAR(15) NOT NULL DEFAULT 'WAITING',	-- 처리 상태
  	process_server_ip 		VARCHAR(15),							-- 처리 서버 IP
  	process_start_time		TIMESTAMP NULL DEFAULT NULL,			-- 처리 시작 시간
  	process_end_time		TIMESTAMP NULL DEFAULT NULL,			-- 처리 종료 시간
  	
  	PRIMARY KEY(event_id)     
);
ALTER TABLE me2_event_$tableNo$ ADD INDEX idx_001(process_status, heavy_yn, scheduled_process_time);

-- 처리 완료된 me2day event 저장 테이블
CREATE TABLE me2_event_log (
	db_no				INT NOT NULL,
	table_no			INT NOT NULL,
  	event_id			BIGINT NOT NULL,
  	event_time			TIMESTAMP NULL DEFAULT NULL,  -- NOT NULL로 선언할 방법이 없어 NULLABLE로 선언
  	event_target		VARCHAR(20) NOT NULL,
  	event_operation		VARCHAR(20) NOT NULL,
	event_fire_user_id	VARCHAR(255) NOT NULL,
  	event_options		VARCHAR(10000),
  	heavy_yn			enum('Y','N') NOT NULL,
  	
  	process_status 		VARCHAR(15) NOT NULL,
  	process_server_ip 	VARCHAR(15),
  	process_start_time	TIMESTAMP NULL DEFAULT NULL,
  	process_end_time	TIMESTAMP NULL DEFAULT NULL,
  	
  	reprocess_yn        enum('Y','N') NOT NULL DEFAULT 'N',		-- 재처리를 수행 했는지의 여부
  	
  	PRIMARY KEY(db_no, table_no, event_id)     
);
ALTER TABLE me2_event_log ADD INDEX idx_001(db_no, table_no, event_id);
ALTER TABLE me2_event_log ADD INDEX idx_002(event_time);

-- processor별 이벤트 처리 일정 저장 테이블
CREATE TABLE me2_event_process_schedule_$tableNo$ (
  	schedule_id   				BIGINT NOT NULL AUTO_INCREMENT,	-- 처리 일정 일련번호
	processor_name 				VARCHAR(20) NOT NULL,			-- 처리기 이름
  	process_try_count          	INTEGER NOT NULL,				-- 처리 시도 횟수
  	process_targets       		TEXT,							-- 처리 대상
  	last_process_failed_time    TIMESTAMP NULL DEFAULT NULL,  	-- 최근 처리 실패 시간. NOT NULL로 선언할 방법이 없어 NULLABLE로 선언
  	scheduled_process_time		TIMESTAMP NULL DEFAULT NULL,  	-- 예상 처리 시간. NOT NULL로 선언할 방법이 없어 NULLABLE로 선언
  	process_status 				VARCHAR(15) NOT NULL DEFAULT 'FAIL',	-- 처리 상태
  	process_server_ip 			VARCHAR(15),							-- 처리 서버 IP
  	process_start_time			TIMESTAMP NULL DEFAULT NULL,			-- 처리 시작 시간
	
  	event_id             		BIGINT NOT NULL,
  	event_time           		TIMESTAMP NULL DEFAULT NULL,  -- NOT NULL로 선언할 방법이 없어 NULLABLE로 선언
  	event_target         		VARCHAR(20) NOT NULL,
  	event_operation      		VARCHAR(20),
	event_fire_user_id   		VARCHAR(255) NOT NULL,
  	event_options        		VARCHAR(10000),
  	heavy_yn					enum('Y','N') NOT NULL DEFAULT 'N',
  	
  	PRIMARY KEY (schedule_id)
);
ALTER TABLE me2_event_process_schedule_$tableNo$ ADD INDEX idx_001(scheduled_process_time, process_status);
ALTER TABLE me2_event_process_schedule_$tableNo$ ADD INDEX idx_002(event_id);
ALTER TABLE me2_event_process_schedule_$tableNo$ ADD INDEX idx_003(process_status);
ALTER TABLE me2_event_process_schedule_$tableNo$ ADD INDEX idx_004(processor_name);

-- 처리에 실패한 processor별 이벤트 처리 저장 테이블
CREATE TABLE me2_event_process_fail_log (
	db_no						INT NOT NULL,
	table_no					INT NOT NULL,
  	schedule_id   				BIGINT NOT NULL DEFAULT 0,
	processor_name 				VARCHAR(20) NOT NULL,
  	process_try_count          	INTEGER NOT NULL,
  	process_targets       		TEXT,
  	last_process_failed_time    TIMESTAMP NULL DEFAULT NULL,  -- NOT NULL로 선언할 방법이 없어 NULLABLE로 선언
  	next_process_time  			TIMESTAMP NULL DEFAULT NULL,  -- NOT NULL로 선언할 방법이 없어 NULLABLE로 선언
  	process_status 				VARCHAR(15) NOT NULL,
  	process_server_ip 			VARCHAR(15),
  	process_start_time			TIMESTAMP NULL DEFAULT NULL,
  	
  	event_id             		BIGINT NOT NULL,
  	event_time           		TIMESTAMP NULL DEFAULT NULL,  -- NOT NULL로 선언할 방법이 없어 NULLABLE로 선언
  	event_target         		VARCHAR(20) NOT NULL,
  	event_operation      		VARCHAR(20),
	event_fire_user_id   		VARCHAR(255) NOT NULL,
  	event_options        		VARCHAR(10000),
  	heavy_yn					enum('Y','N') NOT NULL,
  	
  	fail_message				VARCHAR(10000) NULL	-- 처리 실패 메시지
);

ALTER TABLE me2_event_process_fail_log ADD INDEX idx_001(db_no, table_no, schedule_id);
ALTER TABLE me2_event_process_fail_log ADD INDEX idx_002(table_no, event_id, processor_name);
ALTER TABLE me2_event_process_fail_log ADD INDEX idx_003(event_time);
