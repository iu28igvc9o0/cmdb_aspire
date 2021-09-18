package com.aspire.ums.cmdb.openstack.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.aspire.ums.cmdb.automate.exception.AutomateException;
import com.aspire.ums.cmdb.ipCollect.utils.VmwareInstanceQueryHelper;
import com.aspire.ums.cmdb.ipCollect.utils.VmwareQuantityQueryHelper;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackDataDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackImageDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackResult;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackSubnetDTO;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackImage;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackSubnet;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackAllocationPoolService;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackImageService;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackSubnetService;
import com.aspire.ums.cmdb.openstack.service.OpenstackFullImageSyncService;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * 全量同步openstack
 *
 * @author jiangxuwen
 * @date 2020/11/24 17:51
 */
@Service
@Slf4j
public class OpenstackFullImageSyncServiceImpl implements OpenstackFullImageSyncService {

    private static final Integer PAGE_SIZE = 2000;

    private static final Integer OPS_QUERY_COUNT = 100;

    @Autowired
    private ICmdbOpenstackSubnetService subnetService;

    @Autowired
    private ICmdbOpenstackImageService imageService;

    @Autowired
    private ICmdbOpenstackAllocationPoolService allocationPoolService;

    @Override
    public void syncOpenstackSubnet() {
        log.info(">>>>> 开始同步[OpenstackSubnet] >>>>");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int pageCount = 0;
        try {
            List<String> opsInstanceIdList = Lists.newArrayList();
            List<String> addIdList = Lists.newArrayList();
            List<String> editIdList = Lists.newArrayList();
            List<String> delIdList = Lists.newArrayList();

            // 查询本地所有InstanceId
            List<String> localIdList = subnetService.getAllInstanceId();
            log.info("网管已存在的[OpenstackSubnet]数量:[{}]", localIdList.size());

            // 1、获取数据总数
            OpenStackResult<OpenStackSubnetDTO> result = VmwareQuantityQueryHelper.queryOpenstackSubnet(null, 1, 1, true);
            int totalCount = result.getData().getTotalCount();
            // 2、计算总页数
            pageCount = (totalCount / PAGE_SIZE) + 1;
            log.info("自动化平台的[OpenstackSubnet]数量:[{}]", totalCount);

            for (int page = 1; page <= pageCount; page++) {
                OpenStackResult<OpenStackSubnetDTO> opsResult = VmwareQuantityQueryHelper.queryOpenstackSubnet(null, page,
                        PAGE_SIZE, true);
                OpenStackDataDTO<OpenStackSubnetDTO> dto = opsResult.getData();
                List<OpenStackSubnetDTO> dataList2 = dto.getDataList();
                if (CollectionUtils.isNotEmpty(dataList2)) {
                    List<String> opsInstanceIdList2 = dataList2.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
                    opsInstanceIdList.addAll(opsInstanceIdList2);
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
            List<String> updateIdList = opsInstanceIdList.stream().filter(item -> localIdList.contains(item))
                    .collect(Collectors.toList());
            editIdList.addAll(updateIdList);
            log.info("[OpenstackSubnet]待删除数量:[{}],待新增数量:[{}],待更新数量:[{}]", reduceIdList.size(), newIdList.size(),
                    updateIdList.size());

            // 删除instance.
            if (CollectionUtils.isNotEmpty(delIdList)) {
                subnetService.deleteByInstanceIdList(delIdList);
                log.info("删除成功数量:[{}]", delIdList.size());
            }
            // 新增的
            saveOrUpdateSubnet(addIdList, false);
            // 修改的
            saveOrUpdateSubnet(editIdList, true);

        } catch (Exception e) {
            log.error("数据中心数据更新失败.", e);
            throw new AutomateException(e);
        }
        stopWatch.stop();
        log.info("<<<<<< 同步[OpenstackSubnet] 完成,耗时:[{}]ms <<<<<<", stopWatch.getTotalTimeMillis());
    }

    @Override
    public void syncOpenstackImage() {
        log.info(">>>>> 开始同步[OpenstackImage] >>>>");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int pageCount = 0;
        try {
            List<String> opsInstanceIdList = Lists.newArrayList();
            List<String> addIdList = Lists.newArrayList();
            List<String> editIdList = Lists.newArrayList();
            List<String> delIdList = Lists.newArrayList();

            // 查询本地所有InstanceId
            List<String> localIdList = imageService.getAllInstanceId();
            log.info("网管已存在的[OpenstackImage]数量:[{}]", localIdList.size());

            // 1、获取数据总数
            OpenStackResult<OpenStackImageDTO> result = VmwareQuantityQueryHelper.queryOpenstackImage(null, 1, 1, true);
            int totalCount = result.getData().getTotalCount();
            // 2、计算总页数
            pageCount = (totalCount / PAGE_SIZE) + 1;
            log.info("自动化平台的[OpenstackImage]数量:[{}]", totalCount);

            for (int page = 1; page <= pageCount; page++) {
                OpenStackResult<OpenStackImageDTO> opsResult = VmwareQuantityQueryHelper.queryOpenstackImage(null, page, PAGE_SIZE,
                        true);
                OpenStackDataDTO<OpenStackImageDTO> dto = opsResult.getData();
                List<OpenStackImageDTO> dataList2 = dto.getDataList();
                if (CollectionUtils.isNotEmpty(dataList2)) {
                    List<String> opsInstanceIdList2 = dataList2.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
                    opsInstanceIdList.addAll(opsInstanceIdList2);
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
            List<String> updateIdList = opsInstanceIdList.stream().filter(item -> localIdList.contains(item))
                    .collect(Collectors.toList());
            editIdList.addAll(updateIdList);
            log.info("[OpenstackImage]待删除数量:[{}],待新增数量:[{}],待更新数量:[{}]", reduceIdList.size(), newIdList.size(),
                    updateIdList.size());

            // 删除instance.
            if (CollectionUtils.isNotEmpty(delIdList)) {
                imageService.deleteByInstanceIdList(delIdList);
                log.info("删除成功数量:[{}]", delIdList.size());
            }
            // 新增的
            saveOrUpdateImage(addIdList, false);
            // 修改的
            saveOrUpdateImage(editIdList, true);

        } catch (Exception e) {
            log.error("OpenstackImage更新失败.", e);
            throw new AutomateException(e);
        }
        stopWatch.stop();
        log.info("<<<<<< 同步[OpenstackImage] 完成,耗时:[{}]ms <<<<<<", stopWatch.getTotalTimeMillis());
    }

    /**
     * 保存和更新Subnet逻辑.
     *
     * @param idList
     *            instanceId列表
     * @param isUpdate
     *            是否是更新.
     * @return
     */
    private void saveOrUpdateSubnet(List<String> idList, boolean isUpdate) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        List<CmdbOpenstackSubnet> subnetList = Lists.newArrayList();
        List<String> subnetInstanceIdList = Lists.newArrayList();
        List<List<String>> partitionList = Lists.partition(idList, OPS_QUERY_COUNT);
        for (List<String> subList : partitionList) {
            OpenStackResult<OpenStackSubnetDTO> easyOpsResult = VmwareQuantityQueryHelper.queryOpenstackSubnet(subList, 1,
                    OPS_QUERY_COUNT, false);
            OpenStackDataDTO<OpenStackSubnetDTO> dto = easyOpsResult.getData();
            List<OpenStackSubnetDTO> dataList = dto.getDataList();
            if (CollectionUtils.isEmpty(dataList)) {
                continue;
            }

            List<String> instanceIdList = dataList.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
            List<CmdbOpenstackSubnet> entityList = Lists.newArrayList();
            if (isUpdate) {
                entityList = subnetService.findByInstanceIdList(instanceIdList);
            }

            List<CmdbOpenstackSubnet> updateList = VmwareInstanceQueryHelper.getFillOpenStackSubnet(isUpdate, dataList, entityList);
            if (CollectionUtils.isNotEmpty(updateList)) {
                subnetList.addAll(updateList);
            }

            dataList.forEach(e -> {
                subnetInstanceIdList.add(e.getInstanceId());
            });
        }

        if (CollectionUtils.isNotEmpty(subnetList)) {
            if (isUpdate) {
                subnetList.forEach(e -> subnetService.update(e));
                log.info("修改[OpenstackSubnet]成功数量:[{}]", subnetList.size());
            } else {
                subnetService.batchInsert(subnetList);
                log.info("新增[OpenstackSubnet]成功数量:[{}]", subnetList.size());
            }
        }
    }

    /**
     * 保存和更新image逻辑.
     *
     * @param idList
     *            instanceId列表
     * @param isUpdate
     *            是否是更新.
     * @return
     */
    private void saveOrUpdateImage(List<String> idList, boolean isUpdate) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        List<CmdbOpenstackImage> imageList = Lists.newArrayList();
        List<String> imageInstanceIdList = Lists.newArrayList();
        List<List<String>> partitionList = Lists.partition(idList, OPS_QUERY_COUNT);
        for (List<String> subList : partitionList) {
            OpenStackResult<OpenStackImageDTO> easyOpsResult = VmwareQuantityQueryHelper.queryOpenstackImage(subList, 1,
                    OPS_QUERY_COUNT, false);
            OpenStackDataDTO<OpenStackImageDTO> dto = easyOpsResult.getData();
            List<OpenStackImageDTO> dataList = dto.getDataList();
            if (CollectionUtils.isEmpty(dataList)) {
                continue;
            }

            List<String> instanceIdList = dataList.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
            List<CmdbOpenstackImage> entityList = Lists.newArrayList();
            if (isUpdate) {
                entityList = imageService.findByInstanceIdList(instanceIdList);
            }

            List<CmdbOpenstackImage> updateList = VmwareInstanceQueryHelper.getFillOpenStackImage(isUpdate, dataList, entityList);
            if (CollectionUtils.isNotEmpty(updateList)) {
                imageList.addAll(updateList);
            }

            dataList.forEach(e -> {
                imageInstanceIdList.add(e.getInstanceId());
            });
        }

        if (CollectionUtils.isNotEmpty(imageList)) {
            if (isUpdate) {
                imageList.forEach(e -> imageService.update(e));
                log.info("修改[OpenstackImage]成功数量:[{}]", imageList.size());
            } else {
                imageService.batchInsert(imageList);
                log.info("新增[OpenstackImage]成功数量:[{}]", imageList.size());
            }
        }
    }
}
