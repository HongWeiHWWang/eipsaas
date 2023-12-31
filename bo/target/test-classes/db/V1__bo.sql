DROP TABLE IF EXISTS `form_bo_attr`;
CREATE TABLE `form_bo_attr` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `NAME_` varchar(64) NOT NULL COMMENT '属性名称',
  `DESC_` varchar(255) DEFAULT NULL COMMENT '属性描述',
  `ENT_ID_` varchar(64) DEFAULT NULL COMMENT '实体ID',
  `DATA_TYPE_` varchar(40) DEFAULT NULL COMMENT '数据类型。string=字符串；number=数值；datetime=日期（长日期，通过显示格式来限制）',
  `DEFAULT_VALUE_` varchar(1024) DEFAULT NULL COMMENT '基本默认值',
  `FORMAT_` varchar(255) DEFAULT NULL COMMENT '基本类型显示格式',
  `IS_REQUIRED_` int(11) NOT NULL COMMENT '是否必填',
  `ATTR_LENGTH_` int(11) DEFAULT NULL COMMENT '属性长度',
  `DECIMAL_LEN_` int(11) DEFAULT NULL COMMENT '浮点长度',
  `FIELD_NAME_` varchar(50) DEFAULT NULL COMMENT '字段名',
  `SN_` smallint(6) DEFAULT NULL,
  `STATUS_` varchar(20) DEFAULT NULL COMMENT '状态 show：显示，hide：隐藏',
  `INDEX_` int(11) DEFAULT NULL COMMENT '排序',
  `tenant_id_` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for form_bo_data_relation
-- ----------------------------
DROP TABLE IF EXISTS `form_bo_data_relation`;
CREATE TABLE `form_bo_data_relation` (
  `ID_` varchar(50) NOT NULL COMMENT '主键',
  `PK_` varchar(50) DEFAULT NULL COMMENT '主表PK数据',
  `FK_` varchar(50) DEFAULT NULL COMMENT '外键ID数据',
  `PK_NUM_` decimal(20,0) DEFAULT NULL COMMENT '主键值',
  `FK_NUM_` decimal(20,0) DEFAULT NULL COMMENT '外键数字',
  `SUB_BO_NAME` varchar(50) DEFAULT NULL COMMENT '子实体名称',
  `tenant_id_` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for form_bo_def
-- ----------------------------
DROP TABLE IF EXISTS `form_bo_def`;
CREATE TABLE `form_bo_def` (
  `ID_` varchar(50) NOT NULL COMMENT '主键ID',
  `CATEGORY_ID_` varchar(50) DEFAULT NULL COMMENT '分类ID',
  `CATEGORY_NAME_` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `ALIAS_` varchar(50) DEFAULT NULL COMMENT '别名',
  `DESCRIPTION_` varchar(100) DEFAULT NULL COMMENT '表单定义描述',
  `SUPPORT_DB_` smallint(6) DEFAULT NULL COMMENT 'BO支持数据库，相关的BO实体生成物理表',
  `DEPLOYED_` smallint(6) DEFAULT NULL COMMENT '是否发布',
  `STATUS_` varchar(20) DEFAULT NULL COMMENT 'BO状态(normal,正常,forbidden 禁用)',
  `CREATE_BY_` varchar(50) DEFAULT NULL COMMENT '创建人ID',
  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_ORG_ID_` varchar(64) DEFAULT NULL COMMENT '创建人所属组织ID',
  `REV_` int(11) NOT NULL DEFAULT '1' COMMENT '关联锁',
  `UPDATE_BY_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
  `UPDATE_TIME_` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id_` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`ID_`),
  UNIQUE KEY `idx_form_bo_def_alias` (`ALIAS_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for form_bo_ent
-- ----------------------------
DROP TABLE IF EXISTS `form_bo_ent`;
CREATE TABLE `form_bo_ent` (
  `ID_` varchar(64) NOT NULL COMMENT '对象定义ID',
  `PACKAGE_ID_` varchar(50) DEFAULT NULL COMMENT '分类ID',
  `NAME_` varchar(64) NOT NULL COMMENT '对象名称(需要唯一约束)',
  `DESC_` varchar(255) DEFAULT NULL COMMENT '对象描述',
  `STATUS_` varchar(40) NOT NULL COMMENT '状态。inactive=未激活；actived=激活；forbidden=禁用',
  `IS_CREATE_TABLE_` smallint(6) NOT NULL COMMENT '是否生成表',
  `TABLE_NAME_` varchar(50) DEFAULT NULL COMMENT '表名',
  `DS_NAME_` varchar(50) DEFAULT NULL COMMENT '数据源名称',
  `PK_TYPE_` varchar(20) DEFAULT NULL COMMENT '主键类型',
  `IS_EXTERNAL_` smallint(6) DEFAULT NULL COMMENT '是否外部表',
  `PK_` varchar(50) DEFAULT NULL COMMENT '主键字段名称',
  `FK_` varchar(50) DEFAULT NULL COMMENT '外键字段名称',
  `tenant_id_` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for form_bo_ent_relation
-- ----------------------------
DROP TABLE IF EXISTS `form_bo_ent_relation`;
CREATE TABLE `form_bo_ent_relation` (
  `ID_` varchar(50) NOT NULL COMMENT '主键ID',
  `BO_DEFID_` varchar(50) DEFAULT NULL COMMENT 'BO定义ID',
  `PARENT_ID_` varchar(50) DEFAULT NULL COMMENT '上级ID',
  `REF_ENT_ID_` varchar(64) DEFAULT NULL COMMENT '关联实体ID',
  `TYPE_` varchar(50) DEFAULT NULL COMMENT '类型 (主表,main, onetoone,onetomany,manytomany)',
  `tenant_id_` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`ID_`),
  KEY `idx_ent_relation_def_id` (`BO_DEFID_`),
  KEY `idx_ent_relation_ref_ent_id_` (`REF_ENT_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for form_bo_int
-- ----------------------------
DROP TABLE IF EXISTS `form_bo_int`;
CREATE TABLE `form_bo_int` (
  `ID_` varchar(64) NOT NULL COMMENT '业务实例ID',
  `DEF_ID_` varchar(64) NOT NULL COMMENT '对象定义ID',
  `INST_DATA_` text COMMENT '实例数据',
  `CREATE_TIME_` datetime NOT NULL COMMENT '创建时间',
  `tenant_id_` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for form_bo_relation
-- ----------------------------
DROP TABLE IF EXISTS `form_bo_relation`;
CREATE TABLE `form_bo_relation` (
  `ID_` varchar(50) NOT NULL COMMENT '主键',
  `FORM_ID_` varchar(50) DEFAULT NULL COMMENT '表单ID',
  `BO_DEF_ID_` varchar(50) DEFAULT NULL COMMENT 'BO定义ID',
  `tenant_id_` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
