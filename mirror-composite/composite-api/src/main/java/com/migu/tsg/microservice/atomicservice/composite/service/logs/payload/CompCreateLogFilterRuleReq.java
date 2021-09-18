package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;

import lombok.Data;

import java.util.List;

@Data
public class CompCreateLogFilterRuleReq {

    /**
     * uuid
     */
    private String uuid;
    /**
     * 过滤规则类型
     */
    private String ruleType;
    /**
     * 过滤规则名称
     */
    private String name;
    /**
     * 过滤规则描述
     */
    private String description;
    /**
     * 资源池
     */
    private String idcType;
    /**
     * 设备ip
     */
    private String ip;
    /**
     * 关键字
     */
    private String param;
    private List<String> params;
    /**
     * 过滤开始时间
     */
    private String startTime;
    /**
     * 过滤结束时间
     */
    private String endTime;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 包含关键字
     */
    private String includeKey;
    /**
     * 不包含关键字
     */
    private String noIncludeKey;

}
