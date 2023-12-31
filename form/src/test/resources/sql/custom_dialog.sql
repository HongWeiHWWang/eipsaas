/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-19 09:35:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for form_custom_dialog
-- ----------------------------
DROP TABLE IF EXISTS `form_custom_dialog`;
CREATE TABLE `form_custom_dialog` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `NAME_` varchar(64) NOT NULL COMMENT '名字',
  `ALIAS_` varchar(64) NOT NULL COMMENT '别名',
  `STYLE_` smallint(6) DEFAULT NULL COMMENT '显示样式：0-列表，1-树形',
  `OBJ_NAME_` varchar(64) NOT NULL COMMENT '对象名称，如果是表就是表名，视图则视图名',
  `NEED_PAGE_` smallint(6) DEFAULT NULL COMMENT '是否分页',
  `PAGE_SIZE_` int(11) DEFAULT NULL COMMENT '分页大小',
  `DISPLAYFIELD_` text COMMENT '显示字段',
  `CONDITIONFIELD_` text COMMENT '条件字段的json',
  `RESULTFIELD_` text COMMENT '返回字段json',
  `SORTFIELD_` varchar(200) DEFAULT NULL COMMENT '排序字段',
  `DSALIAS_` varchar(64) NOT NULL COMMENT '数据源的别名',
  `IS_TABLE_` smallint(6) NOT NULL COMMENT '是否数据库表0:视图,1:数据库表',
  `DIY_SQL_` varchar(510) DEFAULT NULL COMMENT '自定义SQL',
  `SQL_BUILD_TYPE_` smallint(6) DEFAULT NULL COMMENT 'SQL构建类型',
  `WIDTH_` int(11) DEFAULT NULL COMMENT '弹出框的宽度',
  `HEIGHT_` int(11) DEFAULT NULL COMMENT '弹出框的高度',
  `SELECT_NUM_` int(11) DEFAULT NULL COMMENT '是否单选 -1:多选',
  `SYSTEM_` smallint(6) DEFAULT '0' COMMENT '系统默认',
  `PARENT_CHECK_` smallint(6) DEFAULT NULL COMMENT '树多选父级级联',
  `CHILDREN_CHECK_` smallint(6) DEFAULT NULL COMMENT '树多选子级级联',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义对话框';
