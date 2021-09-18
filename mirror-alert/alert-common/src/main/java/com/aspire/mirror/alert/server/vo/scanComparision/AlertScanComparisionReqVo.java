package com.aspire.mirror.alert.server.vo.scanComparision;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertScanComparisionReqVo {

    // 资源池
    private String idcType;
    // 设备ip
    private String deviceIp;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;
    // 同步状态
    private String synStatus;
    // 开始时间
    private String curMoniStartTime;
    // 结束时间
    private String curMoniEndTime;
    private  int type = 0;//0告警，1监控

    /*-----分页-----*/
    //开始的下标
    private int index;
    //页数
    private int pageNum;
    //条数
    private int pageSize;
}
