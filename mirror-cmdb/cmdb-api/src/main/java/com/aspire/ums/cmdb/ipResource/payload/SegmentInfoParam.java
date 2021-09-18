package com.aspire.ums.cmdb.ipResource.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author fanwenhui
 * @date 2020-06-15 10:00
 * @description 网段列表查询参数实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "网段列表查询参数实体")
public class SegmentInfoParam {

    @ApiModelProperty(name = "idcVal",notes = "所属资源池")
    private String idcVal;
    @ApiModelProperty(name = "deviceCount",notes = "数量（台数）")
    private Integer deviceCount;
    @ApiModelProperty(name = "deviceType",notes = "设备类型：1-物理机,2-虚拟机")
    private String deviceType;
    @ApiModelProperty(name = "ipType",notes = "ip类型:1-管理IP,2-业务IP1,3-业务IP2,4-consoleIp")
    private String ipType;
    @ApiModelProperty(name = "segmentType",notes = "网段类型")
    private String segmentType;
    @ApiModelProperty(name = "segmentSubType",notes = "网段子类")
    private String segmentSubType;
    @ApiModelProperty(name = "firstBusiness",notes = "一级业务线")
    private String firstBusiness;
    @ApiModelProperty(name = "aloneBusiness",notes = "独立业务线（默认取值表单上的独立业务线）")
    private String aloneBusiness;
    @ApiModelProperty(name = "segmentAddress",notes = "网段地址")
    private String segmentAddress;
    @ApiModelProperty(name = "pageNo",notes = "页码")
    private int pageNo;
    @ApiModelProperty(name = "pageSize",notes = "行数")
    private int pageSize;
}
