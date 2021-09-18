-- ----------------------------
-- Table structure for `permissions`
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `uuid` char(36) NOT NULL COMMENT '权限UUID主键',
  `role_uuid` char(36) NOT NULL COMMENT '角色UUID主键',
  `actions` varchar(255) NOT NULL COMMENT '资源操作',
  `resources` varchar(255) NOT NULL COMMENT '资源名',
  `constraints` varchar(255) NOT NULL DEFAULT '[{}]' COMMENT '资源约束',
  PRIMARY KEY (`uuid`),
  KEY `index_permissions_role_uuid` (`role_uuid`) USING BTREE
);

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `uuid` char(36) NOT NULL COMMENT '角色UUID主键',
  `name` char(100) NOT NULL COMMENT '角色名称',
  `namespace` char(100) NOT NULL COMMENT '空间名',
  `admin_role` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为管理员角色',
  `created_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uuid`),
  KEY `index_role_name` (`name`),
  KEY `index_role_namespace` (`namespace`)
);

-- ----------------------------
-- Table structure for `role_parents`
-- ----------------------------
DROP TABLE IF EXISTS `role_parents`;
CREATE TABLE `role_parents` (
  `role_uuid` char(36) NOT NULL COMMENT '角色UUID主键',
  `parent_uuid` char(36) NOT NULL COMMENT '父角色UUID主键',
  `assigned_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '分配时间',
  KEY `index_parents_role_uuid` (`role_uuid`),
  KEY `index_parents_parent_uuid` (`parent_uuid`)
);

-- ----------------------------
-- Table structure for resource_schema
-- ----------------------------
DROP TABLE IF EXISTS `resource_schema`;
CREATE TABLE `resource_schema` (
  `resource` varchar(50) NOT NULL COMMENT '资源类型',
  `general` varchar(5) DEFAULT NULL COMMENT '是否全局',
  `created_at` datetime(6) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`resource`)
);

-- ----------------------------
-- Table structure for resource_schema_actions
-- ----------------------------
DROP TABLE IF EXISTS `resource_schema_actions`;
CREATE TABLE `resource_schema_actions` (
  `resource` varchar(50) NOT NULL COMMENT '资源类型',
  `action` varchar(100) NOT NULL COMMENT '资源操作',
  KEY `index_actions_resource` (`resource`) USING BTREE,
  CONSTRAINT `resource_schema_actions_ibfk_1` FOREIGN KEY (`resource`) REFERENCES `resource_schema` (`resource`)
);

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
);

-- ----------------------------
-- Table structure for roles_user
-- ----------------------------
DROP TABLE IF EXISTS `roles_user`;
CREATE TABLE `roles_user` (
  `role_uuid` char(36) NOT NULL COMMENT '角色UUID主键',
  `namespace` char(100) NOT NULL COMMENT '空间名',
  `username` char(100) NOT NULL COMMENT '用户名',
  `assigned_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '分配时间',
  KEY `index_users_role_uuid` (`role_uuid`),
  KEY `index_users_username` (`username`),
  KEY `index_users_namespace` (`namespace`)
);
