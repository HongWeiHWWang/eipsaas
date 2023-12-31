/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.211_3306
Source Server Version : 50720
Source Host           : 192.168.1.211:3306
Source Database       : x5.6_portal(wangpeng)

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-21 17:18:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for portal_sys_file_classify
-- ----------------------------
DROP TABLE IF EXISTS `portal_sys_file_classify`;
CREATE TABLE `portal_sys_file_classify` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parentId` varchar(255) DEFAULT NULL,
  `orderNo` decimal(10,0) DEFAULT NULL,
  `createBy` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
