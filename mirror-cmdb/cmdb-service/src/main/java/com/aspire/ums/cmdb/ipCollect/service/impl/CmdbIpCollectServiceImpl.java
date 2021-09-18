package com.aspire.ums.cmdb.ipCollect.service.impl;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.cmic.service.ICmdbCmicInstanceService;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.ipCollect.enums.EventInstanceModuleEnum;
import com.aspire.ums.cmdb.ipCollect.mapper.CmdbIpCollectMapper;
import com.aspire.ums.cmdb.ipCollect.payload.entity.BaseIpCollectEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpAddressPoolEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpArpPoolEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpCollectRequest;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpCollectResponse;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpCollectTopTotalResponse;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpConfPoolEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpInfo;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbSurvivalStatusMonitor;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpAddressPoolService;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpArpService;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpClashService;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpCollectService;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpConfService;
import com.aspire.ums.cmdb.ipCollect.service.CmdbVmwareNetworkPortGroupService;
import com.aspire.ums.cmdb.sync.service.producer.CmdbDeviceAssetProducerServiceImpl;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 19:47
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service("CmdbIpCollectService")
public class CmdbIpCollectServiceImpl implements CmdbIpCollectService {

    private static final String OPERATOR_TYPE = "自动通过";

    private static final String DATA_TYPE_IPV6 = "ipv6";

    private static final String DATA_TYPE_IPV4 = "ipv4";

    private static final String DATA_TYPE_BUSINESS = "business";

    private static final String DATA_TYPE_FIRSTTIME = "firstTime";

    private static final String DATA_TYPE_MAC = "mac";

    private static final String OP_TYPE_UPDATE = "update";

    private static final String OP_TYPE_DEL = "del";

    private static final String OP_TYPE_ADD = "add";

    private static final String ONE_FLAG = "1";

    private static final String TWO_FLAG = "2";

    private static final String DATE_FMT_PARTNAME = "yyyy_MM_dd";

    private static final String TABLE_NAME_ADDRESS_IP_HIS = "cmdb_ip_address_pool_his";

    private static final String TABLE_NAME_CONF_IP_HIS = "cmdb_ip_conf_pool_his";

    private static final String TABLE_NAME_ARP_IP_HIS = "cmdb_ip_arp_pool_his";

    @Value("${osa.baseUrl:}")
    private String osaBaseUrl;
    @Value("${osa.deviceUpdateUrl:}")
    private String deviceUpdateUrl;

    @Autowired
    private ICmdbCmicInstanceService iCmdbCmicInstanceService;

    @Autowired
    private CmdbIpClashService clashService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private CmdbIpCollectMapper ipCollectMapper;

    @Autowired
    private CmdbIpAddressPoolService addressPoolService;

    @Autowired
    private CmdbIpConfService confService;

    @Autowired
    private CmdbIpArpService arpService;

    @Resource
    private ConfigDictMapper configDictMapper;

    @Autowired
    private CmdbVmwareNetworkPortGroupService networkPortGroupService;

    @Override
    public List<CmdbIpCollectResponse> findPage(CmdbIpCollectRequest request) {
        return ipCollectMapper.findPage(request);
    }

    @Override
    public Integer findPageCount(CmdbIpCollectRequest request) {
        return ipCollectMapper.findPageCount(request);
    }

    @Override
    public CmdbIpCollectTopTotalResponse findTopTotal(CmdbIpCollectRequest request) {
        return ipCollectMapper.findTopTotal(request);
    }

    @Override
    public List<Map> getResource() {
        List<Map> dataList = Lists.newArrayList();
        List<String> resourceList = ipCollectMapper.getResource();
        resourceList.forEach(item -> {
            Map<String, String> temp = new HashMap<>(1);
            temp.put("value", item);
            temp.put("label", item);
            dataList.add(temp);
        });
        return dataList;
    }

    @Override
    public List<Map> getSource() {
        List<Map> dataList = Lists.newArrayList();
        // 存活IP数据来源 新增 fanwenhui 20200729
        List<ConfigDict> configDictList = configDictMapper.selectDictsByType("cmdbIpCollectSourceType", null, null, null);
        configDictList.forEach(map -> {
            Map<String, String> temp = new HashMap<>(1);
            temp.put("value", map.getValue());
            temp.put("label", map.getName());
            dataList.add(temp);
        });

        return dataList;
    }

    @Override
    public <T extends BaseIpCollectEntity> void batchAdd(List<T> obj, Class<T> clazz, String autoFlag, String updateFlag,
            String ipClashFlag) {

        if (clazz.equals(CmdbIpAddressPoolEntity.class)) {
            addressPoolService.batchAdd((List<CmdbIpAddressPoolEntity>) obj);
        } else if (clazz.equals(CmdbIpConfPoolEntity.class)) {
            confService.batchAdd((List<CmdbIpConfPoolEntity>) obj);
        } else if (clazz.equals(CmdbIpArpPoolEntity.class)) {
            arpService.batchAdd((List<CmdbIpArpPoolEntity>) obj);
        }

        // 如果不是自动化调用的请求，不需要更新地址库新
//        if (TWO_FLAG.equals(autoFlag) && ONE_FLAG.equals(updateFlag)) {
//            updateInnerIpInfo(obj, null, OP_TYPE_ADD);
//        }
        // 冲突IP录入逻辑修改，当自动化新增实例到cmdb时，进行冲突IP比对
        // if ("1".equals(autoFlag)) {
        // // 冲突IP处理 entity
        // obj.forEach(entity -> {
        // clashService.ipClashSave(entity, clazz);
        // });
        // }
        // executorService.execute(() -> {
        // obj.stream().forEach(item -> {
        // iCmdbCmicInstanceService.updateIpInfo(item.getSrcCreator(), getIpData("add", item), OPERATOR_TYPE);
        // });
        // });
    }

    @Override
    public <T extends BaseIpCollectEntity> void batchDeleteByInstanceId(List<String> instanceIds, Class<T> clazz) {
        if (clazz.equals(CmdbIpAddressPoolEntity.class)) {
            addressPoolService.batchDeleteByInstanceId(instanceIds);
        } else if (clazz.equals(CmdbIpConfPoolEntity.class)) {
            confService.batchDeleteByInstanceId(instanceIds);
        } else if (clazz.equals(CmdbIpArpPoolEntity.class)) {
            arpService.batchDeleteByInstanceId(instanceIds);
        }

        taskExecutor.execute(() -> {
            instanceIds.forEach(instanceId -> {
                BaseIpCollectEntity entity = findBaseByInstanceId(instanceId, clazz);
                entity.setTime(new Date());
                updateInnerIpInfo(null, entity, OP_TYPE_DEL);
            });

        });
        // executorService.execute(() -> {
        // instanceIds.stream().forEach(instanceId -> {
        // BaseIpCollectEntity entity = findBaseByInstanceId(instanceId, clazz);
        // entity.setTime(new Date());
        // iCmdbCmicInstanceService.updateIpInfo("alauda", getIpData("del", entity), OPERATOR_TYPE);
        // });
        // });
    }

