package com.aspire.ums.cmdb.ipAudit.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: huanggongrui
 * @Description: 存活未录入CMDB列表响应实体类
 * @Date: create in 2020/5/23 15:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurvivingUnrecordIntranetIpResp extends IpAuditCommonResp {
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
     * 分配状态
     */
    private String assignStatus;
    /**
     * 分配人
     */
    private String assignUser;
    /**
     * 分配时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String assignTime;
    /**
     * 申请人
     */
    private String requestPerson;
}
