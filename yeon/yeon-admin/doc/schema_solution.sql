--------------------------------------------------------------------
------------------------------ Config ------------------------------
--------------------------------------------------------------------
CREATE TABLE cmp_tkt_$solution_name$ (
  	tkt_id 				VARCHAR(32) NOT NULL,			-- 티켓 ID
  	tkt_desc 			VARCHAR(1000) DEFAULT NULL,		-- 티켓 설명
  	use_stat_cd 		VARCHAR(6) NOT NULL,					-- 티켓 상태 코드(중지, 읽기 전용, 정상)
  	rng_cd VARCHAR(20) 	DEFAULT 'ticket',						-- 설정 적용 범위
  	abstract_yn 		ENUM('Y', 'N') NOT NULL DEFAULT 'N',	-- 추상 ticket 여부
  	supr_tkt_ids		VARCHAR(300),							-- 상위 tkt_id
  	upd_chk_intrvl INT 	DEFAULT 60,								-- 초단위의 설정값 변경 여부 검사 간격
  	rgstr_emp_no		VARCHAR(20),							-- 등록자 사번
  	rgst_ymdt 			TIMESTAMP,	-- 티켓 등록일
  	modr_emp_no			VARCHAR(20),							-- 수정자 사번
  	mod_ymdt 			TIMESTAMP,	-- 티켓 정보 수정일
  	PRIMARY KEY(tkt_id)
);

CREATE TABLE cmp_tkt_cnfg_$solution_name$ (
  	tkt_id 			VARCHAR(32) NOT NULL,		-- 티켓 ID
  	cnfg_key_nm 	VARCHAR(50) NOT NULL,		-- 설정 key
  	cnfg_val_cont 	VARCHAR(1000) NOT NULL,		-- 설정 value
  	rgstr_emp_no	VARCHAR(20),
  	modr_emp_no		VARCHAR(20),
  	rgst_ymdt 		TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  	mod_ymdt 		TIMESTAMP NOT NULL DEFAULT 0,
  	PRIMARY KEY(tkt_id, cnfg_key_nm)
);

CREATE TABLE cmp_tkt_cnfg_item_info_$solution_name$ (
	cnfg_key_nm 	VARCHAR(50)	NOT NULL,		-- 설정값 이름
	cnfg_cate		VARCHAR(32),				-- 설정값 분류
	rqr_yn 			ENUM('Y','N') DEFAULT 'N',	-- 필수 여부
	app_mod_yn 		ENUM('Y','N') DEFAULT 'N',	-- 수정 가능 여부
	data_type_cd	VARCHAR(10)	NOT NULL,		-- 데이터 타입
	default_val		VARCHAR(300),				-- 기본 값
	cnfg_desc		VARCHAR(1000),				-- 설명
	rng_cd 			VARCHAR(20) DEFAULT 'ticket',	-- 설정 적용 범위
	vldt_expr		VARCHAR(1000),				-- validation expression
  	rgstr_emp_no	VARCHAR(20),
  	modr_emp_no		VARCHAR(20),
  	rgst_ymdt 		TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  	mod_ymdt 		TIMESTAMP NOT NULL DEFAULT 0,
	
	PRIMARY KEY(cnfg_key_nm)
);

CREATE TABLE cmp_pjt_tkt_cnfg_item_info_$solution_name$ (
	cnfg_pjt_nm 	VARCHAR(150) NOT NULL,		-- 설정을 사용하는 project 이름
	cnfg_key_nm 	VARCHAR(50)	NOT NULL,		-- 설정값 이름
  	rgstr_emp_no	VARCHAR(20),							-- 등록자 사번
  	modr_emp_no		VARCHAR(20),							-- 수정자 사번
  	rgst_ymdt 		TIMESTAMP,	-- 티켓 등록일
  	mod_ymdt 		TIMESTAMP,	-- 티켓 정보 수정일
	PRIMARY KEY(cnfg_pjt_nm, cnfg_key_nm)
);

CREATE TABLE cmp_tkt_cnfg_item_info_auth_$solution_name$ (
	cnfg_key_nm 	VARCHAR(50)	NOT NULL,		-- 설정값 이름
	role_nm			VARCHAR(32) NOT NULL,		-- role 이름
	acss_auth		CHAR(1)		NOT NULL,		-- 접근 권한
  	rgstr_emp_no	VARCHAR(20),							-- 등록자 사번
  	modr_emp_no		VARCHAR(20),							-- 수정자 사번
  	rgst_ymdt 		TIMESTAMP,	-- 티켓 등록일
  	mod_ymdt 		TIMESTAMP,	-- 티켓 정보 수정일
	PRIMARY KEY(cnfg_key_nm, role_nm)
);
