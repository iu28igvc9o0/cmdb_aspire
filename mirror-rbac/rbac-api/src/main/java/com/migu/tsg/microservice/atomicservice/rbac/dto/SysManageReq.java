package com.migu.tsg.microservice.atomicservice.rbac.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SysManageReq {
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