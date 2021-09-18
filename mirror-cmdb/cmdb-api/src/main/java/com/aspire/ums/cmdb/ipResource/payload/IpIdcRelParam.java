package com.aspire.ums.cmdb.ipResource.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 需要修改的IP和资源池关联数据
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/28 17:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IpIdcRelParam {
    @ApiModelProperty(name = "idc", notes = "资源池")
    private String idc;
    @ApiModelProperty(name = "ips", notes = "IP,多个时英文逗号分隔")
    private String ips;
}
