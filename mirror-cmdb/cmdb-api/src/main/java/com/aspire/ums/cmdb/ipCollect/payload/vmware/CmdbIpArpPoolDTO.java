package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 模型id:IP_ARR_POOL
 * 数据来源:arp扫描网段地址
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 10:38
 */
@Data
@ToString(callSuper = true)
public class CmdbIpArpPoolDTO extends BaseIpCollectDTO implements Serializable {
    private static final long serialVersionUID = 2418070201216754140L;
    // 来源设备IP
    @JsonProperty("srcip")
    @JSONField(name = "srcip")
    private String srcip;

    // 指令占用内存百分比
    @JsonProperty("cpu")
    @JSONField(name = "cpu")
    private String cpu;

    // 指令占用内存百分比
    @JsonProperty("mem")
    @JSONField(name = "mem")
    private String mem;
}
