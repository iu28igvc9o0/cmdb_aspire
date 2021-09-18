-- ----------------------------
-- Table structure for resources_resource
-- ----------------------------
DROP TABLE IF EXISTS `resources_resource`;
CREATE TABLE `resources_resource` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `type` char(36) COLLATE utf8_bin NOT NULL,
  `uuid` char(36) COLLATE utf8_bin NOT NULL,
  `namespace` char(36) COLLATE utf8_bin NOT NULL,
  `created_by` char(36) COLLATE utf8_bin DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` char(128) COLLATE utf8_bin NOT NULL,
  `region_id` char(40) COLLATE utf8_bin DEFAULT NULL,
  `space_uuid` char(40) COLLATE utf8_bin NOT NULL,
  `project_uuid` char(36) COLLATE utf8_bin NOT NULL,
  `knamespace_uuid` char(36) COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_resname`(`type`,`name`,`namespace`)
) ENGINE=InnoDB AUTO_INCREMENT=1011047886 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `resource_project`;
CREATE TABLE resource_project (
	project_uuid varchar(36) NOT NULL,
	resource_uuid varchar(36) NOT NULL,
	belongs_to_project varchar(20),
	primary key (project_uuid,resource_uuid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
