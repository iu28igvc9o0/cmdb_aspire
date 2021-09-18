package com.aspire.mirror.alert.api.dto.primarySecondary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertPrimarySecondaryReq {

    /**
     * 告警主次ID
     */
    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 规则描述
     */
    private String description;
    /**
     * 启用状态，0-停用，1-启用
     */
    private String status;
    /**
     * 设备关联类型,1-相同设备；2-不同设备
     */
    private String deviceRelationType;
    /**
     * 主要告警-资源池
     */
    private String primaryIdc;
    /**
     * 主要告警-设备ip
     */
    private String primaryIp;
    /**
     * 主要告警-告警内容
     */
    private String primaryMoniIndex;
    /**
     * 主要告警-告警等级
     */
    private String primaryAlertLevel;
    /**
     * 主要告警-设备过滤条件
     */
    private String primaryOptionCondition;
    /**
     * 维护用户
     */
    private String operateUser;
    /**
     * 次要告警-过滤条件
     */
    private String secondaryOptionCondition;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 修改人
     */
    private String editer;
    /**
     * 修改时间
     */
    private Date updatedAt;
}