    @Override
    public <T extends BaseIpCollectEntity> void modifyForMap(Map<String, Object> obj, Class<T> clazz) {
        BaseIpCollectEntity entity = findBaseByInstanceId(obj.get("instanceId").toString(), clazz);

        if (clazz.equals(CmdbIpAddressPoolEntity.class)) {
            addressPoolService.updateByInstanceId(obj);
        } else if (clazz.equals(CmdbIpConfPoolEntity.class)) {
            confService.updateByInstanceId(obj);
        } else if (clazz.equals(CmdbIpArpPoolEntity.class)) {
            arpService.updateByInstanceId(obj);
        }

        // mac有修改，为冲突IP
        if (obj.containsKey(DATA_TYPE_MAC)) {
            // 冲突IP处理 entity
            clashService.ipClashSave(entity, clazz);
        }

        taskExecutor.execute(() -> {
            entity.setTime(new Date());
            iCmdbCmicInstanceService.updateIpInfo(obj.get("operator").toString(), getIpData(OP_TYPE_UPDATE, entity), OPERATOR_TYPE);
        });
    }

    @Override
    public <T extends BaseIpCollectEntity> void modifyForEntity(T obj, Class<T> clazz) {
        BaseIpCollectEntity entity = findBaseByInstanceId(obj.getInstanceId(), clazz);

        if (clazz.equals(CmdbIpAddressPoolEntity.class)) {
            addressPoolService.modify((CmdbIpAddressPoolEntity) obj);
        } else if (clazz.equals(CmdbIpConfPoolEntity.class)) {
            confService.modify((CmdbIpConfPoolEntity) obj);
        } else if (clazz.equals(CmdbIpArpPoolEntity.class)) {
            arpService.modify((CmdbIpArpPoolEntity) obj);
        }

        // 冲突IP处理 entity
        clashService.ipClashSave(entity, clazz);

        taskExecutor.execute(() -> {
            updateInnerIpInfo(null, obj, OP_TYPE_UPDATE);
        });
        // executorService.execute(() -> {
        // iCmdbCmicInstanceService.updateIpInfo("alauda", getIpData(OP_TYPE_UPDATE, obj), OPERATOR_TYPE);
        // });
    }

    @Override
    public <T extends BaseIpCollectEntity> BaseIpCollectEntity findBaseByInstanceId(String instanceId, Class<T> clazz) {
        List<?> list = new ArrayList<>();
        BaseIpCollectEntity entity = new BaseIpCollectEntity();
        if (clazz.equals(CmdbIpAddressPoolEntity.class)) {
            list = addressPoolService.findByInstanceIdList(Arrays.asList(instanceId));
        } else if (clazz.equals(CmdbIpConfPoolEntity.class)) {
            list = confService.findByInstanceIdList(Arrays.asList(instanceId));
        } else if (clazz.equals(CmdbIpArpPoolEntity.class)) {
            list = arpService.findByInstanceIdList(Arrays.asList(instanceId));
        }
        if (!CollectionUtils.isEmpty(list)) {
            if (null != list.get(0)) {
                BeanUtils.copyProperties(list.get(0), entity);
            }
        }
        return entity;
    }

    @Override
    public <T extends BaseIpCollectEntity> List<T> findEntityByInstanceId(List<String> instanceIds, Class<T> clazz) {
        List<?> list = null;
        if (clazz.equals(CmdbIpAddressPoolEntity.class)) {
            list = addressPoolService.findByInstanceIdList(instanceIds);
        } else if (clazz.equals(CmdbIpConfPoolEntity.class)) {
            list = confService.findByInstanceIdList(instanceIds);
        } else if (clazz.equals(CmdbIpArpPoolEntity.class)) {
            list = arpService.findByInstanceIdList(instanceIds);
        }
        return (List<T>) list;
    }

    @Override
    public <T extends BaseIpCollectEntity> List<String> getAllInstanceId(Class<T> clazz) {
        List<String> list = null;
        if (clazz.equals(CmdbIpAddressPoolEntity.class)) {
            list = addressPoolService.getAllInstanceId();
        } else if (clazz.equals(CmdbIpConfPoolEntity.class)) {
            list = confService.getAllInstanceId();
        } else if (clazz.equals(CmdbIpArpPoolEntity.class)) {
            list = arpService.getAllInstanceId();
        }
        return list;
    }

    /**
     * 封装
     *
     * @param type
     * @param item
     * @return
     */
    private Map<String, Object> getIpData(String type, BaseIpCollectEntity item) {
        String id1 = configDictMapper.getIdByNoteAndCol("未存活", "survival_status");
        String id2 = configDictMapper.getIdByNoteAndCol("已存活", "survival_status");
        String id3 = configDictMapper.getIdByNoteAndCol("未分配", "ipAllocationStatusType");
        String id4 = configDictMapper.getIdByNoteAndCol("已分配", "ipAllocationStatusType");
        String id5 = "";
        if (!StringUtils.isEmpty(item.getResource())) {
            id5 = configDictMapper.getIdByNoteAndCol(item.getResource(), "idcType2");
        }
        Map<String, Object> ipData = Maps.newHashMap();
        ipData.put("ip", item.getIp());
        ipData.put("idcType", id5);
        ipData.put("ip_type", item.getIptype());
        ipData.put("survival_status", id2);
        ipData.put("assign_status", id4);
        if (type.equals("del")) {
            ipData.put("survival_status", id1);
            ipData.put("assign_status", id3);
        }
        // if (type.equals(OP_TYPE_ADD)) {
        // ipData.put("first_survival_time", DateUtils.datetimeToString(item.getTime()));
        // }
        if (StringUtils.isNotEmpty(item.getTime())) {
            ipData.put("latest_survival_time", DateUtils.datetimeToString(item.getTime()));
        }
        return ipData;
    }

    public <T extends BaseIpCollectEntity> void updateInnerIpInfo(List<T> objList, T oneObj, String type) {
        try {
            List<Map<String, Object>> updateIpList = new ArrayList<>();
            // 新增IPv6存活状态信息更新
            List<Map<String, Object>> updateIpV6List = new ArrayList<>();
            if (null != objList && !objList.isEmpty()) {
                objList.forEach(map -> {
                    Map<String, Object> add = getIpData(type, map);
                    if (DATA_TYPE_IPV6.equals(map.getIptype())) {
                        updateIpV6List.add(add);
                    } else {
                        updateIpList.add(add);
                    }
                });
            }
            if (null != oneObj) {
                if (DATA_TYPE_IPV6.equals(oneObj.getIptype())) {
                    updateIpV6List.add(getIpData(type, oneObj));
                } else {
                    updateIpList.add(getIpData(type, oneObj));
                }
            }
            if (!updateIpList.isEmpty()) {
                ipCollectMapper.updateInnerIpInfo(updateIpList);
            }
            if (!updateIpV6List.isEmpty()) {
                ipCollectMapper.updateIpv6Info(updateIpV6List);
            }
        } catch (Exception e) {
            log.info("IP存活定时任务更新失败", e);
        }
    }

