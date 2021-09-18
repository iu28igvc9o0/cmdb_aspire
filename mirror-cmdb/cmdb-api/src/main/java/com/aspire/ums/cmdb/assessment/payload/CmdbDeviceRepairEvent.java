package com.aspire.ums.cmdb.assessment.payload;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* 描述：
* @author
* @date 2019-06-25 19:56:44
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbDeviceRepairEvent {

    /**
     * 维修事件id
     */
    private String id;
    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 级别（一般，重要，严重）
     */
    private String eventLevel;
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
     * 设备类型id
     */
    private String deviceType;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 典配类型
     */
    private String dianpeiType;
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
    private Date problemStartTime;
    /**
     * 故障消除时间
     */
    private Date problemEndTime;
    /**
     * 故障处理时长
     */
    private String problemLong;
    /**
     * 故障处理次数
     */
    private Integer problemHandleCount;
    /**
     * 备件部件更换次数
     */
    private Integer problemPartChangeCount;
    /**
     * 填报人
     */
    private String createUsername;

    /**
     * 填报人电话
     */
    private String createUserPhone;
    /**
     * 填报时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 审批状态 (审批通过，审批驳回，录入待审批)
     */
    private Integer status;
    /**
     * 所属季度
     */
    private String quarter;
}