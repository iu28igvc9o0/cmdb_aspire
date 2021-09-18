package com.aspire.cmdb.agent.schedule;

import com.alibaba.fastjson.JSON;
import com.aspire.cmdb.agent.util.BPMUtil;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApprovalQuery;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.helper.BpmTokenHelper;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectApprovalService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CheckApproveRemind
 * Author:   hangfang
 * Date:     2020/12/7
 * Description: 配置审核有数据则创建工单给阿丽
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@EnableScheduling
@Slf4j
@Component
public class CheckApproveRemind {

    @Autowired
    private CmdbCollectApprovalService approvalService;
    @Autowired
    private ConfigDictService dictService;
    @Autowired
    private ICmdbConfigService configService;

    @Autowired
    private BpmTokenHelper bpmTokenHelper;

    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void checkApprove() {
        List<ConfigDict> idcTypeList= dictService.selectDictsByType("idcType", null, null, null);
        CmdbConfig config = configService.getConfigByCode("check_approve_bpm");
        if (config == null) {
            throw new RuntimeException("未配置配置审核工单信息[check_approve_bpm]");
        }
        Map<String, String> configMap = JSON.parseObject(config.getConfigValue(), Map.class);
        String rwclnrConfig = configMap.get("rwclnr");
        String rwmcConfig = configMap.get("rwmc");
        if (idcTypeList != null && idcTypeList.size() > 0) {
            for (ConfigDict idcType : idcTypeList) {
                CmdbCollectApprovalQuery query = new CmdbCollectApprovalQuery();
                Map<String, String> primaryQuery = new HashMap<>();
                primaryQuery.put("idcType", idcType.getId());
                query.setPrimaryQuery(primaryQuery);
                Integer total = approvalService.listCount(query);
                if (total > 0 ) {
                    String rwclnr = rwclnrConfig.replace("[idcType]", idcType.getValue());
                    rwclnr = rwclnr.replace("[count]", total.toString());
                    Map<String, Object> taskForm = new HashMap<>();
                    taskForm.put("rwlx", "2");
                    taskForm.put("rwclxgfj", "");
                    taskForm.put("zyc", idcType.getValue());
                    taskForm.put("rwclnr", rwclnr);
                    taskForm.put("rwnr", "");
                    taskForm.put("rwmc", rwmcConfig);
                    new BPMUtil().createTask(bpmTokenHelper.getBUSINESSSYSTEMCHANGETOBPMURL(), "admin", taskForm);
                }
            }
        }
    }

}
