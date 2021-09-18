package com.aspire.mirror.alert.server.constant;

import java.util.Map;

/**
 * 公共静态变量
 *
 * @author lupeng
 */
public class Constants {

    // 表单类型
    public static final String MODULE_FORM_TYPE_SINGLEROWTEXT = "singleRowText"; // 单行文本

    public static final String MODULE_FORM_TYPE_MULTIROWTEXT = "multiRowText"; // 多行文本

    public static final String MODULE_FORM_TYPE_RICHTEXT = "richText"; // 富本文

    public static final String MODULE_FORM_TYPE_LISTSEL = "listSel"; // 下拉菜单

    public static final String MODULE_FORM_TYPE_SINGLESEL = "singleSel"; // 单选

    public static final String MODULE_FORM_TYPE_MULTISEL = "multiSel"; // 多选

    public static final String MODULE_FORM_TYPE_DOUBLE = "double"; // 小数

    public static final String MODULE_FORM_TYPE_INT = "int"; // 整数

    public static final String MODULE_FORM_TYPE_IMAGE = "image"; // 图片

    public static final String MODULE_FORM_TYPE_FILE = "file"; // 附件

    public static final String MODULE_FORM_TYPE_DATETIME = "dateTime"; // 时间

    public static final String MODULE_FORM_TYPE_TABLE = "table"; // 表格

    public static final String MODULE_FORM_TYPE_CASCADER = "cascader"; // 级联

    public static final String MODULE_FORM_TYPE_GROUPLINE = "groupLine"; // 属性分组

    // 参数类型
    public static final String MODULE_FORM_PARAM_MINLENGTH = "minLength"; // 最小长度

    public static final String MODULE_FORM_PARAM_MAXLENGTH = "maxLength"; // 最大长度

    public static final String MODULE_FORM_PARAM_VALIDATION = "validation"; // 格式

    public static final String MODULE_FORM_PARAM_MIN = "min"; // 最小值

    public static final String MODULE_FORM_PARAM_MAX = "max"; // 最大值

    public static final String MODULE_FORM_PARAM_PRECISION = "precision"; // 小数位数

    public static final String MODULE_FORM_PARAM_FORMATDATE = "formatDate"; // 日期格式化

    public static Map<String, String> relationMap;

    static {
        relationMap.put("OneToOne", "一对一");
        relationMap.put("OneToMany", "一对多");
        relationMap.put("ManyToOne", "多对一");
        relationMap.put("ManyToMany", "多对多");

        relationMap.put("一对一", "OneToOne");
        relationMap.put("一对多", "OneToMany");
        relationMap.put("多对一", "ManyToOne");
        relationMap.put("多对多", "ManyToMany");
    }

    public static final String MODULE_RELATION_SOURCE = "source_module";

    public static final String MODULE_RELATION_TARGET = "target_module";

    public static final String MODULE_RELATION_ONETOONE = "OneToOne";

    public static final String MODULE_RELATION_ONETOMANY = "OneToMany";

    public static final String MODULE_RELATION_MANYTOONE = "ManyToOne";

    public static final String MODULE_RELATION_MANYTOMANY = "ManyToMany";

    public static final String LOG_ACTION_TYPE_ADDINSTANCE_RELATION_NAME = "建立关系";

    public static final String LOG_ACTION_TYPE_DELINSTANCE_RELATION_NAME = "删除关系";

    public static final String LOG_ACTION_TYPE_ADDINSTANCE_NAME = "新建配置";

    public static final String LOG_ACTION_TYPE_DELINSTANCE_NAME = "删除配置";

    public static final String LOG_ACTION_TYPE_UPDATEINSTANCE_NAME = "修改配置";

    public static final String LOG_ACTION_TYPE_HANDUPINSTANCE_NAME = "移交配置";

    public static final String MODOULE_RELATION_DIRECT_UP = "UP";

    public static final String MODOULE_RELATION_DIRECT_DOWN = "DOWN";

    /**
     * 告警工单类型
     */
    public static final String ORDER_ALERT="1";
    /**
     * 故障工单类型
     */
    public static final String ORDER_HITCH="2";
    /**
     * 维保工单类型
     */
    public static final String ORDER_MAINTENANCE="3";
    /**
     * 告警调优工单类型
     */
    public static final String ORDER_TUNING = "4";

    public static final String ORDER_BEFOR = "1";
    public static final String ORDER_DEALING = "2";
    public static final String ORDER_END = "3";
    public static final String ORDER_ERROR = "4";
    public static final String ORDER_ERROR_DESC = "派单失败";
    /**
     * 操作日志相关部分
     */
    public static final String OPERATE_MODEL_ALERT_ISOLATE = "alert_isolate";
    public static final String OPERATE_MODEL_ALERT_ISOLATE_DESC = "告警屏蔽规则";
    public static final String OPERATE_TYPE_INSERT = "insert";
    public static final String OPERATE_TYPE_INSERT_DESC = "新增";
    public static final String OPERATE_TYPE_UPDATE = "update";
    public static final String OPERATE_TYPE_UPDATE_DESC = "修改";
    public static final String OPERATE_TYPE_DELETE = "delete";
    public static final String OPERATE_TYPE_DELETE_DESC = "删除";

    /**
     * 屏蔽、衍生通用
     */
    public static final String ISOLATE_STATUS_OPEN = "1";
    public static final String ISOLATE_STATUS_STOP = "0";

    public static final String OPERATE_MODEL_ALERT_STANDARD = "alert_standard";
    public static final String OPERATE_MODEL_ALERT_STANDARD_DESC = "告警标准化规则";

    public static final String OPERATE_MODEL_ALERT_DERIVE = "alert_derive";
    public static final String OPERATE_MODEL_ALERT_DERIVE_DESC = "告警衍生规则";

    public static final String OPERATE_MODEL_ALERT_PRIMARY_SECONDARY = "alert_primary_secondary";
    public static final String OPERATE_MODEL_ALERT_PRIMARY_SECONDARY_DESC = "告警主次规则";

    public static final String OPERATE_MODEL_ALERT_BUSINESS = "alert_business";
    public static final String OPERATE_MODEL_ALERT_BUSINESS_DESC = "业务告警规则";

    public static final String OPERATE_MODEL_ALERT_EXCEPTION = "alert_exception";
    public static final String OPERATE_MODEL_ALERT_EXCEPTION_DESC = "异常信息规则";

    /**
     * 设备关联类型，主次告警使用,相同设备
     */
    public static final String DEVICE_RELATION_TYPE_SAME = "1";
    /**
     * 设备关联类型，主次告警使用,不同设备
     */
    public static final String DEVICE_RELATION_TYPE_DIFFERENT = "2";
    /**
     * 主要告警id拼接前缀
     */
    public static final String PREFIX_PRIMARY = "primary_";
    
    public static final String NULL_MONITOR_MONI_INDEX = "%s设备一天内未发现有性能数据";
    public static final String NULL_MONITOR_SOURCE = "monitorScan";
    public static final String NULL_MONITOR_MONI_OBJECT = "性能监控检查";
    public static final String NULL_MONITOR_LEVEL = "2";
    
}
