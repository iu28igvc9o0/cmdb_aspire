package com.aspire.ums.cmdb.ipResource.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanwenhui
 * @date 2020-06-15 10:44
 * @description 资产对应的所有IP类型查询参数实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "资产对应的所有IP类型查询参数实体")
public class AssetIpInfoParam {

    @ApiModelProperty(name = "recoveryType",notes = "回收类型：1-资源，2-ip")
    private String recoveryType;
    @ApiModelProperty(name = "manageIp",notes = "管理IP（默认为当前子表的管理IP字段）")
    private String manageIp;
    @ApiModelProperty(name = "ipType",notes = "IP类型")
    private String ipType;
    @ApiModelProperty(name = "ips",notes = "IP（逗号分隔的字符串）")
    private String ips;
    @ApiModelProperty(name = "pageNo", notes = "页码")
    private int pageNo;
    @ApiModelProperty(name = "pageSize", notes = "行数")
    private int pageSize;
}
