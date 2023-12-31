/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-12 11:40:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for form_template
-- ----------------------------
DROP TABLE IF EXISTS `form_template`;
CREATE TABLE `form_template` (
  `TEMPLATE_ID_` varchar(64) NOT NULL COMMENT '模板id',
  `TEMPLATE_NAME_` varchar(200) DEFAULT NULL COMMENT '模板名称',
  `TEMPLATE_TYPE_` varchar(20) DEFAULT NULL COMMENT '模板类型',
  `MACROTEMPLATE_ALIAS_` varchar(50) DEFAULT NULL COMMENT '模板所向',
  `HTML_` text COMMENT '模板内容',
  `TEMPLATE_DESC_` varchar(400) DEFAULT NULL COMMENT '模板描述',
  `CANEDIT_` int(11) DEFAULT NULL COMMENT '是否可以编辑',
  `ALIAS_` varchar(50) DEFAULT NULL COMMENT '别名',
  PRIMARY KEY (`TEMPLATE_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单模版';
