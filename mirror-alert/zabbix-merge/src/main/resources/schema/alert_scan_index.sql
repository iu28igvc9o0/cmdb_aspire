SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for suyan_monitor_config
-- ----------------------------
DROP TABLE IF EXISTS `suyan_monitor_config`;
CREATE TABLE `suyan_monitor_config` (
  `name_` varchar(64) DEFAULT NULL COMMENT '监控项名称',
  `sy_key_` varchar(4000) DEFAULT NULL COMMENT '苏研监控项key',
  `key_` varchar(64) DEFAULT NULL COMMENT '监控项key',
  `uint_` varchar(12) DEFAULT NULL COMMENT '单位',
  `index_` varchar(12) DEFAULT NULL COMMENT '索引：history/history_uint',
  `sy_uint_` varchar(12) DEFAULT NULL COMMENT '苏研监控项单位',
  `operator_` varchar(12) DEFAULT NULL COMMENT '运算符：*、/',
  `operator_value_` varchar(64) DEFAULT '' COMMENT '运算值',
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `hasNode` smallint(1) DEFAULT '0' COMMENT '0没有1有',
  `deviceType` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of suyan_monitor_config
-- ----------------------------
INSERT INTO `suyan_monitor_config` VALUES ('CPU利用率', 'vm_realtime_cpu_avg_util_percent,bm_realtime_agg_cpu_percent_percent_avg,pm_realtime_cpu_avg_util_percent_percent_avg', 'custom.cpu.avg5.pused', null, null, '%', null, '', '13', '0', 'KVM云主机');
INSERT INTO `suyan_monitor_config` VALUES ('内存利用率', 'vm_realtime_mem_avg_util_percent,bm_realtime_mem_percent_usedWOBuffersCaches,pm_realtime_mem_percent_usedWOBuffersCaches', 'custom.memory.pused', null, null, '%', null, '', '14', '0', 'KVM云主机');
INSERT INTO `suyan_monitor_config` VALUES ('单个分区使用率', 'vm_realtime_disk_percent', 'vfs.fs.size', null, null, '%', null, '', '15', '1', 'KVM云主机');
INSERT INTO `suyan_monitor_config` VALUES ('单块磁盘读速率', 'vm_realtime_disk_device_bytes_read', '', null, null, 'B/s', null, '', '16', '1', 'KVM云主机');
INSERT INTO `suyan_monitor_config` VALUES ('单块网卡入口速率', 'vm_realtime_network_incoming_bytes_rate', 'net.if.in', null, null, 'B/s', null, '', '17', '1', 'KVM云主机');
INSERT INTO `suyan_monitor_config` VALUES ('单块磁盘写速率', 'vm_realtime_disk_device_bytes_write', '', null, null, 'B/s', null, '', '18', '1', 'KVM云主机');
INSERT INTO `suyan_monitor_config` VALUES ('单块网卡出口速率', 'vm_realtime_network_outgoing_bytes_rate', 'net.if.out', null, null, 'B/s', null, '', '19', '1', 'KVM云主机');
INSERT INTO `suyan_monitor_config` VALUES ('CPU使用情况', 'vmware_vm_cpu_usage_average', 'cpu.usage_average', null, null, '%', null, '', '20', '0', 'VMware虚拟机');
INSERT INTO `suyan_monitor_config` VALUES ('磁盘读取速度', 'vmware_vm_disk_read', 'disk.read_average', null, null, 'KB/S', null, '', '21', '0', 'VMware虚拟机');
INSERT INTO `suyan_monitor_config` VALUES ('磁盘写入速度', 'vmware_vm_disk_write', 'disk.write_average', null, null, 'KB/S', null, '', '22', '0', 'VMware虚拟机');
INSERT INTO `suyan_monitor_config` VALUES ('内存使用平均值', 'vmware_vm_men_usage_average', 'mem.usage_average', null, null, '%', null, '', '23', '0', 'VMware虚拟机');
INSERT INTO `suyan_monitor_config` VALUES ('数据传输速度', 'vmware_vm_net_transmitted', 'net.usage_average', null, null, 'KB/S', null, '', '24', '0', 'VMware虚拟机');
INSERT INTO `suyan_monitor_config` VALUES ('单块磁盘读速率', 'bm_realtime_disk_read_rate,pm_realtime_disk_octets_read', '', null, null, 'B/s', null, '', '25', '1', '裸金属');
INSERT INTO `suyan_monitor_config` VALUES ('单块磁盘写速率', 'bm_realtime_disk_write_rate,pm_realtime_disk_octets_write', null, null, null, 'B/s', null, '', '26', '1', '裸金属');
INSERT INTO `suyan_monitor_config` VALUES ('单块磁盘的读iops', 'bm_realtime_disk_io_read_rate,pm_realtime_disk_ops_read', null, null, null, 'request/s', null, '', '27', '1', '裸金属');
INSERT INTO `suyan_monitor_config` VALUES ('单块磁盘的写iops', 'bm_realtime_disk_io_write_rate,pm_realtime_disk_ops_write', null, null, null, 'request/s', null, '', '28', '1', '裸金属');
INSERT INTO `suyan_monitor_config` VALUES ('接收字节数', 'bm_realtime_interface_rxBytes,pm_realtime_interface_rxBytes', 'net.if.in', null, null, 'B', null, '', '29', '1', '裸金属');
INSERT INTO `suyan_monitor_config` VALUES ('发送字节数', 'bm_realtime_interface_txBytes,pm_realtime_interface_txBytes', 'net.if.out', null, null, 'B', null, '', '30', '1', '裸金属');
INSERT INTO `suyan_monitor_config` VALUES ('CPU使用情况平均值', 'vmware_pm_cpu_usage_average', 'cpu.capacity_usagepct_average', null, null, '%', null, '', '31', '0', 'VMware宿主机');
INSERT INTO `suyan_monitor_config` VALUES ('内存使用情况平均值', 'vmware_pm_mem_usage_average', 'mem.host_usagePct', null, null, '%', null, '', '32', '0', 'VMware宿主机');
INSERT INTO `suyan_monitor_config` VALUES ('读取速度', 'vmware_pm_disk_read_average', 'datastore.read_average', null, null, 'KBps', null, '', '33', '0', 'VMware宿主机');
INSERT INTO `suyan_monitor_config` VALUES ('写入速度', 'vmware_pm_disk_write_average', 'datastore.write_average', null, null, 'KBps', null, '', '34', '0', 'VMware宿主机');
INSERT INTO `suyan_monitor_config` VALUES ('数据接收速度', 'vmware_pm_net_received_average', 'net.received_average', null, null, ' KBps', null, '', '35', '0', 'VMware宿主机');
INSERT INTO `suyan_monitor_config` VALUES ('数据传输速度', 'vmware_pm_net_transmitted_average', 'net.usage_average', null, null, ' KBps', null, '', '36', '0', 'VMware宿主机');

ALTER TABLE cmdb_instance ADD INDEX suyanUuid (suyan_uuid);

-- ----------------------------
-- Table structure for kpi_key_to_metricname
-- ----------------------------
DROP TABLE IF EXISTS `kpi_key_to_metricname`;
CREATE TABLE `kpi_key_to_metricname` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zabbix_key` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'zabbix指标',
  `zabbix_desc` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `device_type` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '设备类型',
  `metric_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `metric_desc` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `operation` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '操作，* : 乘以；/:除以',
  `multiple` bigint(20) DEFAULT NULL COMMENT '倍数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='zabbix监控项指标到bomc标准指标名称映射表';

-- ----------------------------
-- Records of kpi_key_to_metricname
-- ----------------------------
INSERT INTO `kpi_key_to_metricname` VALUES ('1', 'custom.cpu.avg5.pused', null, null, 'pm_realtime_cpu_avg_util_percent_percent_avg', 'CPU使用率', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('2', 'cpu.capacity_usagepct_average', null, null, 'vmware_pm_cpu_usage_average', 'CPU使用率', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('3', 'custom.memory.pused', null, null, 'pm_realtime_mem_percent_usedWOBuffersCaches', '内存使用率', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('4', 'mem.host_usagePct', null, null, 'vmware_pm_mem_usage_average', '内存使用率', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('5', 'vm.memory.size[total]', null, null, 'pm_realtime_mem_mem_total', '总内存（G）', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('6', 'hardware.memorySize', null, null, 'bm_realtime_mem_total_size', '总内存（G）', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('7', 'mem.workload', null, null, 'bm_realtime_mem_used_size', '已用内存（G）', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('8', 'net.if.in', null, null, 'vmware_pm_net_received_average', '网络端口接收速率', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('9', 'net.if.out', null, null, 'vmware_pm_net_transmitted_average', '网络端口发送速率', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('10', 'vfs.dev.read[,ops,avg5]', null, null, 'pm_realtime_disk_octets_read', '磁盘读速率', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('11', 'datastore.write_average', null, null, 'vmware_pm_disk_write_average', '磁盘写速率', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('12', 'storage.totalReadLatency_average', null, null, 'vmware_pm_realtime_disk_io_read_rate', '磁盘的读iops', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('13', 'storage.totalWriteLatency_average', null, null, 'vmware_pm_realtime_disk_io_write_rate', '磁盘的写iops', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('14', 'net.received_average', null, null, 'bm_realtime_interface_rxBytes', '接收字节数', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('15', '网口{#IFNAME}接收速率', null, null, 'pm_realtime_interface_rxBytes', '接收字节数', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('16', '网口{#IFNAME}发送速率', null, null, 'pm_realtime_interface_txBytes', '发送字节数', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('17', 'cpu.workload', null, null, 'vmware_vm_cpu_usage_average', 'CPU使用率（%）', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('18', 'mem-host.workload', null, null, 'vmware_vm_men_usage_average', '内存使用率（%）', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('19', 'mem.usage_average', null, null, 'mem.usage_average', '已用内存（G）', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('20', 'config.hardware.memoryKB', null, null, 'config.hardware.memoryKB', '总内存（G）', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('21', 'disk.read_average', null, null, 'vmware_vm_disk_read', '磁盘读速率（KBS）', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('22', 'disk.write_average', null, null, 'vmware_vm_disk_write', '磁盘写速率（KBS）', null, null);
INSERT INTO `kpi_key_to_metricname` VALUES ('26', 'config.hardware.diskSpace', null, null, 'vm_realtime_disk_total_size', '磁盘空间总大小（G）', null, null);

CREATE TABLE `hw_sync_log` (
  `from_time` VARCHAR(32) DEFAULT NULL  COMMENT '统计开始时间',
  `to_time` VARCHAR(32) DEFAULT NULL  COMMENT '统计结束时间',
  `exec_time` DATETIME NOT NULL COMMENT '执行时间',
  `exec_duration` VARCHAR(64) DEFAULT NULL COMMENT '执行时间',
  `status` VARCHAR(32) DEFAULT NULL COMMENT '状态，success，error',
  `content` VARCHAR(4000) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `status_code` INT(11) DEFAULT NULL COMMENT '返回码',
  `config_type` VARCHAR(64) DEFAULT NULL COMMENT '操作',
  `url` VARCHAR(255) DEFAULT NULL COMMENT 'url',
  `tag_type` INT(11) DEFAULT NULL COMMENT '环境',
  KEY `idx_c_to` (`status`,`tag_type`,`config_type`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `instance_hw` (
 `id` VARCHAR(200)  NOT NULL ,
 `extraSpecs` VARCHAR(2000)  DEFAULT NULL ,
 `class_Name` VARCHAR(200)  DEFAULT NULL ,
 `idcType` VARCHAR(32)  DEFAULT NULL ,
 `createdAt` VARCHAR(200)  DEFAULT NULL ,
 `privateIps` VARCHAR(200)  DEFAULT NULL,
 `hostId` VARCHAR(200)  DEFAULT NULL ,
 `nativeId` VARCHAR(200)  DEFAULT NULL ,
 `bizRegionId` VARCHAR(200)  DEFAULT NULL ,
 `status` VARCHAR(200)  DEFAULT NULL ,
 `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
 `monitor_status`  SMALLINT(1) DEFAULT NULL COMMENT '监控状态1监控0不监控',
 `tag_type`  SMALLINT(1) DEFAULT NULL COMMENT '接入环境版本',
 `instance_info` VARCHAR(8000)  DEFAULT NULL ,
  `locales` VARCHAR(4000)  DEFAULT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

CREATE TABLE `indicator_info_hw` (
 `indicator_id` BIGINT(64)  NOT NULL COMMENT '监控指标编号' ,
 `obj_type_id` BIGINT(64)  NOT NULL COMMENT '监控对象类型编号' ,
 `data_type` VARCHAR(255)  DEFAULT NULL COMMENT '数据类型',
 `data_unit`  VARCHAR(255) DEFAULT NULL COMMENT '单位',
 `en_us`  VARCHAR(255) DEFAULT NULL COMMENT '英文描述',
 `zh_cn`  VARCHAR(255) DEFAULT NULL COMMENT '中文描述',
 `group_en_us`  VARCHAR(255) DEFAULT NULL COMMENT '资源类型组英文描述',
 `group_zh_cn`  VARCHAR(255) DEFAULT NULL COMMENT '资源类型组中文描述',
 `tag`  VARCHAR(255) DEFAULT NULL COMMENT '标签',
 `indicator_name`  VARCHAR(255) DEFAULT NULL COMMENT '指标名称',
 `item`  VARCHAR(255) DEFAULT NULL COMMENT '我方的指标名称',
 `operator`  VARCHAR(255) DEFAULT NULL COMMENT '操作乘/除',
 `operator_value`  VARCHAR(255) DEFAULT NULL COMMENT '运算值',
 `data_unit_own`  VARCHAR(255) DEFAULT NULL COMMENT '单位我们这边的',
 `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `zh_cn_obj_type`  VARCHAR(255) DEFAULT NULL COMMENT '监控对象类型中文描述',
  `tag_type` INT(11)  DEFAULT NULL ,
  `monitor_status` INT(11) DEFAULT NULL  COMMENT '监控状态（1监控，0不监控）',
  PRIMARY KEY (`indicator_id`)
) ENGINE=INNODB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;