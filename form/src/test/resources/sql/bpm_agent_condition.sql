/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-20 14:52:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bpm_agent_condition
-- ----------------------------
DROP TABLE IF EXISTS `bpm_agent_condition`;
CREATE TABLE `bpm_agent_condition` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `SETTING_ID_` varchar(64) DEFAULT NULL COMMENT '设定ID',
  `CONDITION_DESC_` varchar(128) DEFAULT NULL COMMENT '条件描述',
  `CONDITION_` varchar(4000) DEFAULT NULL COMMENT '条件',
  `AGENT_ID_` varchar(64) DEFAULT NULL COMMENT '代理人ID',
  `AGENT_NAME_` varchar(128) DEFAULT NULL COMMENT '代理人',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程代理条件';
