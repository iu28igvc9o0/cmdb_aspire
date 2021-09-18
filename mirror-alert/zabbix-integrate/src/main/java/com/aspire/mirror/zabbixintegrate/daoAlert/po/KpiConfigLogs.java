package com.aspire.mirror.zabbixintegrate.daoAlert.po;

import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.daoAlert.po
 * @Author: baiwenping
 * @CreateTime: 2020-04-21 20:24
 * @Description: ${Description}
 */
@Data
public class KpiConfigLogs {
    private Integer id;
    private String configId;
    private String configName;
    private String tag;
    private Date fromTime;
    private Date toTime;
    private Date execTime;
    /**
     * 执行时间，单位：秒
     */
    private String execDuration;
    private String status;
    private String content;
    private Date createTime;
}
