package com.aspire.ums.cmdb.sync.payload;

/**
 * 同步描述.
 *
 * @author jiangxuwen
 * @date 2020/6/12 15:44
 */
public enum CmdbSyncType {

    /** 资产同步. */
    CMDB_DEVICE("cmdb_device_asset", "cmdb资产同步"),

    /** 独立业务线同步. */
    CMDB_BUSINESS_LINE("cmdb_business_line", "cmdb独立业务线同步"),

    /** cmdb财务业务线同步. */
    FINANCE_BUSINESS("cmdb_finance_business", "cmdb财务业务线同步"),

    /** cmdb财务-独立业务线同步. */
    FINANCE_CMDB_BUSINESS("cmdb_business_system", "cmdb财务-独立业务线同步"),

    /** cmdb移动接口人同步. */
    BUSINESS_CONTACT("cmdb_business_contact", "cmdb移动接口人同步"),

    /** cmdb存储信息同步. */
    CMDB_DEVICE_STORAGE("cmdb_finance_business", "cmdb存储信息同步"),

    /** cmdb硬件维保同步. */
    CMDB_DEVICE_MAINTENCE("cmdb_finance_business", "cmdb硬件维保同步"),

    CMDB_INNER_IP_SEGMENT("cmdb_inner_ip_segment", "工程交维IP内网地址库同步"),

    /** 其他IP同步. */
    OTHER_IP("cmdb_device_asset_ip", "cmdb其他IP同步"),

    /** 字典表同步. */
    SYS_DICT("sys_dict", "字典表同步"),

    /** 部门组织同步. */
    SYS_ORG("sys_org", "部门组织同步"),

    /** 用户账号同步. */
    SYS_USER("sys_org", "用户账号同步"),

    /** 部门-用户关系同步. */
    SYS_ORG_USER("sys_org", "部门-用户关系同步");

    public String value;

    private String label;

    CmdbSyncType(String value, String label) {
        this.value = value;
        this.label = label;

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static CmdbSyncType fromValue(String value) {
        for (CmdbSyncType element : CmdbSyncType.values()) {
            if (value.equalsIgnoreCase(element.getValue())) {
                return element;
            }
        }
        throw new IllegalArgumentException("类型错误， 不支持的类型[" + value + "]!");
    }
}