    /**
     * 定期比对cmdb资产的所有非IPV6的ip信息和内网IP地址库进行比对 新增存活IP全量对比 如果cmdb的ip在ip地址库中存在，则更新对应的存活和分配状态，以及存活时间
     */
    @Override
    public void updateCmdbAssetAllIpInfo() {
        try {
            // 获取cmdb资产的未删除的所有IP,资产的IP不更新首次和最近存活时间
            List<CmdbIpInfo> allCmdbIpList = getAllCmdbIpInfo("");
            // 获取自动化平台同步的所有存活内网IP,类型为IPV4,只更新最近存活时间
//            List<CmdbIpInfo> autoInnerIpInfo = ipCollectMapper.getAutoInnerIpInfo();

            // 获取cmdb资产的已删除的所有IP
            List<CmdbIpInfo> delAllCmdbIpList = getAllCmdbIpInfo(ONE_FLAG);

            // 构建更新内网IP地址库的实体
            List<Map<String, Object>> updateIpList = new ArrayList<>();
            List<String> ip4IdcTypeList = new ArrayList<>();
            // 先构建自动化的更新实体，可以将与cmdb相同的ip给去掉
//            autoInnerIpInfo.forEach(map -> {
//                String ip4IdcType = map.getIp4IdcType();
//                if (!ip4IdcTypeList.contains(ip4IdcType)) {
//                    ip4IdcTypeList.add(ip4IdcType);
//                    updateIpList.add(getUpdateIpData(map, OP_TYPE_UPDATE, TWO_FLAG));
//                }
//            });

            allCmdbIpList.forEach(map -> {
                String ip4IdcType = map.getIp4IdcType();
                if (!ip4IdcTypeList.contains(ip4IdcType)) {
                    ip4IdcTypeList.add(ip4IdcType);
                    updateIpList.add(getUpdateIpData(map, OP_TYPE_UPDATE, ONE_FLAG));
                }
            });
            if (!delAllCmdbIpList.isEmpty()) {
                delAllCmdbIpList.forEach(map -> {
                    String ip4IdcType = map.getIp4IdcType();
                    if (!ip4IdcTypeList.contains(ip4IdcType)) {
                        updateIpList.add(getUpdateIpData(map, OP_TYPE_DEL, ONE_FLAG));
                    }
                });
            }
            log.info("内网IP需要更新的分配IP总数为：{},开始更新分配状态到地址库>>>>>>>>", updateIpList.size());
            long begin = System.currentTimeMillis();
            List<List<Map<String, Object>>> partitionList = Lists.partition(updateIpList, 1000);
            for (List<Map<String, Object>> subList : partitionList) {
                ipCollectMapper.updateInnerIpInfo(subList);
            }
            long end = System.currentTimeMillis();
            log.info("success_定时同步已录CMDB资产的分配状态到IP内网地址库成功,耗时：{} 秒", (end - begin) / 1000);
        } catch (Exception e) {
            log.error("fail_定时同步已录CMDB资产的分配状态到IP内网地址库失败,{}", e.getMessage());
        }
    }

    /**
     * 获取cmdb资产的未删除的所有IP
     * 
     * @param delFlag
     *            资产删除标识。1-已删除
     */
    private List<CmdbIpInfo> getAllCmdbIpInfo(String delFlag) {
        // 设备IP
        List<CmdbIpInfo> cmdbIpList1 = ipCollectMapper.getCmdbAllIpInfo1(delFlag);
        // other ip
        List<CmdbIpInfo> cmdbIpList2 = ipCollectMapper.getCmdbAllIpInfo2(delFlag);
        // 业务IP1
        List<CmdbIpInfo> cmdbIpList3 = ipCollectMapper.getCmdbAllIpInfo3(delFlag);
        // 业务IP2
        List<CmdbIpInfo> cmdbIpList4 = ipCollectMapper.getCmdbAllIpInfo4(delFlag);
        // console IP
        List<CmdbIpInfo> cmdbIpList5 = ipCollectMapper.getCmdbAllIpInfo5(delFlag);
        List<CmdbIpInfo> allCmdbIpList = new ArrayList<>(cmdbIpList1);
        allCmdbIpList.addAll(cmdbIpList2);
        allCmdbIpList.addAll(cmdbIpList3);
        allCmdbIpList.addAll(cmdbIpList4);
        allCmdbIpList.addAll(cmdbIpList5);
        return allCmdbIpList;
    }

    /**
     * 构建cmdb资产需要更新IP状态信息
     * 
     * @param cmdbIpInfo
     *            cmdb资产的IP
     * @param type
     *            新增、删除、业务线
     * @param resourceType
     *            ip来源,1-cmdb,2-自动化
     */
    private Map<String, Object> getUpdateIpData(CmdbIpInfo cmdbIpInfo, String type, String resourceType) {
        String id1 = configDictMapper.getIdByNoteAndCol("未存活", "survival_status");
        String id2 = configDictMapper.getIdByNoteAndCol("已存活", "survival_status");
        String id3 = configDictMapper.getIdByNoteAndCol("未分配", "ipAllocationStatusType");
        String id4 = configDictMapper.getIdByNoteAndCol("已分配", "ipAllocationStatusType");
        Map<String, Object> retMap = Maps.newHashMap();
        retMap.put("ip", cmdbIpInfo.getIp());
        retMap.put("idcType", cmdbIpInfo.getIdcType());
        retMap.put("ip_type", cmdbIpInfo.getIpType());
        if (OP_TYPE_DEL.equals(type)) {
            retMap.put("survival_status", id1);
            retMap.put("assign_status", id3);
            return retMap;
        } else if (DATA_TYPE_BUSINESS.equals(type)) {
            retMap.put("businessName1", cmdbIpInfo.getBusinessName1());
            retMap.put("businessName2", cmdbIpInfo.getBusinessName2());
            return retMap;
        } else if (DATA_TYPE_FIRSTTIME.equals(type)) {
            retMap.put("first_survival_time", cmdbIpInfo.getFirstSurvivalTime());
            return retMap;
        }
        retMap.put("assign_status", id4);
        if("chinese".equals(type)) { // 是否需要存中文
            retMap.put("survival_status", "已存活");
        }
        // 如何ip来源是自动化才更新存活状态和最近存活时间
        if (TWO_FLAG.equals(resourceType)) {
            retMap.put("survival_status", id2);
            retMap.put("latest_survival_time", cmdbIpInfo.getLatestSurvivalTime());
        }
        return retMap;
    }

    /**
     * 定期将网管的公网IP采集数据更新到公网地址库的存活信息上来
     */
    @Override
    public void updatePublicIpInfo() {
        try {
            List<CmdbIpInfo> publicIpIpInfo = ipCollectMapper.getPublicIpIpInfo();
            // 构建更新公网IP地址库的实体
            List<Map<String, Object>> updateIpList = new ArrayList<>();
            List<String> delIpList = new ArrayList<>();
            publicIpIpInfo.forEach(map -> {
                updateIpList.add(getUpdateIpData(map, OP_TYPE_UPDATE, ONE_FLAG));
                delIpList.add(map.getIp());
            });

            log.info("公网IP更新存活信息的总数为：{},开始更新公网IP存活信息到地址库>>>>>>>>", updateIpList.size());
            long begin = System.currentTimeMillis();
            List<List<Map<String, Object>>> partitionList = Lists.partition(updateIpList, 1000);
            for (List<Map<String, Object>> subList : partitionList) {
                ipCollectMapper.updatePublicIpInfo(subList);
            }
            List<List<String>> partitionDelList = Lists.partition(delIpList, 1000);
            for (List<String> subList : partitionDelList) {
                ipCollectMapper.updatePublicIpInfo4Del(subList);
            }
            long end = System.currentTimeMillis();
            log.info("success_定时更新公网IP存活信息到地址库成功,耗时：{} 秒", (end - begin) / 1000);
        } catch (Exception e) {
            log.error("fail_定时更新公网IP存活信息到地址库失败,{}", e.getMessage());
        }
    }

