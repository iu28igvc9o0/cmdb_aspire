CREATE TABLE  CODE_DICT(
id NUMBER(11) NOT NULL ,
code_type NVARCHAR2(50) NOT NULL ,
key NVARCHAR2(50) NOT NULL ,
value NVARCHAR2(100) NOT NULL ,
code_desc NVARCHAR2(255) NULL ,
valid_flag NVARCHAR2(1) NOT NULL ,
order_id NUMBER(11) NULL ,
parent_id NUMBER(11) NULL 
);

ALTER TABLE code_dict ADD PRIMARY KEY (id);
COMMENT ON TABLE code_dict IS '编码字典';
COMMENT ON COLUMN code_dict.id IS '主键ID';
COMMENT ON COLUMN code_dict.code_type IS '编码类型';
COMMENT ON COLUMN code_dict.key IS '编码key';
COMMENT ON COLUMN code_dict.value IS '编码值';
COMMENT ON COLUMN code_dict.code_desc IS '描述';
COMMENT ON COLUMN code_dict.valid_flag IS '是否有效or可见，1=是；0=否';
COMMENT ON COLUMN code_dict.order_id IS '排序（从1开始的正整数，NULL表示不排序，各level互不影响）';
COMMENT ON COLUMN code_dict.parent_id IS '上级ID';

INSERT INTO code_dict VALUES ('1', 'theme_access_url', 'in', 'http://10.12.70.39:28129/v1/theme/createThemeData', '内部主题数据接入地址', '1', '1', null);
INSERT INTO code_dict VALUES ('2', 'theme_access_url', 'out', 'www.baidu.com', '外部主题数据接入地址', '1', '2', null);
INSERT INTO code_dict VALUES ('3', 'biz_system', 'AITYYPT', 'AI统一云平台', 'AI统一云平台', '1', '1', null);
INSERT INTO code_dict VALUES ('4', 'biz_system', 'TYXXPTSZHDA', '统一信息平台-数字化档案', '统一信息平台-数字化档案', '1', '1', '3');
INSERT INTO code_dict VALUES ('10', 'theme_grok_pattern', 'IP', 'IP地址', 'IP地址', '1', '1', null);
INSERT INTO code_dict VALUES ('11', 'theme_grok_pattern', 'GREEDYDATA', '字符串', '字符串', '1', '2', null);
INSERT INTO code_dict VALUES ('12', 'theme_grok_pattern', 'NUMBER', '数字', '数字', '1', '3', null);
INSERT INTO code_dict VALUES ('13', 'theme_grok_pattern', 'PATH', 'PATH', 'PATH', '1', '4', null);
INSERT INTO code_dict VALUES ('14', 'theme_grok_pattern', 'DATESTAMP', '时间', '时间', '1', '6', null);
INSERT INTO code_dict VALUES ('15', 'theme_grok_pattern', 'DATE', '日期', '日期', '1', '5', null);
INSERT INTO code_dict VALUES ('16', 'theme_grok_pattern', 'LOGLEVEL', '日志级别', '日志级别', '1', '7', null);
INSERT INTO code_dict VALUES ('17', 'theme_grok_pattern', 'WORD', '日志内容', '数据', '1', '8', null);
INSERT INTO code_dict VALUES ('18', 'dept_type', '1', '正式部门', '部门类型-正式部门', '1', '1', null);
INSERT INTO code_dict VALUES ('19', 'dept_type', '0', '临时部门', '部门类型-正式部门', '1', '2', null);
INSERT INTO code_dict VALUES ('20', 'sex', '1', '男', '性别-男', '1', '1', null);
INSERT INTO code_dict VALUES ('21', 'sex', '0', '女', '性别-女', '1', '2', null);
INSERT INTO code_dict VALUES ('22', 'user_type', '1', '正式用户', '用户类型-正式用户', '1', '1', null);
INSERT INTO code_dict VALUES ('23', 'user_type', '2', '临时用户', '用户类型-临时用户', '1', '2', null);
INSERT INTO code_dict VALUES ('24', 'ldap_status', '0', '禁用', '用户类型-正式用户', '1', '1', null);
INSERT INTO code_dict VALUES ('25', 'ldap_status', '1', '启用', '用户类型-临时用户', '1', '2', null);






CREATE TABLE data_sync_mark (
id NUMBER(11) NOT NULL ,
item_identity NVARCHAR2(30) NOT NULL ,
sync_seq NUMBER(11) NULL ,
last_sync_time TIMESTAMP NULL 
);

ALTER TABLE data_sync_mark ADD PRIMARY KEY (id);

