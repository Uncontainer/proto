CREATE TABLE project (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `portPrefix` int(11) NOT NULL,
  `projectName` varchar(255) DEFAULT NULL,
  `solutionName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8


CREATE TABLE server (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `svr_ip` varchar(15) DEFAULT NULL,
  `lst_shtdn_ymdt` datetime DEFAULT NULL,
  `lst_strt_ymdt` datetime DEFAULT NULL,
  `svr_nm` varchar(50) DEFAULT NULL,
  `svr_prt` int(11) DEFAULT NULL,
  `pjt_nm` varchar(20) DEFAULT NULL,
  `reg_stat_cd` varchar(255) DEFAULT NULL,
  `runng_stat_cd` varchar(255) DEFAULT NULL,
  `sln_nm` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8