package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author fanwenhui
 * @date 2020-12-25 14:28
 * @description 网段-端口组入参实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VmwareNetworkPortGroupDto extends BaseVmwareDTO implements Serializable {
    private static final long serialVersionUID = 4412795795497560775L;

    @JsonProperty("resourcepool")
    @JSONField(name = "resourcepool")
    private String resourcepool;

    @JsonProperty("datacentername")
    @JSONField(name = "datacentername")
    private String datacentername;

    @JsonProperty("portgroupname")
    @JSONField(name = "portgroupname")
    private String portgroupname;
}
