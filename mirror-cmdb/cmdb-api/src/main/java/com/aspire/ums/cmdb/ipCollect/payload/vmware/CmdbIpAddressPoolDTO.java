package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 模型id:IP_ADDRESS_POOL
 * 数据来源:	网络设备
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 15:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CmdbIpAddressPoolDTO extends BaseIpCollectDTO implements Serializable {

    private static final long serialVersionUID = -728673614031309171L;

    // 网关设备IP
    @JsonProperty("gateway")
    @JSONField(name = "gateway")
    private String gateway;

}
