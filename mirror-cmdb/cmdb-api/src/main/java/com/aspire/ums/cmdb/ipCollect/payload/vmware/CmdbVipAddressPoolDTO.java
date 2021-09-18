package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 15:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CmdbVipAddressPoolDTO extends BaseVmwareDTO implements Serializable {

    private static final long serialVersionUID = 8665335107737499935L;
    // IP地址
    @JsonProperty("vip")
    @JSONField(name = "vip")
    private String vip;

    // 当前绑定ip
    @JsonProperty("bindip")
    @JSONField(name = "bindip")
    private String bindip;

    // 飘移IP列表
    @JsonProperty("iplist")
    @JSONField(name = "iplist")
    private String iplist;

    // 虚拟IP使用类型
    @JsonProperty("usetype")
    @JSONField(name = "usetype")
    private String usetype;

}
