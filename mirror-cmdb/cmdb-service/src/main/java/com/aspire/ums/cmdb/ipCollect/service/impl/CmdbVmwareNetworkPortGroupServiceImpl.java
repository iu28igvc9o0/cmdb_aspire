package com.aspire.ums.cmdb.ipCollect.service.impl;

import com.aspire.ums.cmdb.automate.exception.AutomateException;
import com.aspire.ums.cmdb.ipCollect.mapper.CmdbVmwareNetworkPortGroupMapper;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVmwareNetworkPortGroup;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.BaseVmwareDTO;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.EasyOpsDataDTO;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.EasyOpsResult;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.VmwareNetworkPortGroupDto;
import com.aspire.ums.cmdb.ipCollect.service.CmdbVmwareNetworkPortGroupService;
import com.aspire.ums.cmdb.ipCollect.utils.VmwareInstanceQueryHelper;
import com.aspire.ums.cmdb.ipCollect.utils.VmwareQuantityQueryHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fanwenhui
 * @date 2020-12-25 15:11
 * @description
 */
@Slf4j
@Service
public class CmdbVmwareNetworkPortGroupServiceImpl implements CmdbVmwareNetworkPortGroupService {

    private static final Integer PAGE_SIZE = 2000;

    private static final Integer OPS_QUERY_COUNT = 100;

    @Autowired
    private CmdbVmwareNetworkPortGroupMapper mapper;

    @Override
    public CmdbVmwareNetworkPortGroup findById(String id) {
        return mapper.findById(id);
    }

    @Override
    public CmdbVmwareNetworkPortGroup findByInstanceId(String instanceId) {
        return mapper.findByInstanceId(instanceId);
    }

    @Override
    public List<CmdbVmwareNetworkPortGroup> findByInstanceIdList(List<String> instanceIdList) {
        return mapper.findByInstanceIdList(instanceIdList);
    }

    @Override
    public void batchAdd(List<CmdbVmwareNetworkPortGroup> entityList) {
        mapper.batchInsert(entityList);
    }

    @Override
    public void add(CmdbVmwareNetworkPortGroup entity) {
        mapper.insert(entity);
    }

    @Override
    public void modify(CmdbVmwareNetworkPortGroup entity) {
        mapper.update(entity);
    }

    @Override
    public void updateByInstanceId(Map<String, Object> params) {
        mapper.updateByInstanceId(params);
    }

    @Override
    public void delete(String id) {
        mapper.delete(id);
    }

    @Override
    public void deleteByInstanceId(String instanceId) {
        mapper.deleteByInstanceId(instanceId);
    }

    @Override
    public List<String> getAllInstanceId() {
        return mapper.getAllInstanceId();
    }

    @Override
    public void batchDeleteByInstanceId(List<String> instanceIdList) {
        mapper.deleteByInstanceIdList(instanceIdList);
    }

    @Override
    public void buildAndCreateInstance(String eventId, String instanceId) {
        List<CmdbVmwareNetworkPortGroup> entityList = Lists.newArrayList();
        EasyOpsResult<VmwareNetworkPortGroupDto> result = VmwareInstanceQueryHelper.queryNetPortGroup(Collections.singletonList(instanceId));
        if (null == result) {
            log.info("instanceId:[{}]查询自动化接口,对应的网段-端口组实例不存在",instanceId);
            return;
        }
        List<VmwareNetworkPortGroupDto> dataList = result.getData().getDataList();
        if (dataList.isEmpty()) {
            return;
        }
        for (VmwareNetworkPortGroupDto dto : dataList) {
            CmdbVmwareNetworkPortGroup entity = new CmdbVmwareNetworkPortGroup();
            VmwareInstanceQueryHelper.fillNetworkPortGroup(eventId,dto,entity);
            entityList.add(entity);
        }
        if (!entityList.isEmpty()) {
            batchAdd(entityList);
        }
    }

