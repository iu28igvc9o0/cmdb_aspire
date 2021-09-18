

--- mysql
CREATE TABLE `alert_scan_index` (
`id`  int(11) NULL DEFAULT NULL ,
`scan_index`  int(11) NULL DEFAULT NULL COMMENT '扫描索引' ,
`update_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' 
);
DROP TABLE IF EXISTS `cmdb_instance`;
CREATE TABLE `cmdb_instance` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `instance_id` varchar(40) DEFAULT NULL COMMENT '资产编号',
  `device_name` varchar(100) DEFAULT NULL COMMENT '设备名称',
  `host_name` varchar(100) DEFAULT NULL COMMENT '主机名称',
  `device_class` varchar(200) DEFAULT NULL COMMENT '设备分类',
  `device_type` varchar(40) DEFAULT NULL COMMENT '设备类型',
  `device_model` varchar(200) DEFAULT NULL COMMENT '设备型号',
  `device_class_3` varchar(40) DEFAULT NULL COMMENT '设备三级分类',
  `device_mfrs` varchar(200) DEFAULT NULL COMMENT '制造厂商',
  `device_status` varchar(40) DEFAULT NULL COMMENT '设备状态',
  `device_os_type` varchar(200) DEFAULT NULL COMMENT '设备系统类型',
  `os_detail_version` varchar(1000) DEFAULT NULL COMMENT 'os详细版本',
  `ip` varchar(128) DEFAULT NULL COMMENT 'IP地址',
  `is_assigned` varchar(40) DEFAULT NULL COMMENT '是否已经分配',
  `idcType` varchar(40) DEFAULT NULL COMMENT '资源池名称',
  `project_name` varchar(40) DEFAULT NULL COMMENT '项目名称',
  `pod_name` varchar(40) DEFAULT NULL COMMENT 'POD名称',
  `roomId` varchar(40) DEFAULT NULL COMMENT '机房名称',
  `idc_cabinet` varchar(40) DEFAULT NULL COMMENT '机柜名称',
  `department1` varchar(200) DEFAULT NULL COMMENT '一级部门',
  `department2` varchar(200) DEFAULT NULL COMMENT '二级部门',
  `bizSystem` varchar(200) DEFAULT NULL COMMENT '业务系统',
  `is_ansible` varchar(40) DEFAULT NULL COMMENT '是否ansible管理',
  `is_pool` varchar(40) DEFAULT NULL COMMENT '是否资源池管理设备',
  `dept_operation` varchar(40) DEFAULT NULL COMMENT '维护部门',
  `ops` varchar(40) DEFAULT NULL COMMENT '维护人员',
  `is_delete` varchar(2) DEFAULT NULL DEFAULT '0' COMMENT '是否删除',
  `insert_person` varchar(40) DEFAULT NULL COMMENT '录入人',
  `insert_time` varchar(40) DEFAULT NULL COMMENT '录入时间',
  `update_person` varchar(40) DEFAULT NULL COMMENT '最后更新人',
  `update_time` varchar(40) DEFAULT NULL COMMENT '最后更新时间',
  `ip_mask` varchar(40) DEFAULT NULL COMMENT '管理地址掩码',
  `ip_gateway` varchar(40) DEFAULT NULL COMMENT '管理地址网关',
  `device_description` varchar(200) DEFAULT NULL COMMENT '设备描述',
  `suyan_uuid` varchar(40) DEFAULT NULL COMMENT '苏研设备id',
   `node_type` varchar(40) DEFAULT NULL COMMENT '节点类型',
  PRIMARY KEY (`id`),
  key suyanUuid (suyan_uuid),
  key idx_ip (ip)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='cmdb资产信息表';
-- oracle
create table ALERT_SCAN_INDEX
(
  id          NUMBER not null,
  scan_index  NUMBER not null,
  update_time TIMESTAMP(6)
);

create table CMDB_INSTANCE
(
  device_name         VARCHAR2(100),
  device_class        VARCHAR2(200),
  device_type         VARCHAR2(40),
  device_class_3      VARCHAR2(40),
  device_mfrs         VARCHAR2(40),
  device_status       VARCHAR2(40),
  device_os_type      VARCHAR2(40),
  os_detail_version   VARCHAR2(100),
  ip                  VARCHAR2(40),
  ip_mask             VARCHAR2(40),
  ip_gateway          VARCHAR2(40),
  is_assigned         VARCHAR2(40),
  idctype             VARCHAR2(40),
  project_name        VARCHAR2(40),
  pod_name            VARCHAR2(40),
  roomid              VARCHAR2(40),
  idc_cabinet         VARCHAR2(40),
  department1         VARCHAR2(40),
  department2         VARCHAR2(40),
  bizsystem           VARCHAR2(40),
  is_ansible          VARCHAR2(40),
  is_pool             VARCHAR2(40),
  dept_operation      VARCHAR2(40),
  ops                 VARCHAR2(40),
  is_delete           VARCHAR2(2),
  insert_person       VARCHAR2(40),
  insert_time         VARCHAR2(40),
  update_person       VARCHAR2(40),
  update_time         VARCHAR2(40),
  device_model        VARCHAR2(40),
  device_sn           VARCHAR2(40),
  device_description  VARCHAR2(200),
  host_name           VARCHAR2(100),
  bizmodule           VARCHAR2(40),
  instance_id         VARCHAR2(40),
  suyan_uuid          VARCHAR2(40),
  node_type           VARCHAR2(40)
)
tablespace TS_ZABBIX_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 8K
    minextents 1
    maxextents unlimited
  );

  create index suyanUuid on CMDB_INSTANCE (suyan_uuid);
