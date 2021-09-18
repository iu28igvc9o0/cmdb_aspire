package com.aspire.ums.cmdb.config;

/**
 * kafka常量配置.
 *
 * @author jiangxuwen
 * @date 2020/5/13 18:29
 */
public final class KafkaConfigConstant {

    /** -----------------同步到网管的topic start---------------- */
    /** topic--字典表/组织机构. */
    public static final String TOPIC_CONFIG_DICT = "new_cmdb_config_dict_sync";

    /** topic--cmdb资产表. */
    public static final String TOPIC_CMDB_DEVICE_ASSET = "new_cmdb_device_asset_sync";

    /** topic--存储信息. */
    public static final String TOPIC_CMDB_DEVICE_STORAGE = "new_cmdb_device_asset_storage";

    /** topic--其他IP. */
    public static final String TOPIC_CMDB_DEVICE_OTHER_IP = "new_cmdb_device_asset_other_ip";

    /** topic--业务线. */
    public static final String TOPIC_CMDB_BUSINESS = "cmdb_business_sync";

    /** topic--业务线移动接口人. */
    public static final String TOPIC_CMDB_BUSINESS_LINE_CONTACT = "cmdb_business_line_contact_sync";

    /** -----------------同步到网管的topic end---------------- */

    /** 同步表--字典表. */
    public static final String TABLE_CONFIG_DICT = "config_dict";

    public static final String TABLE_SYS_ORG = "sys_org";

    /** 同步表--财务业务线. */
    public static final String TABLE_FINANCIAL_BUSINESS = "financial_business";

    /** 同步表--财务业务线cmdb独立业务线关联. */
    public static final String TABLE_FINANCIAL_BUSINESS_REL = "cmdb_financial_business_rel";

    /** 同步表--cmdb独立业务线. */
    public static final String TABLE_CMDB_BUSINESS_ATTACH = "cmdb_business_attach";

    /** 同步表--移动接口人. */
    public static final String TABLE_CMDB_BUSINESS_INTERFACE = "cmdb_business_interface";

    /** 同步表--资产表. */
    public static final String TABLE_CMDB_DEVICE_ASSET = "cmdb_device_asset";

    /** 同步表--资产表存储信息. */
    public static final String TABLE_CMDB_DEVICE_ASSET_STORAGE = "cmdb_storage_information";

    /** 同步表--资产表其他IP信息. */
    public static final String TABLE_CMDB_DEVICE_ASSET_IP = "cmdb_device_assest_ip";

    /** 同步表--资产表硬件维保. */
    public static final String TABLE_CMDB_DEVICE_ASSET_MAINTENCE = "cmdb_maintenance_information";

    /** 同步表--资产表图片信息. */
    public static final String TABLE_CMDB_DEVICE_ASSET_PICTURE = "cmdb_device_asset_picture";

    /**
     * 1. 通过模型字段和网管的字段映射表cmdb_sync_field_mapping 查找对应关系 2. 网管同步过来的 其他Ip/业务线-独立业务线/硬件维保/存储信息要查找对应的模型id.
     */

    public static final String MAPPING_MODULE = "cmdb_sync_module";

    /** 其他IP模型. */
    public static final String MAPPING_KEY_OTHER_IP = "其他IP";

    /** 硬件维保模型. */
    public static final String MAPPING_KEY_MAINTENCE = "硬件维保";

    public static final String MAPPING_KEY_CMDB_INNER_IP_SEGMENT = "工程交维IP内网网段";

    /** 存储模型. */
    public static final String MAPPING_KEY_STORAGE = "存储信息";

    /** 业务系统模型. */
    public static final String MAPPING_KEY_FINANCE_CMDB_BUSINESS = "业务系统";

    public static final String MAPPING_KEY_CMDB_FINANCE_BUSINESS = "财务业务线";

    public static final String MAPPING_KEY_CMDB__BUSINESS_LINE = "独立业务线";

    public static final String MAPPING_KEY_CMDB__BUSINESS_CONTACT = "业务线移动接口人";

    public static final String MAPPING_KEY_CONFIG_DICT = "数据字典信息";

    public static final String MAPPING_KEY_ORG_SYSTEM = "部门租户信息";

    public static final String MAPPING_KEY_IDC = "资源池";

    /** 网管表和cmdb模型字段映射. */
    public static final String MAPPING_KEY_FIELD = "osa_cmdb_field_mapping";

    /** 资产表模型同步. */
    public static final String MAPPING_TYPE_CMDB_DEVICE_ASSET = "cmdb_device_asset_sync";

    /** 硬件维保模型同步. */
    public static final String MAPPING_TYPE_MAINTENCE = "cmdb_device_maintence_sync";

    /** 工程交维IP内网网段地址库 模型同步. */
    public static final String MAPPING_TYPE_CMDB_INNER_IP_SEGMENT = "cmdb_inner_ip_segment";

    /** 其他IP信息模型同步. */
    public static final String MAPPING_TYPE_SERVER_IP = "cmdb_device_server_ip_sync";

    /** 存储信息模型同步. */
    public static final String MAPPING_TYPE_STORAGE = "cmdb_device_storage_sync";

    /** 业务线模型同步. */
    public static final String MAPPING_TYPE_FINANCE_CMDB_BUSINESS = "cmdb_finance_business_sync";

    public static final String MAPPING_TYPE_DEPARTMENT_ID = "cmdb_config_dict_department";

    public static final String MAPPING_TYPE_CMDB_BUSINESS_LINE = "cmdb_business_line_sync";

    public static final String MAPPING_TYPE_CMDB_BUSINESS_CONTACT = "cmdb_business_contact_sync";

    public static final String MAPPING_TYPE_CONFIG_DICT = "cmdb_config_dict_sync";

    public static final String MAPPING_TYPE_ORG_SYSTEM = "cmdb_org_system_sync";

    public static final String MAPPING_TYPE_FIN_BUSINESS = "cmdb_fin_business_sync";

    public static final String MAPPING_TYPE_IDC = "cmdb_idc_sync";
}
