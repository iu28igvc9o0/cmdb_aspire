package com.aspire.mirror.alert.server.task.monitor;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.alert.server.clientservice.elasticsearch.MonitorKpiServiceClient;
import com.aspire.mirror.alert.server.vo.kpi.MonitorKpiVo;
import com.aspire.mirror.alert.server.util.JsonUtil;
import com.aspire.mirror.alert.server.util.MapUtils;
import com.aspire.mirror.alert.server.dao.kpi.KpiConfigKeyMapper;
import com.aspire.mirror.alert.server.dao.kpi.KpiConfigLogsMapper;
import com.aspire.mirror.alert.server.dao.kpi.KpiResultTransferMapper;
import com.aspire.mirror.alert.server.dao.kpi.po.KpiConfigKey;
import com.aspire.mirror.alert.server.dao.kpi.po.KpiConfigLogs;
import com.aspire.mirror.alert.server.dao.kpi.po.KpiConfigManage;
import com.aspire.mirror.alert.server.dao.kpi.po.KpiResultTransfer;
import com.aspire.mirror.elasticsearch.api.dto.monitor.MonitorCommonRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
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
    @Autowired
    private KpiResultTransferMapper kpiResultTransferMapper;
    @Autowired
    private KpiConfigKeyMapper kpiConfigKeyMapper;