INSERT INTO data_sync_mark VALUES ('1', 'monitor_actions', '100', TO_TIMESTAMP('2018-12-03 16:38:54', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO data_sync_mark VALUES ('2', 'monitor_items', '2463', TO_TIMESTAMP('2019-06-18 16:29:50', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO data_sync_mark VALUES ('3', 'monitor_triggers', '2453', TO_TIMESTAMP('2019-06-13 10:22:26', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO data_sync_mark VALUES ('4', 'monitor_template', '2452', TO_TIMESTAMP('2019-06-13 10:22:27', 'YYYY-MM-DD HH24:MI:SS'));







CREATE TABLE monitor_events (
event_id NVARCHAR2(50) NOT NULL ,
source_type NVARCHAR2(20) NOT NULL ,
source_id NVARCHAR2(50) NOT NULL ,
source NCLOB NULL ,
object_type NVARCHAR2(20) NOT NULL ,
object_id NVARCHAR2(50) NOT NULL ,
object NCLOB NULL ,
value NVARCHAR2(20) NOT NULL ,
acknowledged NVARCHAR2(20) NOT NULL ,
clock NUMBER(11) NOT NULL ,
ns NUMBER(11) NOT NULL 
);
COMMENT ON COLUMN monitor_events.event_id IS '序列号';
COMMENT ON COLUMN monitor_events.source_type IS '事件来源：TRIGGERS-触发器DISCOVERY-新发现REGISTRATION-自动注册（agent/proxy）';
COMMENT ON COLUMN monitor_events.source_id IS 'source_type对应的id';
COMMENT ON COLUMN monitor_events.source IS '保留字段';
COMMENT ON COLUMN monitor_events.object_type IS '1-设备2-业务系统';
COMMENT ON COLUMN monitor_events.object_id IS 'object_type对应的object的ID';
COMMENT ON COLUMN monitor_events.object IS 'object_id相关的扩展存储属性相关业务使用';
COMMENT ON COLUMN monitor_events.value IS '事件类型1-异常0-正常';
COMMENT ON COLUMN monitor_events.acknowledged IS '确认标识：0-未确认1-已确认';
COMMENT ON COLUMN monitor_events.clock IS '事件产生时间';
COMMENT ON COLUMN monitor_events.ns IS '纳秒小于秒的部分';

ALTER TABLE monitor_events ADD PRIMARY KEY (event_id);






CREATE TABLE sync_monitor_actions (
action_id NVARCHAR2(50) NOT NULL ,
name NVARCHAR2(500) NOT NULL ,
event_source NVARCHAR2(20) NOT NULL ,
eval_type NVARCHAR2(20) NULL ,
status NVARCHAR2(20) NOT NULL ,
type NVARCHAR2(20) NOT NULL ,
dealer NVARCHAR2(500) NOT NULL ,
trigger_id NVARCHAR2(50) NOT NULL ,
event_type NUMBER(11) NOT NULL 
);
COMMENT ON COLUMN sync_monitor_actions.action_id IS '事件ID';
COMMENT ON COLUMN sync_monitor_actions.name IS '事件名';
COMMENT ON COLUMN sync_monitor_actions.event_source IS '事件来源0-指来源为触发器trigger1-指来源为自动发现descover2-指来源为自动登记auto_register3-为网络发现产生的事件源';
COMMENT ON COLUMN sync_monitor_actions.eval_type IS '表示执行action的前提条件的逻辑关系0表示and/or1表示and2表示or';
COMMENT ON COLUMN sync_monitor_actions.status IS '状态ON-启动OFF-禁用';
COMMENT ON COLUMN sync_monitor_actions.type IS '类型1-回调url2-函数';
COMMENT ON COLUMN sync_monitor_actions.dealer IS '处理程序如果type为1，则为url如果type为2，则为类名.方法名入参包含事件、指标、触发器信息，需定义';
COMMENT ON COLUMN sync_monitor_actions.trigger_id IS '触发器ID';
COMMENT ON COLUMN sync_monitor_actions.event_type IS '事件类型：1-异常事件2-正常事件3-通用事件';

CREATE INDEX trigger_id ON sync_monitor_actions (trigger_id );
ALTER TABLE sync_monitor_actions ADD PRIMARY KEY (action_id);

INSERT INTO sync_monitor_actions VALUES ('xxx2', 'callback_2', 'TRIGGER', null, 'ON', '1', 'http://10.12.70.39:8128/v1/report_data/data_callback', '4dba1c99-1324-4754-bfa7-1709132d932c', '3');
INSERT INTO sync_monitor_actions VALUES ('xxx3', 'inspection_callback333', 'TRIGGER', '', 'ON', '1', 'http://10.12.70.39:8128/v1/report_data/data_callback', 'd55d0e8e-fe9c-4916-9ed6-a64a1585e049', '3');
INSERT INTO sync_monitor_actions VALUES ('xxxx', 'inspection_callback', 'TRIGGER', null, 'ON', '1', 'http://10.12.70.39:8128/v1/report_data/data_callback', '4dba1c99-1324-4754-bfa7-1709132d932c', '3');
INSERT INTO sync_monitor_actions VALUES ('xxxx44', 'inspection_callback', 'TRIGGER', '', 'ON', '1', 'http://10.12.70.39:8128/v1/report_data/data_callback', 'b7b17db5-58a4-42b0-8489-e476ca9af7fc', '3');







CREATE TABLE sync_monitor_items (
item_id NVARCHAR2(50) NOT NULL ,
name NVARCHAR2(100) NULL ,
type NVARCHAR2(20) NULL ,
template_id NVARCHAR2(50) NULL ,
key NVARCHAR2(100) NULL ,
delay NUMBER(11) NULL ,
history NUMBER(11) NULL ,
status NVARCHAR2(20) NULL ,
value_type NVARCHAR2(20) NULL ,
units NVARCHAR2(20) NULL ,
error NCLOB NULL ,
data_type NVARCHAR2(20) NULL ,
sys_type NVARCHAR2(50) NULL 
);
COMMENT ON COLUMN sync_monitor_items.item_id IS '监控项ID';
COMMENT ON COLUMN sync_monitor_items.name IS '监控项名称';
COMMENT ON COLUMN sync_monitor_items.type IS '监控项类型SCRIPT-脚本（现有）INDEX-监控指标（现有）';
COMMENT ON COLUMN sync_monitor_items.template_id IS '模版ID';
COMMENT ON COLUMN sync_monitor_items.key IS '监控键值';
COMMENT ON COLUMN sync_monitor_items.delay IS '监控周期，单位分钟';
COMMENT ON COLUMN sync_monitor_items.history IS '保留时间，单位天';
COMMENT ON COLUMN sync_monitor_items.status IS '状态：ON-启用OFF-禁用NONSUPPORT-不支持（启用状态的监控项采集不到数据）（暂时不用）';
COMMENT ON COLUMN sync_monitor_items.value_type IS '监控项数据格式：FLOAT-浮点数STR-字符串LOG-日志UINT64-整数TEXT-文本';
COMMENT ON COLUMN sync_monitor_items.units IS '单位';
COMMENT ON COLUMN sync_monitor_items.error IS '错误信息';
COMMENT ON COLUMN sync_monitor_items.data_type IS '监控项数据类型DECIMAL-十进制OCTAL-八进制HEXADECIMAL-十六进制BOOLEAN-布尔值';
COMMENT ON COLUMN sync_monitor_items.sys_type IS '接入监控系统类型（目前为zabbix）,MIRROR,ZABBIX,NONE';

CREATE INDEX template_id
ON sync_monitor_items (template_id );
ALTER TABLE sync_monitor_items ADD PRIMARY KEY (item_id);







CREATE TABLE sync_monitor_template (
template_id NVARCHAR2(50) NOT NULL ,
name NVARCHAR2(100) NOT NULL ,
description NCLOB NOT NULL ,
create_time TIMESTAMP NOT NULL ,
type NVARCHAR2(20) NOT NULL ,
update_time TIMESTAMP NOT NULL ,
fun_type NVARCHAR2(20) NOT NULL 
)

;
COMMENT ON COLUMN sync_monitor_template.template_id IS '模版ID';
COMMENT ON COLUMN sync_monitor_template.name IS '模版名称';
COMMENT ON COLUMN sync_monitor_template.description IS '描述';
COMMENT ON COLUMN sync_monitor_template.create_time IS '创建时间';
COMMENT ON COLUMN sync_monitor_template.type IS '模版类型：,1-硬件,2-网络,3-主机操作系统,4-应用';
COMMENT ON COLUMN sync_monitor_template.update_time IS '更新时间';
COMMENT ON COLUMN sync_monitor_template.fun_type IS '功能类型：,1-监控,2-巡检';

ALTER TABLE sync_monitor_template ADD PRIMARY KEY (template_id);









CREATE TABLE sync_monitor_triggers (
trigger_id NVARCHAR2(50) NOT NULL ,
name NCLOB NOT NULL ,
expression NCLOB NOT NULL ,
url NCLOB NULL ,
status NVARCHAR2(20) NOT NULL ,
value NVARCHAR2(20) NULL ,
priority NVARCHAR2(20) NOT NULL ,
item_id NVARCHAR2(50) NOT NULL ,
param NCLOB NULL 
)

;
COMMENT ON COLUMN sync_monitor_triggers.trigger_id IS '触发器ID';
COMMENT ON COLUMN sync_monitor_triggers.name IS '触发器名称';
COMMENT ON COLUMN sync_monitor_triggers.expression IS '表达式';
COMMENT ON COLUMN sync_monitor_triggers.url IS 'URL';
COMMENT ON COLUMN sync_monitor_triggers.status IS '状态：ON-启用,OFF-禁用';
COMMENT ON COLUMN sync_monitor_triggers.value IS '值类型,OK,PROBLEM,UNKNOWN';
COMMENT ON COLUMN sync_monitor_triggers.priority IS '优先级,0-Not classified,1 -Information,2-Warning,3-Average,4-High,5-Disaster';
COMMENT ON COLUMN sync_monitor_triggers.item_id IS '监控项ID';
COMMENT ON COLUMN sync_monitor_triggers.param IS '脚本类监控触发器的参数值';

CREATE INDEX item_id ON sync_monitor_triggers (item_id );

ALTER TABLE sync_monitor_triggers ADD PRIMARY KEY (trigger_id);










CREATE TABLE monitor_actions (
action_id NVARCHAR2(50) NOT NULL ,
name NVARCHAR2(500) NOT NULL ,
event_source NVARCHAR2(20) NOT NULL ,
eval_type NVARCHAR2(20) NULL ,
status NVARCHAR2(20) NOT NULL ,
type NVARCHAR2(20) NOT NULL ,
dealer NVARCHAR2(500) NOT NULL ,
trigger_id NVARCHAR2(50) NOT NULL ,
event_type NUMBER(11) NOT NULL 
);
COMMENT ON COLUMN monitor_actions.action_id IS '事件ID';
COMMENT ON COLUMN monitor_actions.name IS '事件名';
COMMENT ON COLUMN monitor_actions.event_source IS '事件来源,0-指来源为触发器trigger,1-指来源为自动发现descover,2-指来源为自动登记auto_register,3-为网络发现产生的事件源';
COMMENT ON COLUMN monitor_actions.eval_type IS '表示执行action的前提条件的逻辑关系,0表示and/or,1表示and,2表示or';
COMMENT ON COLUMN monitor_actions.status IS '状态ON-启动,OFF-禁用';
COMMENT ON COLUMN monitor_actions.type IS '类型,1-回调url,2-函数';
COMMENT ON COLUMN monitor_actions.dealer IS '处理程序,如果type为1，则为url,如果type为2，则为类名.方法名,入参包含事件、指标、触发器信息，需定义';
COMMENT ON COLUMN monitor_actions.trigger_id IS '触发器ID';
COMMENT ON COLUMN monitor_actions.event_type IS '事件类型：,1-异常事件,2-正常事件,3-通用事件';

CREATE INDEX trigger_id_monitor
ON monitor_actions (trigger_id);

ALTER TABLE monitor_actions ADD PRIMARY KEY (action_id);







CREATE TABLE monitor_api_server_config (
api_server_id NVARCHAR2(50) NOT NULL ,
url NVARCHAR2(1000) NOT NULL ,
username NVARCHAR2(200) NOT NULL ,
password NVARCHAR2(200) NOT NULL ,
room NVARCHAR2(200) NOT NULL ,
server_type NVARCHAR2(40) NULL 
);
COMMENT ON COLUMN monitor_api_server_config.api_server_id IS 'api配置ID';
COMMENT ON COLUMN monitor_api_server_config.url IS '服务URL地址';
COMMENT ON COLUMN monitor_api_server_config.username IS '用户名';
COMMENT ON COLUMN monitor_api_server_config.password IS '密码';
COMMENT ON COLUMN monitor_api_server_config.room IS '所属机房域';

ALTER TABLE monitor_api_server_config ADD PRIMARY KEY (api_server_id);











CREATE TABLE monitor_biz_theme (
theme_id NVARCHAR2(50) NOT NULL ,
theme_code NVARCHAR2(255) NOT NULL ,
object_type NVARCHAR2(50) NOT NULL ,
object_id NVARCHAR2(50) NULL ,
index_name NVARCHAR2(255) NOT NULL ,
value_type NVARCHAR2(10) NULL ,
up_type NVARCHAR2(10) NOT NULL ,
up_status NVARCHAR2(10) NULL ,
last_up_value NCLOB NULL ,
last_up_time DATE NULL ,
up_switch NVARCHAR2(10) NULL ,
status NVARCHAR2(10) NULL ,
create_time DATE NULL ,
last_fail_time DATE NULL ,
interval NUMBER(11) NULL ,
dim_ids NVARCHAR2(255) NULL ,
theme_name NVARCHAR2(255) NULL ,
log_reg NCLOB NULL ,
log_content NCLOB NULL ,
update_time DATE NULL ,
delete_flag NVARCHAR2(20) NULL 
);
COMMENT ON COLUMN monitor_biz_theme.theme_id IS '主题ID';
COMMENT ON COLUMN monitor_biz_theme.theme_code IS '主题编码';
COMMENT ON COLUMN monitor_biz_theme.object_type IS '关联对象类型,1-设备ID,2-业务系统';
COMMENT ON COLUMN monitor_biz_theme.object_id IS 'object_id';
COMMENT ON COLUMN monitor_biz_theme.index_name IS 'es索引名';
COMMENT ON COLUMN monitor_biz_theme.up_type IS '上报类型0：接口接入1：日志接入';
COMMENT ON COLUMN monitor_biz_theme.up_status IS '上报状态0：成功1：失败';
COMMENT ON COLUMN monitor_biz_theme.last_up_value IS '最近上报值';
COMMENT ON COLUMN monitor_biz_theme.last_up_time IS '最近上报时间';
COMMENT ON COLUMN monitor_biz_theme.up_switch IS '上报开关0：开启1：关闭';
COMMENT ON COLUMN monitor_biz_theme.status IS '状态0：正式1：临时';
COMMENT ON COLUMN monitor_biz_theme.create_time IS '创建时间';
COMMENT ON COLUMN monitor_biz_theme.last_fail_time IS '最近失败上报时间';
COMMENT ON COLUMN monitor_biz_theme.dim_ids IS '维度id集合';
COMMENT ON COLUMN monitor_biz_theme.log_reg IS '日志正则表达式';
COMMENT ON COLUMN monitor_biz_theme.log_content IS '日志样例内容';
COMMENT ON COLUMN monitor_biz_theme.update_time IS '修改时间';
COMMENT ON COLUMN monitor_biz_theme.delete_flag IS '删除标识1-删除0-未删除';


ALTER TABLE monitor_biz_theme ADD PRIMARY KEY (theme_id);








CREATE TABLE monitor_biz_theme_dim (
id NUMBER(11) NOT NULL ,
dim_name NVARCHAR2(255) NULL ,
dim_type NVARCHAR2(10) NULL ,
theme_id NVARCHAR2(50) NULL ,
dim_order NUMBER(11) NULL ,
dim_reg NVARCHAR2(1000) NULL ,
dim_code NVARCHAR2(100) NULL ,
match_flag NVARCHAR2(20) NULL 
);
COMMENT ON COLUMN monitor_biz_theme_dim.dim_name IS '维度名称';
COMMENT ON COLUMN monitor_biz_theme_dim.dim_type IS '维度类型';
COMMENT ON COLUMN monitor_biz_theme_dim.theme_id IS '主题ID';
COMMENT ON COLUMN monitor_biz_theme_dim.dim_order IS '序号';
COMMENT ON COLUMN monitor_biz_theme_dim.dim_reg IS '主题正则表达式';
COMMENT ON COLUMN monitor_biz_theme_dim.dim_code IS '维度编码';
COMMENT ON COLUMN monitor_biz_theme_dim.match_flag IS '匹配类型1-字段2-非字段';

ALTER TABLE monitor_biz_theme_dim ADD PRIMARY KEY (id);









CREATE TABLE monitor_filter (
filter_id NVARCHAR2(50) NOT NULL ,
name NVARCHAR2(500) NOT NULL ,
status NVARCHAR2(20) NOT NULL ,
type NVARCHAR2(20) NOT NULL ,
dealer NVARCHAR2(500) NOT NULL ,
condition NCLOB NOT NULL 
);
COMMENT ON COLUMN monitor_filter.filter_id IS '过滤器ID';
COMMENT ON COLUMN monitor_filter.name IS '过滤器名称';
COMMENT ON COLUMN monitor_filter.status IS '状态,ON-启动,OFF-禁用';
COMMENT ON COLUMN monitor_filter.type IS '类型,1-回调url,2-函数';
COMMENT ON COLUMN monitor_filter.dealer IS '处理程序,如果type为1，则为url,如果type为2，则为类名.方法名,入参包含事件、指标、触发器信息，需定义';
COMMENT ON COLUMN monitor_filter.condition IS '过滤条件,可以按主机、业务系统等维度设计';











CREATE TABLE monitor_functions (
function_id NVARCHAR2(50) NOT NULL ,
item_id NVARCHAR2(50) NOT NULL ,
trigger_id NVARCHAR2(50) NOT NULL ,
function NVARCHAR2(100) NOT NULL ,
parameter NVARCHAR2(500) NOT NULL 
);
COMMENT ON COLUMN monitor_functions.function_id IS '函数ID';
COMMENT ON COLUMN monitor_functions.item_id IS '监控项ID';
COMMENT ON COLUMN monitor_functions.trigger_id IS '触发器ID';
COMMENT ON COLUMN monitor_functions.function IS '函数';
COMMENT ON COLUMN monitor_functions.parameter IS '参数';
ALTER TABLE monitor_functions ADD PRIMARY KEY (function_id);
CREATE INDEX functions_item_id ON monitor_functions (item_id );
CREATE INDEX functions_trigger_id ON monitor_functions (trigger_id );








CREATE TABLE monitor_items (
item_id NVARCHAR2(50) NOT NULL ,
name NVARCHAR2(100) NOT NULL ,
type NVARCHAR2(20) NOT NULL ,
template_id NVARCHAR2(50) NOT NULL ,
key NVARCHAR2(100) NOT NULL ,
delay NUMBER(11) NULL ,
history NUMBER(11) NULL ,
status NVARCHAR2(20) NOT NULL ,
value_type NVARCHAR2(20) NOT NULL ,
units NVARCHAR2(20) NULL ,
error NCLOB NULL ,
data_type NVARCHAR2(20) NULL ,
sys_type NVARCHAR2(50) NOT NULL ,
calc_type NVARCHAR2(20) NULL ,
biz_index NVARCHAR2(200) NULL ,
biz_calc_obj NCLOB NULL ,
biz_calc_exp NVARCHAR2(200) NULL ,
biz_theme_id NVARCHAR2(50) NULL ,
biz_is_zero NVARCHAR2(50) NULL ,
cron NVARCHAR2(20) NULL ,
biz_group NVARCHAR2(500) NULL ,
biz_theme_exp NCLOB NULL ,
group_flag NVARCHAR2(20) NULL 
);
COMMENT ON COLUMN monitor_items.item_id IS '监控项ID';
COMMENT ON COLUMN monitor_items.name IS '监控项名称';
COMMENT ON COLUMN monitor_items.type IS '监控项类型,SCRIPT-脚本（现有）,INDEX-监控指标（现有）';
COMMENT ON COLUMN monitor_items.template_id IS '模版ID';
COMMENT ON COLUMN monitor_items.key IS '监控键值';
COMMENT ON COLUMN monitor_items.delay IS '监控周期，单位分钟';
COMMENT ON COLUMN monitor_items.history IS '保留时间，单位天';
COMMENT ON COLUMN monitor_items.status IS '状态：,ON-启用,OFF-禁用,NONSUPPORT-不支持（启用状态的监控项采集不到数据）（暂时不用）';
COMMENT ON COLUMN monitor_items.value_type IS '监控项数据格式：,FLOAT-浮点数,STR-字符串,LOG-日志,UINT64-整数,TEXT-文本';
COMMENT ON COLUMN monitor_items.units IS '单位';
COMMENT ON COLUMN monitor_items.error IS '错误信息';
COMMENT ON COLUMN monitor_items.data_type IS '监控项数据类型,DECIMAL-十进制,OCTAL-八进制,HEXADECIMAL-十六进制,BOOLEAN-布尔值';
COMMENT ON COLUMN monitor_items.sys_type IS '接入监控系统类型（目前为zabbix）,MIRROR,ZABBIX,NONE';
COMMENT ON COLUMN monitor_items.biz_group IS '计算项分组[{code:value,name:value},{code:value,name:value}]';
COMMENT ON COLUMN monitor_items.biz_theme_exp IS '指标计算公式[{code:value,name:value,exp:value},{code:value,name:value,exp:value}]';
COMMENT ON COLUMN monitor_items.group_flag IS '是否根据模板关联对象进行分组0：否1：是';

ALTER TABLE monitor_items ADD PRIMARY KEY (item_id);
CREATE INDEX items_template_id ON monitor_items (template_id );







CREATE TABLE monitor_template (
template_id NVARCHAR2(50) NOT NULL ,
name NVARCHAR2(100) NOT NULL ,
description NCLOB NOT NULL ,
create_time TIMESTAMP NOT NULL ,
type NVARCHAR2(20) NOT NULL ,
update_time TIMESTAMP NOT NULL ,
fun_type NVARCHAR2(20) NOT NULL ,
mon_type NVARCHAR2(20) NULL ,
status NVARCHAR2(20) NULL ,
sys_type NVARCHAR2(20) NULL 
);
COMMENT ON COLUMN monitor_template.template_id IS '模版ID';
COMMENT ON COLUMN monitor_template.name IS '模版名称';
COMMENT ON COLUMN monitor_template.description IS '描述';
COMMENT ON COLUMN monitor_template.create_time IS '创建时间';
COMMENT ON COLUMN monitor_template.type IS '模版类型：,1-硬件,2-网络,3-主机操作系统,4-应用';
COMMENT ON COLUMN monitor_template.update_time IS '更新时间';
COMMENT ON COLUMN monitor_template.fun_type IS '功能类型：,1-监控,2-巡检';
COMMENT ON COLUMN monitor_template.mon_type IS '监控类型:1系统2业务';
COMMENT ON COLUMN monitor_template.status IS '模版状态';
COMMENT ON COLUMN monitor_template.sys_type IS '系统类型 ZABBIX PROMETHEUS THEME MIRROR';
ALTER TABLE monitor_template ADD PRIMARY KEY (template_id);










CREATE TABLE monitor_template_data_sync (
sync_id NUMBER(11) NOT NULL ,
sync_data_type NVARCHAR2(50) NOT NULL ,
data_id NVARCHAR2(50) NOT NULL ,
operate_type NVARCHAR2(20) NULL 
);
COMMENT ON COLUMN monitor_template_data_sync.sync_id IS '同步';
COMMENT ON COLUMN monitor_template_data_sync.sync_data_type IS '同步数据类型';
COMMENT ON COLUMN monitor_template_data_sync.data_id IS '同步数据ID';
ALTER TABLE monitor_template_data_sync ADD PRIMARY KEY (sync_id);











CREATE TABLE monitor_template_object (
template_object_id NVARCHAR2(50) NOT NULL ,
template_id NVARCHAR2(50) NOT NULL ,
object_type NVARCHAR2(50) NOT NULL ,
object_id NVARCHAR2(50) NULL 
);
COMMENT ON COLUMN monitor_template_object.template_object_id IS '模版设备关系ID';
COMMENT ON COLUMN monitor_template_object.template_id IS '模版ID';
COMMENT ON COLUMN monitor_template_object.object_type IS '关联对象类型,1-设备ID,2-业务系统';
ALTER TABLE monitor_template_object ADD PRIMARY KEY (template_object_id);











CREATE TABLE monitor_theme_key (
theme_id NVARCHAR2(50) NULL ,
dim_code NVARCHAR2(100) NULL 
);
COMMENT ON COLUMN monitor_theme_key.theme_id IS '主题ID';
COMMENT ON COLUMN monitor_theme_key.dim_code IS '维度编码';








CREATE TABLE monitor_triggers (
trigger_id NVARCHAR2(50) NOT NULL ,
name NCLOB NOT NULL ,
expression NCLOB NOT NULL ,
url NCLOB NULL ,
status NVARCHAR2(20) NOT NULL ,
value NVARCHAR2(20) NULL ,
priority NVARCHAR2(20) NOT NULL ,
item_id NVARCHAR2(50) NOT NULL ,
param NCLOB NULL
);
COMMENT ON COLUMN monitor_triggers.trigger_id IS '触发器ID';
COMMENT ON COLUMN monitor_triggers.name IS '触发器名称';
COMMENT ON COLUMN monitor_triggers.expression IS '表达式';
COMMENT ON COLUMN monitor_triggers.url IS 'URL';
COMMENT ON COLUMN monitor_triggers.status IS '状态：,ON-启用,OFF-禁用';
COMMENT ON COLUMN monitor_triggers.value IS '值类型,OK,PROBLEM,UNKNOWN';
COMMENT ON COLUMN monitor_triggers.priority IS '优先级,0-Not classified,1 -Information,2-Warning,3-Average,4-High,5-Disaster';
COMMENT ON COLUMN monitor_triggers.item_id IS '监控项ID';
COMMENT ON COLUMN monitor_triggers.param IS '脚本类监控触发器的参数值';

CREATE INDEX triggers_item_id ON monitor_triggers (item_id );
ALTER TABLE monitor_triggers ADD FOREIGN KEY (item_id) REFERENCES monitor_items (item_id);










CREATE TABLE alert_alerts (
alert_id NVARCHAR2(64) NOT NULL ,
r_alert_id NVARCHAR2(300) NULL ,
event_id NVARCHAR2(80) NULL ,
action_id NUMBER(20) NULL ,
device_id NVARCHAR2(50) NULL ,
device_class NVARCHAR2(128) NULL ,
biz_sys NVARCHAR2(128) NULL ,
moni_index NCLOB NULL ,
moni_object NVARCHAR2(512) NULL ,
cur_moni_value NVARCHAR2(128) NULL ,
cur_moni_time TIMESTAMP NULL ,
alert_level NVARCHAR2(20) NOT NULL ,
item_id NVARCHAR2(50) NULL ,
remark NVARCHAR2(512) NULL ,
order_status NVARCHAR2(50) NULL ,
operate_status NUMBER(4) NULL ,
source NVARCHAR2(100) NULL ,
idc_type NVARCHAR2(128) NULL ,
source_room NVARCHAR2(100) NULL ,
object_type NVARCHAR2(50) NULL ,
object_id NVARCHAR2(50) NULL ,
region NVARCHAR2(50) NULL ,
device_ip NVARCHAR2(100) NULL ,
order_id NVARCHAR2(100) NULL ,
order_type NVARCHAR2(100) NULL ,
alert_start_time TIMESTAMP NULL ,
alert_count NUMBER(11) NULL 
);
COMMENT ON TABLE alert_alerts IS '告警记录表';
COMMENT ON COLUMN alert_alerts.device_class IS '设备类型';
COMMENT ON COLUMN alert_alerts.biz_sys IS '业务系统';
COMMENT ON COLUMN alert_alerts.moni_index IS '监控指标/内容，关联触发器name';
COMMENT ON COLUMN alert_alerts.moni_object IS '监控对象';
COMMENT ON COLUMN alert_alerts.cur_moni_value IS '当前监控值';
COMMENT ON COLUMN alert_alerts.cur_moni_time IS '当前监控时间';
COMMENT ON COLUMN alert_alerts.alert_level IS '告警级别,1-提示,2-低,3-中,4-高,5-严重';
COMMENT ON COLUMN alert_alerts.remark IS '备注';
COMMENT ON COLUMN alert_alerts.order_status IS '1-未派单,2-处理中,3-已完成';
COMMENT ON COLUMN alert_alerts.operate_status IS '警报操作状态:0-待确认,1-已确认,2-待解决,3-已处理';
COMMENT ON COLUMN alert_alerts.source IS '告警来源,MIRROR,ZABBIX';
COMMENT ON COLUMN alert_alerts.idc_type IS '所属位置-资源池';
COMMENT ON COLUMN alert_alerts.source_room IS '机房/资源池';
COMMENT ON COLUMN alert_alerts.object_type IS '告警类型,1-系统,2-业务';
COMMENT ON COLUMN alert_alerts.object_id IS '对象ID，如果是设备告警则是设备ID，如果是业务则是业务系统code';
COMMENT ON COLUMN alert_alerts.region IS '域/资源池code';
COMMENT ON COLUMN alert_alerts.alert_start_time IS '告警开始时间';

ALTER TABLE alert_alerts ADD PRIMARY KEY (alert_id);
CREATE INDEX index_r_alert_id ON alert_alerts (r_alert_id );







CREATE TABLE alert_alerts_detail (
id NUMBER(20) NOT NULL ,
alert_id NVARCHAR2(64) NOT NULL ,
action_id NUMBER(20) NULL ,
event_id NVARCHAR2(80) NULL ,
moni_index NCLOB NULL ,
moni_object NVARCHAR2(512) NULL ,
cur_moni_value NVARCHAR2(128) NULL ,
cur_moni_time TIMESTAMP NULL ,
alert_level NVARCHAR2(20) NOT NULL ,
item_id NVARCHAR2(50) NULL 
);
COMMENT ON TABLE alert_alerts_detail IS '告警上报记录表';
COMMENT ON COLUMN alert_alerts_detail.moni_index IS '监控指标/内容，关联触发器name';
COMMENT ON COLUMN alert_alerts_detail.moni_object IS '监控对象';
COMMENT ON COLUMN alert_alerts_detail.cur_moni_value IS '当前监控值';
COMMENT ON COLUMN alert_alerts_detail.cur_moni_time IS '当前监控时间';
COMMENT ON COLUMN alert_alerts_detail.alert_level IS '告警级别,1-提示,2-低,3-中,4-高,5-严重';

CREATE INDEX index_alert_id ON alert_alerts_detail (alert_id );
ALTER TABLE alert_alerts_detail ADD PRIMARY KEY (id);








CREATE TABLE alert_alerts_his (
alert_id NVARCHAR2(64) NOT NULL ,
r_alert_id NVARCHAR2(300) NULL ,
event_id NVARCHAR2(80) NULL ,
action_id NUMBER(20) NULL ,
device_id NVARCHAR2(50) NULL ,
device_class NVARCHAR2(128) NULL ,
biz_sys NVARCHAR2(128) NULL ,
moni_index NCLOB NOT NULL ,
moni_object NVARCHAR2(512) NULL ,
cur_moni_value NVARCHAR2(128) NULL ,
cur_moni_time TIMESTAMP NOT NULL ,
alert_level NVARCHAR2(20) NOT NULL ,
item_id NVARCHAR2(50) NULL ,
alert_end_time TIMESTAMP NULL ,
remark NVARCHAR2(512) NULL ,
order_status NVARCHAR2(50) NULL ,
clear_time TIMESTAMP NULL ,
clear_user NVARCHAR2(32) NULL ,
clear_content NVARCHAR2(256) NULL ,
source NVARCHAR2(100) NULL ,
idc_type NVARCHAR2(128) NULL ,
source_room NVARCHAR2(100) NULL ,
object_type NVARCHAR2(50) NULL ,
object_id NVARCHAR2(50) NULL ,
region NVARCHAR2(50) NULL ,
device_ip NVARCHAR2(100) NULL ,
order_type NVARCHAR2(20) NULL ,
order_id NVARCHAR2(100) NULL ,
biz_sys_desc NVARCHAR2(200) NULL ,
alert_start_time TIMESTAMP NULL ,
alert_count NUMBER(11) NULL 
);
COMMENT ON TABLE alert_alerts_his IS '告警历史记录表';
COMMENT ON COLUMN alert_alerts_his.device_class IS '设备类型';
COMMENT ON COLUMN alert_alerts_his.biz_sys IS '业务系统';
COMMENT ON COLUMN alert_alerts_his.moni_index IS '监控指标/内容，关联触发器name';
COMMENT ON COLUMN alert_alerts_his.moni_object IS '监控对象';
COMMENT ON COLUMN alert_alerts_his.cur_moni_value IS '当前监控值';
COMMENT ON COLUMN alert_alerts_his.cur_moni_time IS '当前监控时间';
COMMENT ON COLUMN alert_alerts_his.alert_level IS '告警级别,1-提示,2-低,3-中,4-高,5-严重';
COMMENT ON COLUMN alert_alerts_his.alert_end_time IS '告警结束时间';
COMMENT ON COLUMN alert_alerts_his.remark IS '备注';
COMMENT ON COLUMN alert_alerts_his.order_status IS '1-未派单,2-处理中,3-已完成';
COMMENT ON COLUMN alert_alerts_his.clear_time IS '清除时间';
COMMENT ON COLUMN alert_alerts_his.clear_user IS '清除人';
COMMENT ON COLUMN alert_alerts_his.clear_content IS '清除内容';
COMMENT ON COLUMN alert_alerts_his.source IS '告警来源,MIRROR';
COMMENT ON COLUMN alert_alerts_his.idc_type IS '所属位置-资源池';
COMMENT ON COLUMN alert_alerts_his.source_room IS '机房';
COMMENT ON COLUMN alert_alerts_his.order_type IS '工单类型,1-告警,2-故障';
COMMENT ON COLUMN alert_alerts_his.order_id IS '工单ID';
COMMENT ON COLUMN alert_alerts_his.biz_sys_desc IS '业务系统名称（中文）';
COMMENT ON COLUMN alert_alerts_his.alert_start_time IS '告警开始时间';
CREATE INDEX index_alert_alerts_his ON alert_alerts_his (r_alert_id );
ALTER TABLE alert_alerts_his ADD PRIMARY KEY (alert_id);








CREATE TABLE alert_alerts_statistics_sync (
idcType NVARCHAR2(300) NULL ,
alertTotal NUMBER(20) NULL ,
seriousCount NUMBER(20) NULL ,
importantCount NUMBER(20) NULL ,
secondaryCount NUMBER(20) NULL ,
tipsCount NUMBER(20) NULL ,
month NVARCHAR2(24) NULL 
);
COMMENT ON TABLE alert_alerts_statistics_sync IS '告警记录表';
COMMENT ON COLUMN alert_alerts_statistics_sync.seriousCount IS '严重告警';
COMMENT ON COLUMN alert_alerts_statistics_sync.importantCount IS '重要告警';
COMMENT ON COLUMN alert_alerts_statistics_sync.secondaryCount IS '次要告警';
COMMENT ON COLUMN alert_alerts_statistics_sync.tipsCount IS '提示告警';
COMMENT ON COLUMN alert_alerts_statistics_sync.month IS '告警产生月份';
CREATE INDEX index_statistics_alert_id ON alert_alerts_statistics_sync (idcType );









CREATE TABLE alert_config (
ID NVARCHAR2(255) NOT NULL ,
TITLE NVARCHAR2(64) NULL ,
DESCRIPTION NCLOB NULL ,
CREATOR NVARCHAR2(64) NULL ,
CRATE_TIME TIMESTAMP NULL ,
IS_START NCHAR(1) NULL 
);
ALTER TABLE alert_config ADD PRIMARY KEY (ID);








CREATE TABLE alert_config_detail (
ID NVARCHAR2(32) NOT NULL ,
ALERT_CONFIG_ID NVARCHAR2(255) NULL ,
CONFIG_TYPE NCHAR(1) NULL ,
TARGET_ID NCLOB NULL ,
MONIT_TYPE NVARCHAR2(64) NULL ,
ALERT_LEVEL NVARCHAR2(32) NULL 
);

ALTER TABLE alert_config_detail ADD PRIMARY KEY (ID);
CREATE INDEX ALERT_CONFIG_ID_f ON alert_config_detail (ALERT_CONFIG_ID );






CREATE TABLE alert_devicetype_config (
id NVARCHAR2(56) NOT NULL ,
code NVARCHAR2(128) NOT NULL ,
name NVARCHAR2(255) NULL ,
type NVARCHAR2(255) NULL 
);
COMMENT ON COLUMN alert_devicetype_config.code IS '表里面存的名称';
COMMENT ON COLUMN alert_devicetype_config.name IS '展示名称';
COMMENT ON COLUMN alert_devicetype_config.type IS '类型：网络、物理服务器等';








CREATE TABLE alert_device_top_sync (
idcType NVARCHAR2(300) NULL ,
alertLevel NUMBER(4) NULL ,
deviceType NVARCHAR2(10) NULL ,
countOrder NUMBER(11) NULL ,
ip NVARCHAR2(128) NULL ,
pod NVARCHAR2(128) NULL ,
alertCount NUMBER(20) NULL ,
roomId NVARCHAR2(128) NULL ,
month NVARCHAR2(24) NULL ,
deviceMfrs NVARCHAR2(255) NULL 
);
COMMENT ON TABLE alert_device_top_sync IS '告警记录表';
COMMENT ON COLUMN alert_device_top_sync.idcType IS '资源池';
COMMENT ON COLUMN alert_device_top_sync.alertLevel IS '告警级别';
COMMENT ON COLUMN alert_device_top_sync.deviceType IS '设备分类';
COMMENT ON COLUMN alert_device_top_sync.countOrder IS '排名';
COMMENT ON COLUMN alert_device_top_sync.ip IS '设备ip';
COMMENT ON COLUMN alert_device_top_sync.pod IS 'pod';
COMMENT ON COLUMN alert_device_top_sync.alertCount IS '告警数量';
COMMENT ON COLUMN alert_device_top_sync.roomId IS '机房位置';
COMMENT ON COLUMN alert_device_top_sync.month IS '告警产生时间';
COMMENT ON COLUMN alert_device_top_sync.deviceMfrs IS '厂家';

CREATE INDEX index_device_alert_id ON alert_device_top_sync (idcType );






CREATE TABLE alert_distribution_sync (
idcType NVARCHAR2(128) NULL ,
alertLevel NUMBER(4) NULL ,
deviceType NVARCHAR2(128) NULL ,
alertCount NUMBER(20) NULL ,
month NVARCHAR2(24) NULL 
);
COMMENT ON TABLE alert_distribution_sync IS '告警记录表';
COMMENT ON COLUMN alert_distribution_sync.deviceType IS '物理机';
COMMENT ON COLUMN alert_distribution_sync.alertCount IS '防火墙';
CREATE INDEX index_distribution_alert_id ON alert_distribution_sync (idcType );







CREATE TABLE alert_filter (
id NUMBER(4) NOT NULL ,
name NVARCHAR2(64) NOT NULL ,
creater NVARCHAR2(64) NULL ,
editer NVARCHAR2(64) NULL ,
created_at TIMESTAMP NULL ,
updated_at TIMESTAMP NULL ,
note NVARCHAR2(255) NULL 
);
ALTER TABLE alert_filter ADD PRIMARY KEY (id);









CREATE TABLE alert_filter_option (
id NUMBER(4) NOT NULL ,
name NVARCHAR2(64) NOT NULL ,
type NVARCHAR2(64) NOT NULL ,
code NVARCHAR2(64) NOT NULL ,
operate NVARCHAR2(255) NOT NULL ,
source NVARCHAR2(255) NULL ,
content NVARCHAR2(255) NULL ,
method NVARCHAR2(64) NULL ,
status NUMBER(4) NULL ,
jdbc_type NVARCHAR2(64) NULL 
);
COMMENT ON COLUMN alert_filter_option.name IS '筛选条件名称';
COMMENT ON COLUMN alert_filter_option.type IS '展示类型select,string,datetime';
COMMENT ON COLUMN alert_filter_option.operate IS '操作：大于、小于、等于、大于等于、小于等于...分号分割';
COMMENT ON COLUMN alert_filter_option.source IS '数据来源';
COMMENT ON COLUMN alert_filter_option.content IS '配置数据';
COMMENT ON COLUMN alert_filter_option.status IS '1:使用，0：停用';
COMMENT ON COLUMN alert_filter_option.jdbc_type IS '数据类型:string,number';

ALTER TABLE alert_filter_option ADD PRIMARY KEY (id);









CREATE TABLE alert_filter_scene (
id NUMBER(4) NOT NULL ,
name NVARCHAR2(64) NOT NULL ,
filter_id NUMBER(4) NULL ,
option_condition NCLOB NOT NULL ,
operate_user NVARCHAR2(255) NULL ,
creater NVARCHAR2(64) NULL ,
editer NVARCHAR2(64) NULL ,
created_at TIMESTAMP NULL ,
updated_at TIMESTAMP NULL ,
note NVARCHAR2(255) NULL 
);
COMMENT ON TABLE alert_filter_scene IS '告警过滤场景表';
COMMENT ON COLUMN alert_filter_scene.name IS '场景名称';
COMMENT ON COLUMN alert_filter_scene.filter_id IS '过滤器id';
COMMENT ON COLUMN alert_filter_scene.option_condition IS '过滤条件';
COMMENT ON COLUMN alert_filter_scene.operate_user IS '维护用户';
ALTER TABLE alert_filter_scene ADD PRIMARY KEY (id);








CREATE TABLE alert_mail_recipients (
id NUMBER(11) NOT NULL ,
recipient NVARCHAR2(64) NOT NULL ,
password NVARCHAR2(64) NOT NULL ,
mail_server NVARCHAR2(64) NOT NULL ,
receive_protocal NUMBER(4) NULL ,
receive_port NUMBER(11) NOT NULL ,
status NUMBER(4) NULL ,
recipient_desc NVARCHAR2(128) NULL ,
period NUMBER(11) NULL ,
period_unit NUMBER(4) NOT NULL ,
create_time TIMESTAMP NOT NULL ,
update_time TIMESTAMP NOT NULL 
);
COMMENT ON TABLE alert_mail_recipients IS '邮件采集账号表';
COMMENT ON COLUMN alert_mail_recipients.id IS '主键ID';
COMMENT ON COLUMN alert_mail_recipients.recipient IS '收件邮箱';
COMMENT ON COLUMN alert_mail_recipients.password IS '收件邮箱密码';
COMMENT ON COLUMN alert_mail_recipients.mail_server IS '收件邮箱邮件服务器';
COMMENT ON COLUMN alert_mail_recipients.receive_protocal IS '收件协议, 0: pop3, 1: imap';
COMMENT ON COLUMN alert_mail_recipients.receive_port IS '收件邮箱邮件服务器收件端口';
COMMENT ON COLUMN alert_mail_recipients.status IS '是否启用, 0: 未启用, 1: 启用';
COMMENT ON COLUMN alert_mail_recipients.recipient_desc IS '收件邮箱描述';
COMMENT ON COLUMN alert_mail_recipients.period IS '邮箱采集周期-单位:分钟';
COMMENT ON COLUMN alert_mail_recipients.period_unit IS '周期时间单位 0:分钟,1:小时,2:天';
ALTER TABLE alert_mail_recipients ADD PRIMARY KEY (id);








CREATE TABLE alert_mail_resolve_filter (
id NVARCHAR2(50) NOT NULL ,
receiver NVARCHAR2(64) NOT NULL ,
sender NVARCHAR2(64) NOT NULL ,
title_incl NVARCHAR2(64) NOT NULL ,
content_incl NVARCHAR2(64) NULL ,
status NUMBER(4) NULL 
);
COMMENT ON TABLE alert_mail_resolve_filter IS '告警采集配置表';
COMMENT ON COLUMN alert_mail_resolve_filter.id IS '主键ID';
COMMENT ON COLUMN alert_mail_resolve_filter.receiver IS '收件邮箱';
COMMENT ON COLUMN alert_mail_resolve_filter.sender IS '发件邮箱';
COMMENT ON COLUMN alert_mail_resolve_filter.title_incl IS '邮件过滤:标题包含关键字';
COMMENT ON COLUMN alert_mail_resolve_filter.content_incl IS '邮件过滤:内容包含关键字';
COMMENT ON COLUMN alert_mail_resolve_filter.status IS '是否启用, 0: 未启用, 1: 启用';
ALTER TABLE alert_mail_resolve_filter ADD PRIMARY KEY (id);










CREATE TABLE alert_mail_resolve_record (
id NUMBER(11) NOT NULL ,
filter_id NVARCHAR2(50) NOT NULL ,
mail_title NVARCHAR2(64) NOT NULL ,
mail_content NCLOB NULL ,
mail_sender NVARCHAR2(64) NOT NULL ,
mail_receiver NVARCHAR2(64) NOT NULL ,
mail_send_time TIMESTAMP NOT NULL ,
resolve_time TIMESTAMP NOT NULL ,
device_ip NVARCHAR2(64) NOT NULL ,
moni_index NVARCHAR2(64) NULL ,
moni_object NVARCHAR2(128) NULL ,
alert_level NVARCHAR2(20) NOT NULL ,
alert_id NVARCHAR2(64) NOT NULL 
);
COMMENT ON TABLE alert_mail_resolve_record IS '邮件告警采集记录表';
COMMENT ON COLUMN alert_mail_resolve_record.id IS '主键ID';
COMMENT ON COLUMN alert_mail_resolve_record.filter_id IS 'Filter主键ID';
COMMENT ON COLUMN alert_mail_resolve_record.mail_title IS '邮件标题';
COMMENT ON COLUMN alert_mail_resolve_record.mail_content IS '邮件内容';
COMMENT ON COLUMN alert_mail_resolve_record.mail_sender IS '发件人';
COMMENT ON COLUMN alert_mail_resolve_record.mail_receiver IS '收件人';
COMMENT ON COLUMN alert_mail_resolve_record.mail_send_time IS '发件时间';
COMMENT ON COLUMN alert_mail_resolve_record.resolve_time IS '采集时间';
COMMENT ON COLUMN alert_mail_resolve_record.device_ip IS '设备IP';
COMMENT ON COLUMN alert_mail_resolve_record.moni_index IS '告警描述/告警内容';
COMMENT ON COLUMN alert_mail_resolve_record.moni_object IS '告警指标';
COMMENT ON COLUMN alert_mail_resolve_record.alert_level IS '告警级别,1-提示,2-低,3-中,4-高,5-严重';
COMMENT ON COLUMN alert_mail_resolve_record.alert_id IS '告警编号';
ALTER TABLE alert_mail_resolve_record ADD PRIMARY KEY (id);











CREATE TABLE alert_mail_resolve_strategy (
id NUMBER(11) NOT NULL ,
filter_id NVARCHAR2(50) NOT NULL ,
alert_field NVARCHAR2(64) NOT NULL ,
mail_field NUMBER(4) NULL ,
field_match_val NVARCHAR2(64) NULL ,
use_reg NUMBER(4) NULL ,
field_match_reg NVARCHAR2(64) NULL ,
field_match_target NVARCHAR2(64) NULL 
);
COMMENT ON TABLE alert_mail_resolve_strategy IS '告警采集自定义规则表';
COMMENT ON COLUMN alert_mail_resolve_strategy.id IS '主键ID';
COMMENT ON COLUMN alert_mail_resolve_strategy.filter_id IS 'Filter主键ID';
COMMENT ON COLUMN alert_mail_resolve_strategy.alert_field IS '匹配警报的字段名';
COMMENT ON COLUMN alert_mail_resolve_strategy.mail_field IS '邮件的字段名, 0: 标题, 1: 内容, 2:发件人, 3:发件时间';
COMMENT ON COLUMN alert_mail_resolve_strategy.field_match_val IS '配置字段的值';
COMMENT ON COLUMN alert_mail_resolve_strategy.use_reg IS '使用正则匹配, 0: 不使用, 1:使用';
COMMENT ON COLUMN alert_mail_resolve_strategy.field_match_reg IS '自定义正则表达式';
COMMENT ON COLUMN alert_mail_resolve_strategy.field_match_target IS '字段匹配后映射值';
ALTER TABLE alert_mail_resolve_strategy ADD PRIMARY KEY (id);










CREATE TABLE alert_mail_substance (
id NUMBER(11) NOT NULL ,
receiver NVARCHAR2(64) NOT NULL ,
sender NVARCHAR2(64) NOT NULL ,
send_time DATE NULL ,
uuid NVARCHAR2(64) NULL ,
subject NVARCHAR2(64) NULL ,
content NCLOB NULL ,
create_time DATE NOT NULL 
);
COMMENT ON TABLE alert_mail_substance IS '邮件读取记录表';
COMMENT ON COLUMN alert_mail_substance.id IS '主键ID';
COMMENT ON COLUMN alert_mail_substance.receiver IS '收件邮箱';
COMMENT ON COLUMN alert_mail_substance.sender IS '发件邮箱';
COMMENT ON COLUMN alert_mail_substance.send_time IS '发件时间';
COMMENT ON COLUMN alert_mail_substance.uuid IS '邮件唯一标识';
COMMENT ON COLUMN alert_mail_substance.subject IS '邮件标题';
COMMENT ON COLUMN alert_mail_substance.content IS '邮件内容';

ALTER TABLE alert_mail_substance ADD PRIMARY KEY (id);






CREATE TABLE alert_manual_statistic (
room_id NVARCHAR2(100) NOT NULL ,
source_alert_id NVARCHAR2(150) NOT NULL ,
auto_count NUMBER(11) NULL ,
manual_count NUMBER(11) NOT NULL 
);
COMMENT ON COLUMN alert_manual_statistic.room_id IS '机房id';
COMMENT ON COLUMN alert_manual_statistic.source_alert_id IS '原始告警id';
COMMENT ON COLUMN alert_manual_statistic.auto_count IS '自动清除次数';
COMMENT ON COLUMN alert_manual_statistic.manual_count IS '手工清除次数';
CREATE UNIQUE INDEX unique_idx_manual_statistic ON alert_manual_statistic (room_id , source_alert_id );









CREATE TABLE alert_moniter_top_sync (
idcType NVARCHAR2(300) NULL ,
alertLevel NUMBER(4) NULL ,
deviceType NVARCHAR2(10) NULL ,
countOrder NUMBER(11) NULL ,
moniterObject NVARCHAR2(128) NULL ,
moniterAlertCount NUMBER(20) NULL ,
rate NUMBER NULL ,
month NVARCHAR2(24) NULL 
);
COMMENT ON TABLE alert_moniter_top_sync IS '告警记录表';
COMMENT ON COLUMN alert_moniter_top_sync.moniterObject IS '监控对象';
COMMENT ON COLUMN alert_moniter_top_sync.moniterAlertCount IS '告警数量';
COMMENT ON COLUMN alert_moniter_top_sync.rate IS '告警占比';
CREATE INDEX index_moniter_top_sync_id ON alert_moniter_top_sync (idcType );












CREATE TABLE alert_notify_config (
uuid NVARCHAR2(255) NOT NULL ,
name NVARCHAR2(255) NULL ,
is_open NUMBER(11) NULL ,
alert_filter_id NUMBER(11) NULL ,
alert_filter_scene_id NUMBER(11) NULL ,
notify_alert_type NVARCHAR2(255) NULL ,
notify_type NVARCHAR2(255) NULL ,
is_recurrence_interval NUMBER(11) NULL ,
recurrence_interval NVARCHAR2(255) NULL ,
recurrence_interval_util NVARCHAR2(255) NULL ,
resend_time NVARCHAR2(255) NULL ,
email_type NUMBER(11) NULL ,
send_operation NVARCHAR2(10) NULL ,
cur_send_time TIMESTAMP NULL ,
create_time TIMESTAMP NULL 
);
COMMENT ON TABLE alert_notify_config IS '告警通知策略配置表';
COMMENT ON COLUMN alert_notify_config.uuid IS 'uuid';
COMMENT ON COLUMN alert_notify_config.name IS '策略配置名称';
COMMENT ON COLUMN alert_notify_config.is_open IS '是否启用 0-关闭(默认) 1-启用';
COMMENT ON COLUMN alert_notify_config.alert_filter_id IS '告警筛选器id';
COMMENT ON COLUMN alert_notify_config.alert_filter_scene_id IS '告警筛选场景id';
COMMENT ON COLUMN alert_notify_config.notify_alert_type IS '告警通知类型(0-转派, 1-确认,2-派发工单, 3-清除, 4-通知, 5-过滤, 6-工程, 7-维护模式)';
COMMENT ON COLUMN alert_notify_config.notify_type IS '通知类型 0-邮件/短信 1-邮件 2-短信';
COMMENT ON COLUMN alert_notify_config.is_recurrence_interval IS '是否重发  0-关闭(默认) 1-启用';
COMMENT ON COLUMN alert_notify_config.recurrence_interval IS '重发间隔时间';
COMMENT ON COLUMN alert_notify_config.recurrence_interval_util IS '重发间隔单位';
COMMENT ON COLUMN alert_notify_config.email_type IS '邮件发送类型 1-合并 2-单条';
COMMENT ON COLUMN alert_notify_config.send_operation IS '发送记录';
COMMENT ON COLUMN alert_notify_config.cur_send_time IS '最新发送时间';
COMMENT ON COLUMN alert_notify_config.create_time IS '创建时间';

ALTER TABLE alert_notify_config ADD PRIMARY KEY (uuid);












CREATE TABLE alert_notify_config_receiver (
uuid NVARCHAR2(255) NOT NULL ,
alert_notify_config_id NVARCHAR2(255) NOT NULL ,
notify_obj_type NVARCHAR2(255) NULL ,
notify_obj_info NVARCHAR2(255) NULL ,
notify_type NVARCHAR2(10) NULL 
);
COMMENT ON TABLE alert_notify_config_receiver IS '告警通知策略配置人员表';
COMMENT ON COLUMN alert_notify_config_receiver.uuid IS 'uuid';
COMMENT ON COLUMN alert_notify_config_receiver.alert_notify_config_id IS '告警通知策略配置表uuid';
COMMENT ON COLUMN alert_notify_config_receiver.notify_obj_type IS '通知对象类型 1-团队 2-个人';
COMMENT ON COLUMN alert_notify_config_receiver.notify_obj_info IS '通知对象信息';
COMMENT ON COLUMN alert_notify_config_receiver.notify_type IS '通知类型 0-邮件/短信 1-邮件 2-短信';

ALTER TABLE alert_notify_config_receiver ADD PRIMARY KEY (uuid);









CREATE TABLE alert_operation_record (
id NUMBER(20) NOT NULL ,
alert_id NVARCHAR2(200) NOT NULL ,
user_id NVARCHAR2(32) NULL ,
user_name NVARCHAR2(32) NULL ,
operation_type NUMBER(4) NOT NULL ,
operation_time TIMESTAMP NULL ,
operation_status NUMBER(4) NULL ,
content NVARCHAR2(256) NULL 
);
COMMENT ON COLUMN alert_operation_record.alert_id IS '告警id';
COMMENT ON COLUMN alert_operation_record.user_id IS '操作人';
COMMENT ON COLUMN alert_operation_record.user_name IS '操作人名称';
COMMENT ON COLUMN alert_operation_record.operation_type IS '操作类型 0-转派, 1-确认,2-派发工单, 3-清除, 4-通知, 5-过滤, 6-工程, 7-维护模式';
COMMENT ON COLUMN alert_operation_record.operation_time IS '操作时间';
COMMENT ON COLUMN alert_operation_record.operation_status IS '操作状态 0-失败 1-成功,';
COMMENT ON COLUMN alert_operation_record.content IS '操作内容';

CREATE INDEX index_operation_record_id ON alert_operation_record (alert_id );
ALTER TABLE alert_operation_record ADD PRIMARY KEY (id);








CREATE TABLE alert_proxy_idc (
id NUMBER(20) NOT NULL ,
proxy_name NVARCHAR2(64) NULL ,
idc NVARCHAR2(64) NULL ,
remark NVARCHAR2(256) NULL 
);
COMMENT ON TABLE alert_proxy_idc IS '告警代理名称和资源池对应表';

ALTER TABLE alert_proxy_idc ADD PRIMARY KEY (id);










CREATE TABLE alert_report_operate_record (
ID NUMBER(11) NOT NULL ,
ALERT_ID NVARCHAR2(200) NULL ,
USER_ID NVARCHAR2(32) NULL ,
USER_NAME NVARCHAR2(32) NULL ,
REPORT_TYPE NUMBER(4) NULL ,
USER_EMAIL NVARCHAR2(32) NULL ,
DESTINATION NVARCHAR2(64) NULL ,
MESSAGE NCLOB NULL ,
STATUS NUMBER(4) NULL ,
CREATE_TIME TIMESTAMP NULL 
);
COMMENT ON TABLE alert_report_operate_record IS '告警通知操作记录表';
COMMENT ON COLUMN alert_report_operate_record.ALERT_ID IS '警报ID';
COMMENT ON COLUMN alert_report_operate_record.USER_ID IS '发件人ID';
COMMENT ON COLUMN alert_report_operate_record.USER_NAME IS '发件人姓名';
COMMENT ON COLUMN alert_report_operate_record.REPORT_TYPE IS '通知类型, 0:短信,1:邮件';
COMMENT ON COLUMN alert_report_operate_record.USER_EMAIL IS '发件人邮箱';
COMMENT ON COLUMN alert_report_operate_record.DESTINATION IS '短信/邮件 地址';
COMMENT ON COLUMN alert_report_operate_record.MESSAGE IS '短信/邮件 内容';
COMMENT ON COLUMN alert_report_operate_record.STATUS IS '发送状态,0:失败,1:成功';

CREATE INDEX index_operate_id ON alert_report_operate_record (ALERT_ID );
ALTER TABLE alert_report_operate_record ADD PRIMARY KEY (ID);











CREATE TABLE alert_transfer (
id NUMBER(20) NOT NULL ,
alert_id NVARCHAR2(200) NOT NULL ,
user_id NVARCHAR2(32) NULL ,
user_name NVARCHAR2(32) NULL ,
confirm_user_id NVARCHAR2(32) NULL ,
confirm_user_name NVARCHAR2(32) NULL ,
operation_time TIMESTAMP NULL 
);
COMMENT ON COLUMN alert_transfer.alert_id IS '告警id';
COMMENT ON COLUMN alert_transfer.user_id IS '操作人id';
COMMENT ON COLUMN alert_transfer.user_name IS '操作人名称';
COMMENT ON COLUMN alert_transfer.confirm_user_id IS '待确认人id';
COMMENT ON COLUMN alert_transfer.confirm_user_name IS '待确认人name';
COMMENT ON COLUMN alert_transfer.operation_time IS '操作时间';

ALTER TABLE alert_transfer ADD PRIMARY KEY (id);






CREATE TABLE alert_work_config (
uuid NVARCHAR2(255) NOT NULL ,
day_start_time NVARCHAR2(255) NULL ,
day_end_time NVARCHAR2(255) NULL ,
night_start_time NVARCHAR2(255) NULL ,
night_end_time NVARCHAR2(255) NULL 
);
COMMENT ON TABLE alert_work_config IS '值班人员配置表';
COMMENT ON COLUMN alert_work_config.uuid IS 'uuid';
COMMENT ON COLUMN alert_work_config.day_start_time IS '白班 开始时间';
COMMENT ON COLUMN alert_work_config.day_end_time IS '白班 结束时间内';
COMMENT ON COLUMN alert_work_config.night_start_time IS '夜班 开始时间';
COMMENT ON COLUMN alert_work_config.night_end_time IS '夜班 结束时间';
ALTER TABLE alert_work_config ADD PRIMARY KEY (uuid);





CREATE TABLE monitor_log_theme_flush_time (
	flush_time VARCHAR2(50) 
);
COMMENT ON COLUMN monitor_log_theme_flush_time.flush_time IS '刷新时间';
COMMENT ON TABLE code_dict IS '编码字典';


alter table alert_alerts add (message_open varchar2(10)); 
comment on COLUMN alert_alerts.message_open is '是否发短信，0：不需要发送短信；1：需要发送短信';

alter table alert_alerts add (message_send varchar2(10));
comment on COLUMN alert_alerts.message_send is '是否已发短信，0：未发；1：已发';

alter table alert_alerts add (mail_open varchar2(10)) ;
comment on COLUMN alert_alerts.mail_open is '是否需要发送邮件，0：不需要发送邮件；1：需要发送邮件';

alter table alert_alerts add (mail_send varchar2(10)) ;
comment on COLUMN alert_alerts.mail_send is '是否已发邮件，0：未发；1：已发';


alter table alert_alerts_his add (message_open varchar2(10)) ;
comment on COLUMN alert_alerts_his.message_open is '是否发短信，0：不需要发送短信；1：需要发送短信';

alter table alert_alerts_his add (message_send varchar2(10)) ;
comment on COLUMN alert_alerts_his.message_send is '是否已发短信，0：未发；1：已发';

alter table alert_alerts_his add (mail_open varchar2(10)) ;
comment on COLUMN alert_alerts_his.mail_open is '是否需要发送邮件，0：不需要发送邮件；1：需要发送邮件';

alter table alert_alerts_his add (mail_send varchar2(10)) ;
comment on COLUMN alert_alerts_his.mail_send is '是否已发邮件，0：未发；1：已发';



CREATE TABLE alert_notify_config_rule (
id NVARCHAR2(10) NOT NULL ,
rule_name NVARCHAR2(255) NULL ,
rule_cron NVARCHAR2(255) NULL
);


COMMENT ON COLUMN  alert_notify_config_rule.id  IS 'id';
COMMENT ON COLUMN  alert_notify_config_rule.rule_name  IS '定时任务规则名称';
COMMENT ON COLUMN  alert_notify_config_rule.rule_cron  IS 'cron';


CREATE SEQUENCE SEQ_THEME_DIM MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_ALERT_FILTER MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_ALERT_FILTER_SCENE MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_ALERT_ALERTS_DETAIL MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_ALERT_MAIL_RECIPIENTS MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_ALERT_MAIL_RESOLVE_RECORD MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_ALERT_MAIL_RESOLVE_STRATEGY MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_ALERT_REPORT_OPERATE_RECORD MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_ALERT_OPERATION_RECORD MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_ALERT_TRANSFER MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_DATA_SYNC_MARK MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_TEMPLATE_DATA_SYNC MINVALUE 1 START WITH 1 INCREMENT BY 1 NOCACHE;
