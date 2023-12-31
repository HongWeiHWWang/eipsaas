/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-12 18:15:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for form_combinate_dialog
-- ----------------------------
DROP TABLE IF EXISTS `form_combinate_dialog`;
CREATE TABLE `form_combinate_dialog` (
  `ID_` varchar(50) NOT NULL COMMENT '主键',
  `NAME_` varchar(100) DEFAULT NULL COMMENT '名称',
  `ALIAS_` varchar(50) DEFAULT NULL COMMENT '别名',
  `WIDTH_` int(11) DEFAULT NULL COMMENT '宽度',
  `HEIGHT_` int(11) DEFAULT NULL COMMENT '高度',
  `TREE_DIALOG_ID_` varchar(50) DEFAULT NULL COMMENT '树形对话框ID',
  `TREE_DIALOG_NAME_` varchar(100) DEFAULT NULL COMMENT '树形对话框名称',
  `LIST_DIALOG_ID_` varchar(50) DEFAULT NULL COMMENT '列表对话框ID',
  `LIST_DIALOG_NAME_` varchar(100) DEFAULT NULL,
  `FIELD_` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组合对话框';
