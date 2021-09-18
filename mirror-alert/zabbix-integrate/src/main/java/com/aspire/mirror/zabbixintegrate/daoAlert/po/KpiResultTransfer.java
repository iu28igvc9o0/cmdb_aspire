package com.aspire.mirror.zabbixintegrate.daoAlert.po;

import lombok.Data;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.daoAlert.po
 * @Author: baiwenping
 * @CreateTime: 2020-04-16 14:45
 * @Description: ${Description}
 */
@Data
public class KpiResultTransfer {
    /**
     *
     */
    private Integer id;
    /**
     *
     */
    private String configId;
    /**
     * 名称
     */
    private String resultTransferName;
    /**
     * 分组
     */
    private String fieldGroup;
    /**
     * 源字段
     */
    private String sourceField;
    /**
     * 目标字段
     */
    private String targetField;
    /**
     * 转换类型，1-固定值(date为特殊情况)，2-groovy，3-java函数，4-字典转换
     */
    private String transferType;
    /**
     * 转换公式，
     */
    private String transferFormula;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否删除，1-删除，0-未删除
     */
    private String isDelete;
}
