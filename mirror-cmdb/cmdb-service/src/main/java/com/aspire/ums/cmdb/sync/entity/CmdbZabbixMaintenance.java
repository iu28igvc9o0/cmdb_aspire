package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 *
 * @since 2020年08月27日 14:51:44
 * @author jiangxuwen
 * @version v1.0
 */
@Data
public class CmdbZabbixMaintenance implements Serializable {

    private static final long serialVersionUID = 5694268738996601777L;

    /**
     * 主键
     */
    private String id;

    /**
     * 维护模式名称
     */
    private String maintenanceName;

    /**
     * 维护模式描述信息
     */
    private String maintenanceDesc;

    /**
     * 维护模式生效时间
     */
    private Date activeSince;

    /**
     * 维护模式失效时间
     */
    private Date activeTill;

    /**
     * 维护模式主机IP
     */
    private String maintenanceIp;

    /**
     * 
     */
    private String idc;

    /**
     * 
     */
    private String maintenanceId;

    /**
     * 创建来源
     */
    private String source;

    /**
     * bpm_维护模式创建状态_0 失败 1成功
     */
    private String createStatus;

    /**
     * 备注
     */
    private String comment;

    /**
     * 维护模式监控对象
     */
    private String maintenanceMoni;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 操作级别：1-A,2-B,3-C,4-D,5-E
     */
    private String opLevel;

    /**
     * 是否重大操作:1-是,2-否
     */
    private String opGreat;

    /**
     * 是否紧急操作:1-是,2-否
     */
    private String opCritical;

    /**
     * 工单类型：1-现网变更,2-业务变更,3-技术支撑
     */
    private String opOrdertype;

    /**
     * 变更工单发起人账号
     */
    private String opOrderCreatoraco;

    /**
     * 变更工单发起人姓名
     */
    private String opOrderCreator;

    /**
     * 操作级别：1-成功,2-回退,3-失败
     */
    private String opResult;

    /** 是否禁用状态.0-不禁用,1-禁用. */
    private String disableFlag;
}
