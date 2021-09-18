package com.aspire.ums.cmdb.resource.service.impl;

import com.aspire.ums.cmdb.resource.entity.ResourceBuildManageQueryEntity;
import com.aspire.ums.cmdb.resource.entity.ResourceDemandCollect;
import com.aspire.ums.cmdb.resource.entity.ResourceEstimateResponse;
import com.aspire.ums.cmdb.resource.mapper.ResourceDeamandCollectMapper;
import com.aspire.ums.cmdb.resource.service.ResourceDemandCollectService;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceDemandCollectServiceImpl implements ResourceDemandCollectService {
    private final Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ResourceDeamandCollectMapper resourceDeamandCollectMapper;

    @Override
    public Map<String, Object> selectResBuildMaData(ResourceBuildManageQueryEntity entity) {
        List<ResourceDemandCollect> selectData = resourceDeamandCollectMapper.getResourceCollectList(entity);
        Map<String, Object> response = Maps.newHashMap();
        if (CollectionUtils.isEmpty(selectData)) {
            logger.error("The ResBuildMaData is empty.");
            response.put("flag", false);
        } else {
            List<ResourceDemandCollect> selectDataCount = resourceDeamandCollectMapper.getResourceCollectCount(entity);
            response.put("flag", true);
            response.put("sum", selectDataCount.size());
            response.put("result", selectData);
        }
        return response;
    }

    @Override
    public void saveResourceCollectExcelData(List<ResourceDemandCollect> resourceDemandCollectList) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("list", resourceDemandCollectList);
        resourceDeamandCollectMapper.saveResourceCollectExcelData(params);
    }

    private static final String KEY_RECORDS_COUNT = "total";

    @Override
    public ResourceEstimateResponse getCollectAll(Map<String, Object> params) {
        Long total = 0L ;
        /*params.put("startIndex", startIndex);
        params.put("limit", limit);*/
        List<ResourceDemandCollect> list = new ArrayList<>();
        Map<String, Object> collectCount =
                resourceDeamandCollectMapper.getCollectAll_count(String.valueOf(params.get("estimateId")));
        if (collectCount != null) {
            total = (Long) collectCount.get(KEY_RECORDS_COUNT);
            list = resourceDeamandCollectMapper.getCollectAll(params);
        }
        ResourceEstimateResponse result = new ResourceEstimateResponse();
        result.setTotal(total);
        result.setRows(list);
        return result;
    }

    @Override
    public List<Map<String, Object>> getResourceDemandCollectList(ResourceBuildManageQueryEntity entity) {
        return resourceDeamandCollectMapper.getResourceCollectCountXls(entity);
    }
}
