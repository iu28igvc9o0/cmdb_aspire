package com.aspire.mirror.indexproxy.domain;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

@Data
public class MonitorHost {
    private Long id;

    @JsonProperty("hostid")
    private String hostid;

    @JsonProperty("name")
    private String ip;

    @JsonProperty("host")
    private String name;

    private String pool;

    @JsonProperty("proxy_identity")
    private String proxyIdentity;

    private Integer status;

    public MonitorObjectRecord parseToMonitorObject() {
        MonitorObjectRecord monitorObjectRecord = new MonitorObjectRecord();
        monitorObjectRecord.setObjectType(MonitorObjectRecord.Object_TYPE_SELF_MONITOR);
        monitorObjectRecord.setName(this.name);
        monitorObjectRecord.setObjectId(this.id.toString());
        Map<String, Object> extentObjMap = Maps.newHashMap();
        extentObjMap.put("hostid", this.hostid);
        extentObjMap.put("ip", this.ip);
        monitorObjectRecord.setExtendObj(JSON.toJSONString(extentObjMap));
        return monitorObjectRecord;
    }
}