    /**
     * 定期更新ipV6的数据到IP地址库上
     */
    @Override
    public void updateIpv6Info() {
        try {
            // 自动化的IPv6
            List<CmdbIpInfo> allAutoIpv6 = ipCollectMapper.getAllAutoIpv6();
            // cmdb的IPv6
            List<CmdbIpInfo> allCmdbIpv6List = getCmdbIpv6List();
            List<Map<String, Object>> updateIpList = new ArrayList<>();
            List<String> delIpList = new ArrayList<>();
            allAutoIpv6.forEach(map -> {
                updateIpList.add(getUpdateIpData(map, OP_TYPE_UPDATE, TWO_FLAG));
                delIpList.add(map.getIp());
            });

            allCmdbIpv6List.forEach(map -> {
                updateIpList.add(getUpdateIpData(map, OP_TYPE_UPDATE, ONE_FLAG));
                delIpList.add(map.getIp());
            });

            log.info("ipv6更新存活信息的总数为：{},开始ipv6存活信息到地址库>>>>>>>>", updateIpList.size());
            long begin = System.currentTimeMillis();
            List<List<String>> partitionDelList = Lists.partition(delIpList, 1000);
            for (List<String> subList : partitionDelList) {
                ipCollectMapper.updateIpv6Info4Del(subList);
            }

            List<List<Map<String, Object>>> partitionList = Lists.partition(updateIpList, 1000);
            for (List<Map<String, Object>> subList : partitionList) {
                ipCollectMapper.updateIpv6Info(subList);
            }
            long end = System.currentTimeMillis();
            log.info("success_定时更新ipv6存活信息成功,耗时：{} 秒", (end - begin) / 1000);
        } catch (Exception e) {
            log.error("fail_定时更新ipv6存活信息失败,{}", e.getMessage());
        }
    }

    private List<CmdbIpInfo> getCmdbIpv6List() {
        List<CmdbIpInfo> allIpv6List = new ArrayList<>();
        List<CmdbIpInfo> list1 = ipCollectMapper.getCmdbAllIp4Ipv6();
        if (!list1.isEmpty()) {
            list1.forEach(map -> {
                Object[] ipv6Flag = IpUtils.isIPV6Address(map.getIp());
                boolean checkFlag = (Boolean) ipv6Flag[0];
                if (checkFlag) {
                    allIpv6List.add(map);
                }
            });
        }
        return allIpv6List;
    }

    /**
     * 通过资产来更新内网IP的业务线信息
     */
    @Override
    public void updateIpBusinessByAsset() {
        try {
            log.info("开始查询资产业务线信息>>>>>");
            long start = System.currentTimeMillis();
            // 获取cmdb资产的未删除的所有IP
            List<CmdbIpInfo> allCmdbIpList = getAllCmdbIpInfo("");
            long end1 = System.currentTimeMillis();
            log.info("资产业务线查询耗时：{} 秒 >>>>>",(end1 - start) / 1000);
            // 构建更新内网IP地址库的业务线相关的实体
            List<Map<String, Object>> updateIpList = new ArrayList<>();
            List<String> ip4IdcTypeList = new ArrayList<>();
            allCmdbIpList.forEach(map -> {
                String ip4IdcType = map.getIp4IdcType();
                if (!ip4IdcTypeList.contains(ip4IdcType)) {
                    ip4IdcTypeList.add(ip4IdcType);
                    updateIpList.add(getUpdateIpData(map, DATA_TYPE_BUSINESS, ONE_FLAG));
                }
            });
            long end2 = System.currentTimeMillis();
            log.info("内网业务线更新参数拼接耗时 ：{} 秒 >>>>>",(end2 - end1) / 1000);

            long begin = System.currentTimeMillis();
            log.info("内网IP需要更新业务线总数为：{},开始更新地址库业务线信息>>>>>>>>", updateIpList.size());
            List<List<Map<String, Object>>> partitionList = Lists.partition(updateIpList, 2000);
            for (List<Map<String, Object>> subList : partitionList) {
                ipCollectMapper.updateInnerIpInfo(subList);
            }
            ipCollectMapper.updateInnerIpInfoNotInCmdb();
            long end = System.currentTimeMillis();
            log.info("success_定时同步业务线信息内网地址库成功,耗时：{} 秒", (end - begin) / 1000);
        } catch (Exception e) {
            log.error("fail_定时同步业务线信息内网地址库失败,{}", e.getMessage());
        }
    }

    /**
     * 查询字典表获取哪个IP类型需要更新，便于手动更新
     * 
     * @param config
     *            1-内网存活,2-公网存活,3-ipv6存活,4-内网业务线
     */
    @Override
    public String getIpUpdateConfig(String config) {
        String updateStatus = "N";
        List<ConfigDict> configDictList = configDictMapper.selectDictsByType("survivalUpdate", null, null, null);
        if (!configDictList.isEmpty()) {
            ConfigDict configDict = configDictList.get(0);
            String temp = configDict.getValue();
            if (StringUtils.isEmpty(temp)) {
                return updateStatus;
            }
            String[] split = temp.split("-");
            updateStatus = "1".equals(config) ? split[0]
                    : "2".equals(config) ? split[1]
                            : "3".equals(config) ? split[2]
                                    : "4".equals(config) ? split[3]
                                            : "5".equals(config) ? split[4]
                                                    : "6".equals(config) ? split[5]
                                                            : "7".equals(config) ? split[6]
                                                                    : "8".equals(config) ? split[7]
                                                                            : "9".equals(config) ? split[8] : updateStatus;
        }
        return updateStatus;
    }

    @Override
    public Map<String, String> buildConfig4Map(String config) {
        Map<String, String> retMap = new HashMap<>();
        List<ConfigDict> configDictList = configDictMapper.selectDictsByType(config, null, null, null);
        configDictList.forEach(map -> {
            retMap.put(map.getValue(), map.getName());
        });
        return retMap;
    }

    @Override
    public void updateFirstSurvivalTime4IpInfo() {
        try {
            // 获取自动化平台同步的所有最低版本的IP的检测时间作为首次存活时间
            List<CmdbIpInfo> autoIpInfo = ipCollectMapper.getAutoIpInfo4FirstSurvivalTime();
            // 构建更新内网IP地址库的实体
            List<Map<String, Object>> updateIpList = new ArrayList<>();
            List<Map<String, Object>> updateIpv6List = new ArrayList<>();
            autoIpInfo.forEach(map -> {
                if (DATA_TYPE_IPV4.equals(map.getIpType())) {
                    updateIpList.add(getUpdateIpData(map, DATA_TYPE_FIRSTTIME, ""));
                } else {
                    updateIpv6List.add(getUpdateIpData(map, DATA_TYPE_FIRSTTIME, ""));
                }
            });
            if (!updateIpList.isEmpty()) {
                long begin = System.currentTimeMillis();
                List<List<Map<String, Object>>> partition = Lists.partition(updateIpList, 2000);
                for (List<Map<String, Object>> subList : partition) {
                    ipCollectMapper.updateInnerIpInfo(subList);
                }
                long end = System.currentTimeMillis();
                log.info("success_内网IP首次存活时间更新成功,耗时：{} 秒", (end - begin) / 1000);
            }
            if (!updateIpv6List.isEmpty()) {
                long begin = System.currentTimeMillis();
                List<List<Map<String, Object>>> partition = Lists.partition(updateIpv6List, 2000);
                for (List<Map<String, Object>> subList : partition) {
                    ipCollectMapper.updateIpv6Info(subList);
                }
                long end = System.currentTimeMillis();
                log.info("success_Ipv6首次存活时间更新成功,耗时：{} 秒", (end - begin) / 1000);
            }
        } catch (Exception e) {
            log.error("首次存活时间更新失败,{}", e.getMessage());
        }
    }

