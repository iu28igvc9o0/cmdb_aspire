package com.aspire.mirror.alert.server.task.monitor;

import com.aspire.mirror.alert.server.util.MapUtils;
import com.aspire.mirror.alert.server.dao.model.AlertFieldDao;
import com.aspire.mirror.alert.server.dao.cmdbInstance.CmdbInstanceMapper;
import com.aspire.mirror.alert.server.dao.kpi.KpiConfigLogsMapper;
import com.aspire.mirror.alert.server.dao.kpi.KpiConfigManageMapper;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.dao.kpi.po.KpiConfigLogs;
import com.aspire.mirror.alert.server.dao.kpi.po.KpiConfigManage;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.util.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.*;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.biz.monitor
 * @Author: baiwenping
 * @CreateTime: 2020-04-15 09:37
 * @Description: ${Description}
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "monitor.kpi.open", havingValue = "true")
public class MonitorKpiJob {
    @Autowired
    private KpiConfigManageMapper kpiConfigManageMapper;
    @Autowired
    private CmdbInstanceMapper cmdbInstanceMapper;
    @Autowired
    private MonitorKpiHelper monitorKpiHelper;
    @Autowired
    private AlertFieldDao alertFieldDao;
    @Autowired
    private KpiConfigLogsMapper kpiConfigLogsMapper;
    @Value("${monitor.kpi.doingHour: 2}")
    private Integer doingHour;
    @PostConstruct
    private void init() {
    }

    /**
     *
     * @auther baiwenping
     * @Description
     * @Date 16:09 2020/4/17
     * @Param []
     * @return void
     **/
    @Scheduled(cron = "0 */1 * * * ?")
    public void run() {
        long dms = System.currentTimeMillis() / 1000 * 1000 -1;
        Date d = new Date(dms);
        //查询zabbix监控配置
        List<KpiConfigManage> kpiConfigManageList = kpiConfigManageMapper.selectValidList();
        List<KpiConfigManage> list = new ArrayList<>();
        for (KpiConfigManage kpiConfigManage: kpiConfigManageList) {
            String cron = kpiConfigManage.getCron();
            if (StringUtils.isEmpty(cron)) {
                continue;
            }
            try {
                CronExpression cronExp = new CronExpression(cron);
                Date timeAfter = cronExp.getTimeAfter(d);
                long ms = timeAfter.getTime() - dms;
//                log.info("now is {}, timeBefore is {}",d, timeAfter);
                //判断1分钟内发生的配置
                if (ms < 60 * 1000) {
                    list.add(kpiConfigManage);
                }
            } catch (ParseException e) {
                log.error("parse {} cron exception,cron:{}",
                        kpiConfigManage.getKpiName(), kpiConfigManage.getCron());
                log.error("parse error", e);
                continue;
            }
        }

        //处理监控数据指标
        if (list.size() > 0) {
            doMonitorKpi(list);
        }
    }

    public void doMonitorKpi (List<KpiConfigManage> list) {
        long l = System.currentTimeMillis();
        log.info("start doMonitorKpi!");
        boolean ciFlag = false;
        Set<String> ciFieldList = new HashSet<>();
        Map<String, Map<String, Date>> dateMps = Maps.newHashMap();
        for (KpiConfigManage kpiConfigManage: list) {
            Map<String, Date> dateMap = getDateByKpiType(kpiConfigManage, l);
            dateMps.put(kpiConfigManage.getId(), dateMap);
            Date fromTime = dateMap.get("fromTime");
            Date toTime = dateMap.get("toTime");
            if (fromTime == null || toTime == null) {
                continue;
            }
            String relationCiFields = kpiConfigManage.getRelationCiFields();
            if (!StringUtils.isEmpty(relationCiFields)) {
                ciFlag = true;
                String[] split = relationCiFields.split(",");
                for (String str: split) {
                    ciFieldList.add(str);
                }
            }
        }

        // 判断是否需要ci数据
        Map<String, Map<String, Map<String, Object>>> ciMap = null;

        for (KpiConfigManage kpiConfigManage: list) {
            if (monitorKpiHelper.getZabbix().equalsIgnoreCase(kpiConfigManage.getDateSource())) {
                continue;
            }
            Map<String, Date> dateMap = dateMps.get(kpiConfigManage.getId());
            Date fromTime = dateMap.get("fromTime");
            Date toTime = dateMap.get("toTime");
            if (fromTime == null || toTime == null) {
                continue;
            }
            if (ciFlag && ciMap == null) {
                ciMap = getCi(ciFieldList);
            }
            monitorKpiHelper.doMonitorKpiOne(kpiConfigManage, ciMap, l, dateMap);
        }

    }

