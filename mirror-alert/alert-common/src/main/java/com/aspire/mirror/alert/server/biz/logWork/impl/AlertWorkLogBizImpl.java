package com.aspire.mirror.alert.server.biz.logWork.impl;

import com.aspire.mirror.alert.server.biz.logWork.AlertWorkLogBiz;
import com.aspire.mirror.alert.server.dao.logWork.AlertWorkLogDao;
import com.aspire.mirror.alert.server.dao.logWork.po.AlertWorkConfig;
import com.aspire.mirror.alert.server.dao.logWork.po.AlertWorkLogInfo;
import com.aspire.mirror.alert.server.vo.logWork.AlertWorkLogInfoVo;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@Transactional
public class AlertWorkLogBizImpl implements AlertWorkLogBiz {

    @Autowired
    private AlertWorkLogDao alertWorkLogDao;

    @Override
    public String createdAlerts(AlertWorkConfig request) {
        try {
            if ("".equals(request.getUuid()) || null == request.getUuid()) {
                request.setUuid(UUID.randomUUID().toString());
                alertWorkLogDao.insertAlertWorkConfig(request);
            } else {
                alertWorkLogDao.updateAlertWorkConfig(request);
            }
            return "success";
        } catch (Exception e) {
            log.error("[created Alerts work log is error]>>>" + e);
            return "error";
        }
    }

    @Override
    public AlertWorkConfig getAlertWorkConfig() {
        return alertWorkLogDao.getAlertWorkConfig();
    }

    @Override
    public Object getWorkLogInfo(String workName,String workDate,String workTime,String work) {
        try {
            AlertWorkLogInfo alertWorkLogTaskRequest = new AlertWorkLogInfo();
            alertWorkLogTaskRequest.setWorkName(workName);
            String startTime = workTime.split("-")[0];
            String endTime = workTime.split("-")[1];
            String startTimeFrom0 = "";
            String endTimeTo24 = "";
            int isWork = 0;
            if ("1".equals(work)) {
                startTime = workDate + " " + startTime + ":00";
                endTime = workDate + " " + endTime + ":00";
                isWork = 1;
            } else if ("2".equals(work)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String time23 = workDate + " " + "23:59:59";
                String time0 = DateUtils.getSpecifiedDayAfter(workDate,1) + " " + "00:00:00";
                String newStartTime = workDate + " " + startTime + ":00";
                String newEndTime = workDate + " " + endTime + ":00";
                String afterStartTime = DateUtils.getSpecifiedDayAfter(workDate,1) + " " + startTime + ":00";
                String afterEndTime = DateUtils.getSpecifiedDayAfter(workDate,1) + " " + endTime + ":00";
                if ((sdf.parse(newStartTime).getTime() <= sdf.parse(time23).getTime()) &&
                        (Integer.parseInt(startTime.split(":")[0])  > Integer.parseInt(endTime.split(":")[0])) &&
                                (sdf.parse(afterEndTime).getTime() >= sdf.parse(time0).getTime())) {
                    startTime = newStartTime;
                    endTimeTo24 = workDate + " " + "23:59:59";
                    startTimeFrom0 = time0;
                    endTime = afterEndTime;
                    isWork = 2;
                }
//                else if (sdf.parse(newEndTime).getTime() <= sdf.parse(time23).getTime()) {
//                    startTime = newStartTime;
//                    endTime = newEndTime;
//                    isWork = 1;
//                } else if (sdf.parse(afterStartTime).getTime() >= sdf.parse(time0).getTime()) {
//                    startTime = afterStartTime;
//                    endTime = afterEndTime;
//                    isWork = 1;
//                }
            }
            alertWorkLogTaskRequest.setIsWork(isWork);
            alertWorkLogTaskRequest.setStartTime(startTime);
            alertWorkLogTaskRequest.setEndTime(endTime);
            alertWorkLogTaskRequest.setStartTimeFrom0(startTimeFrom0);
            alertWorkLogTaskRequest.setEndTimeTo24(endTimeTo24);
            List<AlertWorkLogInfoVo> alertListByAlerts = alertWorkLogDao.getAlertListByAlerts(alertWorkLogTaskRequest);
            List<AlertWorkLogInfoVo> alertListByAlertsHis = alertWorkLogDao.getAlertListByAlertsHis(alertWorkLogTaskRequest);

            int alertSumCount = 0;int alertClearCount = 0;
            List<AlertWorkLogInfoVo> list = Lists.newArrayList();
            list.addAll(alertListByAlerts);
            list.addAll(alertListByAlertsHis);
            log.info("work log alert data is {}",list.size());

            int alertSeriousCount = 0;int alertHighCount = 0;int alertMediumCount = 0;int alertLowCount = 0;int alertTipCount = 0;
            int alertNotifyCount = 0;int alertTransferCount = 0;int alertConfirmCount=0;int alertOrderCount = 0;
            if (!list.isEmpty()) {
                alertSumCount += list.size();
                for (AlertWorkLogInfoVo map : list) {
                    if ("5".equals(map.getAlertLevel())) {
                        alertSeriousCount += 1;
                    }
                    if ("4".equals(map.getAlertLevel())) {
                        alertHighCount += 1;
                    }
                    if ("3".equals(map.getAlertLevel())) {
                        alertMediumCount += 1;
                    }
                    if ("2".equals(map.getAlertLevel())) {
                        alertLowCount += 1;
                    }
                    if ("1".equals(map.getAlertLevel())) {
                        alertTipCount += 1;
                    }
                    if (map.getOperationType() == 0) {
                        alertTransferCount += 1;
                    }
                    if (map.getOperationType() == 1) {
                        alertConfirmCount += 1;
                    }
                    if (map.getOperationType() == 2) {
                        alertOrderCount += 1;
                    }
                    if (map.getOperationType() == 3) {
                        alertClearCount += 1;
                    }
                    if (map.getOperationType() == 4) {
                        alertNotifyCount += 1;
                    }
                }
            }
            Map<String,Object> result = Maps.newHashMap();
            result.put("alertSumCount",alertSumCount);
            result.put("alertSeriousCount",alertSeriousCount);
            result.put("alertHighCount",alertHighCount);
            result.put("alertMediumCount",alertMediumCount);
            result.put("alertLowCount",alertLowCount);
            result.put("alertTipCount",alertTipCount);
            result.put("alertClearCount",alertClearCount);
            result.put("alertNotifyCount",alertNotifyCount);
            result.put("alertTransferCount",alertTransferCount);
            result.put("alertConfirmCount",alertConfirmCount);
            result.put("alertOrderCount",alertOrderCount);
            // 工单状态 1-未派单 2-处理中 3-已完成
            int orderOne = 0;int orderTwo = 0;int orderThree = 0;
            List<Map<String, Object>> alertsFromAlert = alertWorkLogDao.getAlertsFromAlert(alertWorkLogTaskRequest);
            for (Map<String, Object> map : alertsFromAlert) {
                if ("1".equals(String.valueOf(map.get("order_status")))) {
                    orderOne += 1;
                } else if ("2".equals(String.valueOf(map.get("order_status")))) {
                    orderTwo += 1;
                } else if ("3".equals(String.valueOf(map.get("order_status")))) {
                    orderThree += 1;
                }
            }
            result.put("alertOrderOne",orderOne);
            result.put("alertOrderTwo",orderTwo);
            result.put("alertOrderThree",orderThree);
            result.put("alertOperationOrder",orderTwo + orderThree);
            result.put("workName",workName);
            result.put("workTime",workTime);
            result.put("workDate",DateUtils.getSpecifiedWeek(workDate));
            result.put("work","1".equals(work) ? "白班" : "夜班");
            return result;
        } catch (Exception e) {
            log.error("[get work log is error]>>>" + e);
        }
        return null;
    }

