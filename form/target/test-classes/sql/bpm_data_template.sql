/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-12 17:05:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for form_data_template
-- ----------------------------
DROP TABLE IF EXISTS `form_data_template`;
CREATE TABLE `form_data_template` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `BO_DEF_ID_` varchar(64) DEFAULT NULL COMMENT '业务对象定义id',
  `BO_DEF_ALIAS_` varchar(128) DEFAULT NULL COMMENT '业务对象定义别名',
  `FORM_KEY_` varchar(126) DEFAULT NULL COMMENT '自定义表单key',
  `NAME_` varchar(128) DEFAULT NULL COMMENT '名称',
  `ALIAS_` varchar(128) DEFAULT NULL COMMENT '别名',
  `STYLE_` smallint(6) DEFAULT NULL COMMENT '样式',
  `NEED_PAGE_` smallint(6) DEFAULT '1' COMMENT '是否需要分页 0不分页  1分页',
  `PAGE_SIZE_` smallint(6) DEFAULT NULL COMMENT '分页大小',
  `TEMPLATE_ALIAS_` varchar(128) DEFAULT NULL COMMENT '数据模板别名',
  `TEMPLATE_HTML_` text COMMENT '数据模板代码',
  `DISPLAY_FIELD_` text COMMENT '显示字段',
  `SORT_FIELD_` text COMMENT '排序字段',
  `CONDITION_FIELD_` text COMMENT '条件字段',
  `MANAGE_FIELD_` varchar(2000) DEFAULT NULL COMMENT '管理字段',
  `FILTER_FIELD_` text COMMENT '过滤条件',
  `VAR_FIELD_` varchar(200) DEFAULT NULL COMMENT '变量字段',
  `FILTER_TYPE_` smallint(6) DEFAULT NULL COMMENT '过滤类型（1.建立条件,2.脚本条件）',
  `SOURCE_` smallint(6) DEFAULT NULL COMMENT '数据来源',
  `DEF_ID_` varchar(64) DEFAULT NULL COMMENT '流程定义ID',
  `IS_QUERY_` smallint(6) DEFAULT '1' COMMENT '是否查询',
  `IS_FILTER_` smallint(6) DEFAULT '1' COMMENT '是否过滤',
  `EXPORT_FIELD_` text COMMENT '导出字段',
  `PRINT_FIELD_` text COMMENT '打印字段',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务数据模板';
