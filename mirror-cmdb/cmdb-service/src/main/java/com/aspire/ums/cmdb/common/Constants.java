package com.aspire.ums.cmdb.common;

import java.util.concurrent.TimeUnit;

/**
 * 公共静态变量
 *
 * @author lupeng
 */
public class Constants {

    // 表单类型
    public static final String CODE_CONTROL_TYPE_LISTSEL = "listSel"; // 下拉菜单

    public static final String CODE_CONTROL_TYPE_SINGLESEL = "singleSel"; // 单选

    public static final String CODE_CONTROL_TYPE_MULTISEL = "multiSel"; // 多选

    public static final String CODE_CONTROL_TYPE_DATETIME = "dateTime"; // 时间

    public static final String CODE_CONTROL_TYPE_CASCADER = "cascader"; // 级联

    public static final String CODE_CONTROL_TYPE_IP = "ip"; // 级联

    // 表单验证方法
    public static final String CODE_VALIDATE_REQUIRE = "isRequire"; // 非空

    public static final String CODE_VALIDATE_NUMBER = "isNumber"; // 数字

    public static final String CODE_VALIDATE_IP = "isIp"; // IP

    public static final String CODE_VALIDATE_EMAIL = "isEmail"; // Email

    public static final String CODE_VALIDATE_PHONE = "isPhone"; // Phone

    // 数据源类型
    public static final String CODE_SOURCE_TYPE_DICT = "数据字典"; // 数据字典

    public static final String CODE_SOURCE_TYPE_TABLE = "数据表"; // 数据表

    public static final String CODE_SOURCE_TYPE_REF_MODULE = "引用模型"; // 引用模型

    // 默认码表长度
    public static final Integer CODE_DEFAULT_LENGTH = 40; // 默认码表长度

    // 日期格式
    public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String DATE_PATTERN_YYYY_MM_DD_01 = "yyyy/MM/dd";

    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    // 主机使用年限
    public static final Integer CI_DEFAULT_USE_TIME = 5;

    // CI维保相关字段
    public static final String CI_ID = "id"; // ID

    public static final String CI_INSERT_TIME = "insert_time"; // 录入时间

    public static final String CI_UPDATE_TIME = "update_time"; // 修改时间

    public static final String CI_MAINTEN_START_TIME = "online_time"; // 服务开始时间

    public static final String CI_MAINTEN_END_TIME = "maintenance_time"; // 服务结束时间

    public static final String CI_MAINTEN_PROVIDER = "provider"; // 服务供应商名称

    public static final String CI_MAINTEN_PROVIDER_CONT = "provider_cont"; // 服务联系人

    public static final String CI_MAINTEN_PROVIDER_CONT_PHONE = "provider_cont_phone"; // 服务联系人电话

    public static final String CI_MAINTEN_PROVIDER_CONT_EMAIL = "provider_cont_email"; // 服务联系人邮箱

    public static final String CI_MAINTEN_SCRAP_TIME = "scrap_time"; // 硬件设备报废时间

    public static final String CI_MAINTEN_LIFE_PERIOD = "life_period"; // 使用年限

    public static final String CI_MAINTEN_DEPT_OPERATION = "dept_operation"; // 维护部门

    public static final String CI_MAINTEN_OPS = "ops"; // 维护人员

    public static final String CI_MAINTEN_OPS_PHONE = "ops_phone"; // 维护人员电话

    public static final String CI_MAINTEN_OPS_EMAIL = "ops_email"; // 维护人员邮箱

    // 是否
    public static final String YES = "是";

    public static final String NO = "否";

    // REDIS KEY前缀设置
    public static final String REDIS_MODULE_MODULE_PREFIX = "CMDB_MODULE:%s";

    public static final String REDIS_TEMP_MODULE_MODULE_PREFIX = "CMDB_MODULE_TEMP:%s";

    public static final String REDIS_MODULE_CATALOG_PREFIX = "CMDB_MODULE_CATALOG:%s";

    public static final String REDIS_MODULE_DETAIL = REDIS_MODULE_MODULE_PREFIX + "_DETAIL"; // 模型key

    public static final String REDIS_MODULE_TABLE = REDIS_MODULE_MODULE_PREFIX + "_SQL"; // 模型查询SQL key

    public static final String REDIS_MODULE_COUNT_TABLE = REDIS_MODULE_MODULE_PREFIX + "_COUNT_SQL"; // 模型查询SQL key

    public static final String REDIS_TEMP_MODULE_TABLE = REDIS_TEMP_MODULE_MODULE_PREFIX + "_SQL"; // 临时模型查询SQL key

    public static final String REDIS_MODULE_COLUMNS = REDIS_MODULE_MODULE_PREFIX + "_COLUMNS"; // 模型查询SQL key

    public static final String REDIS_MODULE_GROUP_SETTING = "MODULE_GROUP_SETTING:%s_%s" + ""; // 模型分组 key

    public static final String REDIS_MODULE_TABLE_COLUMN = "CMDB_TABLE:%s"; // 模型表列

    public static final String REDIS_MODULE_DICT_DATA = REDIS_MODULE_MODULE_PREFIX + "_%s_DICT_DATA"; // 引用模型字典数据

    public static final String REDIS_MODULE_CATALOG_TREE = REDIS_MODULE_CATALOG_PREFIX + "_CATALOG_TREE"; // 模型分组key

