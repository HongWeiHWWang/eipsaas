/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_test

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-14 15:03:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for portal_sys_file
-- ----------------------------
DROP TABLE IF EXISTS `portal_sys_file`;
CREATE TABLE `portal_sys_file` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `XB_TYPE_ID_` varchar(64) DEFAULT NULL COMMENT '附件分类ID',
  `FILE_NAME_` varchar(128) NOT NULL,
  `FILE_TYPE_` varchar(40) DEFAULT NULL,
  `STORE_TYPE_` varchar(40) NOT NULL,
  `FILE_PATH_` varchar(255) DEFAULT NULL,
  `BYTES_` longblob,
  `BYTE_COUNT_` int(11) DEFAULT NULL,
  `EXT_` varchar(20) DEFAULT NULL,
  `NOTE_` varchar(255) DEFAULT NULL,
  `CREATOR_` varchar(64) DEFAULT NULL,
  `CREATOR_NAME_` varchar(64) DEFAULT NULL,
  `CREATE_TIME_` datetime NOT NULL,
  `IS_DEL_` char(1) NOT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
