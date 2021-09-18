package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import lombok.Data;

import java.util.Date;

@Data
public class CompSysManageReq {
    /**
     * 主键id，存uuid
     */
    private String id;

    /**
     * 系统名称
     */
    private String name;

    /**
     * 创建人
     */
    private String creater;

    private Date createTime;
}