/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-12 18:03:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bpm_selector_def
-- ----------------------------
DROP TABLE IF EXISTS `bpm_selector_def`;
CREATE TABLE `bpm_selector_def` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `NAME_` varchar(40) DEFAULT NULL COMMENT '名称',
  `ALIAS_` varchar(64) DEFAULT NULL COMMENT '别名',
  `GROUP_FIELD_` varchar(512) DEFAULT NULL COMMENT '组合字段',
  `BUTTONS_` varchar(512) DEFAULT NULL COMMENT '按钮定义',
  `IS_CUSTOM_` smallint(6) DEFAULT NULL COMMENT '系统预定义',
  `FLAG_` smallint(6) DEFAULT '0' COMMENT '标记是否系统默认',
  `METHOD_` varchar(64) DEFAULT NULL COMMENT '选择器对应的js方法名称',
  `CONF_KEY_` varchar(64) DEFAULT NULL COMMENT '已选数据参数的传递key',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='控件组合定义';
