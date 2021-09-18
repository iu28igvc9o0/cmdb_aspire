package com.aspire.ums.cmdb.ipCollect.payload.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fanwenhui
 * @date 2020-12-25 14:32
 * @description 云管数据模型基类.
 */
@Data
public class BaseCmdbVmwareEntity implements Serializable {
    private static final long serialVersionUID = 8126730092199054505L;

    /** 数据库主键. */
    private String id;

    /** 云管实例ID. */
    private String instanceId;

    /** 云管eventId. */
    private String eventId;

    private String name;

    /** 云管端的创建时间. */
    private Date srcCreateTime;

    /** 云管端的修改时间. */
    private Date srcOptTime;

    /** 云管端的上次修改时间. */
    private Date srcPreOptTime;

    /** 云管端的创建人. */
    private String srcCreator;

    /** 创建时间. */
    private Date createTime;

    /** 修改时间. */
    private Date updateTime;

    /** 版本号. */
    private Integer version;

    /** 删除标识. */
    private String delFlag;
}
