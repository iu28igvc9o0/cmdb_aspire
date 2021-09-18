package com.aspire.ums.cmdb.collect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AlertModel
 * Author:   zhu.juwang
 * Date:     2019/3/18 17:34
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertModel {
    private String	moniResult;		// 指标监测结果 1: 新增告警     2: 解除告警
    private String	deviceIP;		// 所属设备
    private String	servSystem;		// 所属业务系统
    private String	monitorRoom;	// 监控机房
    private String	monitorObject;	// 监控对象
    private String	monitorIndex;	// 监控指标
    private String	moniIndexValue;
    private String	alertLevel;		// 告警级别
    private String	alertDesc;		// 告警描述
    private String	curMoniTime;	// 当前监测时间 yyyy-MM-dd HH:mm:ss
    private String	curMoniValue;	// 当前监测值
}
