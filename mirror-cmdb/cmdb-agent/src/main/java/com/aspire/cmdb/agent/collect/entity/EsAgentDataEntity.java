package com.aspire.cmdb.agent.collect.entity;

import com.aspire.ums.cmdb.util.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 说明:
 * 工程: BPM
 * 作者: zhujuwang
 * 时间: 2021/1/14 10:08
 */
@NoArgsConstructor
@Data
public class EsAgentDataEntity {
    private String agentDate;
    private String ip;
    private String pool;
    private String result;
    private String instanceId;
    private String otherIp;
    private String exsiIp;
    private String vmName;
    private String source;
    private String type;
    private String collectorServer;
    private String message;

    public EsAgentDataEntity parseMapToEntity(Map<String, Object> map) {
        EsAgentDataEntity entity = new EsAgentDataEntity();
        if (this.haveValue(map, "agent_date")) {
            entity.setAgentDate(map.get("agent_date").toString());
        }
        if (this.haveValue(map, "ip")) {
            entity.setIp(map.get("ip").toString());
        }
        if (this.haveValue(map, "pool")) {
            entity.setPool(map.get("pool").toString());
        }
        if (this.haveValue(map, "result")) {
            entity.setResult(map.get("result").toString());
        }
        if (this.haveValue(map, "instance_id")) {
            entity.setInstanceId(map.get("instance_id").toString());
        }
        if (this.haveValue(map, "other_ip")) {
            entity.setOtherIp(map.get("other_ip").toString());
        }
        if (this.haveValue(map, "exsi_ip")) {
            entity.setExsiIp(map.get("exsi_ip").toString());
        }
        if (this.haveValue(map, "vm_name")) {
            entity.setVmName(map.get("vm_name").toString());
        }
        if (this.haveValue(map, "source")) {
            entity.setSource(map.get("source").toString());
        }
        if (this.haveValue(map, "type")) {
            entity.setType(map.get("type").toString());
        }
        if (this.haveValue(map, "collector_server")) {
            entity.setCollectorServer(map.get("collector_server").toString());
        }
        if (this.haveValue(map, "message")) {
            entity.setMessage(map.get("message").toString());
        }
        return entity;
    }

    private boolean haveValue (Map<String, Object> map, String key) {
        return map.containsKey(key) && StringUtils.isNotEmpty(map.get(key));
    }

    
}
