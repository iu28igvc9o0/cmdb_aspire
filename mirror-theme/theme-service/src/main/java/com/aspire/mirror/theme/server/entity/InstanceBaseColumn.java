package com.aspire.mirror.theme.server.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.ums.cmdb.maintain.entity
 * 类名称:    InstanceBaseColumn.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/8/16 17:14
 * 版本:      v1.0
 */
@Data
@ToString
public class InstanceBaseColumn implements Serializable {
    private static final long serialVersionUID = -5240471523419693581L;
    private String instanceId;
    private String name;
    private String moduleId;
    private String insertTime;
    private String updateTime;
    private String moduleName;
    private String roomId;
    private String roomName;
    private String bizSystemName;
    private String ip;
    private String bizSystem;
    private Integer isDelete;
    /**
     * 所属位置-资源池
     */
    private String idcType;
    /**
     * 所属域
     */
    private String deviceRegion;
    /**
     * 设备分类
     */
    private String deviceClass;


}
