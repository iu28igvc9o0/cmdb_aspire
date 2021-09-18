package com.aspire.ums.cmdb.ipResource.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanwenhui
 * @date 2020-06-15 10:40
 * @description 资源选择弹框列表查询参数实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "资源选择弹框列表查询参数实体")
public class AssetInfoParam {

    @ApiModelProperty(name = "recoveryType",notes = "回收类型：1-资源，2-ip")
    private String recoveryType;
    @ApiModelProperty(name = "assetIds",notes = "资产ID")
    private String assetIds;
    @ApiModelProperty(name = "idcVal",notes = "所属资源池")
    private String idcVal;
    @ApiModelProperty(name = "aloneBusiness",notes = "独立业务线ID")
    private String aloneBusiness;
    @ApiModelProperty(name = "aloneBusiness2",notes = "独立业务子模块ID")
    private String aloneBusiness2;
    @ApiModelProperty(name = "ips",notes = "IP（逗号分隔的字符串）")
    private String ips;
    @ApiModelProperty(name = "deviceClass",notes = "设备分类")
    private String deviceClass;
    @ApiModelProperty(name = "deviceType",notes = "设备类型")
    private String deviceType;
    @ApiModelProperty(name = "machineRoom",notes = "机房位置")
    private String machineRoom;
    @ApiModelProperty(name = "hostBack",notes = "主备")
    private String hostBack;
    @ApiModelProperty(name = "pageNo", notes = "页码")
    private int pageNo;
    @ApiModelProperty(name = "pageSize", notes = "行数")
    private int pageSize;
    @ApiModelProperty(name = "aloneBusinessCode",notes = "独立业务线Code")
    private String aloneBusinessCode;
    @ApiModelProperty(name = "aloneBusinessCode2",notes = "独立业务子模块Code")
    private String aloneBusinessCode2;

}
