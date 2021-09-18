package com.aspire.mirror.zabbixintegrate.biz.monitor;

import com.aspire.mirror.zabbixintegrate.dao.HistoryMapper;
import com.aspire.mirror.zabbixintegrate.daoAlert.KpiConfigKeyMapper;
import com.aspire.mirror.zabbixintegrate.daoAlert.KpiConfigLogsMapper;
import com.aspire.mirror.zabbixintegrate.daoAlert.KpiResultTransferMapper;
import com.aspire.mirror.zabbixintegrate.daoAlert.po.KpiConfigKey;
import com.aspire.mirror.zabbixintegrate.daoAlert.po.KpiConfigLogs;
import com.aspire.mirror.zabbixintegrate.daoAlert.po.KpiConfigManage;
import com.aspire.mirror.zabbixintegrate.daoAlert.po.KpiResultTransfer;
import com.aspire.mirror.zabbixintegrate.domain.MonitorKpiDto;
import com.aspire.mirror.zabbixintegrate.util.CommonUtils;
import com.aspire.mirror.zabbixintegrate.util.JsonUtil;
import com.aspire.mirror.zabbixintegrate.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.*;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.biz.monitor
 * @Author: baiwenping
 * @CreateTime: 2020-04-24 10:02
 * @Description: ${Description}
 */
@Slf4j
@Component
public class MonitorKpiHelper {
    @Value("${monitor.kpi.batchCount: 2000}")
    private int batchCount;
    @Value("${monitor.kpi.topic: TOPIC_MONITOR_KPI}")
    private String topicName;
    @Value("${monitor.kpi.ipfrom: interface}")
    private String ipFrom;
    @Value("${local.alertIndex}")
    private String alertIndex;
    @Value("${monitor.kpi.iplist:}")
    private String ipList;
    @Autowired
    private KpiResultTransferMapper kpiResultTransferMapper;
    @Autowired
    private KpiConfigKeyMapper kpiConfigKeyMapper;
    @Autowired
    private HistoryMapper historyMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private MonitorKpiDictHelper monitorKpiDictHelper;
    @Autowired
    private KpiConfigLogsMapper kpiConfigLogsMapper;
    private static final String DB = "db";
    private static final String ES = "es";
    private static final String ZABBIX = "ZABBIX";
    private static final String HOSTS = "hosts";
    private static final String HOSTS_NAME = "hosts_name";
    public static final String DOING = "doing";

    @Async
    public void doMonitorKpiOne (KpiConfigManage kpiConfigManage,
                                 Map<String, Map<String, Object>> ciMap, long l, Map<String, Date> dateMap) {
        log.info("doMonitorKpiOne start, kpi id is {}, kpi name is {}.",  kpiConfigManage.getId(), kpiConfigManage.getKpiName());

        int size = 0;
        Date fromTime = dateMap.get("fromTime");
        Date toTime = dateMap.get("toTime");
        if (fromTime == null || toTime == null) {
            return;
        }

        // 写日志
        KpiConfigLogs logs = new KpiConfigLogs();
        logs.setConfigId(kpiConfigManage.getId());
        logs.setConfigName(kpiConfigManage.getKpiName());
        logs.setTag(alertIndex);
        logs.setFromTime(fromTime);
        logs.setToTime(toTime);
        logs.setExecTime(new Date(l));
        logs.setStatus(DOING);
        kpiConfigLogsMapper.insert(logs);

        String content = "";
        String status = "success";
        try {
            String dateSource = kpiConfigManage.getDateSource();
            List<Map<String, Object>> mapList = null;
            if (ZABBIX.equalsIgnoreCase(dateSource)) { //此处只有zabbix
                mapList = doZabbixKpi(kpiConfigManage, fromTime, toTime);
            } else if (DB.equalsIgnoreCase(dateSource)) {
                // TODO 此处zabbix算法，不需要
            } else if (ES.equalsIgnoreCase(dateSource)) {
                // TODO 此处zabbix算法，不需要
            }
            if (mapList != null && !mapList.isEmpty()) {
                size = mapList.size();
                long l1 = System.currentTimeMillis();
                resultTransferToKafka(kpiConfigManage, ciMap, mapList, fromTime);
                log.info("resultTransferToKafka use {} ms", size, (System.currentTimeMillis() - l1));
            }
            content += "处理" + size + "条监控数据。";
        } catch (Exception e) {
            log.error("doZabbixKpi error ", e);
            status = "error";
            content += e.getMessage();
        } finally {
            long l1 = System.currentTimeMillis();
            log.info("doMonitorKpiOne use {} ms, kpi id is {}, kpi name is {}.",
                    (l1 - l), kpiConfigManage.getId(), kpiConfigManage.getKpiName());
            logs.setCreateTime(new Date(l1));
            logs.setExecDuration((l1 - l) / 1000 + "s");
            logs.setStatus(status);
            logs.setContent(content);
            kpiConfigLogsMapper.update(logs);
            ciMap = null;
        }

    }

