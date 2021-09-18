package com.aspire.ums.cmdb.ipAudit.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ip稽核--IPV6存活未分配IP")
public class IpAuditSurvivingUnassignIpV6 {

    @ApiModelProperty(name = "id",notes = "主键")
    private String id;
    @ApiModelProperty(name = "ip",notes = "检测需记录的ip")
    private String ip;
    @ApiModelProperty(name = "checkTime",notes = "检测时间")
    private Date checkTime;
    @ApiModelProperty(name = "processingStatus",notes = "处理状态")
    private String processingStatus;
    @ApiModelProperty(name = "reason",notes = "原因说明")
    private String reason;
    @ApiModelProperty(name = "operator",notes = "操作人")
    private String operator;
    @ApiModelProperty(name = "operatingTime",notes = "操作时间")
    private Date operatingTime;
    @ApiModelProperty(name = "checkNum",notes = "检测次数")
    private Long checkNum;
    @ApiModelProperty(name = "isDelete",notes = "删除标志 1 删除")
    private Integer isDelete;
    @ApiModelProperty(name = "updateTime",notes = "更新时间")
    private Date updateTime;
    @ApiModelProperty(name = "updatePerson",notes = "更新人")
    private String updatePerson;

    // =========IP地址库字段============
    private String ipv6Id; // ipv6地址库id
    private String ipv6SegmentId; // ipv6网段id
    private String ipv6SegmentName; // ipv6网段名称
    private String dc; // 所属资源池
    private String isPool;
    private String ipv6SegmentType;
    private String ipv6SegmentSubType;
    private String ipv6SegmentUse;
    private String firstBusinessSystem;
    private String aloneBusinessSystem;
    private String isCmdbManager;
    private String useBusiness;
}