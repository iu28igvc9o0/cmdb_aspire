package com.aspire.mirror.log.server.dao.po;

import lombok.Data;

@Data
public class LogAlertRuleListReqDTO {
    /**
     * 规则名称
     */
    private String name;
    /**
     * 资源池
     */
    private String idcType;
    /**
     * 设备ip
     */
    private String ip;
    /**
     * 运行状态
     */
    private String runStatus;

    // 分页
    private Integer isPage = 1;// -1 不分页  1 分页
    private Integer pageNo;
    private Integer pageSize;
    private Integer begin;

}
