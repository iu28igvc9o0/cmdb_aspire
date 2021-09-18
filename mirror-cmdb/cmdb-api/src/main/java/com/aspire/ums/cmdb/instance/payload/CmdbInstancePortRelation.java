package com.aspire.ums.cmdb.instance.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
* 描述：
* @author
* @date 2019-09-05 10:26:54
*/
@Data
public class CmdbInstancePortRelation {

    /**
     * ID
     */
    private String id;
    /**
     * 本端设备ID
     */
    @JsonProperty("aInstanceId")
    private String aInstanceId;

    /**
     * 本端设备名称
     */
    @JsonProperty("aDeviceName")
    private String aDeviceName;

    /**
     * 本端图标地址
     */
    @JsonProperty("aIconUrl")
    private String aIconUrl;
    /**
     * 本端端口
    */
    @JsonProperty("aPortId")
    private String aPortId;

    /**
     * 对端设备ID
     */
    @JsonProperty("zInstanceId")
    private String zInstanceId;

    /**
     * 对端设备Ip
     */
    @JsonProperty("zIp")
    private String zIp;
    /**
     * 对端设备名称
     */
    @JsonProperty("zDeviceName")
    private String zDeviceName;
    /**
     * 对端资源池名称
     */
    @JsonProperty("zIdcType")
    private String zIdcType;
    /**
     * 对端图标地址
     */
    @JsonProperty("zIconUrl")
    private String zIconUrl;
    /**
     * 对端设备端口名称
     */
    @JsonProperty("zPortId")
    private String zPortId;
    /**
     * 连接属性
     */
    private String connectType;
    /**
     * 描述
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;


    @JsonIgnore
    public String getAInstanceId() {
        return aInstanceId;
    }
    @JsonIgnore
    public String getAPortId() {
        return aPortId;
    }
    @JsonIgnore
    public String getZInstanceId() {
        return zInstanceId;
    }
    @JsonIgnore
    public String getZPortId() {
        return zPortId;
    }

    @JsonIgnore
    public String getAIconUrl() {
        return aIconUrl;
    }
    @JsonIgnore
    public String getZIconUrl() {
        return zIconUrl;
    }
    @JsonIgnore
    public String getZIp() {
        return zIp;
    }
    @JsonIgnore
    public String getZDeviceName() {
        return zDeviceName;
    }
    @JsonIgnore
    public String getZIdcType() {
        return zIdcType;
    }
}