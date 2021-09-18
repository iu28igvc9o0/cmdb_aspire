package com.aspire.ums.cmdb.sync.payload;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/5/13 19:06
 */
public enum CmdbOptType {
    /** 创建. */
    OPT_ADD("add", "创建"),

    /** 修改. */
    OPT_MODIFY("modify", "修改"),

    /** 删除. */
    OPT_DEL("delete", "删除");

    public String value;

    private String label;

    CmdbOptType(String value, String label) {
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

    public static CmdbOptType fromValue(String value) {
        for (CmdbOptType element : CmdbOptType.values()) {
            if (value.equalsIgnoreCase(element.getValue())) {
                return element;
            }
        }
        throw new IllegalArgumentException("类型错误， 不支持的类型[" + value + "]!");
    }
}
