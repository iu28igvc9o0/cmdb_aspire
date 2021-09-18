package com.aspire.mirror.indexproxy.zabbixdatasync;

import com.aspire.mirror.indexproxy.config.ProxyIdentityConfig;
import com.aspire.mirror.indexproxy.domain.*;
import com.aspire.mirror.indexproxy.helper.DistributeLockHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * zabbix模板数据同步管理域
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.indexproxy.zabbixdatasync
 * 类名称:    ZabbixTemplateDataSyncJobRunner
 * 类描述:    zabbix模板数据同步管理域
 * 创建人:    JinSu
 * 创建时间:  2020/10/21 20:33
 * 版本:      v1.0
 */
@Component
@Slf4j
public class ZabbixTemplateDataSyncJobRunner {
//    @Autowired
//    private ZabbixTemplateDataSync dataListSync;

    private ScheduledThreadPoolExecutor executor;

    @Autowired
    private ZabbixItemCollectBiz zabbixItemCollectBiz;

    @Autowired
    private ProxyIdentityConfig proxyIdentityConfig;

//    @Value("${templateDataSync.interval}")
//    private Integer						interval;

    @Autowired
    private TemplateDataListPublisher templateDataListPublisher;

    @Autowired
    private DistributeLockHelper distributeLockHelper;

    @Autowired(required = false)
    private RedissonClient redissonClient;

    private ThreadPoolExecutor pool;

