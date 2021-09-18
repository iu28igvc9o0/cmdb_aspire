package com.aspire.ums.cmdb.cmic.service.impl;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.aspire.ums.cmdb.cmic.mapper.CmdbCmicInstanceMapper;
import com.aspire.ums.cmdb.cmic.service.ICmdbCmicInstanceService;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.util.DateUtilsNew;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司 FileName: CmdbCmicInstanceServiceImpl Author: zhu.juwang Date: 2020/6/1 16:04 Description:
 * ${DESCRIPTION} History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Service
@Slf4j
public class CmdbCmicInstanceServiceImpl implements ICmdbCmicInstanceService {

    @Autowired
    private ICmdbInstanceService instanceService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ICmdbConfigService configService;

    @Autowired
    private CmdbCmicInstanceMapper cmdbCmicInstanceMapper;

    @Override
    public Map<String, Object> updateIpInfo(String userName, Map<String, Object> ipData, String operatorType) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Map<String, Object> returnMap = new HashMap<>();
        String moduleId = ipData.get("module_id") == null ? null : ipData.get("module_id").toString();
        String instanceIds = "";
        Map<String, Object> updateMap = Maps.newHashMap();
        if (ipData.containsKey("instance_id") && StringUtils.isNotEmpty(ipData.get("instance_id"))) {
            instanceIds = ipData.get("instance_id").toString();
        } else {
            if (!ipData.containsKey("ip_type") && !StringUtils.isNotEmpty(ipData.get("ip_type"))) {
                returnMap.put("flag", "error");
                returnMap.put("msg", "Parameter ip_type is require.");
                return returnMap;
            }
            if (!ipData.containsKey("ip") && !StringUtils.isNotEmpty(ipData.get("ip"))) {
                returnMap.put("flag", "error");
                returnMap.put("msg", "Parameter ip is require.");
                return returnMap;
            }
            if (!ipData.containsKey("idcType") && !StringUtils.isNotEmpty(ipData.get("idcType"))) {
                returnMap.put("flag", "error");
                returnMap.put("msg", "Parameter idcType is require.");
                return returnMap;
            }
            String ipType = ipData.get("ip_type").toString();

            if (ipType.equalsIgnoreCase("ipv4")) {
                moduleId = configService
                        .getConfigByCode("inner_ip_module_id",
                                new RuntimeException("Missing cmdb_config [config_code=inner_ip_module_id] record."))
                        .getConfigValue();
            } else if (ipType.equalsIgnoreCase("ipv6")) {
                moduleId = configService
                        .getConfigByCode("ipv6_ip_module_id",
                                new RuntimeException("Missing cmdb_config [config_code=ipv6_ip_module_id] record."))
                        .getConfigValue();
            } else {
                returnMap.put("flag", "error");
                returnMap.put("msg", "Don't support ip type " + ipType + ".");
                return returnMap;
            }
            if (StringUtils.isEmpty(moduleId)) {
                returnMap.put("flag", "error");
                returnMap.put("msg", "Resolve module_id error. Module_id can't be null.");
                return returnMap;
            }
            Map<String, Object> data = moduleService.getModuleDataByPrimarys(moduleId, ipData);
            if (data != null) {
                instanceIds = data.get("id").toString();
            } else {
                returnMap.put("flag", "error");
                returnMap.put("msg", "Can't find ip record.");
                return returnMap;
            }
        }
        Module module = moduleService.getModuleDetail(moduleId);
        String tablename = module.getModuleCatalog().getCatalogCode();
        // updateMap.put("tableName", tablename);
        // 根据模型修改太慢，这里先写死
        if (StringUtils.isNotEmpty(instanceIds)) {
            List<Future<String>> futureList = Lists.newArrayList();
            ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
            List<String> instanceIdList = Splitter.on(",").splitToList(instanceIds);
            // CountDownLatch countDownLatch = new CountDownLatch(instanceIdList.size());
            // updateMap.put("instanceIdList", instanceIdList);
            // for (Map.Entry<String, Object> entry : ipData.entrySet()) {
            // if (ImmutableList.of("module_id", "instance_id", "operator").contains(entry.getKey())) {
            // continue;
            // }
            // updateMap.put(entry.getKey(), entry.getValue());
            // }
            //
            // cmdbCmicInstanceMapper.updateAssignStatus(tablename, instanceIdList, updateMap);

            for (String instanceId : instanceIdList) {
                InstanceUpdateTask task = new InstanceUpdateTask(instanceId, userName, ipData, operatorType, instanceService);
                futureList.add(cachedThreadPool.submit(task));
                // futureList.add(EventThreadUtils.INSTANCE_POOL.submit(task));
            }
            for (Future<String> future : futureList) {
                try {
                    future.get(5, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            cachedThreadPool.shutdown();
        }
        stopWatch.stop();
        log.info("修改IP地址分配状态耗时:{}ms", stopWatch.getTotalTimeMillis());
        returnMap.put("flag", "success");
        return returnMap;
    }

    @Override
    public void batchInsertIpRepository(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertIpRepository(list);
    }

    @Override
    public void batchInsertInnerRepository(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertInnerRepository(list);
    }

    @Override
    public void batchInsertInnerSegment(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertInnerSegment(list);
    }

    @Override
    public void batchInsertInnerIp(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertInnerIp(list);
    }

    @Override
    public void batchInsertOperateLog(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertOperateLog(list);
    }

    @Override
    public void batchInsertPublicRepository(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertPublicRepository(list);
    }

    @Override
    public void batchInsertPublicSegment(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertPublicSegment(list);
    }

    @Override
    public void batchInsertPublicIp(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertPublicIp(list);
    }

    @Override
    public void batchInsertIPV6Repository(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertIPV6Repository(list);
    }

    @Override
    public void batchInsertIPV6Segment(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertIPV6Segment(list);
    }

    @Override
    public void batchInsertIPV6Ip(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertIPV6Ip(list);
    }

    @Override
    public void updateAssignStatus4InnerIp(Map<String, String> params) {
        cmdbCmicInstanceMapper.updateAssignStatus4InnerIp(params);
    }

    private static class InstanceUpdateTask implements Callable<String> {

        private String instanceId;

        private String userName;

        private Map<String, Object> ipData;

        private String operatorType;

        private ICmdbInstanceService instanceService;

        public InstanceUpdateTask(String instanceId, String userName, Map<String, Object> ipData, String operatorType,
                ICmdbInstanceService instanceService) {
            this.instanceId = instanceId;
            this.userName = userName;
            this.ipData = ipData;
            this.operatorType = operatorType;
            this.instanceService = instanceService;
        }

        @Override
        public String call() throws Exception {
            return instanceService.updateInstance(instanceId, userName, ipData, operatorType);
        }

    }

    @Override
    public void batchInsertServerProject(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertServerProject(list);
    }

    @Override
    public void batchInsertServerCabinet(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertServerCabinet(list);
    }

    @Override
    public void batchInsertServerCabinetRecord(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertServerCabinetRecord(list);
    }

    @Override
    public void batchInsertNetworkLineMgrList(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertNetworkLineMgr(list);
    }

    @Override
    public void batchInsertNetworkLineRecord(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertNetworkLineRecord(list);
    }

    @Override
    public void updateServerCabinet(String optType, Map<String, Object> params) {
        if ("decr".equals(optType)) {
            cmdbCmicInstanceMapper.decrServerCabinet(params);
        } else if ("incr".equals(optType)) {
            cmdbCmicInstanceMapper.incrServerCabinet(params);
        }
    }

    @Override
    public void genServerCabinetBillMonthly() {
        List<Map<String, String>> resultMap = cmdbCmicInstanceMapper.selectServerCabinet4Bill(Maps.newHashMap());
        for (Map<String, String> map : resultMap) {
            String id = map.get("id");
            // 机柜的上电时间
            String serverBillStartDate = map.get("bill_start_date");
            LocalDate serverBillStartLocalDate = DateUtilsNew.parseStr2LocalDate(serverBillStartDate);
            // 机柜的下架时间
            String serverOfflineDate = map.get("offline_date");
            LocalDate serverOfflineLocalDate = DateUtilsNew.parseStr2LocalDate(serverOfflineDate);

            // 结算配置的计费开始日期
            String billStartDate = map.get("billing_start_date");
            LocalDate billStartLocalDate = DateUtilsNew.parseStr2LocalDate(billStartDate);
            // 结算配置的计费截至日期
            String billEndTime = map.get("bill_end_time");
            LocalDate billEndLocalDate = DateUtilsNew.parseStr2LocalDate(billEndTime);

            LocalDate date = LocalDate.now();
            LocalDate previousMonthDay = date.minusMonths(1);
            int days = previousMonthDay.lengthOfMonth();

            // 本月的第一天
            LocalDate firstday = LocalDate.of(previousMonthDay.getYear(), previousMonthDay.getMonth(), 1);
            // 本月的最后一天
            LocalDate lastDay = previousMonthDay.with(TemporalAdjusters.lastDayOfMonth());
            if (firstday.isBefore(serverBillStartLocalDate)) {
            
            }
        }
    }

    @Override
    public void batchInsertNetworkLineHis(List<Map<String, Object>> list) {
        cmdbCmicInstanceMapper.batchInsertNetworkLineHis(list);
    }

    @Override
    public void batchInsertNetworkLineBill(List<Map<String, Object>> list) {

    }
}
