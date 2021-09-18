package com.aspire.ums.cmdb.v3.module.event.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.cmic.service.CmdbModuleKafkaEventService;
import com.aspire.ums.cmdb.cmic.util.EventThreadUtils;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import com.aspire.ums.cmdb.v3.module.event.EventConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ChangeOtherModuleDataEvent
 * Author:   hangfang
 * Date:     2020/10/19
 * Description: 改变业务系统级别同步更改主机资源的业务系统级别
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class ChangeOtherModuleDataEvent extends AbstractModuleEvent {

    @Autowired
    ICmdbInstanceService instanceService;
    @Autowired
    ICmdbConfigService configService;
    @Autowired
    ICmdbCodeService codeService;

    @Override
    public void initSpringBeans() {
        if (this.instanceService == null) {
            this.instanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
        if (this.configService == null) {
            this.configService = SpringUtils.getBean(ICmdbConfigService.class);
        }
        if (this.codeService == null) {
            this.codeService = SpringUtils.getBean(ICmdbCodeService.class);
        }
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("flag", "true");
        try {
            String username = handleData.get("username").toString();
            // 获取需要进行变更的字段
            CmdbConfig changeEvent = configService.getConfigByCode(String.format(EventConst.CHANGE_EVENT_MODULE, moduleId));
            if (changeEvent == null) {
                log.error("未配置模型{}的变更配置", moduleId);
                return null;
            }
            // {"codeId":"","changeModuleId":"","changeCodeId":""}
            Map changeEventInfo = JSONObject.parseObject(changeEvent.getConfigValue(), Map.class);
            String codeId = changeEventInfo.get("codeId").toString();
            String changeModuleId = changeEventInfo.get("changeModuleId").toString();
            String changeCodeId = changeEventInfo.get("changeCodeId").toString();
            String relationCode = changeEventInfo.get("relationCode").toString();
            CmdbSimpleCode fromCode = codeService.getSimpleCodeById(codeId);
            CmdbSimpleCode changeCode = codeService.getSimpleCodeById(changeCodeId);
            // 获取业务系统级别审核数据
            List<CmdbCollectApproval> approvals = JSONArray.parseArray(JSON.toJSONString(handleData.get("approvals")), CmdbCollectApproval.class);
            // 批量更新的查询map
            Map<String, Object> batchUpdate = new HashMap<>();
            // 获取所有需要变更的值
            List<Map<String, Object>> updateList = new ArrayList<>();
            CmdbCollectApproval changeApproval = new CmdbCollectApproval();
            for (CmdbCollectApproval approval : approvals) {
                // 如果不是字段更新
                if (StringUtils.isEmpty(approval.getCodeId())) {
                    continue;
                }
                if (!approval.getCodeId().equals(codeId)) {
                    continue;
                }
                // 如果是有变更事件的字段
                BeanUtils.copyProperties(approval, changeApproval);
                JSONObject rd = JSONObject.parseObject(approval.getResourceData());
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("codeId", changeCode.getFiledCode());
                updateMap.put("value", rd.get(fromCode.getFiledCode()));
                updateList.add(updateMap);
            }
            if (updateList.size() == 0) {
                return resultData;
            }
            List<Map<String, Object>> queryList = new ArrayList<>();
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("field", relationCode);
            queryMap.put("operator", "=");
            queryMap.put("value", instanceId);
            queryList.add(queryMap);
            batchUpdate.put("update", updateList);
            batchUpdate.put("querys", queryList);
            List<String> instanceList = instanceService.getBatchUpdateApprovals(null, changeModuleId, batchUpdate);
            for (String id :  instanceList) {
                EventThreadUtils.FIXED_POOL.execute(() -> {
                    log.info(">>>>>>> 开始处理变更事件 设备id:{}变更字段{}", id, changeCode.getFiledCode());
                    CmdbCollectApproval approval = new CmdbCollectApproval();
                    approval.setInstanceId(id);
                    approval.setCodeId(changeCodeId);
                    approval.setModuleId(changeModuleId);
                    approval.setOwnerModuleId(changeModuleId);
                    Map<String, Object> resourceData = new HashMap<>();
                    JSONObject rd = JSONObject.parseObject(changeApproval.getResourceData());
                    resourceData.put(changeCode.getFiledCode(), rd.get(fromCode.getFiledCode()));
                    approval.setResourceData(JSON.toJSONString(resourceData));
                    List<CmdbCollectApproval> approvalList = new ArrayList<>();
                    approvalList.add(approval);
                    instanceService.update(username, approvalList);
                    log.info(">>>>>>> 处理变更事件结束 设备id:{}变更字段{}", id, changeCode.getFiledCode());
                });

            }
        } catch (Exception e) {
            log.error(">>>>>>> 处理变更事件异常，error：{}", e.getMessage());
            return resultData;
        }
        return resultData;
    }
}
