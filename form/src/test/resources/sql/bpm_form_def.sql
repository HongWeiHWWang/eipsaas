/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-07 14:20:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for form_meta
-- ----------------------------
DROP TABLE IF EXISTS `form_meta`;
CREATE TABLE `form_meta` (
  `ID_` varchar(50) NOT NULL COMMENT '主键',
  `KEY_` varchar(50) DEFAULT NULL COMMENT '表单key值',
  `NAME_` varchar(50) DEFAULT NULL COMMENT '定义名称',
  `TYPE_` varchar(50) DEFAULT NULL COMMENT '分类名称',
  `TYPE_ID_` varchar(50) DEFAULT NULL COMMENT '分类',
  `EXPAND_` text COMMENT '扩展字段',
  `OPINION_CONF_` varchar(1000) DEFAULT NULL COMMENT '意见配置',
  `CREATE_BY_` varchar(50) DEFAULT NULL COMMENT '创建人ID',
  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY_` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `UPDATE_TIME_` datetime DEFAULT NULL COMMENT '更新时间',
  `CREATE_ORG_ID_` varchar(255) DEFAULT NULL COMMENT '创建组织',
  `DESC_` varchar(200) DEFAULT NULL COMMENT '描述',
  `GANGED_` text COMMENT '联动设置',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单元数据定义';
