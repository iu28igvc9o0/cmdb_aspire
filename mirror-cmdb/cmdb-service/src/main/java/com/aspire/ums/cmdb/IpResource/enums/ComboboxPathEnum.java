package com.aspire.ums.cmdb.IpResource.enums;

/**
 * combobox下拉path值
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/17 18:01
 */
public enum ComboboxPathEnum {
    IDC_LIST("getIdcList", "资源池"),
    FIRST_BUSINESS("getFirstBusiness", "一级业务线-独立业务线"),
    ALONE_BUSINESS("getAloneBusiness", "独立业务线-独立业务子模块"),
    SEGMENT_TYPE("getSegmentType", "网段类型-网段子类"),
    ;

    ComboboxPathEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    private String code;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
