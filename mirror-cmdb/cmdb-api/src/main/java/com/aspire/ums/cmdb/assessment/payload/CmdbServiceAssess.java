package com.aspire.ums.cmdb.assessment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.assessment.payload
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/15 20:31
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbServiceAssess {
    /**
     * 设备类型
     */
    private String deviceType;
    
    /**
     * 厂商
     */
    private String produce;
    
    /**
     * 维修事件-事件数
     */
    private String eventCount;

    /**
     * 维修事件-总事件数
     */
    private String totalEventCount;
    
    /**
     * 维修事件-事件占比
     */
    private String eventRate;
    
    /**
     * 维修事件-设备占比
     */
    private String eventDeviceRate;
    
    /**
     * 维修事件-指标值
     */
    private String eventTargetValue;
    
    /**
     * 维修事件-得分
     */
    private String eventScore;
    
    /**
     * 故障事件故障数
     */
    private String problemCount;

    /**
     * 故障事件得分
     */
    private String problemScore;
    
    /**
     * 设备问题-问题数
     */
    private String equipmentProblemCount;

    /**
     * 总设备问题数
     */
    private String totalEquipmentProblemCount;

    /**
     * 设备问题-问题占比
     */
    private String equipmentProblemRate;
    
    /**
     * 设备问题-设备占比
     */
    private String equipmentProblemDeviceRate;
    
    /**
     * 设备问题指标值
     */
    private String equipmentProblemTargetValue;
    
    /**
     * 设备问题得分
     */
    private String equipmentProblemScore;
    
    /**
     * 故障处理时长-总时长
     */
    private String eventProblemHandleLong;
    
    /**
     * 故障处理时长-指标值
     */
    private String problemHandleTargetValue;
    
    /**
     * 故障处理时长-得分
     */
    private String problemHandleScore;
    
    /**
     * 业务恢复处理平均时长-总时长
     */
    private String bizRepairLong;
    
    /**
     * 业务恢复处理平均时长-指标值
     */
    private String bizRepairTargetValue;
    
    /**
     * 业务恢复处理平均时长-得分
     */
    private String bizRepairScore;
    
    /**
     * 服务评估得分-指标值
     */
    private String serviceScore;
    
    /**
     * 综合满意度-指标值
     */
    private String satisfactionScore;
    
    /**
     * 最终得分
     */
    private String score;
    
//    /**
//     * 最终得分（用于展示）
//     */
//    private float score2;
//
//    /**
//     * 系数 (用于计算总得分 小于2个false 0.9 大于1个false 0.4)
//     */
//    private float rate;
//
    /**
     * 总设备量
     */
    private String totalDeviceCount;

    /**
     * 所属季度
     */
    private String quarter;

    /**
     * 创建时间
     */
    private Date createTime;
}
