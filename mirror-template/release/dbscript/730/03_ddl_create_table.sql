DROP TABLE IF EXISTS `schedule_event_config`;
CREATE TABLE IF NOT EXISTS `schedule_event_config` (
  `config_uuid` VARCHAR(36) NOT NULL comment '配置主键ID',
  `rule` VARCHAR(128) NOT NULL comment '定时规则',
  `last_run_at` BIGINT comment '上次执行时间',
  `next_run_at` BIGINT comment '下次执行时间',
  `webhook_url` VARCHAR(255) NOT NULL comment '定时服务执行地址',
  `secret_key` VARCHAR(32) comment '定时配置的私钥',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  PRIMARY KEY (`config_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin comment '定时服务规则表';


DROP TABLE IF EXISTS `schedule_event_history`;
CREATE TABLE IF NOT EXISTS `schedule_event_history` (
  `history_uuid` VARCHAR(36) NOT NULL comment '历史主键ID',
  `plan_run_at` BIGINT NOT NULL comment '计划执行时间',
  `actual_run_at` BIGINT NOT NULL comment '实际执行时间',
  `webhook_url` VARCHAR(255) NOT NULL comment '定时服务执行地址',
  `result_status_code` INT NOT NULL comment '执行状态',
  `result_error` VARCHAR(64) NOT NULL comment '错误结果',
  `config_id` VARCHAR(64) NOT NULL comment '定时服务规id',
  PRIMARY KEY (`history_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin comment '定时服务历史表';
