//package com.aspire.mirror.theme.server.biz.handler.cmdb;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.aspire.mirror.common.constant.Constant;
//import com.aspire.mirror.common.util.DateUtil;
//import com.aspire.mirror.theme.server.clientservice.cmdb.CmdbServiceClient;
//import com.aspire.mirror.theme.server.constant.BizConstant;
//import com.aspire.mirror.theme.server.service.InstanceService;
//import com.google.common.collect.Maps;
//import org.redisson.api.RBucket;
//import org.redisson.api.RedissonClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReadWriteLock;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
//
///**
// * 扫描游标hodler类   <br/>
// * Project Name:zabbix-integrate
// * File Name:AlertScanIndexHolder.java
// * Package Name:com.aspire.mirror.zabbixintegrate.biz
// * ClassName: AlertScanIndexHolder <br/>
// * date: 2018年10月17日 下午7:23:59 <br/>
// *
// * @author pengguihua
// * @since JDK 1.6
// */
//@Component
//@ConditionalOnProperty(value = "middleware.configuration.switch.redis", havingValue = "true")
//public final class IpBizSysCacheHolder {
//    Logger LOGGER = LoggerFactory.getLogger(IpBizSysCacheHolder.class);
//    @Autowired(required = false)
//    private RedissonClient redissonClient;
//
//    @Autowired
//    private CmdbServiceClient cmdbService;
//
//    @Autowired
//    private InstanceService instanceService;
//
////    @Value("${systemType}")
////    private String systemType;
//
//    //    private volatile Map<String, String> flagMap = new HashMap<>();        // 扫描游标是否更改标记
//    private volatile Map<String, String> ipBizSysMap = new HashMap<>();       // 系统中缓存的扫描游标
//
//    private volatile Map<String, String> deivceIpMap = new HashMap<>();
//
//    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
//    private static final Lock R_LOCK = LOCK.readLock();
//    private static final Lock W_LOCK = LOCK.writeLock();
//
//    /**
//     * 从数据库初始化游标缓存值. <br/>
//     * <p>
//     * 作者： pengguihua
//     */
//    @PostConstruct
//    private void initLoad() {
//        Map<String, String> ipBizsysCorrespondenceMap = (Map<String, String>) redissonClient.getBucket(Constant
//                .KEY_CORRESPONDENCE).get();
//
//        Map<String, String> deviveIpIdMap = (Map<String, String>) redissonClient.getBucket
//                (Constant.KEY_DEVICEIP).get();
//
//        this.updateIpBizSysMap(ipBizsysCorrespondenceMap, deviveIpIdMap);
//    }
//
//    /**
//     * 获取当前游标值. <br/>
//     * <p>
//     * 作者： pengguihua
//     *
//     * @return
//     */
//    public Map<String, String> getIpBizSysMap() {
//        R_LOCK.lock();
//        try {
//            return this.ipBizSysMap;
//        } finally {
//            R_LOCK.unlock();
//        }
//    }
//
//    /**
//     * 获取当前游标值. <br/>
//     * <p>
//     * 作者： jinsu
//     *
//     * @return
//     */
//    public Map<String, String> getDeivceIpMap() {
//        R_LOCK.lock();
//        try {
//            return this.deivceIpMap;
//        } finally {
//            R_LOCK.unlock();
//        }
//    }
//
//    /**
//     * 更新游标. <br/>
//     * <p>
//     * 作者： pengguihua
//     *
//     * @param map
//     */
//    public void updateIpBizSysMap(Map<String, String> map, Map<String, String> deviceIpMap) {
//        W_LOCK.lock();
//        try {
//            this.ipBizSysMap = map;
//            this.deivceIpMap = deviceIpMap;
//            //                flagMap = map;
//        } finally {
//            W_LOCK.unlock();
//        }
//    }
//
//    //    /**
//    //     * 更新游标. <br/>
//    //     * <p>
//    //     * @param map
//    //     */
//    //    public void updateDeviceIpMap(Map<String, String> map) {
//    //        W_LOCK.lock();
//    //        try {
//    //            this.deivceIpMap = map;
//    ////                flagMap = map;
//    //        } finally {
//    //            W_LOCK.unlock();
//    //        }
//    //    }
//
//    /**
//     * 定时刷新到 数据库. <br/>
//     * <p>
//     * 作者： pengguihua
//     */
//    @PreDestroy
//    @Scheduled(cron = "${genIpBizCorrespond.cron}")
//    private void flush() {
//        // 如果未存在更改, 直接返回
//
//        //        W_LOCK.lock();
//        try {
//            RBucket<String> alarmFlag = redissonClient.getBucket(Constant.KEY_HOST_FRESHTIME);
//            LOGGER.info("AutoCacheTask[genIpBizCorrespond] freshTime is {}" + alarmFlag.get());
//            Date now = new Date();
//            Object instanceList;
//            instanceList = cmdbService.getInstanceBaseInfoListByFreshTime(alarmFlag.get());
//
//            JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(instanceList));
//            if (jsonArray.size() > 0) {
//                RBucket<Map<String, String>> ipBizsysCorrespondenceFlag = redissonClient.getBucket
//                        (Constant.KEY_CORRESPONDENCE);
//
//                Map<String, String> map = ipBizsysCorrespondenceFlag.get();
//                if (CollectionUtils.isEmpty(map)) {
//                    map = Maps.newHashMap();
//                }
//                // 同时处理设备IP设备ID Map
//                RBucket<Map<String, String>> deviveIpIdFlag = redissonClient.getBucket
//                        (Constant.KEY_DEVICEIP);
//                Map<String, String> deviveIpIdMap = deviveIpIdFlag.get();
//                if (CollectionUtils.isEmpty(deviveIpIdMap)) {
//                    deviveIpIdMap = Maps.newHashMap();
//                }
//                for (int i = 0; i < jsonArray.size(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    if (!StringUtils.isEmpty(jsonObject.getString("ip"))) {
//                        Integer isDelete = jsonObject.getInteger("isDelete");
//                        if (isDelete == 0) {
//                            map.put(jsonObject.getString("ip"), jsonObject.getString("bizSystem"));
//                            deviveIpIdMap.put(jsonObject.getString("ip"), jsonObject.getString("instanceId"));
//                        } else if (isDelete == 1) {
//                            map.remove(jsonObject.getString("ip"));
//                            deviveIpIdMap.remove(jsonObject.getString("ip"));
//                        }
//                    }
//                }
//                ipBizsysCorrespondenceFlag.set(map);
//
//                deviveIpIdFlag.set(deviveIpIdMap);
//                // 设置本地缓存
//                this.updateIpBizSysMap(map, deviveIpIdMap);
//                LOGGER.info("AutoCacheTask[genIpBizCorrespond] ipBizsysCorrespondence size is {}",
//                        ipBizsysCorrespondenceFlag.get
//                                ().size());
//            }
//            alarmFlag.set(DateUtil.format(now, DateUtil.DATE_TIME_FORMAT));
//
//
//        } catch (Throwable e) {
//            LOGGER.error("Error when flush the scan index cache to DB.", e);
//        } finally {
//            //            W_LOCK.unlock();
//        }
//    }
//}
