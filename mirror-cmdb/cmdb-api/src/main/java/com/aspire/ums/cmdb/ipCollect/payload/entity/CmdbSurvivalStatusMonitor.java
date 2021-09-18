package com.aspire.ums.cmdb.ipCollect.payload.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * cmdb资产存活状态检测
 *
 * @author jiangxuwen
 * @date 2020/12/29 16:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbSurvivalStatusMonitor implements Serializable {

    private static final long serialVersionUID = -494399401895902185L;

    private String id;

    private String instanceId;

    private String survivalStatus;

    private Date latestSurvivalTime;

    private Date crateTime;

    private Date updateTime;
}
