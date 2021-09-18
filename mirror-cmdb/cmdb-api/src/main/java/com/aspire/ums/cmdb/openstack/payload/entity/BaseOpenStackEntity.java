package com.aspire.ums.cmdb.openstack.payload.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 云管数据模型基类.
 *
 * @author jiangxuwen
 * @date 2020/3/15 11:13
 */
@Data
public class BaseOpenStackEntity implements Serializable {

    private static final long serialVersionUID = 7754244265464130004L;

    /** 数据库主键. */
    private String id;

    /** 云管实例ID. */
    private String instanceId;

    /** 云管eventId. */
    private String eventId;

    /**
     *
     */
    private String name;

    /**
     * 云管端的创建时间
     */
    private Date srcCreateTime;

    /**
     * 云管端的修改时间
     */
    private Date srcModifyTime;

    /**
     * 云管端的上次修改时间
     */
    private String srcModifier;

    /**
     * 云管端的创建人
     */
    private String srcCreator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 删除标识
     */
    private String delFlag;
}