    @Override
    public void synAllNetworkPortGroup() {
        int pageCount;
        try {
            List<String> opsInstanceIdList = Lists.newArrayList();
            List<String> addIdList = Lists.newArrayList();
            List<String> editIdList = Lists.newArrayList();
            List<String> delIdList = Lists.newArrayList();

            // 查询本地所有InstanceId
            List<String> localIdList = mapper.getAllInstanceId();
            log.info("网管已存在的NetworkPortGroup数量:[{}]", localIdList.size());
            // 1、获取数据总数
            EasyOpsResult<VmwareNetworkPortGroupDto> result = VmwareQuantityQueryHelper.queryNetworkPortGroup(null,1, 1, true);
            int totalCount = result.getData().getTotalCount();
            // 2、计算总页数
            pageCount = (totalCount / PAGE_SIZE) + 1;
            log.info("自动化平台查询到的[Template]数量:[{}]", totalCount);
            // 3、获取对端所有 InstanceId
            for (int page = 1; page <= pageCount; page++) {
                EasyOpsResult<VmwareNetworkPortGroupDto> networkPortGroup = VmwareQuantityQueryHelper.queryNetworkPortGroup(null,page, PAGE_SIZE, true);
                EasyOpsDataDTO<VmwareNetworkPortGroupDto> dto = networkPortGroup.getData();
                List<VmwareNetworkPortGroupDto> dataList = dto.getDataList();
                if (CollectionUtils.isNotEmpty(dataList)) {
                    List<String> collect = dataList.stream().map(BaseVmwareDTO::getInstanceId).collect(Collectors.toList());
                    opsInstanceIdList.addAll(collect);
                }
            }
            // 要删除的ID列表,网管有的，自动化没有的，就是删除。
            List<String> reduceIdList = localIdList.stream().filter(item -> !opsInstanceIdList.contains(item))
                    .collect(Collectors.toList());
            delIdList.addAll(reduceIdList);
            // 新增的
            List<String> newIdList = opsInstanceIdList.stream().filter(item -> !localIdList.contains(item))
                    .collect(Collectors.toList());
            addIdList.addAll(newIdList);
            // 修改的 交集
            List<String> updateIdList = opsInstanceIdList.stream().filter(localIdList::contains)
                    .collect(Collectors.toList());
            editIdList.addAll(updateIdList);
            log.info("[NetworkPortGroup]待删除数量:[{}],待新增数量:[{}],待更新数量:[{}]", reduceIdList.size(), newIdList.size(), updateIdList.size());

            // 删除instance实例
            if (CollectionUtils.isNotEmpty(delIdList)) {
                mapper.deleteByInstanceIdList(delIdList);
                log.info("[NetworkPortGroup]删除成功数量:[{}]", delIdList.size());
            }

            // 新增instance实例
            saveOrUpdateNetworkPortGroup(addIdList,false);
            // 修改instance实例
            saveOrUpdateNetworkPortGroup(editIdList,true);

        } catch (Exception e) {
            log.error("[NetworkPortGroup]全量同步失败", e);
            throw new AutomateException(e);
        }
    }

    /**
     * 新增或者修改网段-端口组
     * @param idList 实例ID列表
     * @param isUpdate 是否更新，true-更新，false-新增
     */
    private void saveOrUpdateNetworkPortGroup(List<String> idList, boolean isUpdate) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        List<CmdbVmwareNetworkPortGroup> networkPortGroupList = Lists.newArrayList();
        List<List<String>> partitionList = Lists.partition(idList, OPS_QUERY_COUNT);
        for (List<String> subList : partitionList) {
            EasyOpsResult<VmwareNetworkPortGroupDto> easyOpsResult = VmwareInstanceQueryHelper.queryNetPortGroup(subList, 1, OPS_QUERY_COUNT);
            EasyOpsDataDTO<VmwareNetworkPortGroupDto> dto = easyOpsResult.getData();
            if (null == dto) {
                continue;
            }
            List<VmwareNetworkPortGroupDto> dataList = dto.getDataList();
            if (CollectionUtils.isEmpty(dataList)) {
                continue;
            }
            List<String> instanceIdList = dataList.stream().map(BaseVmwareDTO::getInstanceId).collect(Collectors.toList());
            List<CmdbVmwareNetworkPortGroup> entityList = Lists.newArrayList();
            if (isUpdate) {
                entityList = mapper.findByInstanceIdList(instanceIdList);
            }
            List<CmdbVmwareNetworkPortGroup> updateList = VmwareInstanceQueryHelper.getFillNetworkPortGroup(isUpdate, dataList, entityList);
            if (CollectionUtils.isNotEmpty(updateList)) {
                networkPortGroupList.addAll(updateList);
            }
        }

        if (isUpdate) {
            networkPortGroupList.forEach(e -> mapper.update(e));
            log.info("[NetworkPortGroup]更新成功数量:[{}]", networkPortGroupList.size());
        } else {
            mapper.batchInsert(networkPortGroupList);
            log.info("[NetworkPortGroup]新增成功数量:[{}]", networkPortGroupList.size());
        }
    }
}
