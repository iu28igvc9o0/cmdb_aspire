DROP TABLE IF EXISTS `image_registry`;
CREATE TABLE IF NOT EXISTS `image_registry` (
  `registry_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '镜像源uuid',
  `description` text COLLATE utf8_bin NOT NULL comment ' 镜像源简要描述',
  `endpoint` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '端点地址',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment ' 更新时间',
  `created_at` timestamp NOT NULL comment ' 创建时间',
  `protocol` VARCHAR(6) COLLATE utf8_bin NOT NULL comment '协议,http|https',
  `channel` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '公有云上隧道地址',
  `issuer` VARCHAR(50) COLLATE utf8_bin NOT NULL comment 'registry auth issuer',
  `audience` VARCHAR(50) COLLATE utf8_bin NOT NULL comment 'registry auth audience',
  `region_id` VARCHAR(64) COLLATE utf8_bin NOT NULL comment '集群ID',
  `display_name` VARCHAR(64) COLLATE utf8_bin NOT NULL comment '显示名称',
  `is_public` tinyint(1) NOT NULL comment ' 是否公用0:否,1:是',
  PRIMARY KEY (`registry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin  comment '镜像源注册表';

DROP TABLE IF EXISTS `image_project`;
CREATE TABLE IF NOT EXISTS `image_project` (
  `project_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '项目ID',
  `project_name` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '项目名称',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '创建时间',
  `registry_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '镜像源ID',
  PRIMARY KEY (`project_id`),
  KEY `idx_ip_registry_id` (`registry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin comment '镜像项目表';

DROP TABLE IF EXISTS `image_repository`;
CREATE TABLE IF NOT EXISTS `image_repository` (
  `repo_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '镜像仓库uuid',
  `description` VARCHAR(128) COLLATE utf8_bin comment '镜像仓库简要描述',
  `full_description` text COLLATE utf8_bin comment '镜像仓库详细描述',
  `download` int(32) NOT NULL comment '下载次数',
  `upload` int(32) NOT NULL comment '上传次数',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  `pushed_at` timestamp NULL DEFAULT NULL comment '上传时间',
  `pulled_at` timestamp NULL DEFAULT NULL comment '下载时间',
  `created_at` timestamp NOT NULL comment '创建时间',
  `registry_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '镜像源ID',
  `is_public` tinyint(1) NOT NULL comment '是否公有0:否,1:是',
  `project_id` VARCHAR(36) COLLATE utf8_bin DEFAULT NULL comment '项目ID',
  PRIMARY KEY (`repo_id`),
  KEY `idx_ir_project_id` (`project_id`),
  KEY `idx_ir_registry_id` (`registry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin  comment '镜像仓库';


DROP TABLE IF EXISTS `image_sync_base_config`;
CREATE TABLE IF NOT EXISTS `image_sync_base_config` (
  `config_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '同步配置ID',
  `config_name` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '同步配置名称',
  `namespace` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '命名空间',
  `created_by` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '创建者',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  `created_at` timestamp NOT NULL comment '创建时间',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin  comment '同步镜像配置表';

DROP TABLE IF EXISTS `image_sync_source_config`;
CREATE TABLE IF NOT EXISTS `image_sync_source_config` (
  `source_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '源ID',
  `type` VARCHAR(64) COLLATE utf8_bin NOT NULL DEFAULT 'REPOSITORY' comment '类型,REPOSITORY',
  `info` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '信息',
  `config_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '配置ID',
  PRIMARY KEY (`source_id`),
  KEY `idx_issc_config_id` (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin  comment '同步源仓库服务器配置表';


DROP TABLE IF EXISTS `image_sync_dest_config`;
CREATE TABLE IF NOT EXISTS `image_sync_dest_config` (
  `dest_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '目标ID',
  `is_http` tinyint(1) NOT NULL  comment '是否为http协议,0:否,1:是',
  `endpoint` VARCHAR(128) COLLATE utf8_bin DEFAULT NULL  comment '端点',
  `username` VARCHAR(128) COLLATE utf8_bin DEFAULT NULL  comment '用户名',
  `password` VARCHAR(128) COLLATE utf8_bin DEFAULT NULL  comment '用户密码',
  `config_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '配置',
  `dest_type` VARCHAR(32) COLLATE utf8_bin NOT NULL comment '目标类型,内部:INTERNAL_REGISTRY,外部:EXTERNAL_REGISTRY',
  `internal_id` VARCHAR(36) COLLATE utf8_bin DEFAULT NULL comment '内部镜像源ID',
  PRIMARY KEY (`dest_id`),
  KEY `idx_isdc_config_id` (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin comment '同步目标仓库服务器配置表';


DROP TABLE IF EXISTS `image_sync_config_history`;
CREATE TABLE IF NOT EXISTS `image_sync_config_history` (
  `history_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '历史ID',
  `config_id` varchar(38) COLLATE utf8_bin NOT NULL comment '配置ID',
  `config_name` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '配置名称',
  `namespace` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '命名空间',
  `source_repo_id` varchar(38) COLLATE utf8_bin NOT NULL comment '源仓库id',
  `source_registry_name` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '源仓库服务器名称',
  `source_project_name` VARCHAR(128) COLLATE utf8_bin DEFAULT NULL comment '源项目名称',
  `source_repo_name` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '源仓库名称',
  `source_is_http` tinyint(1) NOT NULL comment '是否为http协议,0:否,1:是',
  `source_registry_endpoint` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '源仓库服务器端点',
  `source_username` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '源用户名',
  `source_password` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '源用户密码',
  `region_id` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '集群ID',
  `tag` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '标签',
  `dest_is_http` tinyint(1) NOT NULL comment '是否为http协议0:否,1:是',
  `dest_endpoint` VARCHAR(128) COLLATE utf8_bin NOT NULL comment '目标端点',
  `dest_username` VARCHAR(128) COLLATE utf8_bin DEFAULT NULL comment '目标用户名',
  `dest_password` VARCHAR(128) COLLATE utf8_bin DEFAULT NULL comment '目标用户密码',
  `created_by` VARCHAR(36) COLLATE utf8_bin NOT NULL comment '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '创建时间',
  `started_at` timestamp NULL DEFAULT NULL comment '开始时间',
  `finished_at` timestamp NULL DEFAULT NULL comment '完成时间',
  `status` VARCHAR(64) COLLATE utf8_bin NOT NULL DEFAULT 'B' comment ' 状态,B,D,I,F,S,W',
  `source_is_public` tinyint(1) NOT NULL comment '是否公有,0:否,1:是',
  `dest_id` VARCHAR(36) COLLATE utf8_bin DEFAULT NULL comment '目标id',
  PRIMARY KEY (`history_id`),
  KEY `idx_isch_config_id` (`config_id`),
  KEY `idx_isch_source_repo_id` (`source_repo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin  comment '同步仓库服务器历史表';