    @Override
    public <T extends BaseIpCollectEntity> BaseIpCollectEntity findDataByIpList(String ip, Class<T> clazz) {
        List<?> list = new ArrayList<>();
        BaseIpCollectEntity entity = new BaseIpCollectEntity();
        if (clazz.equals(CmdbIpAddressPoolEntity.class)) {
            list = addressPoolService.findDataByIpList(Arrays.asList(ip));
        } else if (clazz.equals(CmdbIpConfPoolEntity.class)) {
            list = confService.findDataByIpList(Arrays.asList(ip));
        } else if (clazz.equals(CmdbIpArpPoolEntity.class)) {
            list = arpService.findDataByIpList(Arrays.asList(ip));
        }
        if (!CollectionUtils.isEmpty(list)) {
            if (null != list.get(0)) {
                BeanUtils.copyProperties(list.get(0), entity);
            }
        }
        return entity;
    }

    @Override
    public void buildAndSaveIpClashList4Now() {
        // 获取当天的所有存活IP
        try {
            long begin = System.currentTimeMillis();
            List<CmdbIpCollectResponse> ipClashList4Now = ipCollectMapper.getIpClashList4Now();

            // 通过时间区间进行划分不同时间段的检测数据
            Map<String,List<CmdbIpCollectResponse>> timeKeyMap = new HashMap<>();
            for (CmdbIpCollectResponse map : ipClashList4Now) {
                String timeKey = map.getTimeKey();
                String ip2Idc = map.getIp2Idc();
                String key = ip2Idc + "-" + timeKey;
                List<CmdbIpCollectResponse> ipList = timeKeyMap.get(key);
                if (null == ipList || ipList.isEmpty()) {
                    ipList = new ArrayList<>();
                }
                ipList.add(map);
                timeKeyMap.put(key,ipList);
            }

            // 获取相同资源池和IP数多于2的存活IP
            Map<String, List<CmdbIpCollectResponse>> allIpCollect = new HashMap<>();
            for (Map.Entry<String, List<CmdbIpCollectResponse>> entry : timeKeyMap.entrySet()) {
                List<CmdbIpCollectResponse> value = entry.getValue();
                if (value.size() > 1) {
                    allIpCollect.put(entry.getKey(), value);
                }
            }
            clashService.saveIpClashList4Now(allIpCollect);
            long end = System.currentTimeMillis();
            log.info("success_自动化二期冲突IP同步,耗时：{} 秒", (end - begin) / 1000);
        } catch (Exception e) {
            log.error("自动化二期冲突IP同步异常：{}", e.getMessage());
        }
    }

    public void buildAndSaveIpClashList4Now_bak() {
        // 获取当天的所有存活IP
        try {
            int clashCount4Now = clashService.getClashMain4Now();
            if (clashCount4Now > 0) {
                log.info("今日的冲突IP已入库，总数量为：{}", clashCount4Now);
                return;
            }
            long begin = System.currentTimeMillis();
            List<CmdbIpCollectResponse> ipClashList4Now = ipCollectMapper.getIpClashList4Now();
            Map<String, List<CmdbIpCollectResponse>> tempMap = new HashMap<>();
            for (CmdbIpCollectResponse map : ipClashList4Now) {
                List<CmdbIpCollectResponse> temp = tempMap.get(map.getIp2Idc());
                if (null == temp || temp.isEmpty()) {
                    temp = new ArrayList<>();
                }
                temp.add(map);
                tempMap.put(map.getIp2Idc(), temp);
            }
            // 获取相同资源池和IP数多于2的存活IP
            Map<String, List<CmdbIpCollectResponse>> allIpCollect = new HashMap<>();
            for (Map.Entry<String, List<CmdbIpCollectResponse>> entry : tempMap.entrySet()) {
                List<CmdbIpCollectResponse> value = entry.getValue();
                if (value.size() > 1) {
                    allIpCollect.put(entry.getKey(), value);
                }
            }
            clashService.saveIpClashList4Now(allIpCollect);
            long end = System.currentTimeMillis();
            log.info("success_自动化二期冲突IP同步,耗时：{} 秒", (end - begin) / 1000);
        } catch (Exception e) {
            log.error("自动化二期冲突IP同步异常：{}", e.getMessage());
        }
    }

    @Override
    public void alterCmdbIpPart(String type) {
        Map<String, String> partMap = new HashMap<>();
        Date nowDate = new Date();
        Date nextDate = DateUtils.addDate(nowDate, 1);
        String next_time_par = DateUtils.datetimeToString(DATE_FMT_PARTNAME, nextDate);
        String next_time = DateUtils.datetimeToString(DateUtils.DEFAULT_DATE_FMT, nextDate);
        partMap.put("next_time_par", next_time_par);
        partMap.put("next_time", next_time);
        if (EventInstanceModuleEnum.IP_ADDRESS_POOL.getKey().equals(type)) {
            int count = ipCollectMapper.getCmdbIpAddressCount();
            int partCount = ipCollectMapper.getCmdbIpAddressHisCount4NowDate();
            log.info("ipAddressPool >>> 当天的分区数据量为：{}，实时存活ip的数据量为：{}", partCount, count);
            if (count > partCount) {
                String partName = ipCollectMapper.getCmdbIpHisPartName(TABLE_NAME_ADDRESS_IP_HIS, next_time_par);
                log.info("ipAddressPool >>> 查询到当天的分区名称为: {}", partName);
                if (StringUtils.isEmpty(partName)) {
                    log.info("ipAddressPool >>> begin 创建今天的分区");
                    ipCollectMapper.alterCmdbIpAddressHisPart(partMap);
                    log.info("ipAddressPool >>> end 今天的分区创建成功");
                } else {
                    log.info("ipAddressPool >>> begin 清除今天的分区数据");
                    ipCollectMapper.deletePartDataByPartName(TABLE_NAME_ADDRESS_IP_HIS, next_time_par);
                    log.info("ipAddressPool >>> end 分区数据清除成功");
                }
                log.info("ipAddressPool >>> begin 录入今天的分区数据");
                ipCollectMapper.saveCmdbIpAddressHis();
                log.info("ipAddressPool >>> end 分区数据录入成功");
                log.info("ipAddressPool >>> begin 删除实时上一天的存活ip数据");
                ipCollectMapper.deleteCmdbIpAddressPreDay();
                log.info("ipAddressPool >>> end 上一天的存活ip数据删除成功");
            }
        } else if (EventInstanceModuleEnum.IP_CONF_POOL.getKey().equals(type)) {
            int count = ipCollectMapper.getCmdbIpConfCount();
            int partCount = ipCollectMapper.getCmdbIpConfHisCount4NowDate();
            log.info("IpConfPool >>> 当天的分区数据量为：{}，实时存活ip的数据量为：{}", partCount, count);
            if (count > partCount) {
                String partName = ipCollectMapper.getCmdbIpHisPartName(TABLE_NAME_CONF_IP_HIS, next_time_par);
                log.info("IpConfPool >>> 查询到当天的分区名称为: {}", partName);
                if (StringUtils.isEmpty(partName)) {
                    log.info("IpConfPool >>> begin 创建今天的分区");
                    ipCollectMapper.alterCmdbIpConfHisPart(partMap);
                    log.info("IpConfPool >>> end 今天的分区创建成功");
                } else {
                    ipCollectMapper.deletePartDataByPartName(TABLE_NAME_CONF_IP_HIS, next_time_par);
                }
                log.info("IpConfPool >>> begin 录入今天的分区数据");
                ipCollectMapper.saveCmdbIpConfHis();
                log.info("IpConfPool >>> end 分区数据录入成功");
                log.info("IpConfPool >>> begin 删除实时上一天的存活ip数据");
                ipCollectMapper.deleteCmdbIpConfPreDay();
                log.info("IpConfPool >>> end 上一天的存活ip数据删除成功");
            }
        } else if (EventInstanceModuleEnum.IP_ARP_POOL.getKey().equals(type)) {
            int count = ipCollectMapper.getCmdbIpArpCount();
            int partCount = ipCollectMapper.getCmdbIpArpHisCount4NowDate();
            log.info("IpArpPool >>> 当天的分区数据量为：{}，实时存活ip的数据量为：{}", partCount, count);
            if (count > partCount) {
                String partName = ipCollectMapper.getCmdbIpHisPartName(TABLE_NAME_ARP_IP_HIS, next_time_par);
                log.info("IpArpPool >>> 查询到当天的分区名称为: {}", partName);
                if (StringUtils.isEmpty(partName)) {
                    log.info("IpArpPool >>> begin 创建今天的分区");
                    ipCollectMapper.alterCmdbIpArpHisPart(partMap);
                    log.info("IpArpPool >>> end 今天的分区创建成功");
                } else {
                    ipCollectMapper.deletePartDataByPartName(TABLE_NAME_ARP_IP_HIS, next_time_par);
                }
                log.info("IpArpPool >>> begin 录入今天的分区数据");
                ipCollectMapper.saveCmdbIpArpHis();
                log.info("IpArpPool >>> end 分区数据录入成功");
                log.info("IpArpPool >>> begin 删除实时上一天的存活ip数据");
                ipCollectMapper.deleteCmdbIpArpPreDay();
                log.info("IpArpPool >>> end 上一天的存活ip数据删除成功");
            }
        }
    }

