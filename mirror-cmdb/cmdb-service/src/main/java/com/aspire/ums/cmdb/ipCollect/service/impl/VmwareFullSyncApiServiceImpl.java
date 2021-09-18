package com.aspire.ums.cmdb.ipCollect.service.impl;

import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.ipCollect.enums.EventInstanceModuleEnum;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpAddressPoolEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpArpPoolEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpConfPoolEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVipCollectEntity;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.*;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpClashService;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpCollectService;
import com.aspire.ums.cmdb.ipCollect.service.CmdbVipCollectService;
import com.aspire.ums.cmdb.ipCollect.service.VmwareFullSyncApiService;
import com.aspire.ums.cmdb.ipCollect.utils.VmwareInstanceQueryHelper;
import com.aspire.ums.cmdb.ipCollect.utils.VmwareQuantityQueryHelper;
import com.aspire.ums.cmdb.util.DateUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/21 11:15
 */
@Slf4j
@Service("VmwareFullSyncApiService")
@Transactional(rollbackFor = Exception.class)
public class VmwareFullSyncApiServiceImpl implements VmwareFullSyncApiService {

    // 插入最大处理数
    private static final Integer SAVE_SIZE = 500;

    //最大行数
    private static final Integer PAGE_SIZE = 2000;

    private static final Integer OPS_QUERY_COUNT = 1000;
    @Autowired
    private CmdbIpCollectService collectService;

    @Autowired
    private CmdbVipCollectService vipCollectService;

