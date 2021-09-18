package com.aspire.ums.cmdb.resource.entity;//package com.aspire.ums.cmdb.resource.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResBuildNameResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("addbuildName")
    private String addBuildName;

    @JsonProperty("addresourcePool")
    private String addResourcePool;

    @JsonProperty("addserverType")
    private String addServerType;

    @JsonProperty("addserverName")
    private String addServerName;

    @JsonProperty("addcount")
    private Integer addCount;

    @JsonProperty("addunit")
    private String addUnit;

    @JsonProperty("addconfigDetail")
    private String addConfigDetail;
}
