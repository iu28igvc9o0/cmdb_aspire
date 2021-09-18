package com.aspire.ums.cmdb.instance.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbInstancePortQuery
 * Author:   hangfang
 * Date:     2019/9/5
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class CmdbInstancePortQuery {
    /**
     * 本端设备id
     */
    @JsonProperty("aInstanceId")
    private String aInstanceId;

    /**
     * 本端端口
     */
    @JsonProperty("aPortId")
    private String aPortId;
    /**
     * 对端设备IP
     */
    @JsonProperty("zInstanceIp")
    private String zInstanceIp;
    /**
     * 对端设备端口名称
     */
    @JsonProperty("zPortId")
    private String zPortId;

    /**
     * 当前页
     */
    private Integer pageNo;
    /**
     * 每页显示数量
     */
    private Integer pageSize;

    @JsonIgnore
    public void setAInstanceId(String aInstanceId) {
        this.aInstanceId = aInstanceId;
    }
    @JsonIgnore
    public void setAPortId(String aPortId) {
        this.aPortId = aPortId;
    }
    @JsonIgnore
    public void setZInstanceIp(String zInstanceIp) {
        this.zInstanceIp = zInstanceIp;
    }
    @JsonIgnore
    public void setZPortId(String zPortId) {
        this.zPortId = zPortId;
    }
}