    private Map<String, Map<String, Map<String, Object>>> getCi (Set<String> ciFieldList) {
        Map<String, Map<String, Map<String, Object>>> ciMap = new HashMap<>();

        Map<String, Object> queryMap = new HashMap<>();
        List<AlertFieldVo> modelFieldList = alertFieldDao.getAlertFieldListByTableName("cmdb_instance", null);
        if (!modelFieldList.isEmpty()) {

            Set<String> cmdbFieldList = new HashSet<>();
            for (AlertFieldVo alertModelField: modelFieldList) {
                String fieldCode = alertModelField.getFieldCode();
                for (String str : ciFieldList) {
                    if(str.equalsIgnoreCase(alertModelField.getFieldCode())) {
                        cmdbFieldList.add(fieldCode);
                    }
                }
            }
            if (cmdbFieldList.size() > 0) {
                StringBuilder sb= new StringBuilder();
                sb.append("instance_id,");
                for (String str : cmdbFieldList) {
                    sb.append(str).append(",");
                }
                queryMap.put("cmdbField", sb.substring(0, sb.length()-1));
            }
        }

        List<Map<String, Object>> mapList = cmdbInstanceMapper.selectAllByField(queryMap);
        Map<String, Map<String, Object>> idInstanceMap = null;
        log.info("query cmdb instance size is {}", mapList.size());
        for (Map<String, Object> map: mapList) {
            String ip = MapUtils.getString(map, "ip");
            String instanceId = MapUtils.getString(map, "instance_id");
            if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(instanceId)) {
                String[] split = ip.split(",");
                for (String s: split) {
                    String trim = s.trim();
                    if (!StringUtils.isEmpty(trim)) {
                        idInstanceMap = ciMap.get(trim);
                        if (idInstanceMap == null) {
                            idInstanceMap = Maps.newHashMap();
                        }
                        idInstanceMap.put(instanceId, map);
                        ciMap.put(trim, idInstanceMap);
                    }
                }
            }
        }
        log.info(" cmdb instance actually size is {}", mapList.size());
        mapList.clear();
        return ciMap;
    }

    /**
     * 根据kpi类型获取时间，后续此方法需抽取出来
     * @auther baiwenping
     * @Description
     * @Date 15:22 2020/4/20
     * @Param [kpiType]
     * @return java.util.Date
     **/
    private Map<String, Date> getDateByKpiType (KpiConfigManage kpiConfigManage, long l) {
        Map<String, Date> map = new HashMap<>();
        String id = kpiConfigManage.getId();
        Map<String, Object> query = new HashMap<>();
        query.put("configId", id);
        Date now = new Date();
        List<KpiConfigLogs> logsList = kpiConfigLogsMapper.selectNewestByConfigId(query);
        //如果上一条还没有执行结束，则不执行新的一条
        if (!logsList.isEmpty() && monitorKpiHelper.DOING.equalsIgnoreCase(logsList.get(0).getStatus())) {
            KpiConfigLogs kpiConfigLogs = logsList.get(0);
            Date execTime = kpiConfigLogs.getExecTime();
            if (execTime == null || (now.getTime() - execTime.getTime()) > doingHour * 3600 * 1000) {
                kpiConfigLogs.setStatus("error");
                kpiConfigLogs.setContent("exec time more than two hours");
                kpiConfigLogsMapper.update(kpiConfigLogs);
            }
            return map;
        }
        String kpiType = kpiConfigManage.getKpiType();
        if ("1".equals(kpiType)) {
            if (logsList.isEmpty()) {
                getHourDate(map, null, l, now);
            } else {
                KpiConfigLogs kpiConfigLogs = logsList.get(0);
                Date toTimeL = kpiConfigLogs.getToTime();
                getHourDate(map, toTimeL, l, now);
            }
        } else if ("2".equals(kpiType)) {
            if (logsList.isEmpty()) {
                getDayDate(map, null, l, now);
            } else {
                KpiConfigLogs kpiConfigLogs = logsList.get(0);
                Date toTimeL = kpiConfigLogs.getToTime();
                getDayDate(map, toTimeL, l, now);
            }
        } else if ("3".equals(kpiType)) {
            if (logsList.isEmpty()) {
                getMonthDate(map, null, l, now);
            } else {
                KpiConfigLogs kpiConfigLogs = logsList.get(0);
                Date toTimeL = kpiConfigLogs.getToTime();
                getMonthDate(map, toTimeL, l, now);
            }

        } else {
            Date startTime = kpiConfigManage.getStartTime();
            Integer duration = kpiConfigManage.getDuration();
            Date endTime = kpiConfigManage.getEndTime();
            if (StringUtils.isEmpty(duration)) {
                return map;
            }
            if (logsList.isEmpty()) {
                if (startTime != null) {
                    Date toTime = new Date(startTime.getTime() + duration * 60000);
                    if (!toTime.after(now) && endTime != null && !toTime.after(endTime)) {
                        map.put("fromTime", startTime);
                        map.put("toTime", toTime);
                    }
                } else {
                    if (duration > 0 && duration < 60) { //按小时统计
                        if (duration < 5) {

                            l = l/60000 * 60000 - 300000;
                        } else {
                            l = l/(60000 * duration) * 60000 * duration;
                        }
                        Date toTime = new Date(l);
                        Date fromTime = new Date(l - duration * 60000);
                        if (logsList.isEmpty() || !logsList.get(0).getToTime().after(fromTime)) {
                            map.put("fromTime", fromTime);
                            map.put("toTime", toTime);
                        }
                    } else if (duration >= 60 && duration < 1440) { //按小时统计
                        l = l/3600000 * 3600000;
                        Date toTime = new Date(l);
                        Date fromTime = new Date(l - duration * 60000);
                        if (logsList.isEmpty() || !logsList.get(0).getToTime().after(fromTime)) {
                            map.put("fromTime", fromTime);
                            map.put("toTime", toTime);
                        }
                    } else {
                        l = l/(24 * 3600 * 1000) * (24 * 3600 * 1000) -TimeZone.getDefault().getRawOffset();
                        Date toTime = new Date(l);
                        Date fromTime = new Date(l - duration * 60000);
                        if (logsList.isEmpty() || !logsList.get(0).getToTime().after(fromTime)) {
                            map.put("fromTime", fromTime);
                            map.put("toTime", toTime);
                        }
                    }

                }

            } else {
                KpiConfigLogs kpiConfigLogs = logsList.get(0);
                Date toTimeL = kpiConfigLogs.getToTime();
                if (startTime!= null && startTime.after(toTimeL)) {
                    toTimeL = startTime;
                }
                Date toTime = new Date(toTimeL.getTime() + duration * 60000);
                if (!toTime.after(now) && (endTime == null || !toTime.after(endTime))) {
                    map.put("fromTime", toTimeL);
                    map.put("toTime", toTime);
                }
            }
        }
        Date toTime = map.get("toTime");
        // 不处理五分钟之内的数据
        if (toTime != null && toTime.getTime() > (System.currentTimeMillis() - 300000)) {
            map.clear();
        }
        return map;

    }

    private void getHourDate (Map<String, Date> map, Date fromDate, long l, Date now) {
        if (fromDate == null) {
            l = (l - 3600000)/3600000 * 3600000;
            Date fromTime = new Date(l);
            Date toTime = new Date(l+3600000);
            map.put("fromTime", fromTime);
            map.put("toTime", toTime);
        } else {
            Date toTime = new Date(fromDate.getTime() + 3600000);
            if (!toTime.after(now)) {
                map.put("fromTime", fromDate);
                map.put("toTime", toTime);
            }
        }
    }

    private void getDayDate (Map<String, Date> map, Date fromDate, long l, Date now) {
        if (fromDate == null) {
            l = (l - (24 * 3600 * 1000))/(24 * 3600 * 1000) * (24 * 3600 * 1000) -TimeZone.getDefault().getRawOffset();
            Date fromTime = new Date(l);
            Date toTime = new Date(l + 24 * 3600 * 1000);
            map.put("fromTime", fromTime);
            map.put("toTime", toTime);
        } else {
            Date toTime = new Date(fromDate.getTime() + 24 * 3600 * 1000);
            if (!toTime.after(now)) {
                map.put("fromTime", fromDate);
                map.put("toTime", toTime);
            }
        }
    }

    private void getMonthDate (Map<String, Date> map, Date fromDate, long l, Date now) {
        if (fromDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(l);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date toTime = new Date(calendar.getTimeInMillis());
            calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) -1));
            Date fromTime = new Date(calendar.getTimeInMillis());
            map.put("fromTime", fromTime);
            map.put("toTime", toTime);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(fromDate.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date fromTime = new Date(calendar.getTimeInMillis());
            calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) + 1));
            Date toTime = new Date(calendar.getTimeInMillis());
            if (!toTime.after(now)) {
                map.put("fromTime", fromTime);
                map.put("toTime", toTime);
            }
        }
    }
}
