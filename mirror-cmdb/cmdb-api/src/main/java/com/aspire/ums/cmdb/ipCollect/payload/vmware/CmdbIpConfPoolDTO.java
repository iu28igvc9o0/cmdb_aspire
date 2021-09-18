package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 模型id:IP_CONF_POOL
 * 数据来源:网络设备配置文件解析
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 10:33
 */
@Data
@ToString(callSuper = true)
public class CmdbIpConfPoolDTO extends BaseIpCollectDTO implements Serializable {
    private static final long serialVersionUID = -1219617487223354035L;

    // 来源设备IP
    @JsonProperty("srcip")
    @JSONField(name = "srcip")
    private String srcip;
}
