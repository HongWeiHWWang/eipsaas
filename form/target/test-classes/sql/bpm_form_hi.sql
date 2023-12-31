/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-12 09:48:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for form_definition_hi
-- ----------------------------
DROP TABLE IF EXISTS `form_definition_hi`;
CREATE TABLE `form_definition_hi` (
  `ID_` varchar(64) NOT NULL COMMENT 'ID',
  `FORM_ID_` varchar(64) NOT NULL COMMENT '对应表单ID',
  `NAME_` varchar(64) NOT NULL COMMENT '表单名称',
  `DESC_` varchar(255) DEFAULT NULL COMMENT '表单描述',
  `FORM_HTML_` text COMMENT '表单设计（HTML代码）',
  `CREATE_USER_ID_` varchar(64) DEFAULT NULL COMMENT '创建人ID',
  `CREATE_USER_NAME_` varchar(64) DEFAULT NULL COMMENT '创建人Name',
  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
  `FORM_KEY_` varchar(64) DEFAULT NULL,
  `VERSION_` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程表单HTML设计历史记录';
