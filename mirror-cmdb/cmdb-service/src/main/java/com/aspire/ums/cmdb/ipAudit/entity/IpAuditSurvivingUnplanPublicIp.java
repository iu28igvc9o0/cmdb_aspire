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
@ApiModel(value = "ip稽核--存活未规划公网IP")
public class IpAuditSurvivingUnplanPublicIp {

    @ApiModelProperty(name = "id",notes = "主键")
    private String id;
    @ApiModelProperty(name = "ip",notes = "检测需记录的ip")
    private String ip;
    @ApiModelProperty(name = "networkIp",notes = "网络设备IP")
    private String networkIp;
    @ApiModelProperty(name = "dc",notes = "所属资源池")
    private String dc;
    @ApiModelProperty(name = "checkTime",notes = "检测时间")
    private Date checkTime;
    @ApiModelProperty(name = "isNotify",notes = "是否已通知:0:是,1:否")
    private String isNotify;
    @ApiModelProperty(name = "processingStatus",notes = "处理状态,默认为暂不处理")
    private String processingStatus;
    @ApiModelProperty(name = "reason",notes = "原因说明")
    private String reason;
    @ApiModelProperty(name = "operator",notes = "操作人")
    private String operator;
    @ApiModelProperty(name = "operatingTime",notes = "操作时间")
    private Date operatingTime;
    @ApiModelProperty(name = "checkNum",notes = "检测次数")
    private Long checkNum;
    @ApiModelProperty(name = "orderNum",notes = "工单号")
    private String orderNum;
    @ApiModelProperty(name = "isDelete",notes = "删除标志 1 删除")
    private Integer isDelete;
    @ApiModelProperty(name = "updateTime",notes = "更新时间")
    private Date updateTime;
    @ApiModelProperty(name = "updatePerson",notes = "更新人")
    private String updatePerson;

}