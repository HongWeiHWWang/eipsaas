DROP TABLE IF EXISTS `ex_student`;
CREATE TABLE `ex_student` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `NAME_` varchar(255) DEFAULT NULL COMMENT '名称',
  `BIRTHDAY_` timestamp NULL DEFAULT NULL COMMENT '出生日期',
  `SEX_` smallint(6) DEFAULT NULL COMMENT '性别',
  `DESC_` varchar(512) DEFAULT NULL COMMENT '说明',
  `tenant_id_` bigint(12) DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生表';