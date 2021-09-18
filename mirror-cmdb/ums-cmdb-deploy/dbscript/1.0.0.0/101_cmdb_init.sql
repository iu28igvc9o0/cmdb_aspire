CREATE TABLE cmdb_module (
`id` varchar(40) NOT NULL,
`name` varchar(40) NOT NULL,
`code` varchar(40) NOT NULL COMMENT '模型编号',
`parentId` varchar(60) NULL COMMENT '父类编号',
`iconUrl` varchar(100) NOT NULL COMMENT '图标URL',
`sortIndex` int(2) NOT NULL DEFAULT 0 COMMENT '排序值',
`disabled` varchar(20) NOT NULL DEFAULT true COMMENT '是否可用（true/false）',
`builtin` varchar(20) NOT NULL DEFAULT false COMMENT '是否内置(true/false)',
`insertTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`isDelete` int(2) NOT NULL DEFAULT 0 COMMENT '是否删除（0:否 1：是）',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb模型表';

CREATE TABLE cmdb_icon (
`id` varchar(40) NOT NULL,
`iconUrl` varchar(100) NOT NULL COMMENT '图表URL',
`iconCategory` int(2) NOT NULL DEFAULT 1 COMMENT '图标类别(0:系统图标 1自定义图标)',
`sortIndex` int(8) NOT NULL DEFAULT 0 COMMENT '排序值',
`insertTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`isDelete` int(2) NULL DEFAULT 0 COMMENT '是否删除（0:否 1：是）',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb图标表';

CREATE TABLE cmdb_form (
`id` varchar(40) NOT NULL,
`moduleId` varchar(40) NOT NULL COMMENT '模型id',
`name` varchar(60) NOT NULL COMMENT '属性名称',
`code` varchar(40) NOT NULL COMMENT '表单编码',
`type` varchar(40) NOT NULL COMMENT '类型',
`defaultValue` varchar(40) NULL COMMENT '默认值',
`required` varchar(20) NOT NULL DEFAULT false COMMENT '是否必填(true/false)',
`builtin` varchar(20) NOT NULL DEFAULT false COMMENT '是否内置(true/false)',
`hidden` varchar(20) NOT NULL DEFAULT false COMMENT '是否隐藏(true/false)',
`keyAttr` varchar(20) NOT NULL DEFAULT false COMMENT '关键属性(true/false)',
`rate` varchar(40) NULL COMMENT '采集频率(低频/中频/高频)',
`unit` varchar(40) NULL COMMENT '单位',
`sortIndex` int(2) NOT NULL DEFAULT 0 COMMENT '排序值',
`insertTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`isDelete` int(2) NOT NULL DEFAULT 0 COMMENT '是否删除（0:否 1：是）',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb表单定义表';

CREATE TABLE cmdb_form_param (
`id` varchar(40) NOT NULL,
`key` varchar(40) NOT NULL COMMENT '参数名',
`value` varchar(200) NOT NULL COMMENT '参数值',
`formId` varchar(40) NOT NULL COMMENT '属性id',
`sortIndex` int(2) NOT NULL DEFAULT 0 COMMENT '排序值',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb表单参数表';

CREATE TABLE cmdb_form_options (
`id` varchar(40) NOT NULL,
`name` varchar(100) NOT NULL COMMENT '选项名',`value` varchar(100) NULL COMMENT '选项值',
`default` varchar(40) NOT NULL DEFAULT false COMMENT '是否默认(true/false)',
`color` varchar(40) NULL COMMENT '颜色编码',
`parentId` varchar(40) NULL COMMENT '父编号',
`formId` varchar(40) NOT NULL COMMENT '表单id',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb表单选项表';

CREATE TABLE cmdb_form_fields (
`id` varchar(40) NOT NULL,
`key` varchar(40) NOT NULL,
`name` varchar(40) NOT NULL COMMENT '字段名称',
`sortIndex` int(2) NOT NULL COMMENT '排序值',
`formId` varchar(40) NOT NULL COMMENT '参数编号',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb表单表格字段表';

CREATE TABLE cmdb_relation (
`id` varchar(40) NOT NULL,
`name` varchar(40) NOT NULL COMMENT '关系名',
`builtin` varchar(20) NOT NULL DEFAULT false COMMENT '是否内置(true/false)',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb关系表';

CREATE TABLE cmdb_module_relation (
`id` varchar(40) NOT NULL,
`sourceModuleId` varchar(40) NOT NULL COMMENT '发起模型id',
`targetModuleId` varchar(40) NOT NULL COMMENT '目标模型id',
`relationId` varchar(40) NOT NULL COMMENT '关系类型id',
`builtin` varchar(20) NOT NULL DEFAULT false COMMENT '是否内置(true/false)',
`restriction` varchar(20) NOT NULL COMMENT '限制条件(OneToOne,OneToMany,ManyToOne,ManyToMany)',`insertTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',`updateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb模型关系表';

CREATE TABLE cmdb_instance (
`id` varchar(40) NOT NULL,
`name` varchar(40) NOT NULL COMMENT '实例名称',
`moduleId` varchar(40) NOT NULL COMMENT ' 类型编码',
`insertTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`isDelete` int(2) NOT NULL DEFAULT 0 COMMENT '是否删除（0:否 1：是）',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb实例表';

CREATE TABLE cmdb_maintain_view (
`id` varchar(40) NOT NULL,
`circleId` varchar(40) NOT NULL COMMENT '维护圈id',
`name` varchar(40) NOT NULL COMMENT '视图名称',
`sort` int(2) NOT NULL COMMENT '排序值',
`defaultView` varchar(20) NOT NULL DEFAULT false COMMENT '是否默认(true/false)',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb维护圈视图表';

CREATE TABLE cmdb_instance_tag (
`id` varchar(40) NOT NULL,
`name` varchar(40) NOT NULL COMMENT '标签名称',
`instanceId` varchar(40) NOT NULL COMMENT '实例id',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb实例标签表';

CREATE TABLE cmdb_instance_form_value (
`id` varchar(40) NOT NULL,
`formId` varchar(40) NOT NULL COMMENT '表单id',
`formCode` varchar(40) NOT NULL COMMENT '表单编码',
`formValue` text NOT NULL,
`instanceId` varchar(40) NULL,
PRIMARY KEY (`id`) 
)
COMMENT='cmdb实例字段表';

CREATE TABLE cmdb_maintain_view_condition (
`moduleId` varchar(40) NOT NULL COMMENT '模型id',
`viewId` varchar(40) NOT NULL COMMENT '视图id'
)
COMMENT='cmdb维护圈视图过滤表';

CREATE TABLE cmdb_circle (
`id` varchar(40) NOT NULL,
`name` varchar(40) NOT NULL COMMENT '名称',
`code` varchar(40) NOT NULL COMMENT '编码',
`dec` varchar(200) NULL COMMENT '描述',
`type` int(2) NOT NULL DEFAULT 0 COMMENT '类型(0:维护圈，1：消费圈)',
`isTop` int(2) NOT NULL DEFAULT 0 COMMENT '是否置顶（0:否 1：是）',
`insertTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`isDelete` int(2) NOT NULL DEFAULT 0 COMMENT '是否删除（0:否 1：是）',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb圈子表';

CREATE TABLE cmdb_instance_relation_detail (
`id` varchar(40) NOT NULL,
`sourceInstanceId` varchar(40) NOT NULL COMMENT '实例id',
`targerInstanceId` varchar(40) NOT NULL COMMENT '目标实例id',
`moduleRelationId` varchar(40) NOT NULL COMMENT '关系id',
`circleId` varchar(40) NULL COMMENT '圈子id',`insertTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',`updateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb实例关系表';

CREATE TABLE cmdb_circle_chart (
`id` varchar(40) NOT NULL,
`name` varchar(40) NOT NULL COMMENT '名称',
`desc` varchar(200) NULL COMMENT '描述',
`cirleId` varchar(40) NOT NULL COMMENT '圈子id',
`insertTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`updateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
`isDelete` int(2) NOT NULL DEFAULT 0 COMMENT '是否删除（0:否 1：是）',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb消费图表';

CREATE TABLE cmdb_circle_chart_node (
`id` varchar(40) NOT NULL,
`chartId` varchar(40) NOT NULL COMMENT '消费图id',
`instanceId` varchar(40) NOT NULL COMMENT '实例id',
`moduleId` varchar(40) NOT NULL COMMENT '模型id',
`positionX` float(4,2) NOT NULL COMMENT 'x坐标',
`positionY` float(4,2) NOT NULL COMMENT 'y坐标',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb消费图节点表';

CREATE TABLE cmdb_circle_chart_link (
`id` varchar(40) NOT NULL,
`chartId` varchar(40) NOT NULL COMMENT '消费图id',
`sourceInstanceId` varchar(40) NOT NULL COMMENT '发起实例id',
`targetInstanceId` varchar(40) NOT NULL COMMENT '目标实例id',
`relationId` varchar(40) NOT NULL COMMENT '关系id',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb消费图节点关系表';

CREATE TABLE cmdb_instance_circle (
`instanceId` varchar(40) NULL COMMENT '实例id',
`circleId` varchar(40) NULL COMMENT '维护圈id'
)
COMMENT='cmdb实例圈子表';

CREATE TABLE cmdb_config_log (
`id` varchar(40) NOT NULL,
`name` varchar(40) NOT NULL COMMENT '配置项名称',`instanceId` varchar(40) NOT NULL COMMENT '实例id',`moduleName` varchar(40) NOT NULL COMMENT '模型名称',
`circleName` varchar(40) NULL,
`circleId` varchar(40) NULL,
`action` varchar(40) NOT NULL COMMENT '操作',
`owner` varchar(40) NULL COMMENT '操作人id',
`ownerName` varchar(40) NULL COMMENT '操作人名',
`desc` varchar(500) NULL,
`insertTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb配置动态日志表';
CREATE TABLE cmdb_relation_log (`id` varchar(40) NOT NULL,`name` varchar(40) NOT NULL COMMENT '配置实例名称',`instanceId` varchar(40) NOT NULL COMMENT '配置实例id',`circleId` varchar(40) NULL COMMENT '维护圈id',`relationName` varchar(40) NOT NULL COMMENT '关系',`targetName` varchar(40) NOT NULL COMMENT '目标配置名称',`action` varchar(40) NOT NULL COMMENT '操作',`owner` varchar(40) NULL COMMENT '提交人id',`ownerName` varchar(40) NULL COMMENT '提交人姓名',`insertTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',PRIMARY KEY (`id`) )COMMENT='cmdb关系动态日志表';
CREATE TABLE cmdb_form_script (
`id` varchar(40) NOT NULL,
`formId` varchar(40) NOT NULL COMMENT '表单id',
`tag` varchar(40) NOT NULL COMMENT '脚本标签id',
`language` varchar(40) NULL COMMENT '脚本语言(python/shell)',
`script` text NULL COMMENT '脚本代码',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb表单脚本表';

CREATE TABLE cmdb_form_rule (
`id` varchar(40) NOT NULL,
`name` varchar(40) NOT NULL COMMENT '规则名称',`code` varchar(40) NOT NULL,
`rule` varchar(200) NOT NULL COMMENT '规则',
PRIMARY KEY (`id`) 
)
COMMENT='cmdb表单验证规则表';

CREATE TABLE cmdb_form_tag (
`id` varchar(40) NOT NULL,
`moduleId` varchar(40) NOT NULL COMMENT '模型id',
`tag` varchar(40) NOT NULL COMMENT '标签',PRIMARY KEY (`id`) 
)
COMMENT='cmdb模型标签表';
CREATE TABLE cmdb_column_filter (  `id` varchar(40) NOT NULL,  `menuType` varchar(40) NOT NULL COMMENT '菜单类型:Maintain:维护圈 Repertry:仓库',  `moduleId` varchar(40) NOT NULL COMMENT '模型ID',  `columnInfo` text COMMENT '选择字段信息',  `insertTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',  `updateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',  PRIMARY KEY (`id`)) COMMENT='cmdb模型字段过滤表';
CREATE INDEX index_cmdb_module_code on cmdb_module(code);CREATE INDEX index_cmdb_instance_moduleId on cmdb_instance(moduleId);CREATE INDEX index_cmdb_form_value_instanceId on cmdb_instance_form_value(instanceId);