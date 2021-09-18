package com.aspire.mirror.common.constant;

public class Constant {
    /**
     * IS
     */
    public static final String IS = "1";
    /**
     * IS_NOT
     */
    public static final String IS_NOT = "0";
    /**
     * END
     */
    public static final String END = "end";
    /**
     * 模板导出模板路径配置
     */
    public static final String EXPORT_TEMPLATE_NAME = "export/exportTemplate.xls";
    public static final String EXPORT_NEW_TEMPLATE_NAME = "export/exportTemplateNew.xls";

    public static final String THEME_EXPLAIN_DOC_PATH = "download/bizThemeAccessInstruction.docx";
    /**
     * 工单状态-未派单
     */
    public static final String ORDER_BEFOR = "1";
    /**
     * 工单状态-处理中
     */
    public static final String ORDER_DEALING = "2";
    /**
     * 工单状态-已完成
     */
    public static final String ORDER_END = "3";

    /**
     * 派单类型 自动告警工单
     */
    public static final String AUTO_ALERT_ORDER = "自动告警工单";
    /**
     * 派单类型  手动告警工单
     */
    public static final String ALERT_ORDER = "告警工单";
    /**
     * 派单类型  故障工单
     */
    public static final String HITCH_ORDER = "故障工单";
    /**
     * 派单类型  调优工单
     */
    public static final String TUNING_ORDER = "调优工单";

    /**
     * 派单类型  维保工单
     */
    public static final String MAINTENANCE_ORDER = "维保工单";

    /**
     * 工单类型-警告
     */
    public static final String ORDER_WARN = "1";

    public static final String[] ESCAPE_DIM_REG_ARRAY = {"$","(",")","*","+",".","[","]","?","\\","^","{","}","|"};

    public static final String KEY_HOST_FRESHTIME = "HOST_FRESHTIME";

    public static final String KEY_THEME_FRESHTIME = "THEME_FRESHTIME_0";

    public static final String KEY_CORRESPONDENCE = "IP_BIZSYS_CORRESPONDENCE";

    public static final String KEY_DEVICEIP = "KEY_DEVICEIP";

    public static final String KEY_THEME_LIST = "THEME_LIST";

    public static final String KEY_THEMEDIM_LIST = "THEME_DIM_LIST";

    public static final String OBJECT_TYPE_BIZ_SYS = "2";

    public static final String OBJECT_TYPE_DEVICE = "1";
}
