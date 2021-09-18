-- ----------------------------
-- Table structure for `architect_template`
-- ----------------------------
DROP TABLE IF EXISTS `architect_template`;
CREATE TABLE `architect_template` (
  `uuid` varchar(36) NOT NULL COMMENT 'uuid 为 PK',
  `name` varchar(100) NOT NULL COMMENT '项目模版名称',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `template_name` (`name`)
) COMMENT='项目模版主要信息';

-- ----------------------------
-- Table structure for `architect_template_item`
-- ----------------------------
DROP TABLE IF EXISTS `architect_template_item`;
CREATE TABLE `architect_template_item` (
  `template_uuid` varchar(36) NOT NULL COMMENT '项目模版uuid',
  `id` int(11) NOT NULL COMMENT '资源ID，必须在同一个项目模版内是唯一的',
  `name` varchar(100) NOT NULL COMMENT '资源名称，可以待 [name] placeholder 来自动分配项目名称',
  `required` tinyint(1) DEFAULT '0' COMMENT '是否必须创建资源的选项',
  `depends_on` varchar(100) NOT NULL COMMENT '其它资源依赖关系，为json 数组保存，内容是其它资源ID字段 [1,2]',
  `item_type` varchar(20) NOT NULL COMMENT '资源类型，支持 role或者resource',
  `item_data` varchar(10000) NOT NULL COMMENT '资源类型payload信息 为json保存，role和resource格式不同',
  UNIQUE KEY `template_item_id` (`template_uuid`,`id`) USING BTREE
) COMMENT='项目模版声明的资源';

-- ----------------------------
-- Table structure for `architect_project`
-- ----------------------------
DROP TABLE IF EXISTS `architect_project`;
CREATE TABLE `architect_project` (
  `uuid` varchar(36) NOT NULL COMMENT '项目uuid',
  `name` varchar(100) NOT NULL COMMENT '项目名称',
  `namespace` varchar(100) NOT NULL COMMENT '根账号',
  `template` varchar(100) NOT NULL COMMENT '基于模版的名称',
  `template_uuid` varchar(36) NOT NULL COMMENT '基于模版的uuid',
  `status` varchar(30) NOT NULL COMMENT '项目状态：pending，creating，success，deleting，error',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `project_namespace_name` (`namespace`,`name`) USING BTREE
)  COMMENT='项目表';

