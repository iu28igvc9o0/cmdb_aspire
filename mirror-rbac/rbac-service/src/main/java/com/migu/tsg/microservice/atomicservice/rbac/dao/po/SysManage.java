package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import lombok.Data;

import java.util.Date;

@Data
public class SysManage {
    /**
     * 主键id，存uuid
     */
    private String id;

    /**
     * 系统名称
     */
    private String name;

    private String manageCode;

    /**
     * 创建人
     */
    private String creater;

    private Date createTime;

    /**
     * 是否删除，1-未删除，0-已删除
     */
    private String delStatus;
}