package com.aspire.ums.cmdb.ipResource.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanwenhui
 * @date 2020-06-15 9:50
 * @description 自动回填可分配IP查询参数实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "自动回填可分配IP查询参数实体")
public class AutoAllocateIpParam {

    @ApiModelProperty(name = "aloneBusiness",notes = "所属独立业务Code")
    private String aloneBusiness;
    @ApiModelProperty(name = "idcVal",notes = "资源池")
    private String idcVal;
    @ApiModelProperty(name = "deviceCount",notes = "数量（台数）")
    private Integer deviceCount;
    @ApiModelProperty(name = "deviceType",notes = "设备类型:1-物理机,2-虚拟机")
    private String deviceType;
}
