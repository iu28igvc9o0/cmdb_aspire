package com.aspire.ums.cmdb.ipResource.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-06-15 10:12
 * @description IP地址库更新参数实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "IP地址库更新参数实体")
public class IpInfoUpdateParam {

    @ApiModelProperty(name = "ipIdcRelMap",notes = "需要修改的IP和资源池关联数据")
    private List<IpIdcRelParam> ipIdcRelMap;
    @ApiModelProperty(name = "assignStatus",notes = "分配状态")
    private String assignStatus;
    @ApiModelProperty(name = "assignUser",notes = "分配人")
    private String assignUser;
    @ApiModelProperty(name = "assignTime",notes = "分配时间")
    private String assignTime;
    @ApiModelProperty(name = "survivalStatus",notes = "存活状态")
    private String survivalStatus;
    @ApiModelProperty(name = "applicater",notes = "申请人")
    private String applicater;
    @ApiModelProperty(name = "applicatOrder",notes = "申请工单")
    private String applicatOrder;
    @ApiModelProperty(name = "applicatTime",notes = "申请时间")
    private String applicatTime;
    @ApiModelProperty(name = "useLife",notes = "使用期限(年),保留小数点后2位")
    private String useLife;
    @ApiModelProperty(name = "ipUpdateFlag",notes = "是否清空该Ip的其他信息:1-IP信息正常更新,2-清空IP其他信息")
    private String ipUpdateFlag;
}