    @Override
    public void updateCmdbAssetSurvialInfo() {
        log.info(">>> 开始执行CMDB资产存活更新任务 >>>>");
        try {
            long start = System.currentTimeMillis();
            List<CmdbIpInfo> autoIpInfo = ipCollectMapper.getAutoInnerIpInfo(); // 每日自动化检测的存活IP
            long end = System.currentTimeMillis();
            log.info("存活IP查询完成,耗时：{} 秒", (end - start) / 1000);

            long start1 = System.currentTimeMillis();
            List<Map<String, Object>> updateList = new ArrayList<>(); // 构建资产实体更新内容
            List<String> ip4IdcList = new ArrayList<>(); // 根据IP和资源池去重
            autoIpInfo.forEach(map -> {
                String ip4Idc = map.getIp4IdcType();
                if (!ip4IdcList.contains(ip4Idc)) {
                    ip4IdcList.add(ip4Idc);
                    updateList.add(getUpdateIpData(map, "chinese", "2"));
                }
            });
            long end1 = System.currentTimeMillis();
            log.info("资产存活信息构建完成,耗时：{} 秒", (end1 - start1) / 1000);
            if (updateList.isEmpty()) {
                return;
            }
            // 通过IP和资源池批量查询主机资源表，获取并更新资产的存活信息
            long start2 = System.currentTimeMillis();
            List<Map<String, Object>> cmdbList = new ArrayList<>();
            List<List<Map<String, Object>>> partition = Lists.partition(updateList, 1000);
            for (List<Map<String, Object>> subList : partition) {
                List<Map<String, Object>> tempList = ipCollectMapper.getCmdbInfoListByIpAndIdc(subList);
                if (null != subList || !tempList.isEmpty()) {
                    cmdbList.addAll(tempList);
                }
            }
            long end2 = System.currentTimeMillis();
            log.info("资产存活关联实例ID查询完成,数量为：{},耗时：{} 秒", cmdbList.size(), (end2 - start2) / 1000);

            long start3 = System.currentTimeMillis();
            List<List<Map<String, Object>>> cmdbPartList = Lists.partition(cmdbList, 1000);
            for (List<Map<String, Object>> list : cmdbPartList) {
                ipCollectMapper.updateCmdbListInfo(list);
            }
            long end3 = System.currentTimeMillis();
            log.info("资产的存活信息更新入库完成,耗时：{} 秒", (end3 - start3) / 1000);
            log.info("资产存活信息同步总共耗时：{} 秒", (System.currentTimeMillis() - start) / 1000);
        } catch (Exception e) {
            log.error("资产存活信息同步失败", e);
        }
    }

    @Override
    public void updateInnerIpFreeCount() {
        ipCollectMapper.updateInnerIpFreeCount();
    }

    @Override
    public void findCmdbAssetSurvial2Kafka() {
        try {
            // 全量查询资产的自动采集IP和存活状态、最近存活时间
            List<Map<String, String>> updateInfo = ipCollectMapper.findDeviceUpdateInfo();
            JSONObject header = new JSONObject();
            JSONObject params = new JSONObject();
            params.put("deviceUpList",updateInfo);
            String url = osaBaseUrl + deviceUpdateUrl;
            HttpUtil.postMethod(url, header, params);
        } catch (Exception e) {
            log.error("资产存活信息发送kafka失败", e);
        }
    }

    @Override
    public void synAllNetworkPortGroup() {
        networkPortGroupService.synAllNetworkPortGroup();
    }

