CREATE TABLE portal_service_param
(
   ID_                  NATIONAL VARCHAR(64) NOT NULL COMMENT '主键',
   SET_ID_              NATIONAL VARCHAR(64) COMMENT '服务设置ID',
   NAME_                NATIONAL VARCHAR(64) COMMENT '参数名称',
   TYPE_                NATIONAL VARCHAR(40) COMMENT '参数类型',
   DESC_                NATIONAL VARCHAR(512) COMMENT '参数说明',
   PRIMARY KEY (ID_)
);

ALTER TABLE portal_service_param COMMENT '服务调用参数表';

CREATE TABLE portal_service_set
(
   ID_                  NATIONAL VARCHAR(64) NOT NULL COMMENT '主键',
   NAME_                VARCHAR(255) COMMENT '名称',
   ALIAS_               NATIONAL VARCHAR(64) COMMENT '别名',
   URL_                 NATIONAL VARCHAR(1024) COMMENT 'wsdl地址',
   ADDRESS_             NATIONAL VARCHAR(1024) COMMENT '接口调用地址',
   METHOD_NAME_         NATIONAL VARCHAR(200) COMMENT '调用的方法名称',
   NAMESPACE_           NATIONAL VARCHAR(512) COMMENT '名称空间',
   SOAP_ACTION_         NATIONAL CHAR(1) NOT NULL COMMENT '构建soap的模式',
   INPUT_SET_           LONGTEXT COMMENT '输入参数设定',
   PRIMARY KEY (ID_)
);

ALTER TABLE portal_service_set COMMENT '服务调用设置表';

ALTER TABLE portal_service_set ADD UNIQUE INDEX idx_service_set_alias (ALIAS_) ;