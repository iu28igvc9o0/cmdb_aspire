package com.aspire.ums.cmdb.v3.module.event.code;

import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: NetworkSegmentOwnerEvent
 * Author:   hangfang
 * Date:     2020/5/28
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class InnerRequestPersonEvent extends AbstractModuleEvent {
    @Autowired
    private ICmdbConfigService cmdbConfigService;
    @Autowired
    private ICmdbInstanceService instanceService;
    @Override
    public void initSpringBeans() {
        if (this.cmdbConfigService == null) {
            this.cmdbConfigService = SpringUtils.getBean(ICmdbConfigService.class);
        }
        if (this.instanceService == null) {
            this.instanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        this.initSpringBeans();
        Map<String, Object> returnMap = new HashMap<>();
        //获取申请人
        Map<String, Object> selectItem = handleData.containsKey("selectItem") ? (Map<String, Object>)handleData.get("selectItem") : null;
        String requestPerson = selectItem == null ? null : selectItem.get("value").toString();
        CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("from_request_person_fileds");
        List<String> resultFileds = Arrays.asList(cmdbConfig.getConfigValue().split(","));
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isEmpty(requestPerson)) {
            result.put("value", "已申请");

        } else {
            result.put("value", "未申请");
        }
        result.put("filedCode", resultFileds.get(0));
        Map<String, Object> styleMap = new HashMap<>();
        styleMap.put("disabled", true);
        List<Map<String, Object>> resultList = new ArrayList<>();
        result.put("style", styleMap);
        resultList.add(result);
        returnMap.put("flag", true);
        returnMap.put("msg", "查询成功");
        returnMap.put("resultData", resultList);
        return returnMap;
    }
}
