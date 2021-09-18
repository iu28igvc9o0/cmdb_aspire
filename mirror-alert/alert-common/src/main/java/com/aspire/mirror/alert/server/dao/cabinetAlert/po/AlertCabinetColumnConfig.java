package com.aspire.mirror.alert.server.dao.cabinetAlert.po;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 历史告警详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsHisDetailResponse.java
 * 类描述:    历史告警详情
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:18
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class AlertCabinetColumnConfig {
    private Integer id;

    private String idcType;

    private String roomId;
    
    /**
                   修改人
     */
    private String editor;

    private Date createTime;

    private Date updateTime;

    /** 1所有资源池2资源池3机房*/
    private Integer configType;
    
    private Integer timeRange;
    //设备告警百分比
    private Integer alertPercentage;
    
    
}
