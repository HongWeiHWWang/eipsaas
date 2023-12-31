/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-12 16:14:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for form_field
-- ----------------------------
DROP TABLE IF EXISTS `form_field`;
CREATE TABLE `form_field` (
  `ID_` varchar(20) NOT NULL COMMENT '主键',
  `NAME_` varchar(50) DEFAULT NULL COMMENT '字段名',
  `DESC_` varchar(100) DEFAULT NULL COMMENT '描述',
  `FORM_ID_` varchar(50) DEFAULT NULL COMMENT '表单元数据ID',
  `BO_DEF_ID_` varchar(50) DEFAULT NULL COMMENT 'BO定义ID',
  `ENT_ID_` varchar(255) DEFAULT NULL COMMENT '实体ID',
  `GROUP_ID_` varchar(50) DEFAULT NULL COMMENT '分组ID',
  `CALCULATION_` text COMMENT '计算表达式',
  `TYPE_` varchar(50) DEFAULT NULL COMMENT '数据类型',
  `BO_ATTR_ID_` varchar(50) DEFAULT NULL COMMENT 'BO属性定义',
  `CTRL_TYPE_` varchar(50) DEFAULT NULL COMMENT '控件类型',
  `VALID_RULE_` text COMMENT '验证规则',
  `OPTION_` text COMMENT '表单配置选项',
  `SN_` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`ID_`),
  KEY `idx_formfield_formid` (`FORM_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单字段定义';
