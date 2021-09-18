package com.aspire.ums.cmdb.instance.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.ums.cmdb.instance.payload
 * 类名称:    CmdbDeviceTypeCount.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/8/29 14:47
 * 版本:      v1.0
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CmdbDeviceTypeByConditonCount {
    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("数量")
    private String count;

    @ApiModelProperty("cpu总核数")
    private String cpuCoreNumber;

    @ApiModelProperty("内存总量")
    private String memorySize;
    
    @ApiModelProperty("业务系统")
    private String bizSystem;
    
    @ApiModelProperty("一级部门")
    private String department1;
    
    @ApiModelProperty("资源池")
    private String idcType;

}
