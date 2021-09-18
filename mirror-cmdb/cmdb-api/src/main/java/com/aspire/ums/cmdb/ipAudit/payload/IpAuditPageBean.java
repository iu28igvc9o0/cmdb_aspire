package com.aspire.ums.cmdb.ipAudit.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huanggongrui
 * @Description: ip稽核分页响应实体类
 * @Date: create in 2020/5/26 14:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpAuditPageBean<T> {
    /**
     * 总数
     */
    private int count;
    /**
     * 列表对象
     */
    private List<T> result;
    //----------------------存活未录入CMDB相关字段---------------------------
    /**
     * 未录CMDB的IP总数
     */
    private Integer numOfUnrecordIp;
    /**
     * 待处理IP数
     */
    private Integer numOfToBeProcessedIp;
    /**
     * 暂不处理IP数
     */
    private Integer numOfNotProcessIp;
    /**
     * 非法使用IP数
     */
    private Integer numOfUnsuitableUseIp;

    //----------------------已录CMDB未存活IP相关字段---------------------------
    /**
     * 问题资产总数
     */
    private Integer numOfProblemAsset;
    /**
     * 未存活IP总数
     */
    private Integer numOfUnsurvivingIp;
    /**
     * 待处理资产数
     */
    private Integer numOfToBeProcessAsset;

    //----------------------已录CMDB未存活IP相关字段---------------------------
    /**
     * 一个月内未存活IP总数
     */
    private Integer numOfUnsurvivingIpWithinOneMonth;
    /**
     * 三个月内未存活IP总数
     */
    private Integer numOfUnsurvivingIpWithinThreeMonths;
    //-----------------------登记已过期IP相关字段------------------------------
    /**
     * 未登记IP总数
     */
    private Integer numOfUnregistration;
    //-----------------------存活未规划IP相关字段------------------------------
    /**
     * 存活未规划IP总数
     */
    private Integer numOfSurvivingUnplannedIp;
    //-----------------------存活未分配IP相关字段------------------------------
    /**
     * 存活未分配IP总数
     */
    private Integer numOfSurvivingUnassginIp;
    //----------------------存活未规划IP相关字段(暂不处理，待处理IP数同 存活未录入CMD)---------------------------
    /**
     * 存活未规划IP总数
     */
    private Integer numOfUnPlanIp;

}