    /**
     * 查询zabbix数据
     * @param kpiConfigManage
     * @param fromTime
     * @param toTime
     * @return
     */
    public List<Map<String, Object>> doZabbixKpi (KpiConfigManage kpiConfigManage, Date fromTime, Date toTime) {
        String execFormatType = kpiConfigManage.getExecFormatType();
        String dateSource = kpiConfigManage.getDateSource();
        String id = kpiConfigManage.getId();
        long l1 = System.currentTimeMillis();
        if ("1".equals(execFormatType)) { //目前只处理sql形式
            String sql = kpiConfigManage.getExecFormat();
            if (sql.indexOf("#{ipList}") > -1) {
                if (StringUtils.isEmpty(ipList)) {
                    return new ArrayList<>();
                } else {
                    sql = sql.replaceAll("\\#\\{ipList\\}", ipList);
                }
            }
            String keysSql = getKeysById(id, dateSource);
            sql = sql.replaceAll("\\#\\{keys\\}", keysSql);
            sql = sql.replaceAll("\\#\\{fromTime\\}", "'" + DateFormatUtils.format(fromTime, "yyyy-MM-dd HH:mm:ss") + "'");
            sql = sql.replaceAll("\\#\\{toTime\\}", "'" + DateFormatUtils.format(toTime, "yyyy-MM-dd HH:mm:ss") + "'");
            log.info("sql is : {}", sql);
            List<Map<String, Object>> mapList = historyMapper.selectBySql(sql);
            int size = mapList.size();
            long l2 = System.currentTimeMillis();
            log.info("query kpi data size is : {}; use {} ms", size, (l2 - l1));
            return mapList;
        } else if ("4".equals(execFormatType)){ //内置公式，即内部定义好的方法
            String execObject = kpiConfigManage.getExecObject();
            if (StringUtils.isEmpty(execObject)) {
                execObject = "history";
                if (kpiConfigManage.getInsertObject().indexOf("uint") > -1 ||
                        kpiConfigManage.getKpiName().indexOf("百分比") > -1) {
                    execObject = "history_uint";
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("table", execObject);
            map.put("fromTime", DateFormatUtils.format(fromTime, "yyyy-MM-dd HH:mm:ss"));
            map.put("toTime", DateFormatUtils.format(toTime, "yyyy-MM-dd HH:mm:ss"));
            map.put("keys", getKeysById(id, dateSource));
            if ("history".equalsIgnoreCase(execObject)) {
                map.put("valueType", 0);
            } else if ("history_uint".equalsIgnoreCase(execObject)) {
                map.put("valueType", 3);
            }

            log.info("query kpi params are : {}", map);
            List<Map<String, Object>> mapList;
            if (HOSTS.equalsIgnoreCase(ipFrom)) {
                mapList = historyMapper.selectKpiByHosts(map);
            } else if (HOSTS_NAME.equalsIgnoreCase(ipFrom)) {
                mapList = historyMapper.selectKpiByHostsName(map);
            } else {
                mapList = historyMapper.selectKpi(map);
            }

            int size = mapList.size();
            long l2 = System.currentTimeMillis();
            log.info("query kpi data size is : {}; use {} ms", size, (l2 - l1));
            return mapList;
        } else {
            // TODO
        }
        return new ArrayList<>();
    }

    /**
     * 核心方法，处理结果转换并写入kafka
     * @param kpiConfigManage
     * @param ciMap
     * @param mapList
     */
    private void resultTransferToKafka (KpiConfigManage kpiConfigManage,
                                        Map<String, Map<String, Object>> ciMap, List<Map<String, Object>> mapList, Date fromTime) {
        String id = kpiConfigManage.getId();
        String relationCiFields = kpiConfigManage.getRelationCiFields();
        List<String> ciFieldList = new ArrayList<>();
        if (!StringUtils.isEmpty(relationCiFields)) {
            String[] split = relationCiFields.split(",");
            for (String s: split) {
                ciFieldList.add(s);
            }
        }
        List<KpiResultTransfer> transferList = kpiResultTransferMapper.selectByConfigId(id);
        int size = mapList.size();
        MonitorKpiDto monitorKpiDto = new MonitorKpiDto();
        monitorKpiDto.setDataType(kpiConfigManage.getInsertObjectType());
        monitorKpiDto.setDataTable(getInsertObject(kpiConfigManage.getInsertObject(), fromTime));
        //写入列名
        List<String> fieldList = new ArrayList<>();
        Map<String, Object> commonMap = new HashMap<>();
        Map<String, KpiResultTransfer> transferMap = new HashMap<>();
        boolean dateFlag = false;
        boolean dateStrFlag = false;
        if (transferList.isEmpty()) {
            fieldList.addAll(mapList.get(0).keySet());
        } else {
            for (KpiResultTransfer kpiResultTransfer: transferList) {
                fieldList.add(kpiResultTransfer.getTargetField());
                if ("1".equals(kpiResultTransfer.getTransferType())) {//固定值
                    if("date".equalsIgnoreCase(kpiResultTransfer.getTransferFormula())) {
                        commonMap.put(kpiResultTransfer.getTargetField(), fromTime.getTime() / 1000);
                        dateFlag = true;
                    } else if ("dateStr".equalsIgnoreCase(kpiResultTransfer.getTransferFormula())) {
                        commonMap.put(kpiResultTransfer.getTargetField(), fromTime.toInstant().toString());
                        dateStrFlag = true;
                    } else {
                        commonMap.put(kpiResultTransfer.getTargetField(), kpiResultTransfer.getTransferFormula());
                    }
                } else if (StringUtils.isEmpty(kpiResultTransfer.getSourceField())) { //此处暂不处理
                    // TODO
                } else {
                    transferMap.put(kpiResultTransfer.getSourceField(), kpiResultTransfer);
                }
            }
        }
        if (dateFlag) {
            commonMap.put("clock", fromTime.getTime() / 1000);
        }
        if (dateStrFlag) {
            commonMap.put("datetime", fromTime.toInstant().toString());
        }
        monitorKpiDto.setFieldList(fieldList);

        String field1 = null;
        //计算格式
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> map = null;
        Map<String, Object> monitorKpiMap = null;
        KpiResultTransfer kpiResultTransfer = null;
        String ip = null;
        String transferType = null;
        String objStr = null;
        for (int i = 0; i < size; i++) {
            // 每2000条批量发送到kafka
            if (i != 0 && i % batchCount == 0) {
                monitorKpiDto.setDataList(dataList);
                kafkaTemplate.send(topicName, JsonUtil.toJacksonJson(monitorKpiDto));
                dataList.clear();
            }

            map = mapList.get(i);

            if (!ciFieldList.isEmpty()) {
                if (StringUtils.isEmpty(field1)) {
                    for (String key: map.keySet()) {
                        if ("ip".equalsIgnoreCase(key)) {
                            field1 = key;
                            break;
                        }
                    }
                }
                if (StringUtils.isEmpty(field1)) {
                    for (Map.Entry<String, Object> entry: map.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (value == null) {
                            continue;
                        }
                        String valueStr = value.toString();
                        if (valueStr.matches("(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})")) {
                            field1 = key;
                            break;
                        }
                    }
                }
                if (!StringUtils.isEmpty(field1)) {
                    ip = MapUtils.getString(map, field1);
                    if (ip != null) {
                        Map<String, Object> cmdbInstance = ciMap.get(ip);
                        if (cmdbInstance != null) {
                            for (String ciField: ciFieldList) {
                                map.put(ciField, cmdbInstance.get(ciField));
                            }
                        } else {
                            map.put("tag", alertIndex);
                        }
                    }
                }
            }

            if (transferList.isEmpty()) {
                map.putAll(commonMap);
                dataList.add(map);
            } else {
                monitorKpiMap = new HashMap<>();
                monitorKpiMap.putAll(commonMap);
                for (String key: transferMap.keySet()) {
                    kpiResultTransfer = transferMap.get(key);
                    transferType = kpiResultTransfer.getTransferType();
                    if (StringUtils.isEmpty(transferType)) {
                        monitorKpiMap.put(kpiResultTransfer.getTargetField(), map.get(key));
                    } else if ("4".equals(transferType)) { //字典
                        objStr = MapUtils.getString(map, key);
                        monitorKpiMap.put(kpiResultTransfer.getTargetField(), monitorKpiDictHelper.getValueByName(kpiResultTransfer.getTransferFormula(), objStr));
                    } else if ("2".equals(transferType)) {
                        // TODO
                        monitorKpiMap.put(kpiResultTransfer.getTargetField(), map.get(key));
                    } else if ("3".equals(transferType)) {
                        // TODO
                        monitorKpiMap.put(kpiResultTransfer.getTargetField(), map.get(key));
                    } else {
                        // TODO
                        monitorKpiMap.put(kpiResultTransfer.getTargetField(), map.get(key));
                    }
                }
                dataList.add(monitorKpiMap);
            }

        }
        if (dataList.size() > 0) {
            monitorKpiDto.setDataList(dataList);
            kafkaTemplate.send(topicName, JsonUtil.toJacksonJson(monitorKpiDto));
            dataList.clear();
        }
        mapList.clear();
        transferList.clear();
    }

    /**
     * 获取表或者索引精确的值，转换时间戳
     * @auther baiwenping
     * @Description
     * @Date 14:28 2020/4/20
     * @Param [insertObject, date]
     * @return java.lang.String
     **/
    private String getInsertObject (String insertObject, Date date) {
        if (insertObject.matches("^.*\\{.+\\}.*$")) {
            String format = insertObject.replaceAll("^.*\\{", "").replaceAll("\\}.*$", "");
            try {
                String formatDate = DateFormatUtils.format(date, format);
                insertObject = insertObject.replaceAll("\\{" + format + "\\}", formatDate);
            } catch (Exception e) {
                insertObject = insertObject.replaceAll("\\{", "").replaceAll("\\}", "");
            }
        }
        return insertObject;
    }

    /**
     *
     * @auther baiwenping
     * @Description
     * @Date 17:46 2020/4/17
     * @Param [id, dateSource]
     * @return java.lang.String
     **/
    private String getKeysById (String id, String dateSource) {
        List<KpiConfigKey> kpiConfigKeyList = kpiConfigKeyMapper.selectByConfigId(id);
        String returnStr = null;
        if (DB.equalsIgnoreCase(dateSource) || ZABBIX.equalsIgnoreCase(dateSource)) {
            returnStr = CommonUtils.generateKeysDb(kpiConfigKeyList);
        } else if(ES.equalsIgnoreCase(dateSource)) {

        }
        return returnStr;
    }

    /**
     *
     * @auther baiwenping
     * @Description
     * @Date 17:46 2020/4/17
     * @Param [id, dateSource]
     * @return java.lang.String
     **/
    private List<String> getKeyListById (String id, String dateSource) {
        List<KpiConfigKey> kpiConfigKeyList = kpiConfigKeyMapper.selectByConfigId(id);
        if (DB.equalsIgnoreCase(dateSource) || ZABBIX.equalsIgnoreCase(dateSource)) {
            return CommonUtils.generateKeyListDb(kpiConfigKeyList);
        } else if(ES.equalsIgnoreCase(dateSource)) {

        }
        return new ArrayList<>();
    }




    public String getZabbix() {
        return ZABBIX;
    }
}
