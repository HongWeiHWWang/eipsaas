-- ----------------------------
-- Table structure for portal_sys_properties
-- ----------------------------
DROP TABLE IF EXISTS `portal_sys_properties`;
CREATE TABLE `portal_sys_properties` (
  `ID` varchar(64) NOT NULL COMMENT '主键',
  `NAME` varchar(64) DEFAULT NULL COMMENT '名称',
  `ALIAS` varchar(64) DEFAULT NULL COMMENT '别名',
  `GROUP_` varchar(64) DEFAULT NULL COMMENT '分组',
  `VALUE` varchar(200) DEFAULT NULL COMMENT '参数值',
  `ENCRYPT` int(11) DEFAULT NULL COMMENT '是否加密',
  `CREATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `DESCRIPTION` varchar(300) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置属性';


-- ----------------------------
-- Table structure for portal_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `portal_sys_menu`;
CREATE TABLE `portal_sys_menu` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `PARENT_ID_` varchar(64) DEFAULT NULL COMMENT '父id',
  `NAME_` varchar(128) DEFAULT NULL COMMENT '菜单名称',
  `ALIAS_` varchar(128) DEFAULT NULL COMMENT '别名用作state',
  `MENU_URL_` varchar(512) DEFAULT NULL,
  `TEMPLATE_URL_` varchar(512) DEFAULT NULL,
  `MENU_ICON_` varchar(64) DEFAULT NULL COMMENT '图标',
  `LOAD_` text COMMENT '$ocLazyLoad.load 方法的参数',
  `ENABLE_MENU_` int(11) DEFAULT NULL COMMENT '显示到菜单(1,显示,0 ,不显示)',
  `HAS_CHILDREN_` int(11) DEFAULT NULL COMMENT '是否有子节点 1 有  0 没有',
  `OPENED_` int(11) DEFAULT NULL COMMENT '默认展开    1展开 0 不展开',
  `SN_` varchar(64) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统菜单';

-- ----------------------------
-- Table structure for sys_method
-- ----------------------------
DROP TABLE IF EXISTS `sys_method`;
CREATE TABLE `sys_method` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `MENU_ALIAS_` varchar(128) DEFAULT NULL COMMENT '菜单资源别名',
  `ALIAS_` varchar(128) DEFAULT NULL COMMENT '别名',
  `NAME_` varchar(128) DEFAULT NULL COMMENT '请求方法名',
  `REQUEST_URL_` varchar(256) DEFAULT NULL COMMENT '请求地址',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统请求方法的配置 （用于角色权限配置）';

-- ----------------------------
-- Table structure for portal_sys_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `portal_sys_role_auth`;
CREATE TABLE `portal_sys_role_auth` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `ROLE_ALIAS_` varchar(128) DEFAULT NULL COMMENT '角色别名',
  `MENU_ALIAS_` varchar(128) DEFAULT NULL COMMENT '菜单别名',
  `METHOD_ALIAS_` varchar(128) DEFAULT NULL COMMENT '请求方法别名',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限配置';


-- ----------------------------
-- Table structure for sys_datasource
-- ----------------------------
DROP TABLE IF EXISTS `sys_datasource`;
CREATE TABLE `sys_datasource` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `NAME_` varchar(128) DEFAULT '名称',
  `ALIAS_` varchar(64) NOT NULL COMMENT '别名',
  `DB_TYPE_` varchar(64) DEFAULT NULL COMMENT '数据源id',
  `SETTING_JSON_` longtext COMMENT 'Json存储配置',
  `INIT_ON_START_` smallint(6) DEFAULT NULL COMMENT '在启动时，启动连接池，并添加到spring容器中管理。',
  `ENABLED_` smallint(6) DEFAULT NULL COMMENT '是否生效',
  `CLASS_PATH_` varchar(128) DEFAULT NULL,
  `INIT_METHOD_` varchar(200) DEFAULT '初始化方法，有些可以不填写',
  `CLOSE_METHOD_` varchar(150) DEFAULT '关闭数据源的时候应该调用的方法，可不填',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_datasource
-- ----------------------------

-- ----------------------------
-- Table structure for portal_sys_datasorce_def
-- ----------------------------
DROP TABLE IF EXISTS `portal_sys_datasorce_def`;
CREATE TABLE `portal_sys_datasorce_def` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `NAME_` varchar(64) DEFAULT NULL COMMENT '数据源名称',
  `CLASS_PATH_` varchar(100) DEFAULT NULL COMMENT '数据源全路径',
  `SETTING_JSON_` longtext COMMENT '属性配置',
  `INIT_METHOD_` varchar(64) DEFAULT NULL COMMENT '初始化方法，有些可以不填写',
  `IS_SYSTEM_` smallint(6) DEFAULT NULL COMMENT '是系统默认的',
  `CLOSE_METHOD_` varchar(150) DEFAULT NULL COMMENT '关闭数据源的时候应该调用的方法，可不填(classPath.methodName)',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of portal_sys_datasorce_def
-- ----------------------------
INSERT INTO `portal_sys_datasorce_def` VALUES ('1', 'DURID数据源', 'com.alibaba.druid.pool.DruidDataSource', '[{\"name\":\"username\",\"comment\":\"username\",\"type\":\"java.lang.String\",\"baseAttr\":\"1\",\"default\":\"root\"},{\"name\":\"password\",\"comment\":\"password\",\"type\":\"java.lang.String\",\"baseAttr\":\"1\",\"default\":\"root\"},{\"name\":\"initialSize\",\"comment\":\"initialSize\",\"type\":\"int\",\"baseAttr\":\"1\",\"default\":\"10\"},{\"name\":\"maxActive\",\"comment\":\"maxActive\",\"type\":\"int\",\"baseAttr\":\"1\",\"default\":\"100\"},{\"name\":\"minIdle\",\"comment\":\"minIdle\",\"type\":\"int\",\"baseAttr\":\"1\",\"default\":\"10\"},{\"name\":\"maxWait\",\"comment\":\"maxWait\",\"type\":\"long\",\"baseAttr\":\"1\",\"default\":\"60000\"},{\"name\":\"validationQuery\",\"comment\":\"validationQuery\",\"type\":\"java.lang.String\",\"baseAttr\":\"1\",\"default\":\"select * from ACT_GE_PROPERTY\"},{\"name\":\"testOnBorrow\",\"comment\":\"testOnBorrow\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"false\"},{\"name\":\"testOnReturn\",\"comment\":\"testOnReturn\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"false\"},{\"name\":\"testWhileIdle\",\"comment\":\"testWhileIdle\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"true\"},{\"name\":\"poolPreparedStatements\",\"comment\":\"poolPreparedStatements\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"true\"},{\"name\":\"maxPoolPreparedStatementPerConnectionSize\",\"comment\":\"maxPoolPreparedStatementPerConnectionSize\",\"type\":\"int\",\"baseAttr\":\"1\",\"default\":\"20\"},{\"name\":\"filters\",\"comment\":\"filters\",\"type\":\"java.util.List\",\"baseAttr\":\"1\",\"default\":\"stat\"},{\"name\":\"timeBetweenEvictionRunsMillis\",\"comment\":\"timeBetweenEvictionRunsMillis\",\"type\":\"long\",\"baseAttr\":\"1\",\"default\":\"60000\"},{\"name\":\"minEvictableIdleTimeMillis\",\"comment\":\"minEvictableIdleTimeMillis\",\"type\":\"long\",\"baseAttr\":\"1\",\"default\":\"300000\"},{\"name\":\"removeAbandoned\",\"comment\":\"removeAbandoned\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"true\"},{\"name\":\"removeAbandonedTimeoutMillis\",\"comment\":\"removeAbandonedTimeoutMillis\",\"type\":\"long\",\"baseAttr\":\"1\",\"default\":\"60000\"},{\"name\":\"logAbandoned\",\"comment\":\"logAbandoned\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"true\"},{\"isAdd\":true,\"name\":\"url\",\"comment\":\"url\",\"type\":\"java.lang.String\",\"baseAttr\":\"1\",\"default\":\"\"}]', null, '1', null);

-- ----------------------------
-- Table structure for `portal_sys_identity`
-- ----------------------------
DROP TABLE IF EXISTS `portal_sys_identity`;
CREATE TABLE `portal_sys_identity` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `NAME_` varchar(64) DEFAULT NULL COMMENT '名称',
  `ALIAS_` varchar(20) DEFAULT NULL COMMENT '别名',
  `REGULATION_` varchar(128) DEFAULT NULL COMMENT '规则',
  `GEN_TYPE_` smallint(6) DEFAULT NULL COMMENT '生成类型',
  `NO_LENGTH_` int(11) DEFAULT NULL COMMENT '流水号长度',
  `CUR_DATE_` varchar(20) DEFAULT NULL COMMENT '当前日期',
  `INIT_VALUE_` int(11) DEFAULT NULL COMMENT '初始值',
  `CUR_VALUE_` int(11) DEFAULT NULL COMMENT '当前值',
  `STEP_` smallint(6) DEFAULT NULL COMMENT '步长',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流水号定义';

-- ----------------------------
-- Table structure for `portal_sys_type_group`
-- ----------------------------
DROP TABLE IF EXISTS `portal_sys_type_group`;
CREATE TABLE `portal_sys_type_group` (
  `ID_` varchar(64) NOT NULL COMMENT '主键ID',
  `GROUP_KEY_` varchar(64) NOT NULL COMMENT '分类组业务主键',
  `NAME_` varchar(128) NOT NULL COMMENT '分类名',
  `FLAG_` int(11) DEFAULT NULL COMMENT '标识',
  `SN_` int(11) DEFAULT NULL COMMENT '序号',
  `TYPE_` smallint(6) DEFAULT NULL COMMENT '类别。0=平铺结构；1=树型结构',
  `CREATE_BY_` varchar(64) DEFAULT NULL COMMENT '创建人ID',
  `CREATE_TIME_` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `CREATE_ORG_ID_` varchar(64) DEFAULT NULL COMMENT '创建者所属组织ID',
  `UPDATE_BY_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
  `UPDATE_TIME_` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统分类组值表';

-- ----------------------------
-- Table structure for `sys_type`
-- ----------------------------
DROP TABLE IF EXISTS `sys_type`;
CREATE TABLE `sys_type` (
  `ID_` varchar(64) NOT NULL COMMENT '分类ID',
  `TYPE_GROUP_KEY_` varchar(64) NOT NULL COMMENT '所属分类组业务主键',
  `NAME_` varchar(128) NOT NULL COMMENT '分类名称',
  `TYPE_KEY_` varchar(64) NOT NULL COMMENT '节点的分类Key',
  `STRU_TYPE_` varchar(40) NOT NULL COMMENT 'flat 平行；tree 树形',
  `PARENT_ID_` varchar(64) DEFAULT NULL COMMENT '父节点',
  `DEPTH_` int(11) DEFAULT NULL COMMENT '层次',
  `PATH_` varchar(255) DEFAULT NULL COMMENT '路径',
  `IS_LEAF_` char(1) DEFAULT NULL COMMENT '是否叶子节点。Y=是；N=否',
  `OWNER_ID_` varchar(64) DEFAULT NULL,
  `SN_` int(11) NOT NULL COMMENT '序号',
  `CREATE_BY_` varchar(64) DEFAULT NULL COMMENT '创建人ID',
  `CREATE_TIME_` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `CREATE_ORG_ID_` varchar(64) DEFAULT NULL COMMENT '创建者所属组织ID',
  `UPDATE_BY_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
  `UPDATE_TIME_` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='总分类表。用于显示平级或树层次结构的分类，可以允许任何层次结构。';


-- ----------------------------
-- Table structure for `portal_sys_dic`
-- ----------------------------
DROP TABLE IF EXISTS `portal_sys_dic`;
CREATE TABLE `portal_sys_dic` (
  `ID_` varchar(64) NOT NULL COMMENT '主键',
  `TYPE_ID_` varchar(64) DEFAULT NULL COMMENT '类型ID',
  `KEY_` varchar(40) DEFAULT NULL COMMENT '字典值代码,在同一个字典中值不能重复',
  `NAME_` varchar(128) DEFAULT NULL COMMENT '字典值名称',
  `PARENT_ID_` varchar(64) DEFAULT NULL COMMENT '父ID',
  `SN_` int(11) DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典';
