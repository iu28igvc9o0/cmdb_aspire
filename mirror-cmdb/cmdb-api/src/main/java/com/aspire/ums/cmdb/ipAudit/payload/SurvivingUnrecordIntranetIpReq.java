package com.aspire.ums.cmdb.ipAudit.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: huanggongrui
 * @Description: 存活未录入CMDB列表请求实体类
 * @Date: create in 2020/5/23 15:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurvivingUnrecordIntranetIpReq extends IpAuditCommonReq {

    /**
     * 使用状态:合法使用；非法使用
     */
    private String useStatus;
    /**
     * 网段类型
     */
    private String networkSegmentType;
    /**
     * 网段子类
     */
    private String networkSegmentSubType;
    /**
     * 地址用途
     */
    private String ipUse;
    /**
     * 分配一级业务
     */
    private String firstBusinessSystem;
    /**
     * 分配状态:已分配,未分配
     */
    private String assignStatus;
    /**
     * 分配人
     */
    private String assignUser;
    /**
     * 分配时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String assignTimeBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String assignTimeEnd;
    /**
     * 申请人
     */
    private String requestPerson;
}
