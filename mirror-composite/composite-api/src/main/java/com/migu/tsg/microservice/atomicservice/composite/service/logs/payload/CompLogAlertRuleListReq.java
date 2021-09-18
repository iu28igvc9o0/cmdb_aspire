package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;

import lombok.Data;

@Data
public class CompLogAlertRuleListReq {
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
    private Integer isPage = 1;
    private Integer pageNo;
    private Integer pageSize;
    private Integer begin;

}
