-- ----------------------------
-- Table structure for portal_sys_joblog
-- ----------------------------
DROP TABLE IF EXISTS `portal_sys_joblog`;
CREATE TABLE `portal_sys_joblog` (
  `ID_` varchar(50) NOT NULL COMMENT '主键',
  `JOB_NAME_` varchar(100) DEFAULT NULL COMMENT '任务名称',
  `TRIG_NAME_` varchar(100) DEFAULT NULL COMMENT '计划名称',
  `START_TIME_` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `END_TIME_` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '结束时间',
  `CONTENT_` longtext COMMENT '日志内容',
  `STATE_` int(11) DEFAULT NULL COMMENT '处理状态',
  `RUN_TIME_` int(11) DEFAULT NULL COMMENT '运行时长',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时器日志';
