package com.aspire.mirror.alert.api.dto.alert;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class AlertsHisDataRequest extends GeneralAuthConstraintsAware {
    /**
     * 设备IP
     */
    private String deviceIp;
    /**
     * 告警级别
     */
    private String alertLevel;
    /**
     * 资源池
     * 全部/......
     */
    private String idcType;
    private String authIdcType;
    private String idcTypeOtherWise;
    /**
     * 设备分类
     * 服务器/.....
     */
    private String deviceClass;
    /**
     * 设备提供商
     */
    private String deviceMfrs;
    /**
     * 业务系统
     * 全部
     * ......
     */
    private String bizSys;
    /**
     * 来源
     */
    private String source;
    /**
     * 设备类型
     * 主机/.....
     */
    private String deviceType;
    /**
     * 告警描述
     */
    private String monitDesc;
    /**
     * 告警类型(告警分类)
     * 系统
     * 设备
     */
    private String objectType;
    /**
     * 告警开始时间
     */
    private String alertCreateTimeRangeStart;
    private Date alertCreateStartTime;
    private String alertCreateTimeRangeEnd;
    private Date alertCreateEndTime;
    /**
     * 当前告警时间
     */
    private String curMoniTimeFrom;
    private String curMoniTimeTo;
    private Date curMoniTimeStart;
    private Date curMoniTimeEnd;
    /**
     * 机房
     */
    private String sourceRoom;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 主机名称
     */
    private String hostName;
    /**
     * 监控项
     */
    private String monitItems;
    private List<String> monitItemList;
    /**
     * 通知方式
     */
    private String notifyType;
    private List<String> notifyTypeList;
    /**
     * 通知状态
     */
    private String notifyStatus;
    /**
     * 通知时间
     */
    private String notifyTimeRangeStart;
    private Date notifyStartTime;
    private String notifyTimeRangeEnd;
    private Date notifyEndTime;
    /**
     * 转派时间
     */
    private String transferTimeRangeStart;
    private Date transferStartTime;
    private String transferTimeRangeEnd;
    private Date transferEndTime;
    /**
     * 转派操作人
     */
    private String transferStaff;
    /**
     * 待确认人员
     */
    private String toConfirmStaff;
    /**
     * 当前用户
     */
    private String userName;
    /**
     * 设备实例ID
     */
    private String instanceId;
    /**
     * 查询类别
     * 活动 0/确认 1 告警
     */
    private String queryType;
    /**
     * 操作状态
     * 0-待确认,1-已确认,2-待解决,3-已处理
     */
    private String operateStatus;
    /**
     * 时间区间
     */
    private String span;

    /**
     * 是否清除
     */
    private String isClear;
    private String clearTimeRangeStart;
    private Date clearStartTime;
    private String clearTimeRangeEnd;
    private Date clearEndTime;

    /**
     * 派单
     */
    private String deliver;
    private String deliverTimeRangeStart;
    private Date deliverStartTime;
    private String deliverTimeRangeEnd;
    private Date deliverEndTime;

    /**
     * 分页
     */
    @NotNull
    @JsonProperty("page_size")
    private Integer pageSize;

    @NotNull
    @JsonProperty("page_no")
    private int pageNo;

    @NotEmpty
    @JsonProperty("sort_name")
    private String sortName;

    @NotEmpty
    private String order;



}
