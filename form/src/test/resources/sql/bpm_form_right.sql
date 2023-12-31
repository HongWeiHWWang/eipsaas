/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-12 10:20:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for form_right
-- ----------------------------
DROP TABLE IF EXISTS `form_right`;
CREATE TABLE `form_right` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `FORM_KEY_` varchar(64) DEFAULT NULL COMMENT '表单KEY',
  `FLOW_KEY_` varchar(64) DEFAULT NULL COMMENT '流程定义KEY',
  `NODE_ID_` varchar(60) DEFAULT NULL COMMENT '节点ID',
  `PARENT_FLOW_KEY_` varchar(64) DEFAULT NULL COMMENT '父流程key',
  `PERMISSION_` text COMMENT '权限',
  `PERMISSION_TYPE_` int(11) DEFAULT NULL COMMENT '权限类型 1 流程权限，2 实例权限',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单权限';
