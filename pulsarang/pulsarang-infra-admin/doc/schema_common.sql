CREATE DATABASE me2config CHARACTER SET 'UTF8';
INSERT INTO db VALUES('%', 'me2config', 'me2dev', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
flush privileges;

--------------------------------------------------------------------
------------------------------- Admin ------------------------------
-------------------------------------------------------------------- 
CREATE TABLE cmp_adm (
	adm_id 			VARCHAR(32) NOT NULL,			-- admin_id
	adm_nm 			VARCHAR(30),
	adm_desc 		VARCHAR(100),
	rgstr_emp_no	VARCHAR(20),							-- 등록자 사번
  	modr_emp_no		VARCHAR(20),							-- 수정자 사번
  	rgst_ymdt 		TIMESTAMP,	-- 티켓 등록일
  	mod_ymdt 		TIMESTAMP,	-- 티켓 정보 수정일
  	PRIMARY KEY(adm_id)
);

CREATE TABLE cmp_tkt_adm (
	adm_id VARCHAR(32) 	NOT NULL,
	sln_nm VARCHAR(32) 	NOT NULL,			-- Solution 이름
	tkt_id VARCHAR(32) 	NOT NULL,			-- ticket 이름
	role_nm VARCHAR(10) NOT NULL,				-- admin role(super, svc, ...)
	rgstr_emp_no		VARCHAR(20),							-- 등록자 사번
  	modr_emp_no			VARCHAR(20),							-- 수정자 사번
  	rgst_ymdt 			TIMESTAMP,	-- 티켓 등록일
  	mod_ymdt 			TIMESTAMP,	-- 티켓 정보 수정일
	PRIMARY KEY(adm_id, sln_nm, tkt_id)
);

CREATE TABLE cmp_adm_user_id (
	adm_id     		VARCHAR(32)     NOT NULL,
	adm_user_id     VARCHAR(32)     NOT NULL,
	adm_user_dmn	VARCHAR(150)	NOT NULL DEFAULT 'naver.com',
	rgstr_emp_no	VARCHAR(20),							-- 등록자 사번
  	modr_emp_no		VARCHAR(20),							-- 수정자 사번
  	rgst_ymdt 		TIMESTAMP,	-- 티켓 등록일
  	mod_ymdt 		TIMESTAMP,	-- 티켓 정보 수정일
	PRIMARY KEY(adm_user_id, adm_user_dmn)
);

CREATE TABLE cmp_role (
	sln_nm 			VARCHAR(32) 	NOT NULL,	-- 컴포넌트 이름
	role_nm     	VARCHAR(32)     NOT NULL,	-- role 이름
	supr_role_nm	VARCHAR(32),				-- 상위 role 이름
	role_lv     	INT             NOT NULL,	-- role level
    role_desc    	VARCHAR(1000),				-- role 설명
    rgstr_emp_no	VARCHAR(20),							-- 등록자 사번
  	modr_emp_no		VARCHAR(20),							-- 수정자 사번
  	rgst_ymdt 		TIMESTAMP,	-- 티켓 등록일
  	mod_ymdt 		TIMESTAMP,	-- 티켓 정보 수정일
	PRIMARY KEY(sln_nm, role_nm)
);

--------------------------------------------------------------------
------------------------------ Server ------------------------------
--------------------------------------------------------------------
CREATE TABLE cmp_svr_info (
	sln_nm 			VARCHAR(32)	NOT NULL,			-- 컴포넌트 이름
	pjt_nm			VARCHAR(150) NOT NULL,			-- 프로젝트 이름
	svr_nm 			VARCHAR(32) NOT NULL,			-- 서버 이름
	svr_ip 			VARCHAR(15) NOT NULL,			-- 서버 IP
	svr_prtcl 		VARCHAR(10) NOT NULL,			-- 서버 protocol
	svr_port		INT NOT NULL,					-- 서버 port
	svr_cnfg_path	VARCHAR(300) NOT NULL,			-- protocol이 npc일 경우 모듈 이름, http일 경우 path
	rgstr_emp_no	VARCHAR(20),							-- 등록자 사번
  	modr_emp_no		VARCHAR(20),							-- 수정자 사번
  	rgst_ymdt 		TIMESTAMP,	-- 티켓 등록일
  	mod_ymdt 		TIMESTAMP,	-- 티켓 정보 수정일
  	rgst_stat_cd 	INT NOT NULL DEFAULT 1,				-- 티켓 상태 코드(중지, 읽기 전용, 정상)
	PRIMARY KEY(sln_nm, pjt_nm, svr_nm)
);

--------------------------------------------------------------------
----------------------------- Solution ----------------------------
--------------------------------------------------------------------
CREATE TABLE cmp_sln_info (
	sln_nm VARCHAR(32) NOT NULL,			-- 컴포넌트 이름
	pjt_nms VARCHAR(1000),					-- ','로 구분되는 컴포넌트를 구성하는 어플리케이션들 
	rgstr_emp_no		VARCHAR(20),							-- 등록자 사번
  	modr_emp_no			VARCHAR(20),							-- 수정자 사번
  	rgst_ymdt 			TIMESTAMP,	-- 티켓 등록일
  	mod_ymdt 			TIMESTAMP,	-- 티켓 정보 수정일
  	PRIMARY KEY(sln_nm)
);

--------------------------------------------------------------------
------------------------------- Lock -------------------------------
--------------------------------------------------------------------
CREATE TABLE cmp_lck_info (
	sln_nm 			VARCHAR(32)	NOT NULL,			-- 컴포넌트 이름
	lck_nm 			VARCHAR(150) NOT NULL,
	rgst_ymdt		TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,	-- 등록일
	PRIMARY KEY(sln_nm, lck_nm)
);

