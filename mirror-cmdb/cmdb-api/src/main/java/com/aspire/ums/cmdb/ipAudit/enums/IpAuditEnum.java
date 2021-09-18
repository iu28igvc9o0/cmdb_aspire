package com.aspire.ums.cmdb.ipAudit.enums;

/**
 * @author fanwenhui
 * @date 2020-06-08 16:27
 * @description IP类型枚举
 */
public enum IpAuditEnum {

    INNER_IP("innerIp","内网IP"),
    PUBLIC_IP("pubIp","公网IP"),
    IPV6("ipv6","公网IP"),
    UN_PLAN_PUBLIC_IP("pubIp1","存活未规划公网IP"),
    UN_ASSIGN_PUBLIC_IP("pubIp2","存活分配划公网IP"),
    REGISTER_EXPIRED_PUBLIC_IP("pubIp3","登记已过期公网IP"),
    UN_PLAN_IPV6("ipv61","存活未规划IPv6"),
    UN_ASSIGN_IPV6("ipv62","存活分配划IPv6"),
    REGISTER_EXPIRED_IPV6("ipv63","登记已过期IPv6"),;

    private String value;
    private String label;

    IpAuditEnum(String value,String label){
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

    public static IpAuditEnum fromValue(String value) {
        for (IpAuditEnum ipAuditEnum : IpAuditEnum.values()) {
            if (value.equalsIgnoreCase(ipAuditEnum.getValue())) {
                return ipAuditEnum;
            }
        }
        throw new IllegalArgumentException("类型错误， 不支持的类型[" + value + "]!");
    }

}