    @Override
    public Object getWorkLogList(String workName, String workMonth) {
        List<Map<String,Object>> result = Lists.newArrayList();
        try {
            Date nowDate=new Date();
            final String format2 = new SimpleDateFormat( "yyyyMM" ).format(nowDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            final String nowDateFormat = new SimpleDateFormat("yyyy-MM-dd").format(nowDate);
            // 获取值班时间配置
            AlertWorkConfig alertWorkConfig = alertWorkLogDao.getAlertWorkConfig();
            // 获取指定日期的当前月份
            List<String> dateList = DateUtils.getDateList(workMonth);
            Map<String,Map<String,Object>> dateData = Maps.newHashMap();
            for (String date : dateList) {
                Map<String,Object> res = Maps.newHashMap();
                res.put("workName",workName);
                res.put("dayTime", alertWorkConfig.getDayStartTme() + "-" + alertWorkConfig.getDayEndTme());
                res.put("nightTime", alertWorkConfig.getNightStartTme() + "-" + alertWorkConfig.getNightEndTme());
                res.put("isB", Boolean.TRUE);
                res.put("isH", Boolean.TRUE);
                dateData.put(date, res);
            }
            // 获取key值
            Set<String> sets = dateData.keySet();
            String newWorkMonth = workMonth.split("-")[0] + workMonth.split("-")[1];
            if (Integer.parseInt(newWorkMonth) == Integer.parseInt(format2)) {
                String date9 = nowDateFormat + " " + "09:00:00";
                String date18 = nowDateFormat + " " + "18:00:00";
                final Map<String, Object> map0 = dateData.get(DateUtils.getSpecifiedDayAfter( nowDateFormat, -1 ));
                final Map<String, Object> map1 = dateData.get(nowDateFormat);
                if (nowDate.getTime() < sdf.parse(date9).getTime()) {
                    map0.put("isH", Boolean.FALSE);
                    dateData.put(DateUtils.getSpecifiedDayAfter( nowDateFormat, -1 ),map0);
                } else if (nowDate.getTime() < sdf.parse(date18).getTime() && nowDate.getTime() > sdf.parse(date9).getTime()) {
                    map1.put("isB", Boolean.FALSE);
                    map1.put( "isH", Boolean.FALSE );
                    dateData.put(nowDateFormat,map1);
                } else if (nowDate.getTime() > sdf.parse(date18).getTime()) {
                    map1.put( "isH", Boolean.FALSE );
                    dateData.put(nowDateFormat,map1);
                }
            }
            TreeSet<String> treeSet = new TreeSet<>();
            treeSet.addAll(sets);
            for (String set : treeSet) {
                Map<String, Object> map = dateData.get(set);
                map.put("date",DateUtils.getSpecifiedWeek(set));
                map.put("workDate",set);
                if (Integer.parseInt(newWorkMonth) > Integer.parseInt(format2)) {
                    map.put( "isB", Boolean.FALSE );
                    map.put( "isH", Boolean.FALSE );
                }
                if (Integer.parseInt(newWorkMonth) == Integer.parseInt(format2) &&
                    Integer.parseInt(set.split("-")[2]) > Integer.parseInt(nowDateFormat.split("-")[2])) {
                    map.put( "isB", Boolean.FALSE );
                    map.put( "isH", Boolean.FALSE );
                }
                result.add(map);
            }
            return result;
        } catch (Exception e) {
            log.error("[get work log is error]>>>" + e);
        }
        return null;
    }
}
