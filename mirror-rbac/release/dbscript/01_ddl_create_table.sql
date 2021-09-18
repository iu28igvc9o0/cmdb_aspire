-- ----------------------------
-- Table structure for `permissions`
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `uuid` char(36) COLLATE utf8_bin NOT NULL COMMENT '权限UUID主键',
  `role_uuid` char(36) COLLATE utf8_bin NOT NULL COMMENT '角色UUID主键',
  `actions` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '资源操作',
  `resources` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '资源名',
  `constraints` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '[{}]' COMMENT '资源约束',
  PRIMARY KEY (`uuid`),
  KEY `index_permissions_role_uuid` (`role_uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `uuid` char(36) COLLATE utf8_bin NOT NULL COMMENT '角色UUID主键',
  `name` char(100) COLLATE utf8_bin NOT NULL COMMENT '角色名称',
  `namespace` char(100) COLLATE utf8_bin NOT NULL COMMENT '空间名',
  `admin_role` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为管理员角色',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`uuid`),
  KEY `index_role_name` (`name`),
  KEY `index_role_namespace` (`namespace`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for `role_parents`
-- ----------------------------
DROP TABLE IF EXISTS `role_parents`;
CREATE TABLE `role_parents` (
  `role_uuid` char(36) COLLATE utf8_bin NOT NULL COMMENT '角色UUID主键',
  `parent_uuid` char(36) COLLATE utf8_bin NOT NULL COMMENT '父角色UUID主键',
  `assigned_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
  KEY `index_parents_role_uuid` (`role_uuid`),
  KEY `index_parents_parent_uuid` (`parent_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for resource_schema
-- ----------------------------
DROP TABLE IF EXISTS `resource_schema`;
CREATE TABLE `resource_schema` (
  `resource` varchar(50) NOT NULL COMMENT '资源类型',
  `general` varchar(5) DEFAULT NULL COMMENT '是否全局',
  `created_at` datetime(6) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`resource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for resource_schema_actions
-- ----------------------------
DROP TABLE IF EXISTS `resource_schema_actions`;
CREATE TABLE `resource_schema_actions` (
  `resource` varchar(50) NOT NULL COMMENT '资源类型',
  `action` varchar(100) NOT NULL COMMENT '资源操作',
  KEY `index_actions_resource` (`resource`) USING BTREE,
  CONSTRAINT `resource_schema_actions_ibfk_1` FOREIGN KEY (`resource`) REFERENCES `resource_schema` (`resource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ---------------------------- 
-- Table structure for resource_schema_constraints
-- ----------------------------
DROP TABLE IF EXISTS `resource_schema_constraints`;
CREATE TABLE `resource_schema_constraints` (
  `resource` varchar(50) NOT NULL COMMENT '资源类型',
  `const_key` varchar(100) NOT NULL COMMENT '资源约束名',
  `const_value` varchar(100) NOT NULL COMMENT '资源约束值',
  KEY `index_constraints_resource` (`resource`) USING BTREE,
  CONSTRAINT `resource_schema_constraints_ibfk_1` FOREIGN KEY (`resource`) REFERENCES `resource_schema` (`resource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ---------------------------- 
-- Table structure for users_user
-- ----------------------------
CREATE TABLE `users_user` (
  `uid` varchar(32) NOT NULL,
  `password` char(128) NOT NULL COMMENT '用户密码',
  `last_login` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
  `username` char(30) NOT NULL COMMENT '唯一的用户名',
  `email` char(254) NOT NULL COMMENT '邮箱',
  `realname` char(30) NOT NULL COMMENT '用户姓名',
  `is_active` tinyint(1) NOT NULL COMMENT ' 根账号是否激活',
  `is_admin` tinyint(1) NOT NULL COMMENT '是否是平台管理员',
  `joined_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建账号时间',
  `account_type` int(32) NOT NULL COMMENT '账号类型',
  `company` char(50) DEFAULT NULL COMMENT '公司名称',
  `mobile` char(15) DEFAULT NULL COMMENT '电话号码',
  `logo_file` varchar(100) DEFAULT NULL COMMENT 'logo地址'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for roles_user
-- ----------------------------
DROP TABLE IF EXISTS `roles_user`;
CREATE TABLE `roles_user` (
  `role_uuid` char(36) COLLATE utf8_bin NOT NULL COMMENT '角色UUID主键',
  `namespace` char(100) COLLATE utf8_bin NOT NULL COMMENT '空间名',
  `username` char(100) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `assigned_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
  KEY `index_users_role_uuid` (`role_uuid`),
  KEY `index_users_username` (`username`),
  KEY `index_users_namespace` (`namespace`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for `architect_template`
-- ----------------------------
DROP TABLE IF EXISTS `architect_template`;
CREATE TABLE `architect_template` (
  `uuid` varchar(36) COLLATE utf8_bin NOT NULL COMMENT 'uuid 为 PK',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '项目模版名称',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `template_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='项目模版主要信息';

-- ----------------------------
-- Table structure for `architect_template_item`
-- ----------------------------
DROP TABLE IF EXISTS `architect_template_item`;
CREATE TABLE `architect_template_item` (
  `template_uuid` varchar(36) COLLATE utf8_bin NOT NULL COMMENT '项目模版uuid',
  `id` int(11) NOT NULL COMMENT '资源ID，必须在同一个项目模版内是唯一的',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '资源名称，可以待 [name] placeholder 来自动分配项目名称',
  `required` tinyint(1) DEFAULT '0' COMMENT '是否必须创建资源的选项',
  `depends_on` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '其它资源依赖关系，为json 数组保存，内容是其它资源ID字段 [1,2]',
  `item_type` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '资源类型，支持 role或者resource',
  `item_data` varchar(10000) COLLATE utf8_bin NOT NULL COMMENT '资源类型payload信息 为json保存，role和resource格式不同',
  UNIQUE KEY `template_item_id` (`template_uuid`,`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='项目模版声明的资源';

-- ----------------------------
-- Table structure for `architect_project`
-- ----------------------------
DROP TABLE IF EXISTS `architect_project`;
CREATE TABLE `architect_project` (
  `uuid` varchar(36) COLLATE utf8_bin NOT NULL COMMENT '项目uuid',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '项目名称',
  `namespace` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '根账号',
  `template` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '基于模版的名称',
  `template_uuid` varchar(36) COLLATE utf8_bin NOT NULL COMMENT '基于模版的uuid',
  `status` varchar(30) COLLATE utf8_bin NOT NULL COMMENT '项目状态：pending，creating，success，deleting，error',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `project_namespace_name` (`namespace`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='项目表';

-- ----------------------------
-- Table structure for `architect_project_item`:暂时不用
-- ----------------------------
CREATE TABLE IF NOT EXISTS architect_project_item (
    project_uuid VARCHAR(36) NOT NULL COMMENT "项目uuid",
    template_uuid VARCHAR(36) NOT NULL COMMENT "项目模版uuid",
    uuid VARCHAR(36) NOT NULL COMMENT "资源uuid",
    id INTEGER NOT NULL COMMENT "模版中的ID字段",
    name VARCHAR(100) NOT NULL COMMENT "资源名称",
    required BOOLEAN DEFAULT false COMMENT "是否必须创建的资源",
    depends_on VARCHAR(100) NOT NULL COMMENT "其它资源依赖关系，为json 数组保存，内容是其它资源ID字段 [1,2]",
    item_type VARCHAR(20) NOT NULL COMMENT "资源类型，支持 role或者resource",
    item_data VARCHAR(10000) NOT NULL COMMENT "资源类型payload信息 为json保存，role和resource格式不同",
    status VARCHAR(30) NOT NULL COMMENT "资源状态 pending，creating，success，deleting，error",
    error VARCHAR(10000) NOT NULL COMMENT "创建时如果API返回错误在此列保存",
    created_at TIMESTAMP NOT NULL DEFAULT now() COMMENT "资源创建时间",
    updated_at TIMESTAMP NOT NULL DEFAULT now() COMMENT "资源更新时间",
    CONSTRAINT project_item_id UNIQUE (project_uuid, id),
    FOREIGN KEY (project_uuid) REFERENCES architect_project (uuid) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='项目创建的资源 - 首先需要在模版声明的';




