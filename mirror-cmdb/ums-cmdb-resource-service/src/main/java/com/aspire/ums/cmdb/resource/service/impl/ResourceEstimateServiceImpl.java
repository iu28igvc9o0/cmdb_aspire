package com.aspire.ums.cmdb.resource.service.impl;

import com.aspire.ums.cmdb.resource.entity.ResourceDemandCollect;
import com.aspire.ums.cmdb.resource.entity.ResourceEstimate;
import com.aspire.ums.cmdb.resource.entity.ResourceEstimateResponse;
import com.aspire.ums.cmdb.resource.mapper.ResourceDeamandCollectMapper;
import com.aspire.ums.cmdb.resource.mapper.ResourceEstimateMapper;
import com.aspire.ums.cmdb.resource.service.ResourceEstimateService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ResourceEstimateServiceImpl implements ResourceEstimateService {

    private final Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ResourceEstimateMapper resourceEstimateMapper;
    @Autowired
    private ResourceDeamandCollectMapper resourceDeamandCollectMapper;

    @Override
    public List<String> getResourcePoolAll_config() {
        return resourceEstimateMapper.getResourcePoolAll_config();
    }

    private static final String KEY_RESOURCE_POOL = "resourcePool";
    private static final String KEY_STATUS = "status";
    private static final String KEY_QUERY_START_INDEX = "startIndex";
    private static final String KEY_QUERY_LIMIT = "limit";
    private static final String KEY_RECORDS_COUNT = "total";

    private List<String> getCollectIdsList(String collectIds) {
        List<String> collectIdList = new ArrayList<>();
        if (collectIds.indexOf(",") != -1) {
            for (String cname : collectIds.split(",")) {
                collectIdList.add(cname);
            }
        } else {
            collectIdList.add(collectIds);
        }
        return collectIdList;
    }

    @Override
    public ResourceEstimateResponse queryDataGrid(Map<String, Object> params, Integer startIndex, Integer limit) {
        Long total = 0L ;
        List<ResourceEstimate> list = new ArrayList<>();
        params.put(KEY_QUERY_START_INDEX, startIndex);
        params.put(KEY_QUERY_LIMIT, limit);
        Map<String, Object> estimateCount = resourceEstimateMapper.getResourceEstimate_All_count(params);
        if (estimateCount != null) {
            total = (Long) estimateCount.get(KEY_RECORDS_COUNT);
            list = resourceEstimateMapper.getResourceEstimate_All(params);
        }
        ResourceEstimateResponse result = new ResourceEstimateResponse();
        result.setTotal(total);
        result.setRows(list);
        return result;
    }

    @Override
    public ResourceEstimateResponse queryCollectDataGrid(Map<String, Object> params,
                                                         Integer startIndex, Integer limit) {
        Long total = 0L ;
        List<ResourceDemandCollect> list = new ArrayList<>();
        params.put(KEY_QUERY_START_INDEX, startIndex);
        params.put(KEY_QUERY_LIMIT, limit);
        Map<String, Object> collectCount = resourceDeamandCollectMapper.getCollectList_count(params);
        if (collectCount != null) {
            total = (Long) collectCount.get(KEY_RECORDS_COUNT);
            list = resourceDeamandCollectMapper.getCollectList(params);
        }
        ResourceEstimateResponse result = new ResourceEstimateResponse();
        result.setTotal(total);
        result.setRows(list);
        return result;
    }

    @Override
    public ResourceEstimate getCollectByCollectIds(String collectIds) throws Exception {
        ResourceEstimate estimate = new ResourceEstimate();
        List<String> collectIdList = getCollectIdsList(collectIds);
        if (CollectionUtils.isEmpty(collectIdList)) {
            logger.info("根据ID查询资源收集表中数据-参数为空");
            return estimate;
        }
        estimate = resourceEstimateMapper.getCollectByCollectIds(collectIdList);
        if (estimate == null) {
            throw new Exception("根据ID查询资源收集表中数据出现错误");
        }
        return estimate;
    }

    @Override
    public void updateEstimate(ResourceEstimate estimate) throws Exception {
        if(null == estimate ){
            logger.info("[资源建设管理-资源预估] 更新预估参数为空");
            return;
        }
        try {
            resourceEstimateMapper.updateEstimate(estimate);
        } catch (Exception e) {
            logger.error("更新预估异常",e);
            throw new Exception("更新预估异常",e);
        }

        //更新资源需求收集表estimate_id
        try {
            if (StringUtils.isNotEmpty(estimate.getCollectId())) {
                List<String> collectIdList = getCollectIdsList(estimate.getCollectId());
				/*//清空已关联的
				resourceEstimateDao.cleanCollectEstimateId(estimate.getId());*/

                //关联预估
                resourceEstimateMapper.updateResourceDeamandCollectEstimateId(estimate.getId(),collectIdList);
            }
        } catch (Exception e) {
            logger.error("更新资源需求收集表estimate_id异常",e);
            throw new Exception("更新资源需求收集表estimate_id异常",e);
        }
    }

    @Override
    public void addEstimate(ResourceEstimate estimate) throws Exception {
        if(null == estimate || StringUtils.isEmpty(estimate.getCollectId())){
            logger.info("[资源建设管理-资源预估] 新增预估参数为空");
            return;
        }
        //新增到预估表
        try {
            resourceEstimateMapper.addEstimate(estimate);
        } catch (Exception e) {
            logger.error("新增预估异常",e);
            throw new Exception("新增预估异常",e);
        }

        //更新资源需求收集表estimate_id
        try {
            List<String> collectIdList = getCollectIdsList(estimate.getCollectId());
            resourceEstimateMapper.updateResourceDeamandCollectEstimateId(estimate.getId(), collectIdList);
        } catch (Exception e) {
            logger.error("更新资源需求收集表estimate_id异常",e);
            throw new Exception("更新资源需求收集表estimate_id异常",e);
        }
    }

    @Override
    public int isClosedByPoolName(String resourcePool) {
        int r = -1;
        try {
            r = resourceEstimateMapper.isClosedByPoolName(resourcePool);
        } catch (Exception e){
            logger.error("查询[资源池："+resourcePool+"]是否关闭异常", e);
        }
        return r;
    }

    /**
     * 关闭资源预估
     * @param id 资源预估ID
     * @throws Exception
     */
    public void closeEstimate(String id, String user) throws Exception{
        if (StringUtils.isEmpty(id))
            throw new Exception("关闭资源预估ID参数为空");
        try {
            //获取系统当前用户
//            User user = UserUtils.getUser();
            resourceEstimateMapper.closeEstimate(id, user);
        } catch (Exception e) {
            logger.error("关闭资源预估[id:" + id + "]异常",e);
            throw new Exception("关闭资源预估[id:" + id + "]异常",e);
        }
    }
}
