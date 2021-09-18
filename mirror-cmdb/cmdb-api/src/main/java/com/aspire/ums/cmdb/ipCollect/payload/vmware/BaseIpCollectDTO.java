package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 存活IP采集基类
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 10:30
 */
@Data
@ToString(callSuper = true)
public class BaseIpCollectDTO extends BaseVmwareDTO implements Serializable {
    private static final long serialVersionUID = 993934720694890028L;

    // IP地址
    @JsonProperty("ip")
    @JSONField(name = "ip")
    private String ip;

    // IP类型
    @JsonProperty("iptype")
    @JSONField(name = "iptype")
    private String iptype;

    // MAC地址
    @JsonProperty("mac")
    @JSONField(name = "mac")
    private String mac;
}
