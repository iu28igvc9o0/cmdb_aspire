package com.aspire.ums.cmdb.assessment.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.faultEvent.payload
 * 类名称:
 * 类描述: 故障事件信息
 * 创建人: PJX
 * 创建时间: 2019/6/25 20:13
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbProblemEvent implements Serializable {
    
    /**
     * 故障事件id
     */
    private String id;
    
    /**
     * 故障名称
     */
    private String problemName;
    
    /**
     * 故障等级
     */
    private String problemLevel;
    
    /**
     * 发生省份
     */
    private String province;
    
    /**
     * 发生地市
     */
    private String city;
    
    /**
     * 厂家
     */
    private String produce;
    
    /**
     * 设备分类
     */
    private String deviceType;
    
    /**
     * 设备类型
     */
    private String deviceModel;
    
    /**
     * 典配类型
     */
    private String typicalType;
    
    /**
     * 维保归属
     */
    private String repaireBelong;
    
    /**
     * 故障部件类型
     */
    private String problemPartType;
    
    /**
     * 故障开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp problemStartTime;
    
    /**
     * 业务恢复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp bizRegainTime;
    
    /**
     * 故障消除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp problemRemoveTime;
    
    /**
     * 业务恢复时长
     */
    private String bizRegainLong;
    
    /**
     * 故障处理时长
     */
    private String problemHandleLong;
    
    /**
     * 故障处理次数
     */
    private String problemHandleCount;
    
    /**
     * 备件部件更换次数
     */
    private String problemPartChangeCount;
    
    /**
     * 故障报告及相关证明材料
     */
    private String reportDesc;
    
    /**
     * 审批状态 (1审批通过，0审批驳回，-1录入待审批)
     */
    private int status;

    private String createUsername;
    
    private String createUserPhone;
    
    private boolean isdiableEdit;

    /**
     * 所属季度
     */
    private String quarter;
}
