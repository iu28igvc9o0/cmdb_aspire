package com.aspire.ums.cmdb.v3.module.event.code;

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
public class PublicNetworkSegmentOwnerEvent extends AbstractModuleEvent {
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
        //获取选择网段id参数
        Map<String, Object> selectItem = handleData.containsKey("selectItem") ? (Map<String, Object>)handleData.get("selectItem") : null;
        String segmentId = selectItem == null ? null : selectItem.get("id").toString();        //获取内网网段模型id
        CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("public_segment_module_id");
        String segmentModuleId = cmdbConfig.getConfigValue();
        //获取需要从网段中获取的字段
        cmdbConfig = cmdbConfigService.getConfigByCode("from_public_segment_fileds");
        List<String> resultFileds = Arrays.asList(cmdbConfig.getConfigValue().split(","));
        //获取需要不需要设置禁用的字段
        cmdbConfig = cmdbConfigService.getConfigByCode("from_public_segment_fileds_undisabled");
        List<String> unDisabledFields = Arrays.asList(cmdbConfig.getConfigValue().split(","));
        Map<String, Object> instanceData = StringUtils.isEmpty(segmentId) ? new HashMap<>() : instanceService.getInstanceDetail(segmentModuleId, segmentId);
        List<Map<String, Object>> resultList = new ArrayList<>();
        //组装数据
        for (String filed :  resultFileds) {
            Map<String, Object> result = new HashMap<>();
            result.put("filedCode", filed);
            result.put("value", instanceData.get(filed));
            Map<String, Object> styleMap = new HashMap<>();
            if (!unDisabledFields.contains(filed)) {
                styleMap.put("disabled", true);
            } else {
                styleMap.put("disabled", false);
            }
            result.put("style", styleMap);
            resultList.add(result);
        }
        returnMap.put("flag", true);
        returnMap.put("msg", "查询成功");
        returnMap.put("resultData", resultList);
        return returnMap;
    }
}
