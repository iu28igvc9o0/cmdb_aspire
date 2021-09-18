/*
Navicat MySQL Data Transfer

Source Server         : 巡检_10.12.3.157
Source Server Version : 50721
Source Host           : 10.12.3.157:3306
Source Database       : inspection

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-08-22 10:58:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for data_sync_mark
-- ----------------------------
DROP TABLE IF EXISTS `data_sync_mark`;
CREATE TABLE `data_sync_mark` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`item_identity`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`sync_seq`  int(11) NULL DEFAULT 0 ,
`last_sync_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sync_monitor_actions
-- ----------------------------
DROP TABLE IF EXISTS `sync_monitor_actions`;
CREATE TABLE `sync_monitor_actions` (
  `action_id` varchar(50) NOT NULL COMMENT '事件ID',
  `name` varchar(500) NOT NULL COMMENT '事件名',
  `event_source` varchar(20) NOT NULL COMMENT '事件来源\r\n0-指来源为触发器trigger\r\n1-指来源为自动发现descover\r\n2-指来源为自动登记auto_register\r\n3-为网络发现产生的事件源\r\n',
  `eval_type` varchar(20) DEFAULT NULL COMMENT '表示执行action的前提条件的逻辑关系\r\n0表示and/or\r\n1表示and\r\n2表示or\r\n',
  `status` varchar(20) NOT NULL COMMENT '状态\r\nON-启动\r\nOFF-禁用',
  `type` varchar(20) NOT NULL COMMENT '类型\r\n1-回调url\r\n2-函数',
  `dealer` varchar(500) NOT NULL COMMENT '处理程序\r\n如果type为1，则为url\r\n如果type为2，则为类名.方法名\r\n入参包含事件、指标、触发器信息，需定义',
  `trigger_id` varchar(50) NOT NULL COMMENT '触发器ID',
  `event_type` int(11) NOT NULL COMMENT '事件类型：\r\n1-异常事件\r\n2-正常事件\r\n3-通用事件',
  PRIMARY KEY (`action_id`),
  KEY `trigger_id` (`trigger_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for sync_monitor_items
-- ----------------------------
DROP TABLE IF EXISTS `sync_monitor_items`;
CREATE TABLE `sync_monitor_items` (
  `item_id` varchar(50) NOT NULL COMMENT '监控项ID',
  `name` varchar(100) NOT NULL COMMENT '监控项名称',
  `type` varchar(20) NOT NULL COMMENT '监控项类型\r\nSCRIPT-脚本（现有）\r\nINDEX-监控指标（现有）',
  `template_id` varchar(50) NOT NULL COMMENT '模版ID',
  `key` varchar(100) NOT NULL COMMENT '监控键值',
  `delay` int(8) DEFAULT NULL COMMENT '监控周期，单位分钟',
  `history` int(8) DEFAULT NULL COMMENT '保留时间，单位天',
  `status` varchar(20) NOT NULL COMMENT '状态：\r\nON-启用\r\nOFF-禁用\r\nNONSUPPORT-不支持（启用状态的监控项采集不到数据）（暂时不用）',
  `value_type` varchar(20) NOT NULL COMMENT '监控项数据格式：\r\nFLOAT-浮点数\r\nSTR-字符串\r\nLOG-日志\r\nUINT64-整数\r\nTEXT-文本',
  `units` varchar(20) DEFAULT NULL COMMENT '单位',
  `error` varchar(4000) DEFAULT NULL COMMENT '错误信息',
  `data_type` varchar(20) DEFAULT NULL COMMENT '监控项数据类型\r\nDECIMAL-十进制\r\nOCTAL-八进制\r\nHEXADECIMAL-十六进制\r\nBOOLEAN-布尔值\r\n',
  `sys_type` varchar(50) NOT NULL COMMENT '接入监控系统类型（目前为zabbix）\r\nMIRROR\r\nZABBIX\r\nNONE',
  PRIMARY KEY (`item_id`),
  KEY `template_id` (`template_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for sync_monitor_template
-- ----------------------------
DROP TABLE IF EXISTS `sync_monitor_template`;
CREATE TABLE `sync_monitor_template` (
  `template_id` varchar(50) NOT NULL COMMENT '模版ID',
  `name` varchar(100) NOT NULL COMMENT '模版名称',
  `description` varchar(4000) NOT NULL COMMENT '描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `type` varchar(20) NOT NULL COMMENT '模版类型：\r\n1-硬件\r\n2-网络\r\n3-主机操作系统\r\n4-应用\r\n',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `fun_type` varchar(20) NOT NULL COMMENT '功能类型：\r\n1-监控\r\n2-巡检',
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for sync_monitor_triggers
-- ----------------------------
DROP TABLE IF EXISTS `sync_monitor_triggers`;
CREATE TABLE `sync_monitor_triggers` (
  `trigger_id` varchar(50) NOT NULL COMMENT '触发器ID',
  `name` varchar(4000) NOT NULL COMMENT '触发器名称',
  `expression` varchar(4000) NOT NULL COMMENT '表达式',
  `url` varchar(2000) DEFAULT NULL COMMENT 'URL',
  `status` varchar(20) NOT NULL COMMENT '状态：\r\nON-启用\r\nOFF-禁用',
  `value` varchar(20) DEFAULT NULL COMMENT '值类型\r\nOK\r\nPROBLEM\r\nUNKNOWN',
  `priority` varchar(20) NOT NULL COMMENT '优先级\r\n0-Not classified\r\n1 -Information\r\n2-Warning\r\n3-Average\r\n4-High\r\n5-Disaster\r\n',
  `item_id` varchar(50) NOT NULL COMMENT '监控项ID',
  `param` varchar(2000) DEFAULT NULL COMMENT '脚本类监控触发器的参数值',
  PRIMARY KEY (`trigger_id`),
  KEY `item_id` (`item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for monitor_events
-- ----------------------------
DROP TABLE IF EXISTS `monitor_events`;
CREATE TABLE `monitor_events` (
`event_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '序列号' ,
`source`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事件来源：\r\nTRIGGERS-触发器\r\nDISCOVERY-新发现\r\nREGISTRATION-自动注册（agent/proxy）\r\n' ,
`source_type`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事件来源类型' ,
`object`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'TRIGGER-触发器' ,
`object_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'object类型对应的object的ID（触发器ID）' ,
`value`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '事件类型\r\n1-异常\r\n0-正常' ,
`acknowledged`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '确认标识：\r\n0-未确认\r\n1-已确认' ,
`clock`  int(10) NOT NULL COMMENT '事件产生时间' ,
`ns`  int(10) NOT NULL COMMENT '纳秒\r\n小于秒的部分' ,
`device_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '事件产生对象' ,
`biz_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`biz_object`  varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
