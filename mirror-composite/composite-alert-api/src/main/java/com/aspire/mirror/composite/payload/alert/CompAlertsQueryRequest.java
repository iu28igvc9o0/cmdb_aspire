package com.aspire.mirror.composite.payload.alert;

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
public class CompAlertsQueryRequest extends GeneralAuthConstraintsAware {
    /**
     * 设备
     * 包含include/不包含exclude
     */
    private String deviceOp;
    /**
     * 设备IP
     */
    private String deviceIp;
    /**
     * 监控项
     * 包含include/不包含exclude
     */
    private String monitOp;
    /**
     * 监控项
     */
    private String monitItems;
    private List<String> monitItemList;
    /**
     * 告警级别
     */
    private String alertLevel;
    /**
     * 查询类别
     * 活动 0/确认 1 告警
     */
    private String queryType;
    /* ------------------------------------------------ 高级检索 --------------------------------------------------- */
    /**
     * 时间区间
     */
    private String span;
    /**
     * 告警
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
     * 告警类型(告警分类)
     * 系统
     * 设备
     */
    private String objectType;
    /**
     * 操作状态
     * 0-待确认,1-已确认,2-待解决,3-已处理
     */
    private String operateStatus;
    /**
     * 设备类型
     * 主机/.....
     */
    private String deviceType;
    /**
     * 设备分类
     * 服务器/.....
     */
    private String deviceClass;
    /**
     * 主机名称
     */
    private String hostName;
    /**
     * 业务系统
     * 全部
     * ......
     */
    private String bizSys;
    /**
     * 告警描述
     */
    private String monitDesc;
    /**
     * 资源池
     * 全部/......
     */
    private String idcType;
    private String authIdcType;
    private String idcTypeOtherWise;
    /**
     * 来源
     */
    private String source;
    /**
     * 机房
     */
    private String sourceRoom;
    /**
     * 设备提供商
     */
    private String deviceMfrs;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 通知方式
     */
    private String notifyType;
    private List<String> notifyTypeList;
    private String notifyStatus;
    /**
     * 通知
     */
    private String notifyTimeRangeStart;
    private Date notifyStartTime;
    private String notifyTimeRangeEnd;
    private Date notifyEndTime;
    /**
     * 待确认人员
     */
    private String toConfirmStaff;
    /**
     * 确认
     */
    private String confirmStaff;
    private String confirmTimeRangeStart;
    private Date confirmStartTime;
    private String confirmTimeRangeEnd;
    private Date confirmEndTime;
    /**
     * 转派
     */
    private String transferStaff;
    private String transferTimeRangeStart;
    private Date transferStartTime;
    private String transferTimeRangeEnd;
    private Date transferEndTime;
    /**
     * 派单
     */
    private String deliver;
    private String deliverTimeRangeStart;
    private Date deliverStartTime;
    private String deliverTimeRangeEnd;
    private Date deliverEndTime;
    /**
     * 是否清除
     */
    private String isClear;
    private String clearTimeRangeStart;
    private Date clearStartTime;
    private String clearTimeRangeEnd;
    private Date clearEndTime;
    /**
     * 是否过滤
     */
    private String isFiltered;
    private String filterTimeRangeStart;
    private Date filterStartTime;
    private String filterTimeRangeEnd;
    private Date filterEndTime;
    /**
     * 是否工程
     */
    private String isProject;
    private String projectTimeRangeStart;
    private Date projectStartTime;
    private String projectTimeRangeEnd;
    private Date projectEndTime;
    /**
     * 是否维护模式
     */
    private String isMaintain;
    private String maintainTimeRangeStart;
    private Date maintainStartTime;
    private String maintainTimeRangeEnd;
    private Date maintainEndTime;

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

    // 当前用户
    private String userName;
    /**
     * 设备实例ID
     */
    private String instanceId;
    // 查询参数
    private String searchText;

}
