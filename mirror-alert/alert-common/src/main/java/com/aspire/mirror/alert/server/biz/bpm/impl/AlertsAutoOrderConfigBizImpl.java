package com.aspire.mirror.alert.server.biz.bpm.impl;

import com.aspire.mirror.alert.server.biz.bpm.AlertsAutoOrderConfigBiz;
import com.aspire.mirror.alert.server.vo.bpm.AlertBpmStartCallBack;
import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.dao.bpm.AlertsAutoOrderConfigDao;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderConfigDetailVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderConfigVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderLogVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderLogReqVo;
import com.aspire.mirror.alert.server.util.AlertFilterCommonUtil;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.alert.AlertsBizV2;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.common.constant.Constant;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
public class AlertsAutoOrderConfigBizImpl implements AlertsAutoOrderConfigBiz {

    @Autowired
    private AlertsAutoOrderConfigDao dao;
    @Autowired
    private AlertsBizV2 alertsBizV2;
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageResponse<AlertAutoOrderConfigDetailVo> getAlertAutoOrderConfigList(String configName,
                                                                                  String isOpen,
                                                                                  String startTime,
                                                                                  String endTime,
                                                                                  String orderType,
                                                                                  String orderTimeInterval,
                                                                                  Integer pageNum,
                                                                                  Integer pageSize) {
        PageResponse<AlertAutoOrderConfigDetailVo> response = new PageResponse<AlertAutoOrderConfigDetailVo>();
        response.setCount(dao.getAlertAutoOrderConfigCount(configName, isOpen, startTime, endTime, orderType, orderTimeInterval,pageNum, pageSize));
        List<AlertAutoOrderConfigDetailVo> alertAutoOrderConfigList =
                dao.getAlertAutoOrderConfigList(configName, isOpen, startTime, endTime, orderType, orderTimeInterval, pageNum, pageSize);
        alertAutoOrderConfigList.forEach(item -> {
            switch (item.getOrderType()) {
                case Constants.ORDER_ALERT:
                    item.setOrderType(Constant.ALERT_ORDER);
                    break;
                case Constants.ORDER_HITCH:
                    item.setOrderType(Constant.HITCH_ORDER);
                    break;
                case Constants.ORDER_MAINTENANCE:
                    item.setOrderType(Constant.MAINTENANCE_ORDER);
                    break;
                case Constants.ORDER_TUNING:
                    item.setOrderType(Constant.TUNING_ORDER);
                    break;
                default: // 其它工单类型
                    break;
            }

        });
        response.setResult(alertAutoOrderConfigList);
        return response;
    }

    @Override
    public void createAlertAutoOrderConfig(AlertAutoOrderConfigVo request) {
        request.setUuid(UUID.randomUUID().toString());
        dao.createAlertAutoOrderConfig(request);
    }

    @Override
    public String checkName(String configName) {
        AlertAutoOrderConfigDetailVo alertAutoOrderConfigDetailVo = dao.checkName(configName);
        return null == alertAutoOrderConfigDetailVo ? null : alertAutoOrderConfigDetailVo.getUuid();
    }

    @Override
    public void updateAlertAutoOrderConfig(AlertAutoOrderConfigVo request) {
        dao.updateAlertAutoOrderConfig(request);
    }

    @Override
    public AlertAutoOrderConfigDetailVo getAlertAutoOrderConfigDetail(String uuid) {
        return dao.getAlertAutoOrderConfigDetail(uuid);
    }

    @Override
    public void deleteAlertAutoOrderConfig(List<String> uuidList) {
        dao.deleteAlertAutoOrderConfig(uuidList);
    }

