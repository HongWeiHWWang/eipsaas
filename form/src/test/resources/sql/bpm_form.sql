/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-07 15:28:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for form_definition
-- ----------------------------
DROP TABLE IF EXISTS `form_definition`;
CREATE TABLE `form_definition` (
  `ID_` varchar(50) NOT NULL COMMENT '主键',
  `DEF_ID_` varchar(50) DEFAULT NULL COMMENT '表单元数据定义ID',
  `NAME_` varchar(200) DEFAULT NULL COMMENT '表单名称',
  `FORM_KEY_` varchar(50) DEFAULT NULL COMMENT '表单key',
  `DESC_` varchar(200) DEFAULT NULL COMMENT '描述',
  `FORM_HTML_` text COMMENT '表单定义HTML',
  `STATUS_` varchar(20) DEFAULT NULL COMMENT '状态 draft=草稿；deploy=发布',
  `FORM_TYPE_` varchar(20) DEFAULT NULL COMMENT '表单类型 分为 pc,mobile',
  `TYPE_ID_` varchar(50) DEFAULT NULL COMMENT '所属分类ID',
  `TYPE_NAME_` varchar(100) DEFAULT NULL COMMENT '分类名称',
  `IS_MAIN_` char(1) DEFAULT NULL COMMENT '是否主版本',
  `VERSION_` int(11) DEFAULT NULL COMMENT '版本号',
  `CREATE_BY_` varchar(50) DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_ORG_ID_` varchar(50) DEFAULT NULL COMMENT '创建组织ID',
  `UPDATE_BY_` varchar(50) DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME_` datetime DEFAULT NULL COMMENT '更新时间',
  `FORM_TAB_TITLE_` varchar(200) DEFAULT NULL COMMENT '表单tab标题',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单定义';
