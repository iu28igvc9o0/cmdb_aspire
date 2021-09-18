package com.aspire.cmdb.agent.sync.schedule;

import com.aspire.cmdb.agent.client.LldpInfoServiceClient;
import com.aspire.mirror.elasticsearch.api.dto.LldpData;
import com.aspire.mirror.elasticsearch.api.dto.LldpInfo;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortRelation;
import com.aspire.ums.cmdb.v2.collectUnknown.service.CmdbCollectUnknownService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstancePortRelationService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    DevicePortRelationSyncSchedule.java
 * 类描述:    设备端口关系同步任务
 * 创建人:    Luowenbo
 * 创建时间:  2019/10/21 16:39
 * 版本:      v1.0
 */

//@Component
//@EnableScheduling
@Slf4j
public class DevicePortRelationSyncSchedule {

    @Autowired
    private LldpInfoServiceClient lldpInfoServiceClient;

    @Autowired
    private ICmdbInstanceService instanceService;

    @Autowired
    private CmdbCollectUnknownService unknownService;

    @Autowired
    private ICmdbInstancePortRelationService cmdbInstancePortRelationService;

    @Scheduled(cron = "0 0 * * * ? ")
    public void syncLldpInfo() {
        List<LldpInfo> LldpInfoList =  lldpInfoServiceClient.querylldpInfoByidcAndIp(null,null);
        for (LldpInfo first : LldpInfoList) {
            List<CmdbInstancePortRelation> entitys = new ArrayList<>();
            CmdbInstance ci = new CmdbInstance();
            ci.setIp(first.getHost());
            ci.setIdcType(first.getPoolname());
            // 本段IP 和 资源池 查找CI
            CmdbInstance localCI = instanceService.get(ci);
            if(localCI == null) {
                log.error("vm_ip:{},pool:{}.未查到相应模型",first.getHost() , first.getPoolname());
                CmdbCollectUnknown collectUnknown = new CmdbCollectUnknown();
                collectUnknown.setIp(first.getHost());
                collectUnknown.setIdcType(first.getPoolname());
                collectUnknown.setDataFrom("监控上报");
                collectUnknown.setRemark("来源于监控上报数据--端口关系");
                unknownService.insert(collectUnknown);
                log.info("vm_ip:{},pool:{}. 已添加到未知设备", first.getHost(), first.getPoolname());
                continue;
            }
            String localInstanceId = localCI.getId();
            for(LldpData second : first.getLldpData()) {
                CmdbInstance peerCi = new CmdbInstance();
                peerCi.setIp(second.getPeerDeviveInfo().getPeerHost());
                peerCi.setIdcType(first.getPoolname());
                // 对端IP 和 资源池 查找CI (如果不存在，直接跳过不做处理)
//                log.info("对端设备信息data:{}.", second);
                CmdbInstance peerCI = instanceService.get(peerCi);
                if(peerCI == null) {
                    log.info("对端设备ip:{},pool:{}. 不存在", second.getPeerDeviveInfo().getPeerHost(), first.getPoolname());
                    CmdbCollectUnknown collectUnknown = new CmdbCollectUnknown();
                    collectUnknown.setIp(second.getPeerDeviveInfo().getPeerHost());
                    collectUnknown.setIdcType(first.getPoolname());
                    collectUnknown.setDataFrom("监控上报");
                    collectUnknown.setRemark("来源于监控上报数据--端口关系");
                    unknownService.insert(collectUnknown);
                    log.info("vm_ip:{},pool:{}. 已添加到未知设备", second.getPeerDeviveInfo().getPeerHost(), first.getPoolname());
                    continue;
                }
                String peerInstanceId = peerCI.getId();
                // 本端instanceId 和 端口 判断是否已存在
                CmdbInstancePortRelation selectObj = new CmdbInstancePortRelation();
                selectObj.setAInstanceId(localInstanceId);
                selectObj.setAPortId(second.getPortDesc());
                CmdbInstancePortRelation judgeObj = cmdbInstancePortRelationService.get(selectObj);
                // 判断新增还是更新
                if(judgeObj != null) {
                    CmdbInstancePortRelation addObj = new CmdbInstancePortRelation();
                    addObj.setAInstanceId(localInstanceId);
                    addObj.setAPortId(second.getPortDesc());
                    cmdbInstancePortRelationService.delete(addObj);
                }
                CmdbInstancePortRelation addObj = new CmdbInstancePortRelation();
                addObj.setAInstanceId(localInstanceId);
                addObj.setAPortId(second.getPortDesc());
                addObj.setZInstanceId(peerInstanceId);
                addObj.setZPortId(second.getPeerDeviveInfo().getPeerPortDesc());
                addObj.setConnectType("连接");
                addObj.setRemark("来源于监控上报数据");
                entitys.add(addObj);
            }
            cmdbInstancePortRelationService.insertByBatch(entitys);
        }
    }
}