    @Override
    public void updateCmdbAssetSurvival() {
        log.info(">>> 根据管理IP、业务IP1、业务IP2、consoleIP更新存活状态 >>>>");
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<Map<String, String>> resultList = ipCollectMapper.findDeviceAssetSurvivalList();
        if (CollectionUtils.isEmpty(resultList)) {
            log.warn("匹配到的资产存活列表为空!!!");
            return;
        }
        stopwatch.stop();
        log.info("获取待更新的资产存活列表耗时:[{}]ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset().start();
        log.warn("待更新的资产存活列表:[{}]", resultList.size());
        Map<String, List<String>> paramMap = Maps.newHashMap();
        for (Map<String, String> map : resultList) {
            String instanceId = map.get("id");
            String latestSurvivalTime = map.get("latestSurvivalTime");
            if (org.apache.commons.lang3.StringUtils.isBlank(latestSurvivalTime)) {
                continue;
            }
            if (paramMap.get(latestSurvivalTime) == null) {
                List<String> instanceIdList = Lists.newArrayList();
                instanceIdList.add(instanceId);
                paramMap.put(latestSurvivalTime, instanceIdList);
            } else {
                paramMap.get(latestSurvivalTime).add(instanceId);
            }
        }
        if (MapUtils.isEmpty(paramMap)) {
            log.warn("按存活时间分组的列表为空!!!");
            return;
        }
        List<Map<String, Object>> suvivalList = Lists.newArrayList();
        for (Map.Entry<String, List<String>> entry : paramMap.entrySet()) {
            String latestSurvivalTime = entry.getKey();
            List<String> instanceIdList = entry.getValue();
            List<List<String>> partitionList = Lists.partition(instanceIdList, 200);
            for (List<String> idList : partitionList) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("latestSurvivalTime", latestSurvivalTime);
                map.put("survivalStatus", "已存活");
                map.put("instanceIdList", idList);
                suvivalList.add(map);
                ipCollectMapper.updateDeviceAsset2Survival(map);
            }
        }
        log.info("更新资产的存活状态为“已存活”,耗时:[{}]ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset().start();
        List<CmdbSurvivalStatusMonitor> survivalStatusMonitorList = Lists.newArrayList();
        for (Map<String, Object> map : suvivalList) {
            String latestSurvivalTime = map.get("latestSurvivalTime").toString();
            String survivalStatus = map.get("survivalStatus").toString();
            List<String> instanceIdList = (List<String>) map.get("instanceIdList");
            for (String instanceId : instanceIdList) {
                CmdbSurvivalStatusMonitor monitor = new CmdbSurvivalStatusMonitor();
                monitor.setId(UUIDUtil.getUUID());
                monitor.setSurvivalStatus(survivalStatus);
                monitor.setLatestSurvivalTime(DateUtils.parseDate(latestSurvivalTime, "yyyy-MM-dd HH:mm:ss"));
                monitor.setInstanceId(instanceId);
                survivalStatusMonitorList.add(monitor);
            }
        }
        List<List<CmdbSurvivalStatusMonitor>> partition2 = Lists.partition(survivalStatusMonitorList, 200);
        for (List<CmdbSurvivalStatusMonitor> subList : partition2) {
            ipCollectMapper.saveSurvivalMonitor(subList);
        }
        log.info("保存“已存活”到存活状态检测表,耗时:[{}]ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        log.info("<<< 根据管理IP、业务IP1、业务IP2、consoleIP更新存活状态 完成 <<<");

    }

    @Override
    public void updateCmdbAsset2UnSurvival() {
        log.info(">>> 开始执行[cmdb资产未存活状态更新]任务 >>>>");
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<String> instanceIdList = ipCollectMapper.findDeviceAssetUnSurvivalList();
        log.warn("待更新的未存活的资产列表:[{}]", instanceIdList.size());
        if (CollectionUtils.isEmpty(instanceIdList)) {
            return;
        }
        stopwatch.stop();
        log.info("获取待更新的未存活的资产列表耗时:[{}]ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset().start();
        List<List<String>> partition = Lists.partition(instanceIdList, 200);
        for (List<String> subIdList : partition) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("instanceIdList", subIdList);
            params.put("survivalStatus", "未存活");
            ipCollectMapper.updateSurvivalStatus(params);
        }
        log.info("更新资产的存活状态为“未存活”,耗时:[{}]ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset().start();
        List<CmdbSurvivalStatusMonitor> survivalStatusMonitorList = Lists.newArrayList();
        for (String instanceId : instanceIdList) {
            CmdbSurvivalStatusMonitor monitor = new CmdbSurvivalStatusMonitor();
            monitor.setId(UUIDUtil.getUUID());
            monitor.setInstanceId(instanceId);
            monitor.setSurvivalStatus("未存活");
            survivalStatusMonitorList.add(monitor);
        }
        List<List<CmdbSurvivalStatusMonitor>> partition2 = Lists.partition(survivalStatusMonitorList, 200);
        for (List<CmdbSurvivalStatusMonitor> subList : partition2) {
            ipCollectMapper.saveSurvivalMonitor(subList);
        }
        stopwatch.stop();
        log.info("保存“未存活”到存活状态检测表,耗时:[{}]ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        log.info("<<< 执行[cmdb资产未存活状态更新]任务 完成 <<<");
    }

    @Override
    public void reportCmdbAutoStatus() {
        log.info(">>> 开始执行[自动化采集异常设备报表]任务 >>>>");
        Stopwatch stopwatch = Stopwatch.createStarted();
        ipCollectMapper.saveHostServerAutoStatusReport();
        stopwatch.stop();
        log.info("初始化cmdb资产自动化检测报表数据,耗时:[{}]ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset().start();
        ipCollectMapper.updateHostServerAutoStatusReport();
        stopwatch.stop();
        log.info("更新cmdb资产自动化检测报表数据,耗时:[{}]ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        log.info("<<< 执行[自动化采集异常设备报表]任务 完成 <<<");
    }

    @Override
    public void synAllInnerIpSurvival() {
        String statusId = configDictMapper.getIdByNoteAndCol("未存活", "survival_status");
        ipCollectMapper.updateAllInnerIpSurvival(statusId);
    }

    @Override
    public void deleteCmdbIpAddressNowDay(String nowDay) {
        ipCollectMapper.deleteCmdbIpAddressNowDay(nowDay);
    }

    @Override
    public void deleteCmdbIpConfNowDay(String nowDay) {
        ipCollectMapper.deleteCmdbIpConfNowDay(nowDay);
    }

    @Override
    public void deleteCmdbIpArpNowDay(String nowDay) {
        ipCollectMapper.deleteCmdbIpArpNowDay(nowDay);
    }

    @Override
    public void updateAssetOtherIp() {
        // 获取资产和配置信息的比对IP
        List<Map<String, String>> assetOtherIp = ipCollectMapper.findAssetOtherIp();
        List<Map<String,Object>> updateList = new ArrayList<>();
        for (Map<String, String> map : assetOtherIp) {
            Map<String,Object> updateMap = new HashMap<>();
            Set<String> otherIpSet = new HashSet<>();
            String otherIp = map.get("otherIp");
            String manageIp = map.get("manageIp");
            String ywIp1 = map.get("ywIp1");
            String ywIp2 = map.get("ywIp2");
            String consoleIp = map.get("consoleIp");
            if (StringUtils.isNotEmpty(otherIp)) {
                String[] split = otherIp.split(",");
                for (String ip : split) {
                    // 比对配置信息的网卡IP是否存在于资产的IP中，不存在则作为该资产的其他IP录入
                    if (StringUtils.isEmpty(ip)) {
                        continue;
                    }
                    if (ip.equals(manageIp)) { // 1.判断是否等同于管理IP
                        continue;
                    } else {
                        if (StringUtils.isNotEmpty(ywIp1) && ip.equals(ywIp1)) { // 2.判断是否等同于业务IP1
                            continue;
                        } else {
                            if (StringUtils.isNotEmpty(ywIp2) && ip.equals(ywIp2)) { // 3.判断是否等同于业务IP2
                                continue;
                            } else {
                                if (StringUtils.isNotEmpty(consoleIp) && ip.equals(consoleIp)) { // 4.判断是否等同于consoleIp
                                    continue;
                                } else {
                                    otherIpSet.add(ip);
                                }
                            }
                        }
                    }
                }
                String join = org.apache.commons.lang.StringUtils.join(otherIpSet, ";");
                if (StringUtils.isNotEmpty(join)) {
                    updateMap.put("instanceId",map.get("assetId"));
                    updateMap.put("otherIp", join);
                    updateList.add(updateMap);
                }
            }
        }
        List<List<Map<String, Object>>> cmdbPartList = Lists.partition(updateList, 200);
        for (List<Map<String, Object>> list : cmdbPartList) {
            ipCollectMapper.updateCmdbListInfo(list);
        }
    }

    @Override
    public void updateBaseCollectIp() {
        log.info("开始进行存活IP更新IP管理的状态");
        long start = System.currentTimeMillis();
        List<BaseIpCollectEntity> ipList = ipCollectMapper.findBaseIpCollectBaseEntity();
        long end = System.currentTimeMillis();
        log.info("存活IP查询完成,耗时：{} ms", (end - start));
        if (!ipList.isEmpty()) {
            List<List<BaseIpCollectEntity>> partition = Lists.partition(ipList, 500);
            for (List<BaseIpCollectEntity> list : partition) {
                long start1 = System.currentTimeMillis();
                updateInnerIpInfo(list, null, OP_TYPE_ADD);
                log.info("IP管理单次更新耗时：{} ms", (System.currentTimeMillis() - start1));
            }
        }
        log.info("IP管理状态更新总耗时：{} 秒", (System.currentTimeMillis() - start) / 1000);
    }

    @Override
    public void initCarrierNetworkForAsset() {
        try {
            log.info("开始执行[初始化资产表的承载网IP] >>>>>>>>>");
            long start = System.currentTimeMillis();

            // 情况1：内网地址库的表与资产表通过“ip(管理ip)”关联，若资产表的承载网IP不存在，则追加承载网IP
            List<Map<String, String>> ipList = ipCollectMapper.findCarrierNetworkWithInnerNeworkIpLibrary("ip");
            final List<Map<String, String>> filterList = new ArrayList<>();
            long updateSize = dealAndUpdateCarrierNetwork(ipList, filterList);  // 数据匹配处理后更新承载网IP
            long end1 = System.currentTimeMillis();
            log.info(String.format("执行[初始化资产表的承载网IP]关联(管理ip)完成,共更新%s条数据，耗时：%s秒", updateSize, (end1 - start) / 1000));
            filterList.clear();

            // 情况2：内网地址库的表与资产表通过"business_ip1"关联，若资产表的承载网IP不存在，则追加承载网IP
            List<Map<String, String>> businessIp1List = ipCollectMapper.findCarrierNetworkWithInnerNeworkIpLibrary("business_ip1");
            updateSize = dealAndUpdateCarrierNetwork(businessIp1List, filterList);
            long end2 = System.currentTimeMillis();
            log.info(String.format("执行[初始化资产表的承载网IP]关联(业务ip1)完成,共更新%s条数据，耗时：%s秒", updateSize, (end2 - end1) / 1000));
            filterList.clear();

            // 情况3：内网地址库的表与资产表通过“business_ip2”关联，若资产表的承载网IP不存在，则追加承载网IP
            List<Map<String, String>> businessIp2List = ipCollectMapper.findCarrierNetworkWithInnerNeworkIpLibrary("business_ip2");
            updateSize = dealAndUpdateCarrierNetwork(businessIp2List, filterList);
            long end3 = System.currentTimeMillis();
            log.info(String.format("执行[初始化资产表的承载网IP]关联(业务ip2)完成,共更新%s条数据，耗时：%s秒", updateSize, (end3 - end2) / 1000));
            filterList.clear();

            // 情况4：内网地址库的表与资产表通过“other_ip”关联，若资产表的承载网IP不存在，则追加承载网IP
            List<String> carrierNetworkIpList = ipCollectMapper.findCarrierNetworkByInnerNeworkIpLibrary();
            List<Map<String, String>> otherIpList = new ArrayList<>();
            List<List<String>> subCarrierNetworkIpList = Lists.partition(carrierNetworkIpList, 100);
            for (List<String> list : subCarrierNetworkIpList) {
                List<Map<String, String>> tmpList = ipCollectMapper.findCarrierNetworkWithOtherIP(list);
                otherIpList.addAll(tmpList);
            }
            updateSize = dealAndUpdateCarrierNetwork(otherIpList, filterList);
            long end4 = System.currentTimeMillis();
            log.info(String.format("执行[初始化资产表的承载网IP]关联(其它ip地址)完成,共更新%s条数据，耗时：%s秒", updateSize, (end4 - end3)  / 1000));
            filterList.clear();

            // 情况5：内网地址库的表与资产表通过关联cmdb_instance_server_ip的“other_ip”，若资产表的承载网IP不存在，则追加承载网IP
            List<Map<String, String>> otherIpRelatedList = ipCollectMapper.findCarrierNetworkWithInnerNeworkIpLibraryRelated("related_server_ip");
            updateSize = dealAndUpdateCarrierNetwork(otherIpRelatedList, filterList);
            long end5 = System.currentTimeMillis();
            log.info(String.format("执行[初始化资产表的承载网IP]关联cmdb_instance_server_ip.other_ip完成,共更新%s条数据，耗时：%s秒", updateSize, (end5 - end4) / 1000));
            filterList.clear();

            // 情况6：内网地址库的表与资产表通过关联 cmdb_instance_console 的“console_ip”，若资产表的承载网IP不存在，则追加承载网IP
            List<Map<String, String>> consoleIpRelatedList = ipCollectMapper.findCarrierNetworkWithInnerNeworkIpLibraryRelated("related_console");
            updateSize = dealAndUpdateCarrierNetwork(consoleIpRelatedList, filterList);
            log.info(String.format("执行[初始化资产表的承载网IP]关联cmdb_instance_console.console_ip完成,共更新%s条数据，耗时：%s秒", updateSize, (System.currentTimeMillis() - end5) / 1000));
            filterList.clear();

            log.info("执行[初始化资产表的承载网IP]完成,耗时：{}秒", (System.currentTimeMillis() - start) / 1000);
        } catch (Exception e) {
            log.error("初始化资产表的承载网IP异常：{}", e);
        }
    }

    private long dealAndUpdateCarrierNetwork(List<Map<String, String>> list, List<Map<String, String>> filterList){
        if (list.size() > 0) {
            HashMap<String, String> filterMap = new HashMap<>();
            list.forEach(map -> {
                if (!"".equals(map.get("inner_ip")) && !Arrays.asList(map.get("carrier_network").trim().split(",")).contains(map.get("inner_ip").trim())) {
                    String carrierNetwork_new = "";
                    if (filterMap.get(map.get("device_id")) != null) {
                        if (!Arrays.asList(filterMap.get(map.get("device_id")).split(",")).contains(map.get("inner_ip"))) {
                            carrierNetwork_new = !"".equals(filterMap.get(map.get("device_id"))) ?
                                    new StringBuilder(filterMap.get(map.get("device_id"))).append(",").append(map.get("inner_ip")).toString() :
                                    map.get("inner_ip");  // inner_ip为内网地址库的承载网IP
                            filterMap.put(map.get("device_id"), carrierNetwork_new);
                        }
                    } else {
                        carrierNetwork_new = !"".equals(map.get("carrier_network").trim()) ?
                                new StringBuilder(map.get("carrier_network")).append(",").append(map.get("inner_ip")).toString() :
                                map.get("inner_ip");
                        filterMap.put(map.get("device_id"), carrierNetwork_new);
                    }
                }
            });

            for (Map.Entry<String, String> e : filterMap.entrySet()) {
                Map<String, String> m = new HashMap<>();
                m.put("id", e.getKey());
                m.put("carrierNetwork", e.getValue());
                filterList.add(m);
            }

            if (filterList.size() > 0) {
                List<List<Map<String, String>>> partition = Lists.partition(filterList, 200);
                for (List<Map<String, String>> subList : partition) {
                    int size = ipCollectMapper.updateCarrierNetworkForAsset(subList);
                }
            }
            return filterList.size();  //更新承载网IP字段的条数
        }
        return 0;
    }



}
