/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-07 14:22:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for form_bo_relation
-- ----------------------------
DROP TABLE IF EXISTS `form_bo_relation`;
CREATE TABLE `form_bo_relation` (
  `ID_` varchar(50) NOT NULL COMMENT '主键',
  `FORM_ID_` varchar(50) DEFAULT NULL COMMENT '表单ID',
  `BO_DEF_ID_` varchar(50) DEFAULT NULL COMMENT 'BO定义ID',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单和BO的关联';