    public static final String REDIS_CODE_PREFIX = "CMDB_CODE:%s"; // 码表key

    public static final String REDIS_CODE_GROUP_PREFIX = "CMDB_CODE_GROUP:%s"; // 码表分组key

    public static final String REDIS_CODE_DICT_DATA_VALUE = REDIS_CODE_PREFIX + "_REF_DATA_VALUE"; // 引用模型字典数据值

    public static final String REDIS_CODE_DETAIL = REDIS_CODE_PREFIX + "_DETAIL"; // 编码key

    public static final String REDIS_DICT_PREFIX = "CMDB_DICT:"; // 编码key

    public static final String REDIS_CONDITION_SETTING_PREFIX = "CMDB_CONDICATION_SETTING:%s";

    public static final String REDIS_CONDITION_SETTING_DETAIL = REDIS_CONDITION_SETTING_PREFIX + "_DETAIL";

    public static final String REDIS_CMDB_CONFIG_PREFIX = "CMDB_CONFIG:%s_DETAIL";

    public static final String REDIS_MODULE_TAB_PREFIX = "CMDB_MODULE_TAB:%s";
    public static final String REDIS_MODULE_TAB_EXPORT_PREFIX = "CMDB_MODULE_TAB_EXPORT:%s";

    public static final String REDIS_MODULE_TAB_TABLE = REDIS_MODULE_TAB_PREFIX + "_SQL"; // 模型关联Tab管理 查询SQL key

    public static final String REDIS_MODULE_COUNT_TAB_TABLE = REDIS_MODULE_TAB_PREFIX + "_COUNT_SQL"; // 模型关联Tab管理 查询SQL key

    public static final String REDIS_MODULE_TAB_EXPORT_TABLE = REDIS_MODULE_TAB_EXPORT_PREFIX + "_SQL"; // 模型关联Tab管理 主机配置导出SQL key

    // REDI KEY 类型
    public static final String REDIS_TYPE_MODULE = "CMDB_MODULE";

    public static final String REDIS_TYPE_CODE = "CMDB_CODE";

    public static final String REDIS_TYPE_CODE_GROUP = "CMDB_CODE_GROUP";

    public static final String REDIS_TYPE_CATALOG = "CMDB_CATALOG";

    public static final String REDIS_TYPE_VIEW = "CMDB_VIEW";

    public static final String REDIS_TYPE_CONDITION_SETTING = "CONDITION_SETTING";

    public static final String REDIS_TYPE_DICT = "CMDB_DICT";

    public static final String REDIS_TYPE_MODULE_TAB = "CMDB_MODULE_TAB";

    public static final String REDIS_RESOURCE_INDEX_ASSIGNED_INFO = "RESOURCE_INDEX:RES_ASSIGNED_INFO";

    // 导入/导出进程redis前缀
    public static final String REDIS_PROCESS = "PROCESS:%s";
    // 导入/导出进程redis详情
    public static final String REDIS_PROCESS_DETAIL = "PROCESS:%s_DETAIL";
    // 导入/导出进程中产生的错误记录
    public static final String REDIS_PROCESS_ERROR = "PROCESS:%s_ERROR";

    public static final String REDIS_VIEW_TREE = "CMDB_VIEW_TREE:%s";

    // 模型对应的表名称
    public static final String CMDB_MODULE_CATALOG_PREFIX = "cmdb_cl"; // 模型分组前缀
    // public static final String CMDB_MODULE_DATA_SUFFIX = "_data"; //模型分组前缀

    // CMDB数据对应的ES索引名称
    public static final String CMDB_MODULE_DATA_ES_INDEX_PREFIX = "cmdb_module_data_"; // 模型数据ES索引

    public static final String CMDB_LOG_ES_INDEX_PREFIX = ""; // 系统操作日志ES索引

    // 内置的权限占位符
    public static final String INNER_AUTH_STRING = "[<auth>]";

    // 主机资源权限分组
    public static final String INSTANCE_AUTH_MODULE = "主机资源";

    // 数据资源权限分组
    public static final String DICT_AUTH_MODULE = "数据资源";

    // 需要开启权限过滤
    public static final Integer NEED_AUTH = 1;

    // 不需要开启权限过滤
    public static final Integer UN_NEED_AUTH = 0;

    // 内置WHERE占位符
    public static final String INNER_WHERE_STRING = "[<where>]";

    // 内置ORDER占位符
    public static final String INNER_ORDER_STRING = "[<order>]";

    // 内置LIMIT占位符
    public static final String INNER_LIMIT_STRING = "[<limit>]";

    /** ip管理顶级模型. */
    public static final String MODULE_IP_REPOSITORY = "cmdb_ip_repository";

    /** 失效时长 */
    public static Long EXPIRE_TIME = 3L;

    /** 失效时长单位 天 */
    public static TimeUnit TIME_UNIT = TimeUnit.DAYS;

    /** 是 */
    public static final int IS_TRUE = 1;
    /** 否 */
    public static final int IS_FALSE = 0;

    // 模型和Tab管理的模型 操作类型
    public static final String MODULE_TAB_LIST = "list";
    public static final String MODULE_TAB_EXPORT = "export";

    /** 云管配置在cmdb_config中的key -资源池*/
    public static final String CLOUD_SQL_PREFIX = "Cloud_%s";


}