    @Override
    public void updateAlertAutoOrderConfigStatus(List<String> uuidList,String configStatus) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("uuidList",uuidList);
        map.put("configStatus",configStatus);
        dao.updateAlertAutoOrderConfigStatus(map);
    }

    @Override
    public void copyAlertAutoOrderConfig(String uuid,String userName) {
        AlertAutoOrderConfigDetailVo alertAutoOrderConfigDetail = dao.getAlertAutoOrderConfigDetail(uuid);
        AlertAutoOrderConfigVo alertAutoOrderConfigVo =
                PayloadParseUtil.fastjsonBaseParse(AlertAutoOrderConfigVo.class, alertAutoOrderConfigDetail);
        alertAutoOrderConfigVo.setUuid(UUID.randomUUID().toString());
        String configName = alertAutoOrderConfigDetail.getConfigName();
        String[] split = configName.split("_");
        SimpleDateFormat SDF2 = new SimpleDateFormat("yyyyMMddHHmmss");
        alertAutoOrderConfigVo.setConfigName(split[0]+ "_" + SDF2.format(new Date()));
        alertAutoOrderConfigVo.setCreator(userName);
        alertAutoOrderConfigVo.setIsOpen("2");
        dao.createAlertAutoOrderConfig(alertAutoOrderConfigVo);
    }

    @Override
    public PageResponse<AlertAutoOrderLogVo> getAlertAutoOrderLogList(AlertAutoOrderLogReqVo request) {
        PageResponse<AlertAutoOrderLogVo> response = new PageResponse<AlertAutoOrderLogVo>();
        response.setCount(dao.getAlertAutoOrderLogCount(request));
        response.setResult(dao.getAlertAutoOrderLogList(request));
        return response;
    }

    @Override
    public List<Map<String, Object>> exportAlertAutoOrderLogList(AlertAutoOrderLogReqVo request) {
        return dao.exportAlertAutoOrderLogList(request);
    }

    private final ConcurrentHashMap<String, Date> localMap = new ConcurrentHashMap<>(1);

    @Override
    public ResponseEntity<String> alertAutoOrderSchedule() {
//        if (redisTemplate.hasKey(AlertConfigConstants.REDIS_ALERT_ORDER_LOCK)) {
//            log.info("Last alert order task not finished!");
//            return new ResponseEntity<String>("", HttpStatus.OK);
//        }
//        redisTemplate.opsForValue().set(AlertConfigConstants.REDIS_ALERT_ORDER_LOCK, AlertCommonConstant.NUM.ONE);
//        redisTemplate.expire(AlertConfigConstants.REDIS_ALERT_ORDER_LOCK, 30, TimeUnit.MINUTES);
        Date curTime = new Date();
        Date date = localMap.get(AlertConfigConstants.REDIS_ALERT_ORDER_LOCK);
        if (date != null && date.getTime() >= (curTime.getTime() - 1800 * 1000) ) {
            log.info("Last alert order task not finished!");
            return new ResponseEntity<String>("", HttpStatus.OK);
        }
        localMap.put(AlertConfigConstants.REDIS_ALERT_ORDER_LOCK, curTime);
        String res = "success";
        try {

            List<String> keys = Lists.newArrayList();
            Map<String, AlertAutoOrderLogVo> alertLists = Maps.newHashMap();

            // 获取处于启用状态的规则
            List<AlertAutoOrderConfigDetailVo> alertAutoOrderConfigList =
                    dao.getAlertAutoOrderConfigList(null, "1", null, null,null,null, null, null);

            log.info("告警派单生效规则：{}", alertAutoOrderConfigList);
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (AlertAutoOrderConfigDetailVo detail : alertAutoOrderConfigList) {
                // 判断规则的生效类型
                if (detail.getConfigTimeType().equals("2")) {

                    Date startTime = SDF.parse(detail.getStartTime());
                    Date endTime = SDF.parse(detail.getEndTime());
                    if (startTime.after(curTime) || endTime.before(curTime)) {
                        continue;
                    }
                }
                // 判断是否处于派单时段
                if (StringUtils.isNotEmpty(detail.getOrderTimeInterval())) {
                    String[] split = detail.getOrderTimeInterval().split("-");
                    String curHour = new SimpleDateFormat("HH:mm").format(curTime);
                    if (split[0].compareTo(curHour) > 0 || split[1].compareTo(curHour) < 0) {
                        continue;
                    }
                }
                // 获取告警数据
                String optionCondition = AlertFilterCommonUtil.getCondition(detail.getAlertFilter());
                Map<String,Object> queryAlertParam = Maps.newHashMap();
                queryAlertParam.put("optionCondition", optionCondition);
//                queryAlertParam.put("startTime", DateUtils.getTime(detail.getOrderTimeSpace()));
//                queryAlertParam.put("endTime", SDF.format(curTime));
                List<Map<String, Object>> alertDataByFilter = dao.getAlertDataByFilter(queryAlertParam);
                log.info("派单规则 ： {} ,查到的符合规则条数是： {}", detail.getConfigName(), alertDataByFilter.size());
                List<AlertAutoOrderLogVo> alerts = PayloadParseUtil.jacksonBaseParse(AlertAutoOrderLogVo.class, alertDataByFilter);
                long orderTimeSpace = System.currentTimeMillis() - detail.getOrderTimeSpace() * 60 * 1000;
                // 数据去重，根据派单类型优先级
                for (AlertAutoOrderLogVo alert : alerts) {
//                    String curMoniTime = alert.getCurMoniTime();
//                    String time = DateUtils.getTime(detail.getOrderTimeSpace());
//                    if (curMoniTime.compareTo(time) > 0) {
//                        continue;
//                    }
                    Date createTime = alert.getCreateTime();
                    // 判断时间
                    if (createTime != null && createTime.getTime() > orderTimeSpace) {
                        continue;
                    }
                    String key = alert.getAlertId();
                    if (keys.contains(key)) {
                        AlertAutoOrderLogVo alertData = alertLists.get(key);
                        int orderType = Integer.parseInt(alertData.getOrderType());
                        if (orderType <= Integer.parseInt(detail.getOrderType())) {
                            alertData.setOrderType(detail.getOrderType());
                            alertData.setConfigId(detail.getUuid());
                            alertLists.put(key, alertData);
                        }
                    } else {
                        keys.add(key);
                        alert.setOrderType(detail.getOrderType());
                        alert.setConfigId(detail.getUuid());
                        alertLists.put(key, alert);
                    }
                }
            }
            log.info("需要派单的条数是:{}, 告警id是:{}", keys.size(), keys);
            // 派单
            for (String key : keys) {
                try {
                    AlertAutoOrderLogVo alert = alertLists.get(key);
                    AlertBpmStartCallBack orderMessage = alertsBizV2.switchOrder("alauda", alert.getAlertId(), Integer.parseInt(alert.getOrderType()));
                    int count = dao.getOrderLogCountByAlertIdAndOrderType(alert.getAlertId(), alert.getOrderType());
                    if (StringUtils.isEmpty(orderMessage.getOrderIdList()) && count > 0) {
                        dao.updateOrderTime(alert.getAlertId(), alert.getOrderType());
                    } else {
                        alert.setOrderId(orderMessage.getOrderIdList());
                        alert.setStatus(StringUtils.isNotEmpty(orderMessage.getOrderIdList()) ? "1" : "0");
                        dao.insertAlertOrderConfigLog(alert);
                    }
                } catch (Exception e) {
                    log.error("[AlertAutoOrderSchedule] Error is {}", e);
                }
            }
        } catch (Exception e) {
            log.error("[AlertAutoOrderSchedule] Error is {}", e);
            res = e.toString();
        } finally {
//            redisTemplate.delete(AlertConfigConstants.REDIS_ALERT_ORDER_LOCK);
            localMap.remove(AlertConfigConstants.REDIS_ALERT_ORDER_LOCK);
        }
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }
}
