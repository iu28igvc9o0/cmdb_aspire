package com.aspire.mirror.alert.api.dto.cabinetAlert;

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
public class AlertCabinetColumnConfigDataDTO {
    private Integer id;
    

    private String idcType;

    private String roomId;
    
    //列头柜名称
    private String cabinetColumnName;
    //管理机柜数
    private int cabinetCount;
    //管理设备数
    private int deviceCount;
    
    //相关业务系统数
    private Integer bizSysCount;
    //0禁用1启用
    private Integer status;
    //1正常2列头柜告警3、机柜告警4电源告警
    private Integer alertStatus;
    
   

    private Date createTime;

    private Date updateTime;


    
    private Integer timeRange;
    //设备告警百分比
    private Integer alertPercentage;
    
  //列头柜告警
    private int cabinetColumnAlertCount;
    
    private int cabinetAlertCount;
    
    private int deviceAlertCount;
    
    private int bizSystemCount;
    
    private String editor;
    
}
