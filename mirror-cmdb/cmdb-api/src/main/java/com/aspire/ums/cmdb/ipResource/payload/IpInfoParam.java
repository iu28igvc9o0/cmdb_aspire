package com.aspire.ums.cmdb.ipResource.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanwenhui
 * @date 2020-06-15 10:06
 * @description Ip列表查询参数实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "IP地址列表查询参数实体")
public class IpInfoParam {

    @ApiModelProperty(name = "segmentId", notes = "网段主键ID")
    private String segmentId;
    @ApiModelProperty(name = "segmentAddress", notes = "网段地址")
    private String segmentAddress;
    @ApiModelProperty(name = "innerIpUse", notes = "用途分类")
    private String innerIpUse;
    @ApiModelProperty(name = "innerIpSubUse", notes = "用途子类")
    private String innerIpSubUse;
    @ApiModelProperty(name = "ip", notes = "可分配Ip")
    private String ip;
    @ApiModelProperty(name = "pageNo", notes = "页码")
    private int pageNo;
    @ApiModelProperty(name = "pageSize", notes = "行数")
    private int pageSize;
    @ApiModelProperty(name = "segmentType", notes = "网段类型")
    private String segmentType;
    @ApiModelProperty(name = "deviceType", notes = "设备类型:1-物理机，2-虚拟机")
    private String deviceType;
}
