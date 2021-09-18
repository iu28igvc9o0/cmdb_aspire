package com.aspire.mirror.indexproxy.domain;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * mirror自监控设备表
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    MirrorHostVO
 * 类描述:    mirror自监控设备表
 * 创建人:    JinSu
 * 创建时间:  2020/11/2 10:21
 * 版本:      v1.0
 */
@Data
public class MirrorHostVO {
    private Long id;

    @JsonProperty("hostid")
    private String hostid;

    @JsonProperty("host")
    private String ip;

    private String name;

    private String pool;

    @JsonProperty("proxy_identity")
    private String proxyIdentity;

    private Integer status;

    public static List<MirrorHostVO> parseFromZabbixHost(List<MonitorHost> right) {
        List<MirrorHostVO> list = Lists.newArrayList();
        for (MonitorHost monitorHost : right) {
            MirrorHostVO mirrorHostVO = new MirrorHostVO();
            mirrorHostVO.setHostid(monitorHost.getHostid());
            mirrorHostVO.setIp(monitorHost.getIp());
            mirrorHostVO.setName(monitorHost.getName());
            mirrorHostVO.setProxyIdentity(monitorHost.getProxyIdentity());
            mirrorHostVO.setPool(monitorHost.getPool());
            mirrorHostVO.setStatus(monitorHost.getStatus());
            list.add(mirrorHostVO);
        }
        return list;
    }

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
