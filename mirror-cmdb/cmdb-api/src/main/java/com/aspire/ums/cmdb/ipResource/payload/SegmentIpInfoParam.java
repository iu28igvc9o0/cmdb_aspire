package com.aspire.ums.cmdb.ipResource.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanwenhui
 * @date 2020-06-15 10:46
 * @description 网段-IP查询参数实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "网段-IP查询参数实体")
public class SegmentIpInfoParam {

    @ApiModelProperty(name = "idcVal",notes = "所属资源池")
    private String idcVal;
    @ApiModelProperty(name = "changeIpCount",notes = "变更IP数")
    private String changeIpCount;
    @ApiModelProperty(name = "ipType",notes = "ip类型：1-内网IP,2-公网IP,3-IPV6")
    private String ipType;
    @ApiModelProperty(name = "segmentType",notes = "网段类型")
    private String segmentType;
    @ApiModelProperty(name = "segmentSubType",notes = "网段子类")
    private String segmentSubType;
    @ApiModelProperty(name = "ipUseClass",notes = "用途分类")
    private String ipUseClass;
    @ApiModelProperty(name = "ipUseSubClass",notes = "用途子类")
    private String ipUseSubClass;
    @ApiModelProperty(name = "firstBusiness",notes = "一级业务线")
    private String firstBusiness;
    @ApiModelProperty(name = "aloneBusiness",notes = "独立业务线（默认取值表单上的独立业务线）")
    private String aloneBusiness;
    @ApiModelProperty(name = "segmentAddress",notes = "网段地址")
    private String segmentAddress;
    @ApiModelProperty(name = "ips",notes = "IP(逗号分隔的字符串)")
    private String ips;
    @ApiModelProperty(name = "pageNo",notes = "页码")
    private int pageNo;
    @ApiModelProperty(name = "pageSize",notes = "行数")
    private int pageSize;
}