//    @Autowired
//    private HistoryMapper historyMapper;
    @Autowired
    private MonitorKpiServiceClient monitorKpiServiceClient;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private MonitorKpiDictHelper monitorKpiDictHelper;
    @Autowired
    private KpiConfigLogsMapper kpiConfigLogsMapper;
    private static final String DB = "db";
    private static final String ES = "es";
    private static final String ZABBIX = "ZABBIX";
    public static final String DOING = "doing";

    @Async
    public void doMonitorKpiOne (KpiConfigManage kpiConfigManage,
                                 Map<String, Map<String, Map<String, Object>>> ciMap, long l, Map<String, Date> dateMap) {
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
                mapList = doESKpi(kpiConfigManage, fromTime, toTime);
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
        }

    }

    /**
     * 查询ES数据
     * @param kpiConfigManage
     * @param fromTime
     * @param toTime
     * @return
     */
    private List<Map<String, Object>> doESKpi (KpiConfigManage kpiConfigManage, Date fromTime, Date toTime) {
        String execFormatType = kpiConfigManage.getExecFormatType();
        String dateSource = kpiConfigManage.getDateSource();
        String id = kpiConfigManage.getId();
        long l1 = System.currentTimeMillis();
        MonitorCommonRequest monitorCommonRequest = new MonitorCommonRequest();

        String execObject = kpiConfigManage.getExecObject();
        if (execObject == null) {
            return Lists.newArrayList();
        }
        monitorCommonRequest.setIndex(getExecIndexs(kpiConfigManage.getExecObject(), fromTime, toTime));
        String keysSql = getKeysById(id, dateSource);
        if ("1".equals(execFormatType)) { //目前只处理dsl形式
            String sql = kpiConfigManage.getExecFormat();
            if (StringUtils.isEmpty(sql)) {
                throw new RuntimeException("exec format is null");
            }
            sql = sql.replaceAll("\\#\\{keys\\}", keysSql);
            sql = replaceTimeES(sql, "fromTime", fromTime);
            sql = replaceTimeES(sql, "toTime", toTime);
            log.info("dsl is : {}", sql);
            monitorCommonRequest.setDsl(JSON.parseObject(sql));
        } else if ("4".equals(execFormatType)){ //内置公式，即内部定义好的方法
            Map<String, Object> map = Maps.newHashMap();
            map.put("size", 0);
            List<Object> list = Lists.newArrayList();
            if (!StringUtils.isEmpty(keysSql)) {
                list.add(keysSql);
            }
            String execFilter = kpiConfigManage.getExecFilter();
            if (!StringUtils.isEmpty(execFilter)) {
                execFilter = execFilter.replaceAll("\\#\\{fromTime\\}", DateFormatUtils.format(fromTime, "yyyy-MM-dd'T'HH:mm:ssZ"));
                execFilter = execFilter.replaceAll("\\#\\{toTime\\}",  DateFormatUtils.format(toTime, "yyyy-MM-dd'T'HH:mm:ssZ"));
                list.add(execFilter);
            }

            if (!list.isEmpty()) {
                Map<String, Object> map1 = Maps.newHashMap();
                Map<String, Object> map2 = Maps.newHashMap();
                map1.put("must", list);
                map2.put("bool", map1);
                map.put("query", map2);
            }

            String execFormat = kpiConfigManage.getExecFormat();
            if (StringUtils.isEmpty(execFormat)) {
                throw new RuntimeException("exec format is null");
            }
            String[] split = execFormat.split(",");
            Map<String, Object> aggs = getAggs(split, 0);
            map.putAll(aggs);
            monitorCommonRequest.setDsl(JSON.parseObject(JSON.toJSONString(map)));
            log.info("dsl is : {}", monitorCommonRequest.getDsl());
        } else {
            return new ArrayList<>();
        }
        List<Map<String, Object>> mapList = monitorKpiServiceClient.query(monitorCommonRequest);
        int size = mapList.size();
        long l2 = System.currentTimeMillis();
        log.info("query kpi data size is : {}; use {} ms", size, (l2 - l1));
        return mapList;
    }

    /**
     * 查询zabbix数据
     * @param kpiConfigManage
     * @param fromTime
     * @param toTime
     * @return
     */
    private List<Map<String, Object>> doZabbixKpi (KpiConfigManage kpiConfigManage, Date fromTime, Date toTime) {
        String execFormatType = kpiConfigManage.getExecFormatType();
        String dateSource = kpiConfigManage.getDateSource();
        String id = kpiConfigManage.getId();
        long l1 = System.currentTimeMillis();
        if ("1".equals(execFormatType)) { //目前只处理sql形式
            String sql = kpiConfigManage.getExecFormat();
            String keysSql = getKeysById(id, dateSource);
            sql = sql.replaceAll("\\#\\{keys\\}", keysSql);
            sql = sql.replaceAll("\\#\\{fromTime\\}", "'" + DateFormatUtils.format(fromTime, "yyyy-MM-dd HH:mm:ss") + "'");
            sql = sql.replaceAll("\\#\\{toTime\\}", "'" + DateFormatUtils.format(toTime, "yyyy-MM-dd HH:mm:ss") + "'");
            log.info("sql is : {}", sql);
            //zabbix不在此处处理，所以初始化为空
            List<Map<String, Object>> mapList = Lists.newArrayList();
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
            List<Map<String, Object>> mapList = Lists.newArrayList();

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
                                        Map<String, Map<String, Map<String, Object>>> ciMap, List<Map<String, Object>> mapList, Date fromTime) {
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
        MonitorKpiVo monitorKpiVo = new MonitorKpiVo();
        monitorKpiVo.setDataType(kpiConfigManage.getInsertObjectType());
        monitorKpiVo.setDataTable(getInsertObject(kpiConfigManage.getInsertObject(), fromTime));
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
        monitorKpiVo.setFieldList(fieldList);

        String field1 = null;
        //计算格式
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> map = null;
        Map<String, Object> monitorKpiMap = null;
        KpiResultTransfer kpiResultTransfer = null;
        String ip = null;
        String transferType = null;
        String objStr = null;
        Map<String, Object> cmdbInstance = null;
        Map<String, Map<String, Object>> idInstanceMap = null;
        for (int i = 0; i < size; i++) {
            // 每2000条批量发送到kafka
            if (i != 0 && i % batchCount == 0) {
                monitorKpiVo.setDataList(dataList);
                kafkaTemplate.send(topicName, JsonUtil.toJacksonJson(monitorKpiVo));
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
                    String instanceId = MapUtils.getString(map, "instance_id");
                    if (ip != null) {
                        idInstanceMap = ciMap.get(ip);
                        cmdbInstance = null;
                        if (!CollectionUtils.isEmpty(idInstanceMap)) {
                            if (StringUtils.isEmpty(instanceId)) {
                                cmdbInstance = idInstanceMap.values().stream().findFirst().get();
                            } else {
                                cmdbInstance = idInstanceMap.get(instanceId);
                            }
                        }

                        if (cmdbInstance != null) {
                            for (String ciField: ciFieldList) {
                                map.put(ciField, cmdbInstance.get(ciField));
                            }
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
            monitorKpiVo.setDataList(dataList);
            kafkaTemplate.send(topicName, JsonUtil.toJacksonJson(monitorKpiVo));
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
     * @param execObject
     * @param fromTime
     * @param toTime
     * @return
     */
    private String getExecIndexs (String execObject, Date fromTime, Date toTime) {
        String execIndexs = execObject;
        if (execObject.matches("^.*\\{.+\\}.*$")) {
            String format = execObject.replaceAll("^.*\\{", "").replaceAll("\\}.*$", "");
            try {
                long duration = checkDuration(format, fromTime);
                Set<String> set = Sets.newHashSet();
                long time = fromTime.getTime();
                set.add(getExecIndex(execObject, time, format));
                long toTimeMs = toTime.getTime();
                set.add(getExecIndex(execObject, toTimeMs, format));
                time += duration;
                while (toTimeMs > time) {

                    set.add(getExecIndex(execObject, time, format));
                    time += duration;
                }
                execIndexs = org.apache.commons.lang.StringUtils.join(set, ",");
            } catch (Exception e) {
                execIndexs = execObject.replaceAll("\\{", "").replaceAll("\\}", "");
            }
        }
        return execIndexs;
    }

    /**
     * 生成执行索引
     * @param execObject
     * @param l
     * @param format
     * @return
     */
    private String getExecIndex(String execObject, long l, String format) {
        String formatDate = DateFormatUtils.format(l, format);
        String execIndex = execObject.replaceAll("\\{" + format + "\\}", formatDate);
        return execIndex;
    }

    /**
     * 时间范围最小以1小时为单位
     * @param format
     * @param fromTime
     * @return
     */
    private long checkDuration (String format, Date fromTime) {
        long duration = 1L;
        long time = fromTime.getTime();
        String formatDate1 = DateFormatUtils.format(fromTime, format);
        long time1 = time + duration * 3600 * 1000;
        String formatDate2 = DateFormatUtils.format(time1, format);
        if (!formatDate1.equals(formatDate2)) {
            time1 = time1 + duration * 3600 * 1000;
            String formatDate3 = DateFormatUtils.format(time1, format);
            if (!formatDate3.equals(formatDate2)) {
                return duration * 3600 * 1000;
            }
        }
        duration = 24L;
        time1 = time + duration * 3600 * 1000;
        String formatDate4 = DateFormatUtils.format(time1, format);
        if (!formatDate1.equals(formatDate4)) {
            time1 = time1 + duration * 3600 * 1000;
            String formatDate5 = DateFormatUtils.format(time1, format);
            if (!formatDate5.equals(formatDate4)) {
                return duration * 3600 * 1000;
            }
        }
        duration = 28L * 24;
        return duration * 3600 * 1000;
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
            returnStr = generateKeysDb(kpiConfigKeyList);
        } else if(ES.equalsIgnoreCase(dateSource)) {
            returnStr = generateKeysES(kpiConfigKeyList);
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
    @Deprecated
    private List<String> getKeyListById (String id, String dateSource) {
        List<KpiConfigKey> kpiConfigKeyList = kpiConfigKeyMapper.selectByConfigId(id);
        if (DB.equalsIgnoreCase(dateSource) || ZABBIX.equalsIgnoreCase(dateSource)) {
            return generateKeyListDb(kpiConfigKeyList);
        } else if(ES.equalsIgnoreCase(dateSource)) {

        }
        return new ArrayList<>();
    }

    /**
     * 把指标项组成数据库所需的格式
     * @auther baiwenping
     * @Description
     * @Date 17:46 2020/4/17
     * @Param [kpiConfigKeyList]
     * @return java.lang.String
     **/
    private List<String>  generateKeyListDb (List<KpiConfigKey> kpiConfigKeyList) {
        List<String> list = new ArrayList<>();
        if (kpiConfigKeyList.isEmpty()) {
            list.add("1=1");
            return list;
        }
        StringBuilder sb1 = new StringBuilder("");
        String field = kpiConfigKeyList.get(0).getKpiKeyField();
        for (KpiConfigKey kpiConfigKey: kpiConfigKeyList) {
            String kpiKeyModel = kpiConfigKey.getKpiKeyModel();
            if (StringUtils.isEmpty(kpiKeyModel) || "1".equals(kpiKeyModel)) {
                sb1.append("'").append(kpiConfigKey.getKpiKey()).append("',");
            } else if ("2".equals(kpiKeyModel)) {
                list.add(kpiConfigKey.getKpiKeyField() + " like '" + kpiConfigKey.getKpiKey() + "%' ");
            }
        }
        if (sb1.length() > 0) {
            list.add(field + " in (" + sb1.substring(0, sb1.length() -1) + ")");
        }
        return list;
    }

    /**
     * 把指标项组成数据库所需的格式
     * @auther baiwenping
     * @Description
     * @Date 17:46 2020/4/17
     * @Param [kpiConfigKeyList]
     * @return java.lang.String
     **/
    private String  generateKeysDb (List<KpiConfigKey> kpiConfigKeyList) {

        if (kpiConfigKeyList.isEmpty()) {
            return "1=1";
        }
        StringBuilder sb = new StringBuilder("(");
        StringBuilder sb1 = new StringBuilder("");
        String field = null;
        for (KpiConfigKey kpiConfigKey: kpiConfigKeyList) {
            String kpiKeyModel = kpiConfigKey.getKpiKeyModel();
            if (StringUtils.isEmpty(kpiKeyModel) || "1".equals(kpiKeyModel)) {
                field = kpiConfigKey.getKpiKeyField();
                sb1.append("'").append(kpiConfigKey.getKpiKey()).append("',");
            } else if ("2".equals(kpiKeyModel)) {
                sb.append(kpiConfigKey.getKpiKeyField()).append(" like '").append(kpiConfigKey.getKpiKey())
                        .append("%' or ");
            }
        }
        String returnStr = null;
        if (sb1.length() > 0) {
            sb.append(field).append(" in (").append(sb1.substring(0, sb1.length() -1)).append("))");
            returnStr = sb.toString();
        } else if (sb.length() > 1) {
            returnStr = sb.substring(0, sb.length() -3) + ")";
        } else {
            returnStr = "1=1";
        }
        return returnStr;
    }

    /**
     * 把指标项组成ES查询所需的格式
     * @auther baiwenping
     * @Description
     * @Date 17:46 2020/4/17
     * @Param [kpiConfigKeyList]
     * @return java.lang.String
     **/
    private String generateKeysES (List<KpiConfigKey> kpiConfigKeyList) {

        if (kpiConfigKeyList.isEmpty()) {
            return "{}";
        }
        List<Map<String, Object>> list = Lists.newArrayList();
        Set<String> set = Sets.newHashSet();
        String field = null;
        Map<String, Object> map1;
        Map<String, Object> map2;
        for (KpiConfigKey kpiConfigKey: kpiConfigKeyList) {
            String kpiKeyModel = kpiConfigKey.getKpiKeyModel();
            if (StringUtils.isEmpty(kpiKeyModel) || "1".equals(kpiKeyModel)) {
                field = kpiConfigKey.getKpiKeyField();
                set.add(kpiConfigKey.getKpiKey());
            } else if ("2".equals(kpiKeyModel)) {
                map1 = new HashMap<>(1);
                map1.put(kpiConfigKey.getKpiKeyField(), kpiConfigKey.getKpiKey());
                map2 = new HashMap<>(1);
                map2.put("match_phrase_prefix", map1);
                list.add(map2);
            }
        }

        if (!set.isEmpty()) {
            map1 = new HashMap<>(1);
            map1.put(field, set);
            map2 = new HashMap<>(1);
            map2.put("terms", map1);
            list.add(map2);
        }

        if (list.isEmpty()) {
            return "";
        }
        map1 = new HashMap<>(1);
        map1.put("should", list);
        map2 = new HashMap<>(1);
        map2.put("bool", map1);
        String returnStr = JSON.toJSONString(map2);
        map1.clear();
        map1.clear();
        list.clear();
        set.clear();
        return returnStr;
    }

    /**
     * 组装聚合语句
     * @param split
     * @param index
     * @return
     */
    private Map<String, Object> getAggs (String[] split, int index) {
        int length = split.length;
        if (index >= length) {
            return null;
        }

        String s = split[index];
        Map<String, Object> map1 = new HashMap<>(1);
        map1.put("field", s);

        Map<String, Object> map2 = Maps.newHashMap();
        if (index == length -1) {
            map2.put("stats", map1);
        } else {
            map2.put("terms", map1);
        }

        Map<String, Object> map3 = getAggs(split, ++index);
        if (map3 != null) {
            map2.putAll(map3);
        }

        Map<String, Object> map4 =new HashMap<>(1);
        map4.put(s, map2);
        Map<String, Object> map5 = new HashMap<>(1);
        map5.put("aggs", map4);
        return map5;
    }

    /**
     *
     * @param str
     * @param keyword
     * @param time
     * @return
     */
    private String replaceTimeES (String str, String keyword, Date time) {
        str = str.replaceAll("\\#\\{"+keyword+"\\}", DateFormatUtils.format(time, "yyyy-MM-dd'T'HH:mm:ssZ"));
        str = str.replaceAll("\\#\\{"+keyword+",str\\}", DateFormatUtils.format(time, "yyyy-MM-dd'T'HH:mm:ssZ"));
        str = str.replaceAll("\\#\\{"+keyword+",s\\}", (time.getTime() / 1000) + "");
        str = str.replaceAll("\\#\\{"+keyword+",ms\\}", time.getTime() + "");
        return str;
    }

    public String getZabbix() {
        return ZABBIX;
    }
}
