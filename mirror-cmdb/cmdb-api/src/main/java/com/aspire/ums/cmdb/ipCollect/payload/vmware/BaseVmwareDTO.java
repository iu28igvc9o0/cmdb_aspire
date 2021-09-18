package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * vmware基类.
 *
 * @author jiangxuwen
 * @date 2020/3/9 16:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseVmwareDTO implements Serializable {

    private static final long serialVersionUID = 4060689027806094L;
    @JsonProperty("id")
    @JSONField(name = "id")
    private String id;

    /**
     * 唯一ID.
     */
    @JsonProperty("name")
    @JSONField(name = "name")
    private String name;

    @JsonProperty("_object_id")
    @JSONField(name = "_object_id")
    private String objectId;

    @JsonProperty("instanceId")
    @JSONField(name = "instanceId")
    private String instanceId;

    @JsonProperty("org")
    @JSONField(name = "org")
    private Long org;

    @JsonProperty("_object_version")
    @JSONField(name = "_object_version")
    private Integer objectVersion;

    @JsonProperty("_ts")
    @JSONField(name = "_ts")
    private Long timestamp;

    @JsonProperty("_version")
    @JSONField(name = "_version")
    private Integer version;

    @JsonProperty("creator")
    @JSONField(name = "creator")
    private String creator;

    @JsonProperty("ctime")
    @JSONField(name = "ctime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

    @JsonIgnore
    @JSONField(serialize = false)
    private Date updateTime;

    // 所属资源池
    @JsonProperty("resource")
    @JSONField(name = "resource")
    private String resource;

    // 检查时间
    @JsonProperty("time")
    @JSONField(name = "time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

}
