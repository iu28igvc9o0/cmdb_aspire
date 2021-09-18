package com.aspire.ums.cmdb.ipResource.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanwenhui
 * @date 2020-06-15 10:09
 * @description 资产导出参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "资产导出参数")
public class AssetExportParam {

    @ApiModelProperty(name = "bpmRunId",notes = "工单号")
    private String bpmRunId;
    @ApiModelProperty(name = "formMainId",notes = "业务表单主键")
    private String formMainId;
    @ApiModelProperty(name = "subTableType",notes = "子表类型：1- 物理机,2- 虚拟机")
    private String subTableType;
}