    @Override
    public void syncIpAddressPool() {
        log.info(">>>>> 开始同步[IpAddressPool] >>>>");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int pageCount = 0;
        try {
            // 1、获取数据总数
            long startCount = System.currentTimeMillis();
            int totalCount = VmwareQuantityQueryHelper.getInstanceCount(EventInstanceModuleEnum.IP_ADDRESS_POOL.getKey());
            log.info("[IpAddressPool]模型总量查询耗时：{}",System.currentTimeMillis() - startCount);
            if (totalCount == 0) {
                return;
            }
            // 2、计算总页数
            pageCount = (totalCount / PAGE_SIZE) + 1;
            log.info("自动化平台的IpAddressPool数量:[{}]", totalCount);

            // 3、删除当天的存活IP
            String nowDay = DateUtils.datetimeToString(DateUtils.DEFAULT_DATE_FMT, new Date());
            collectService.deleteCmdbIpAddressNowDay(nowDay);

            // 4、开始封装需要入库CMDB的数据
            log.info(">>>>> [IpAddressPool]begin-同步网关采集IP数据 >>>>");
            List<CmdbIpAddressPoolEntity> ipCollectEntityList = Lists.newArrayList();
            long start1 = System.currentTimeMillis();
            for (int page = 1; page <= pageCount; page++) {
                long start = System.currentTimeMillis();
                EasyOpsResult<CmdbIpAddressPoolDTO> opsResult = VmwareQuantityQueryHelper.queryIpAddressPool(null, page, PAGE_SIZE, false);
                long end = System.currentTimeMillis();
                log.info("[IpAddressPool]模型第[{}]页接口查询耗时:{} ms",page,end - start);
                EasyOpsDataDTO<CmdbIpAddressPoolDTO> dto = opsResult.getData();
                List<CmdbIpAddressPoolDTO> dataList = dto.getDataList();
                if (CollectionUtils.isNotEmpty(dataList)) {
                    List<CmdbIpAddressPoolEntity> entityList = Lists.newArrayList();
                    List<CmdbIpAddressPoolEntity> updateList = VmwareInstanceQueryHelper.getFillIpAddressPool(false, dataList, entityList,"N");
                    if (CollectionUtils.isNotEmpty(updateList)) {
                        // 获取检测时间是当天的数据，不是今天检测的不录入数据库
                        updateList.forEach(map -> {
                            Date time = map.getTime();
                            boolean toDay = DateUtils.isToDay(time);
                            if (toDay) {
                                ipCollectEntityList.add(map);
                            }
                        });
                    }
                }
                log.info("[IpAddressPool]模型第[{}]页CMDB处理耗时:{} ms",page,System.currentTimeMillis() - end);
            }
            log.info("[IpAddressPool]模型入库数据封装总耗时:{} s,数据总量：{}",(System.currentTimeMillis() - start1) / 1000,ipCollectEntityList.size());

            // 5、进行cmdb入库操作
            long start2 = System.currentTimeMillis();
            if (CollectionUtils.isNotEmpty(ipCollectEntityList)) {
                List<CmdbIpAddressPoolEntity> addList = new ArrayList<>();
                for (int i = 0; i < ipCollectEntityList.size(); i++) {
                    addList.add(ipCollectEntityList.get(i));
                    if (i % OPS_QUERY_COUNT == 0) {
                        ipAddressPoolSave(false, addList);
                        addList.clear();
                    }
                }
                if (CollectionUtils.isNotEmpty(addList)) {
                    ipAddressPoolSave(false, addList);
                }
            }
            log.info("[IpAddressPool]模型入库CMDB耗时:{} s",(System.currentTimeMillis() - start2) / 1000);
            log.info(">>>>> [IpAddressPool]end-同步网关采集IP数据结束 >>>>");

            // 6、将当天的存活IP数据录入分区，上一天数据也入库分区
            log.info(">>>>> [ipAddressPool] begin 录入当天的历史分区表数据 >>>>");
            collectService.alterCmdbIpPart(EventInstanceModuleEnum.IP_ADDRESS_POOL.getKey());
            log.info(">>>>> [ipAddressPool] end 录入当天的历史分区表数据 >>>>");
        } catch (Exception e) {
            log.error("存活IP数据更新失败.", e);
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("<<<<<< 同步[IpAddressPool] 完成,耗时:[{}]秒 <<<<<<", stopWatch.getTotalTimeSeconds());
    }

    public void syncIpAddressPool_bak() {
        log.info(">>>>> 开始同步[IpAddressPool] >>>>");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int pageCount = 0;
        try {
            // 1、删除当天的存活IP
            String nowDay = DateUtils.datetimeToString(DateUtils.DEFAULT_DATE_FMT, new Date());
            collectService.deleteCmdbIpAddressNowDay(nowDay);

            List<String> opsInstanceIdList = Lists.newArrayList();
            List<String> addIdList = Lists.newArrayList();
            List<String> editIdList = Lists.newArrayList();
            List<String> delIdList = Lists.newArrayList();

            // 查询本地所有InstanceId
            List<String> localIdList = collectService.getAllInstanceId(CmdbIpAddressPoolEntity.class);
            log.info("已存在的IpAddressPool数量:[{}]", localIdList.size());

            // 1、获取数据总数
            long startCount = System.currentTimeMillis();
            int totalCount = VmwareQuantityQueryHelper.getInstanceCount(EventInstanceModuleEnum.IP_ADDRESS_POOL.getKey());
            log.info("[IpAddressPool]模型总量查询耗时：{}",System.currentTimeMillis() - startCount);
            if (totalCount == 0) {
                return;
            }
            // 2、计算总页数
            pageCount = (totalCount / PAGE_SIZE) + 1;
            log.info("自动化平台的IpAddressPool数量:[{}]", totalCount);

            // 3、获取对端所有 InstanceId
            log.info(">>>>> [IpAddressPool]begin-获取对端的所有instanceId >>>>");
            for (int page = 1; page <= pageCount; page++) {
                long start = System.currentTimeMillis();
                EasyOpsResult<CmdbIpAddressPoolDTO> opsResult = VmwareQuantityQueryHelper.queryIpAddressPool(null, page, PAGE_SIZE, true);
                log.info("[IpAddressPool]模型第[{}]页查询instanceId耗时:{} ms",page,System.currentTimeMillis() - start);
                EasyOpsDataDTO<CmdbIpAddressPoolDTO> dto = opsResult.getData();
                List<CmdbIpAddressPoolDTO> dataList2 = dto.getDataList();
                if (CollectionUtils.isNotEmpty(dataList2)) {
                    List<String> opsInstanceIdList2 = dataList2.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
                    opsInstanceIdList.addAll(opsInstanceIdList2);
                }
            }
            log.info(">>>>> [IpAddressPool]end-获取对端的所有instanceId >>>>");

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
            log.info("ipAddressPool待删除数量:[{}],待新增数量:[{}],待更新数量:[{}]", reduceIdList.size(), newIdList.size(), updateIdList.size());

            // 删除instance.
            if (CollectionUtils.isNotEmpty(delIdList)) {
                // addressPoolService.batchDeleteByInstanceId(delIdList);
                collectService.batchDeleteByInstanceId(delIdList, CmdbIpAddressPoolEntity.class);
                log.info("删除成功数量:[{}]", delIdList.size());
            }
            // 新增的
            saveOrUpdateIpAddressPool(addIdList, false);
            // 修改的
            saveOrUpdateIpAddressPool(editIdList, true);

            // 将当天的存活IP数据录入分区
            log.info(">>>>> [ipAddressPool] begin 录入当天的历史分区表数据 >>>>");
            collectService.alterCmdbIpPart(EventInstanceModuleEnum.IP_ADDRESS_POOL.getKey());
            log.info(">>>>> [ipAddressPool] end 录入当天的历史分区表数据 >>>>");
        } catch (Exception e) {
            log.error("存活IP数据更新失败.", e);
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("<<<<<< 同步[IpAddressPool] 完成,耗时:[{}]秒 <<<<<<", stopWatch.getTotalTimeSeconds());
    }

    @Override
    public void syncIpConfPool() {
        log.info(">>>>> 开始同步[IpConfPool] >>>>");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int pageCount = 0;
        try {
            long startCount = System.currentTimeMillis();
            int totalCount = VmwareQuantityQueryHelper.getInstanceCount(EventInstanceModuleEnum.IP_CONF_POOL.getKey());
            log.info("[IpConfPool]模型总量查询耗时：{}",System.currentTimeMillis() - startCount);
            if (totalCount == 0) {
                return;
            }
            // 2、计算总页数
            pageCount = (totalCount / PAGE_SIZE) + 1;
            log.info("自动化平台的IpConfPool数量:[{}]", totalCount);

            // 3、删除当天的存活IP
            String nowDay = DateUtils.datetimeToString(DateUtils.DEFAULT_DATE_FMT, new Date());
            collectService.deleteCmdbIpConfNowDay(nowDay);

            // 4、开始封装需要入库CMDB的数据
            log.info(">>>>> [IpConfPool]begin-获取对端的所有instanceId >>>>");
            List<CmdbIpConfPoolEntity> ipCollectEntityList = Lists.newArrayList();
            long start1 = System.currentTimeMillis();
            for (int page = 1; page <= pageCount; page++) {
                long start = System.currentTimeMillis();
                EasyOpsResult<CmdbIpConfPoolDTO> opsResult = VmwareQuantityQueryHelper.queryIpConfPool(null, page, PAGE_SIZE, false);
                long end = System.currentTimeMillis();
                log.info("[IpConfPool]模型第[{}]页接口查询耗时:{} ms",page,end - start);
                EasyOpsDataDTO<CmdbIpConfPoolDTO> dto = opsResult.getData();
                List<CmdbIpConfPoolDTO> dataList = dto.getDataList();
                if (CollectionUtils.isNotEmpty(dataList)) {
                    List<CmdbIpConfPoolEntity> entityList = Lists.newArrayList();
                    List<CmdbIpConfPoolEntity> updateList = VmwareInstanceQueryHelper.getFillIpConfPool(false, dataList, entityList);
                    if (CollectionUtils.isNotEmpty(updateList)) {
                        // 获取检测时间是当天的数据，不是今天检测的不录入数据库
                        updateList.forEach(map -> {
                            Date time = map.getTime();
                            boolean toDay = DateUtils.isToDay(time);
                            if (toDay) {
                                ipCollectEntityList.add(map);
                            }
                        });
                    }
                    log.info("[IpConfPool]模型第[{}]页CMDB处理耗时:{} ms",page,System.currentTimeMillis() - end);
                }
            }
            log.info("[IpConfPool]模型入库数据封装总耗时:{} s,数据总量：{}",(System.currentTimeMillis() - start1) / 1000,ipCollectEntityList.size());

            // 5、进行cmdb入库操作
            long start2 = System.currentTimeMillis();
            if (CollectionUtils.isNotEmpty(ipCollectEntityList)) {
                List<CmdbIpConfPoolEntity> addList = new ArrayList<>();
                for (int i = 0; i < ipCollectEntityList.size(); i++) {
                    addList.add(ipCollectEntityList.get(i));
                    if (i % OPS_QUERY_COUNT == 0) {
                        ipConfPoolSave(false, addList);
                        addList.clear();
                    }
                }
                if (CollectionUtils.isNotEmpty(addList)) {
                    ipConfPoolSave(false, addList);
                }
            }
            log.info("[IpConfPool]模型入库CMDB耗时:{} s",(System.currentTimeMillis() - start2) / 1000);
            log.info(">>>>> [IpConfPool]end-同步网关采集IP数据结束 >>>>");

            // 6.将当天的存活IP数据录入分区
            log.info(">>>>> [IpConfPool] begin 录入当天的历史分区表数据 >>>>");
            collectService.alterCmdbIpPart(EventInstanceModuleEnum.IP_CONF_POOL.getKey());
            log.info(">>>>> [IpConfPool] end 录入当天的历史分区表数据 >>>>");

        } catch (Exception e) {
            log.error("存活IP数据更新失败.", e);
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("<<<<<< 同步[IpConfPool] 完成,耗时:[{}]秒 <<<<<<", stopWatch.getTotalTimeSeconds());
    }

    public void syncIpConfPool_bak() {
        log.info(">>>>> 开始同步[IpConfPool] >>>>");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int pageCount = 0;
        try {
            List<String> opsInstanceIdList = Lists.newArrayList();
            List<String> addIdList = Lists.newArrayList();
            List<String> editIdList = Lists.newArrayList();
            List<String> delIdList = Lists.newArrayList();

            // 查询本地所有InstanceId
            List<String> localIdList = collectService.getAllInstanceId(CmdbIpConfPoolEntity.class);
            log.info("已存在的IpConfPool数量:[{}]", localIdList.size());

            // 1、获取数据总数
            EasyOpsResult<CmdbIpConfPoolDTO> result = VmwareQuantityQueryHelper.queryIpConfPool(null, 1, 1, true);
            int totalCount = result.getData().getTotalCount();
            // 2、计算总页数
            pageCount = (totalCount / PAGE_SIZE) + 1;
            log.info("自动化平台的IpConfPool数量:[{}]", totalCount);

            // 3、获取对端所有 InstanceId
            log.info(">>>>> [IpConfPool]begin-获取对端的所有instanceId >>>>");
            for (int page = 1; page <= pageCount; page++) {
                EasyOpsResult<CmdbIpConfPoolDTO> opsResult = VmwareQuantityQueryHelper.queryIpConfPool(null, page, PAGE_SIZE, true);
                EasyOpsDataDTO<CmdbIpConfPoolDTO> dto = opsResult.getData();
                List<CmdbIpConfPoolDTO> dataList2 = dto.getDataList();
                if (CollectionUtils.isNotEmpty(dataList2)) {
                    List<String> opsInstanceIdList2 = dataList2.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
                    opsInstanceIdList.addAll(opsInstanceIdList2);
                }
            }
            log.info(">>>>> [IpConfPool]end-获取对端的所有instanceId >>>>");

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
            log.info("IpConfPool待删除数量:[{}],待新增数量:[{}],待更新数量:[{}]", reduceIdList.size(), newIdList.size(), updateIdList.size());

            // 删除instance.
            if (CollectionUtils.isNotEmpty(delIdList)) {
                // confService.batchDeleteByInstanceId(delIdList);
                collectService.batchDeleteByInstanceId(delIdList, CmdbIpConfPoolEntity.class);
                log.info("删除成功数量:[{}]", delIdList.size());
            }
            // 新增的
            saveOrUpdateIpConfPool(addIdList, false);
            // 修改的
            saveOrUpdateIpConfPool(editIdList, true);

            // 将当天的存活IP数据录入分区
            log.info(">>>>> [IpConfPool] begin 录入当天的历史分区表数据 >>>>");
            collectService.alterCmdbIpPart(EventInstanceModuleEnum.IP_CONF_POOL.getKey());
            log.info(">>>>> [IpConfPool] end 录入当天的历史分区表数据 >>>>");

        } catch (Exception e) {
            log.error("存活IP数据更新失败.", e);
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("<<<<<< 同步[IpConfPool] 完成,耗时:[{}]秒 <<<<<<", stopWatch.getTotalTimeSeconds());
    }

    @Override
    public void syncIpArpPool() {
        log.info(">>>>> 开始同步[IpArpPool] >>>>");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int pageCount = 0;
        try {
            // 1、获取数据总数
            long startCount = System.currentTimeMillis();
            int totalCount = VmwareQuantityQueryHelper.getInstanceCount(EventInstanceModuleEnum.IP_ARP_POOL.getKey());
            log.info("[IpArpPool]模型总量查询耗时：{} ms",System.currentTimeMillis() - startCount);
            if (totalCount == 0) {
                return;
            }

            // 2、计算总页数
            pageCount = (totalCount / PAGE_SIZE) + 1;
            log.info("自动化平台的IpArpPool数量:[{}]", totalCount);

            // 3、删除当天的存活IP
            String nowDay = DateUtils.datetimeToString(DateUtils.DEFAULT_DATE_FMT, new Date());
            collectService.deleteCmdbIpArpNowDay(nowDay);

            // 4、开始封装需要入库CMDB的数据
            log.info(">>>>> [IpArpPool]begin-同步主机探测IP数据 >>>>");
            List<CmdbIpArpPoolEntity> ipCollectEntityList = Lists.newArrayList();
            long start1 = System.currentTimeMillis();
            for (int page = 1; page <= pageCount; page++) {
                long start = System.currentTimeMillis();
                EasyOpsResult<CmdbIpArpPoolDTO> opsResult = VmwareQuantityQueryHelper.queryIpArpPool(null, page, PAGE_SIZE, false);
                long end = System.currentTimeMillis();
                log.info("[IpArpPool]模型第[{}]页接口查询耗时:{} ms",page,end - start);
                EasyOpsDataDTO<CmdbIpArpPoolDTO> dto = opsResult.getData();
                List<CmdbIpArpPoolDTO> dataList = dto.getDataList();
                if (CollectionUtils.isNotEmpty(dataList)) {
                    List<CmdbIpArpPoolEntity> entityList = Lists.newArrayList();
                    List<CmdbIpArpPoolEntity> updateList = VmwareInstanceQueryHelper.getFillIpArpPool(false, dataList, entityList);
                    if (CollectionUtils.isNotEmpty(updateList)) {
                        if (CollectionUtils.isNotEmpty(updateList)) {
                            // 获取检测时间是当天的数据，不是今天检测的不录入数据库
                            updateList.forEach(map -> {
                                Date time = map.getTime();
                                boolean toDay = DateUtils.isToDay(time);
                                if (toDay) {
                                    ipCollectEntityList.add(map);
                                }
                            });
                        }
                    }
                }
                log.info("[IpArpPool]模型第[{}]页CMDB处理耗时:{} ms",page,System.currentTimeMillis() - end);
            }
            log.info("[IpArpPool]模型入库数据封装总耗时:{} s,数据总量：{}",(System.currentTimeMillis() - start1) / 1000,ipCollectEntityList.size());
            // 5、进行cmdb入库操作
            long start2 = System.currentTimeMillis();
            if (CollectionUtils.isNotEmpty(ipCollectEntityList)) {
                List<CmdbIpArpPoolEntity> addList = new ArrayList<>();
                for (int i = 0; i < ipCollectEntityList.size(); i++) {
                    addList.add(ipCollectEntityList.get(i));
                    if (i % OPS_QUERY_COUNT == 0) {
                        ipArpPoolSave(false, addList);
                        addList.clear();
                    }
                }
                if (CollectionUtils.isNotEmpty(addList)) {
                    ipArpPoolSave(false, addList);
                }
            }
            log.info("[IpArpPool]模型入库CMDB耗时:{} s",(System.currentTimeMillis() - start2) / 1000);

            // 6.将当天的存活IP数据录入分区
            log.info(">>>>> [IpArpPool] begin 录入当天的历史分区表数据 >>>>");
            collectService.alterCmdbIpPart(EventInstanceModuleEnum.IP_ARP_POOL.getKey());
            log.info(">>>>> [IpArpPool] end 录入当天的历史分区表数据 >>>>");

        } catch (Exception e) {
            log.error("存活IP数据更新失败.", e);
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("<<<<<< 同步[IpArpPool] 完成,耗时:[{}]秒 <<<<<<", stopWatch.getTotalTimeSeconds());
    }

    public void syncIpArpPool_bak() {
        log.info(">>>>> 开始同步[IpArpPool] >>>>");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int pageCount = 0;
        try {
            List<String> opsInstanceIdList = Lists.newArrayList();
            List<String> addIdList = Lists.newArrayList();
            List<String> editIdList = Lists.newArrayList();
            List<String> delIdList = Lists.newArrayList();

            // 查询本地所有InstanceId
            List<String> localIdList = collectService.getAllInstanceId(CmdbIpArpPoolEntity.class);
            log.info("已存在的IpArpPool数量:[{}]", localIdList.size());

            // 1、获取数据总数
            EasyOpsResult<CmdbIpArpPoolDTO> result = VmwareQuantityQueryHelper.queryIpArpPool(null, 1, 1, true);
            int totalCount = result.getData().getTotalCount();
            // 2、计算总页数
            pageCount = (totalCount / PAGE_SIZE) + 1;
            log.info("自动化平台的IpArpPool数量:[{}]", totalCount);

            // 3、获取对端所有 InstanceId
            log.info(">>>>> [IpArpPool]begin-获取对端的所有instanceId >>>>");
            for (int page = 1; page <= pageCount; page++) {
                EasyOpsResult<CmdbIpArpPoolDTO> opsResult = VmwareQuantityQueryHelper.queryIpArpPool(null, page, PAGE_SIZE, true);
                EasyOpsDataDTO<CmdbIpArpPoolDTO> dto = opsResult.getData();
                List<CmdbIpArpPoolDTO> dataList2 = dto.getDataList();
                if (CollectionUtils.isNotEmpty(dataList2)) {
                    List<String> opsInstanceIdList2 = dataList2.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
                    opsInstanceIdList.addAll(opsInstanceIdList2);
                }
            }
            log.info(">>>>> [IpArpPool]end-获取对端的所有instanceId >>>>");
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
            log.info("IpArpPool待删除数量:[{}],待新增数量:[{}],待更新数量:[{}]", reduceIdList.size(), newIdList.size(), updateIdList.size());

            // 删除instance.
            if (CollectionUtils.isNotEmpty(delIdList)) {
                collectService.batchDeleteByInstanceId(delIdList, CmdbIpArpPoolEntity.class);
                log.info("删除成功数量:[{}]", delIdList.size());
            }
            // 新增的
            saveOrUpdateIpArpPool(addIdList, false);
            // 修改的
            saveOrUpdateIpArpPool(editIdList, true);

            // 将当天的存活IP数据录入分区
            log.info(">>>>> [IpArpPool] begin 录入当天的历史分区表数据 >>>>");
            collectService.alterCmdbIpPart(EventInstanceModuleEnum.IP_ARP_POOL.getKey());
            log.info(">>>>> [IpArpPool] end 录入当天的历史分区表数据 >>>>");

        } catch (Exception e) {
            log.error("存活IP数据更新失败.", e);
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("<<<<<< 同步[IpArpPool] 完成,耗时:[{}]秒 <<<<<<", stopWatch.getTotalTimeSeconds());
    }

    @Override
    public void syncVipAddressPool() {
        log.info(">>>>> 开始同步[VipAddressPool] >>>>");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int pageCount = 0;
        try {
            List<String> opsInstanceIdList = Lists.newArrayList();
            List<String> addIdList = Lists.newArrayList();
            List<String> editIdList = Lists.newArrayList();
            List<String> delIdList = Lists.newArrayList();

            // 查询本地所有InstanceId
            List<String> localIdList = vipCollectService.getAllInstanceId();
            log.info("已存在的VipAddressPool数量:[{}]", localIdList.size());

            // 1、获取数据总数
            EasyOpsResult<CmdbVipAddressPoolDTO> result = VmwareQuantityQueryHelper.queryVipAddressPool(null, 1, 1, true);
            int totalCount = result.getData().getTotalCount();
            // 2、计算总页数
            pageCount = (totalCount / PAGE_SIZE) + 1;
            log.info("自动化平台的VipAddressPool数量:[{}]", totalCount);

            // 3、获取对端所有 InstanceId
            for (int page = 1; page <= pageCount; page++) {
                EasyOpsResult<CmdbVipAddressPoolDTO> opsResult = VmwareQuantityQueryHelper.queryVipAddressPool(null, page, PAGE_SIZE, true);
                EasyOpsDataDTO<CmdbVipAddressPoolDTO> dto = opsResult.getData();
                List<CmdbVipAddressPoolDTO> dataList2 = dto.getDataList();
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
            log.info("VipAddressPool待删除数量:[{}],待新增数量:[{}],待更新数量:[{}]", reduceIdList.size(), newIdList.size(), updateIdList.size());

            // 删除instance.
            if (CollectionUtils.isNotEmpty(delIdList)) {
                vipCollectService.batchDeleteByInstanceId(delIdList);
                log.info("删除成功数量:[{}]", delIdList.size());
            }
            // 新增的
            saveOrUpdateVipAddressPool(addIdList, false);
            // 修改的
            saveOrUpdateVipAddressPool(editIdList, true);

        } catch (Exception e) {
            log.error("虚拟IP数据更新失败.", e);
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("<<<<<< 同步[VipAddressPool] 完成,耗时:[{}]ms <<<<<<", stopWatch.getTotalTimeMillis());
    }

    /**
     * 保存和更新IpAddressPool逻辑.
     *
     * @param idList   instanceId列表
     * @param isUpdate 是否是更新.
     */
    private void saveOrUpdateIpAddressPool(List<String> idList, boolean isUpdate) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        List<CmdbIpAddressPoolEntity> ipCollectEntityList = Lists.newArrayList();
        List<CmdbIpAddressPoolEntity> oldEntityList = Lists.newArrayList();
        List<List<String>> partitionList = Lists.partition(idList, OPS_QUERY_COUNT);
        // 新增判断是否需要初始化存活IP更新列表的存活和分配状态，Y表示需要
        String updateStatus = collectService.getIpUpdateConfig("1");
        log.info(">>>>> 开始同步[IpAddressPool]模型详情 >>>>>");
        for (List<String> subList : partitionList) {
            long start = System.currentTimeMillis();
            EasyOpsResult<CmdbIpAddressPoolDTO> easyOpsResult = VmwareQuantityQueryHelper.queryIpAddressPool(subList, 1, OPS_QUERY_COUNT, false);
            long end = System.currentTimeMillis();
            log.info("[IpAddressPool]详情查询耗时:{} ms",end -start);
            EasyOpsDataDTO<CmdbIpAddressPoolDTO> dto = easyOpsResult.getData();
            List<CmdbIpAddressPoolDTO> dataList = dto.getDataList();
            if (CollectionUtils.isEmpty(dataList)) {
                continue;
            }

            List<String> instanceIdList = dataList.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
            List<CmdbIpAddressPoolEntity> entityList = Lists.newArrayList();
            if (isUpdate) {
                entityList = collectService.findEntityByInstanceId(instanceIdList, CmdbIpAddressPoolEntity.class);
                oldEntityList.addAll(entityList);
            }
            List<CmdbIpAddressPoolEntity> updateList = VmwareInstanceQueryHelper.getFillIpAddressPool(isUpdate, dataList, entityList,updateStatus);
            if (CollectionUtils.isNotEmpty(updateList)) {
                List<CmdbIpAddressPoolEntity> temList = new ArrayList<>();
                // 获取检测时间是当天的数据，不是今天检测的不录入数据库
                updateList.forEach(map -> {
                    Date time = map.getTime();
                    boolean toDay = DateUtils.isToDay(time);
                    if (toDay) {
                        temList.add(map);
                    }
                });
                if (CollectionUtils.isNotEmpty(temList)) {
                    ipCollectEntityList.addAll(temList);
                }
            }

            if (ipCollectEntityList.size() > SAVE_SIZE) {
                ipAddressPoolSave(isUpdate, ipCollectEntityList);
            }
            log.info("cmdb处理[IpAddressPool]详情耗时:{} ms",System.currentTimeMillis() -end);
        }
        ipAddressPoolSave(isUpdate, ipCollectEntityList);
    }

    /**
     * 保存和更新IpAddressPool
     *
     * @param isUpdate
     * @param ipCollectEntityList
     */
    private void ipAddressPoolSave(boolean isUpdate, List<CmdbIpAddressPoolEntity> ipCollectEntityList) {
        if (CollectionUtils.isNotEmpty(ipCollectEntityList)) {
            if (isUpdate) {
                // ipCollectEntityList.forEach(e -> addressPoolService.modify(e));
//                ipCollectEntityList.forEach(e -> collectService.modifyForEntity(e, CmdbIpAddressPoolEntity.class));
                collectService.batchAdd(ipCollectEntityList, CmdbIpAddressPoolEntity.class,"2","1","1");
                log.info("修改ipAddressPool成功数量:[{}]", ipCollectEntityList.size());
            } else {
                collectService.batchAdd(ipCollectEntityList, CmdbIpAddressPoolEntity.class,"2","1","");
                log.info("新增ipAddressPool成功数量:[{}]", ipCollectEntityList.size());
            }
            ipCollectEntityList.clear();
        }
    }

    /**
     * 保存和更新IpConfPool逻辑.
     *
     * @param idList   instanceId列表
     * @param isUpdate 是否是更新.
     */
    private void saveOrUpdateIpConfPool(List<String> idList, boolean isUpdate) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        List<CmdbIpConfPoolEntity> ipCollectEntityList = Lists.newArrayList();
        List<CmdbIpConfPoolEntity> oldEntityList = Lists.newArrayList();
        List<List<String>> partitionList = Lists.partition(idList, OPS_QUERY_COUNT);
        log.info(">>>>> 开始同步[IpConfPool]模型详情 >>>>>");
        for (List<String> subList : partitionList) {
            EasyOpsResult<CmdbIpConfPoolDTO> easyOpsResult = VmwareQuantityQueryHelper.queryIpConfPool(subList, 1, OPS_QUERY_COUNT, false);
            EasyOpsDataDTO<CmdbIpConfPoolDTO> dto = easyOpsResult.getData();
            List<CmdbIpConfPoolDTO> dataList = dto.getDataList();
            if (CollectionUtils.isEmpty(dataList)) {
                continue;
            }

            List<String> instanceIdList = dataList.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
            List<CmdbIpConfPoolEntity> entityList = Lists.newArrayList();
            if (isUpdate) {
                entityList = collectService.findEntityByInstanceId(instanceIdList, CmdbIpConfPoolEntity.class);
                oldEntityList.addAll(entityList);
            }
            List<CmdbIpConfPoolEntity> updateList = VmwareInstanceQueryHelper.getFillIpConfPool(isUpdate, dataList, entityList);
            if (CollectionUtils.isNotEmpty(updateList)) {
                List<CmdbIpConfPoolEntity> temList = new ArrayList<>();
                // 获取检测时间是当天的数据，不是今天检测的不录入数据库
                updateList.forEach(map -> {
                    Date time = map.getTime();
                    boolean toDay = DateUtils.isToDay(time);
                    if (toDay) {
                        temList.add(map);
                    }
                });
                if (CollectionUtils.isNotEmpty(temList)) {
                    ipCollectEntityList.addAll(temList);
                }
            }

            if (ipCollectEntityList.size() > SAVE_SIZE) {
                ipConfPoolSave(isUpdate, ipCollectEntityList);
            }
        }
        ipConfPoolSave(isUpdate, ipCollectEntityList);
    }

    /**
     * 保存和更新IpConfPool逻辑.
     *
     * @param isUpdate
     * @param ipCollectEntityList
     */
    private void ipConfPoolSave(boolean isUpdate, List<CmdbIpConfPoolEntity> ipCollectEntityList) {
        if (CollectionUtils.isNotEmpty(ipCollectEntityList)) {
            if (isUpdate) {
                collectService.batchAdd(ipCollectEntityList, CmdbIpConfPoolEntity.class,"2","1","1");
                log.info("修改IpConfPool成功数量:[{}]", ipCollectEntityList.size());
            } else {
                collectService.batchAdd(ipCollectEntityList, CmdbIpConfPoolEntity.class,"2","1","");
                log.info("新增IpConfPool成功数量:[{}]", ipCollectEntityList.size());
            }
            ipCollectEntityList.clear();
        }
    }

    /**
     * 保存和更新IpArpPool逻辑.
     *
     * @param idList   instanceId列表
     * @param isUpdate 是否是更新.
     */
    private void saveOrUpdateIpArpPool(List<String> idList, boolean isUpdate) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        List<CmdbIpArpPoolEntity> ipCollectEntityList = Lists.newArrayList();
        List<CmdbIpArpPoolEntity> oldEntityList = Lists.newArrayList();
        List<List<String>> partitionList = Lists.partition(idList, OPS_QUERY_COUNT);
        log.info(">>>>> 开始同步[IpArpPool]模型详情 >>>>>");
        for (List<String> subList : partitionList) {
            EasyOpsResult<CmdbIpArpPoolDTO> easyOpsResult = VmwareQuantityQueryHelper.queryIpArpPool(subList, 1, OPS_QUERY_COUNT, false);
            EasyOpsDataDTO<CmdbIpArpPoolDTO> dto = easyOpsResult.getData();
            List<CmdbIpArpPoolDTO> dataList = dto.getDataList();
            if (CollectionUtils.isEmpty(dataList)) {
                continue;
            }

            List<String> instanceIdList = dataList.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
            List<CmdbIpArpPoolEntity> entityList = Lists.newArrayList();
            if (isUpdate) {
                entityList = collectService.findEntityByInstanceId(instanceIdList, CmdbIpArpPoolEntity.class);
                oldEntityList.addAll(entityList);
            }
            List<CmdbIpArpPoolEntity> updateList = VmwareInstanceQueryHelper.getFillIpArpPool(isUpdate, dataList, entityList);
            if (CollectionUtils.isNotEmpty(updateList)) {
                List<CmdbIpArpPoolEntity> temList = new ArrayList<>();
                // 获取检测时间是当天的数据，不是今天检测的不录入数据库
                updateList.forEach(map -> {
                    Date time = map.getTime();
                    boolean toDay = DateUtils.isToDay(time);
                    if (toDay) {
                        temList.add(map);
                    }
                });
                if (CollectionUtils.isNotEmpty(temList)) {
                    ipCollectEntityList.addAll(temList);
                }
            }

            if (ipCollectEntityList.size() > SAVE_SIZE) {
                ipArpPoolSave(isUpdate, ipCollectEntityList);
            }
        }
        ipArpPoolSave(isUpdate, ipCollectEntityList);
    }

    /**
     * 保存和更新IpArpPool
     *
     * @param isUpdate
     * @param ipCollectEntityList
     */
    private void ipArpPoolSave(boolean isUpdate, List<CmdbIpArpPoolEntity> ipCollectEntityList) {
        if (CollectionUtils.isNotEmpty(ipCollectEntityList)) {
            if (isUpdate) {
                collectService.batchAdd(ipCollectEntityList, CmdbIpArpPoolEntity.class,"2","1","1");
//                ipCollectEntityList.forEach(e -> collectService.modifyForEntity(e, CmdbIpArpPoolEntity.class));
                log.info("修改IpArpPool成功数量:[{}]", ipCollectEntityList.size());
            } else {
                collectService.batchAdd(ipCollectEntityList, CmdbIpArpPoolEntity.class,"2","1","");
                log.info("新增IpArpPool成功数量:[{}]", ipCollectEntityList.size());
            }
            ipCollectEntityList.clear();
        }
    }

    /**
     * 保存和更新VipAddressPool逻辑.
     *
     * @param idList   instanceId列表
     * @param isUpdate 是否是更新.
     */
    private void saveOrUpdateVipAddressPool(List<String> idList, boolean isUpdate) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        List<CmdbVipCollectEntity> vipCollectEntityList = Lists.newArrayList();
        List<List<String>> partitionList = Lists.partition(idList, OPS_QUERY_COUNT);
        for (List<String> subList : partitionList) {
            EasyOpsResult<CmdbVipAddressPoolDTO> easyOpsResult = VmwareQuantityQueryHelper.queryVipAddressPool(subList, 1, OPS_QUERY_COUNT, false);
            EasyOpsDataDTO<CmdbVipAddressPoolDTO> dto = easyOpsResult.getData();
            List<CmdbVipAddressPoolDTO> dataList = dto.getDataList();
            if (CollectionUtils.isEmpty(dataList)) {
                continue;
            }

            List<String> instanceIdList = dataList.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
            List<CmdbVipCollectEntity> entityList = Lists.newArrayList();
            if (isUpdate) {
                entityList = vipCollectService.findByInstanceIdList(instanceIdList);
            }
            List<CmdbVipCollectEntity> updateList = VmwareInstanceQueryHelper.getFillVipAddressPool(isUpdate, dataList, entityList);
            if (CollectionUtils.isNotEmpty(updateList)) {
                vipCollectEntityList.addAll(updateList);
            }

            if (vipCollectEntityList.size() > SAVE_SIZE) {
                vipCollectSave(isUpdate, vipCollectEntityList);
            }
        }
        vipCollectSave(isUpdate, vipCollectEntityList);
    }

    /**
     * 保存和更新VipAddressPool
     *
     * @param isUpdate
     * @param vipCollectEntityList
     */
    private void vipCollectSave(boolean isUpdate, List<CmdbVipCollectEntity> vipCollectEntityList) {
        if (CollectionUtils.isNotEmpty(vipCollectEntityList)) {
            if (isUpdate) {
                vipCollectEntityList.forEach(e -> vipCollectService.modify(e));
                log.info("修改vipAddressPool成功数量:[{}]", vipCollectEntityList.size());
            } else {
                vipCollectService.batchAdd(vipCollectEntityList);
                log.info("新增vipAddressPool成功数量:[{}]", vipCollectEntityList.size());
            }
            vipCollectEntityList.clear();
        }
    }


}