    @Value("${zabbixItemSync.threadCorePoolNum:4}")
    private Integer threadCorePoolNum;
    @Value("${zabbixItemSync.threadMaxPoolNum:8}")
    private Integer threadMaxPoolNum;
    @Value("${zabbixItemSync.threadKeepAliveTime:20}")
    private Integer threadKeepAliveTime;

//    @XxlJob("TemplateSyncJobHandler")
//    public ReturnT<String> templateSync(String param) throws Exception {
//        XxlJobLogger.log("XXL-JOB, start sync zabbix template data.");
//        List<ZabbixTemplateDetailResponse> tempList = zabbixItemCollectBiz.loadZbxTemplateList();
//        List<MonitorTemplateRecord> templateRecords = Lists.newArrayList();
//        for (int i = 0; i < tempList.size(); i++) {
//            ZabbixTemplateDetailResponse zabbixTemplateDetailResponse = tempList.get(i);
//            MonitorTemplateRecord monitorTemplateRecord = zabbixTemplateDetailResponse.parseTemplateData();
//            monitorTemplateRecord.setIndexType(i == 0 ? "START" : i == tempList.size() - 1 ? "END" : "MID");
//            templateRecords.add(monitorTemplateRecord);
//        }
//        //直接同步到template服务
//        templateDataListPublisher.publishStandardIndexDataList(templateRecords, proxyIdentityConfig.getId());
//        return ReturnT.SUCCESS;
//    }
//
//    @XxlJob("HostSyncJobHandler")
//    public ReturnT<String> hostSync(String param) throws Exception {
//        XxlJobLogger.log("XXL-JOB, start sync zabbix host data.");
//        List<MonitorHost> hostList = zabbixItemCollectBiz.loadZbxHostList();
//        //直接同步到template服务
//        templateDataListPublisher.publishHostDataList(hostList, proxyIdentityConfig.getId());
//        return ReturnT.SUCCESS;
//    }
//
    //    @Scheduled(cron = "0 */3 * * * ?")
    @XxlJob("iemJobHandler")
    public ReturnT<String> discoverItemJobHandler(String param) throws Exception {
        XxlJobLogger.log("XXL-JOB, start regenerate relation of item and item prototype.");
//        List<MonitorItemDetailResponse> discoverItemList = zabbixItemCollectBiz.queryZbxDiscoverItem();
//        for (MonitorItemDetailResponse monitorItemDetailResponse : discoverItemList) {
//            if (monitorItemDetailResponse != null && monitorItemDetailResponse.getItemDiscovery() != null) {
//                monitorItemDetailResponse.setTemplateId(monitorItemDetailResponse.getItemDiscovery().get("parent_itemid"));
//            }
//        }
        if (StringUtils.isEmpty(param)) {
            List<MonitorHost> hostList = zabbixItemCollectBiz.loadZbxHostList(false);
            List<String> hostidList = hostList.stream().map(MonitorHost::getHostid).collect(Collectors.toList());
            syncItemRelMap(hostidList);
        } else {
            List<String> hostidList = Arrays.asList(param.split(","));
            syncItemRelMap(hostidList);
        }
//        templateDataListPublisher.publishItemData(discoverItemList, proxyIdentityConfig.getId());
        return ReturnT.SUCCESS;
    }
    @XxlJob("itemPrototypeInstJobHandler")
    public ReturnT<String> itemPrototypeInstSync(String param) throws Exception {
        XxlJobLogger.log("XXL-JOB, start regenerate relation of item prototype.");
        List<String> hostidList = Arrays.asList(param.split(","));
        List<MonitorItemDetailResponse> itemList1 = zabbixItemCollectBiz.loadZbxItemPrototypeByHostid(hostidList, false);
        if (!CollectionUtils.isEmpty(itemList1)) {
            zabbixItemAndPrototypeRelationSync(itemList1);
        }
        return ReturnT.SUCCESS;
    }
    @XxlJob("ZabbixDataSyncJobHandler")
    public ReturnT<String> zabbixInfoSync(String param) throws Exception {
//        Lock lock = distributeLockHelper.getLock(proxyIdentityConfig.getId() + "_zabbix_info_sync");
        String syncLockName = proxyIdentityConfig.getId() + "_sync";
        final Lock syncLock = distributeLockHelper.getReadWriteLock(syncLockName).readLock();
        try {
            boolean flagLock = syncLock.tryLock(5, TimeUnit.SECONDS);
            if (!flagLock) {
                log.warn("ZabbixDataSyncJobHandler get lock {} is error!", syncLockName);
                return ReturnT.FAIL;
            }
            XxlJobLogger.log("XXL-JOB, start sync zabbix data.{}", System.currentTimeMillis());
            // 同步设备
            XxlJobLogger.log("XXL-JOB, first, start sync zabbix host data.{}", System.currentTimeMillis());
            List<MonitorHost> hostList = zabbixItemCollectBiz.loadZbxHostList(false);
            templateDataListPublisher.publishHostDataList(hostList, proxyIdentityConfig.getId());
            //同步模板

            XxlJobLogger.log("XXL-JOB, second, start sync zabbix template data.{}", System.currentTimeMillis());
            List<ZabbixTemplateDetailResponse> tempList = zabbixItemCollectBiz.loadZbxTemplateList(false);
            List<MonitorTemplateRecord> templateRecords = Lists.newArrayList();
            List<String> itemPrototypeIdList = Lists.newArrayList();

            for (int i = 0; i < tempList.size(); i++) {
                ZabbixTemplateDetailResponse zabbixTemplateDetailResponse = tempList.get(i);
                List<MonitorItemDetailResponse> itemPrototypeList = zabbixItemCollectBiz.loadZbxItemPrototypeByHostid(zabbixTemplateDetailResponse.getTemplateId(), false);
                if (!CollectionUtils.isEmpty(itemPrototypeList)) {
                    if (CollectionUtils.isEmpty(zabbixTemplateDetailResponse.getItems())) {
                        zabbixTemplateDetailResponse.setItems(Lists.newArrayList());
                    }
                    zabbixTemplateDetailResponse.getItems().addAll(itemPrototypeList);
                }
                if (!CollectionUtils.isEmpty(zabbixTemplateDetailResponse.getDiscoveries())) {
                    if (CollectionUtils.isEmpty(zabbixTemplateDetailResponse.getItems())) {
                        zabbixTemplateDetailResponse.setItems(Lists.newArrayList());
                    }
                    zabbixTemplateDetailResponse.getItems().addAll(zabbixTemplateDetailResponse.getDiscoveries());
                }
                if (!CollectionUtils.isEmpty(zabbixTemplateDetailResponse.getItems())) {
                    List<String> itemList = zabbixTemplateDetailResponse.getItems().stream().map(MonitorItemDetailResponse::getItemId).collect(Collectors.toList());
                    itemPrototypeIdList.addAll(itemList);
                }
                MonitorTemplateRecord monitorTemplateRecord = zabbixTemplateDetailResponse.parseTemplateData();
                monitorTemplateRecord.setIndexType(i == 0 ? "START" : i == tempList.size() - 1 ? "END" : "MID");
                templateRecords.add(monitorTemplateRecord);
            }
            templateDataListPublisher.publishStandardIndexDataList(templateRecords, proxyIdentityConfig.getId());


            // 同步指标与指标模型关系
//            XxlJobLogger.log("XXL-JOB, thrid, start regenerate relation of item and item prototype.{}", System.currentTimeMillis());
//            List<String> hostidList = hostList.stream().map(MonitorHost::getHostid).collect(Collectors.toList());
//            syncItemRelMap(hostidList);

//            }
//            }
            // 自动发现指标
//            List<MonitorItemDetailResponse> discoverItemList = zabbixItemCollectBiz.queryZbxDiscoverItem();
//            for (MonitorItemDetailResponse monitorItemDetailResponse : discoverItemList) {
//                if (monitorItemDetailResponse != null && monitorItemDetailResponse.getItemDiscovery() != null) {
//                    monitorItemDetailResponse.setTemplateId(monitorItemDetailResponse.getItemDiscovery().get("parent_itemid"));
//                }
//            }
//            if (!CollectionUtils.isEmpty(discoverItemList)) {
//                itemList.addAll(discoverItemList);
//            }
//            templateDataListPublisher.publishItemData(itemList, proxyIdentityConfig.getId());
            XxlJobLogger.log("XXL-JOB, end sync zabbix data.{}", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("sync zabbix template data to template service is error!", e);
        } finally {
            syncLock.unlock();
        }
        return ReturnT.SUCCESS;
    }
    @Value("${zabbixItemSync.partitionSize:50}")
    private Integer itemPartitionSize;

    private void syncItemRelMap(List<String> hostidList) throws InterruptedException {

//            if (!CollectionUtils.isEmpty(itemPrototypeIdList)) {
        TemplateItemListRequest templateItemListRequest = new TemplateItemListRequest();

        List<List<String>> partitionList = Lists.partition(hostidList, itemPartitionSize);
//        final CountDownLatch latch = new CountDownLatch(partitionList.size());
//            for (int i = 0; i < partitionList.size(); i++) {
//                log.info("begin get item, partition {}", i);
//                itemParam.put(TemplateItemListRequest.PARAM_TEMPLATE_ID, itemPrototypeIdList);

        // 普通指标
        partitionList.forEach(list -> {
//            pool.submit(() -> {
                try {
                    log.info("begin get item prototype");
                    List<MonitorItemDetailResponse> itemList1 = zabbixItemCollectBiz.loadZbxItemPrototypeByHostid(list, false);
                    if (!CollectionUtils.isEmpty(itemList1)) {
                        zabbixItemAndPrototypeRelationSync(itemList1);
                    }
                    log.info("begin get item");
                    Map<String, Object> itemParam = Maps.newHashMap();
                    itemParam.put(TemplateItemListRequest.PARAM_HOST_ID, list);
                    templateItemListRequest.setParams(itemParam);
                    List<MonitorItemDetailResponse> itemList2 = zabbixItemCollectBiz.queryZbxTemplateItemList(templateItemListRequest,  false);
                    log.info("=====get item size is {}", itemList2.size());
                    if (!CollectionUtils.isEmpty(itemList2)) {
                        itemList2.stream().filter(item -> item != null && item.getTemplateId().equals("0")).forEach(item -> {
                            if (null != item.getItemDiscovery()) {
                                if (item.getItemDiscovery().getClass().equals(LinkedHashMap.class)) {
                                    Map<String, String> map = (Map<String, String>) item.getItemDiscovery();
                                    item.setTemplateId(map.get("parent_itemid"));
                                }
                            }
                            if (item.getTemplateId().equals("0")) {
                                log.warn("template id is 0 ,itemid is {}", item.getItemId());
                            }
                        });
                        zabbixItemAndPrototypeRelationSync(itemList2);
                    }
                } catch (Exception e) {
                    log.error("get zabbix item is error!", e);
                } finally {
//                    latch.countDown();
//                    log.debug("item sync latch count is {}", latch.getCount());
//                    if (latch.getCount() == 0) {
//                        try {
//                            zabbixItemCollectBiz.logoutZabbix();
//                        } catch (Exception e) {
//                            log.error("zabbix logout is error!", e);
//                        }
//                    }
                }
            });
//        });
//        latch.await();
    }

//    private void initExecutors() {
//        pool = new ThreadPoolExecutor(threadCorePoolNum, threadMaxPoolNum, threadKeepAliveTime,
//                TimeUnit.MINUTES, new ArrayBlockingQueue<>(8));
//    }
//
//    @PostConstruct
//    private void init() {
//        initExecutors();
//    }

    private static final String ITEM_PROTOTYPE_REL_PREFIX = "item_prototype_rel_";

    public void zabbixItemAndPrototypeRelationSync(List<MonitorItemDetailResponse> itemList2) {
        templateDataListPublisher.publishItemData(itemList2, proxyIdentityConfig.getId());
        // 设置缓存
//        itemList2.stream().filter(item -> !item.getTemplateId().equals("0")).forEach(item -> {
//            redissonClient.getMap(ITEM_PROTOTYPE_REL_PREFIX + proxyIdentityConfig.getId()).put(item.getItemId(), item.getTemplateId());
//        });
//        log.info("===zabbix item redis cache size is {}",  redissonClient.getMap(ITEM_PROTOTYPE_REL_PREFIX + proxyIdentityConfig.getId()).size());
    }
}
