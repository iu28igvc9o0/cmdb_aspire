package com.aspire.mirror.zabbixintegrate.biz.alert;

import static com.aspire.mirror.zabbixintegrate.domain.AlertModel.MONI_RESULT_ACTIVE;
import static com.aspire.mirror.zabbixintegrate.domain.AlertModel.MONI_RESULT_REVOKE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.aspire.mirror.zabbixintegrate.domain.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.zabbixintegrate.dao.ZabbixDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
class ZabbixBiz {
    public static final String SOURCE_ZABBIX = "ZABBIX";
    public static final String TIME_FORMAT = "yyyy.MM.dd HH:mm:ss";
    @Value("${local.alertIndex}")
    private String alertIndex;
    @Value("${local.idctype:}")
    private String idctype;
    @Value("${local.ignoreAlertKey:(?!AlarmTime)(?!AlarmOID)(?!AlarmDesc)}")
    private String ignoreAlertKey;
    @Autowired
    private ZabbixDao dao;
    @Autowired
    private AlertScanIndexHolder scanIdxHolder;

    public ZabbixAlertScanIndex fetchDbScanIndex(Integer id) {
        return dao.fetchScanIndex(id);
    }

    public void insertScanIndex(ZabbixAlertScanIndex initScanIndex) {
        dao.insertScanIndex(initScanIndex);
    }

    public void updateScanIndex(ZabbixAlertScanIndex updateModel) {
        dao.updateScanIndex(updateModel);
    }

    /**
     * 抓取监控告警数据. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param startScanIndex
     * @param actionIdArr
     * @param batchCount
     * @return
     */
    public List<AlertModel> fetchAlertList(Long startScanIndex, Integer[] actionIdArr, Integer batchCount) {
        Map<String, Object> params = new HashMap<>();
        params.put("startScanIndex", startScanIndex);
        params.put("actionIdArr", actionIdArr);
        params.put("batchCount", batchCount);
        List<RawAlert> rawAlertList = dao.fetchAlertEventList(params);
        List<AlertModel> resultList = new ArrayList<>();

        int indexNum = 0;
        for (RawAlert rawAlert : rawAlertList) {
            Long alertIdNum = Long.parseLong(rawAlert.getAlertId());
            if (alertIdNum > startScanIndex) {
                startScanIndex = alertIdNum;
            }
//			log.info("alert message is : {}", rawAlert.getMessage());
            Map<String, String> map = convertMsgContent2Map(rawAlert.getMessage());
            if (!NumberUtils.isDigits(map.get("itemid"))) {
                log.error("The scaned alert message has no itemid, the alert will be ignored!!");
                continue;
            }
            AlertModel model = new AlertModel();
            model.setIndexNum(indexNum++);
            String umsAlertLevel = UmsAlertMetaConfig.getMatchedUmsLevel(map.get("alertLevel"));
//			model.setUmsAlertId(createUmsAlertId(this.alertIndex, map.get("itemid"), umsAlertLevel));
            model.setAlertId(createUmsAlertId(this.alertIndex, rawAlert.getActionId(), map.get("itemid"), umsAlertLevel));
//			model.setZbxAlertId(rawAlert.getAlertId());
//			model.setZbxEventId(rawAlert.getEventId());
            model.setZbxItemId(Integer.parseInt(map.get("itemid")));
            model.setSubject(rawAlert.getSubject());
            model.setHostName(map.get("hostname"));
            model.setDeviceIP(map.get("device"));
            model.setItemKey(map.get("key"));
            String businessSystem = map.get("businessSystem");
            if (StringUtils.isNotEmpty(businessSystem)) {
                //model.setBusinessSystem(businessSystem);
                model.setBusinessSystem(idctype);
            }
//            else if (StringUtils.isNotEmpty(idctype)) {
//                model.setBusinessSystem(idctype);
//            }
            Date clockDate = new Date(rawAlert.getClock() * 1000L);
            String parseTime = new SimpleDateFormat(TIME_FORMAT).format(clockDate);
            model.setCurMoniTime(parseTime);
            model.setMonitorIndex(getMoniIndex(map));
            model.setMoniIndexValue(map.get("moniIndexValue"));
            model.setCurMoniValue(map.get("curMoniValue"));
//			model.setCurMoniTime(map.get("curMoniTime"));
            model.setAlertStartTime(map.get("alertStartTime"));
//			model.setAlertDesc(map.get("alertDesc"));
            model.setKeyComment(map.get("alertDesc"));
            model.setAlertLevel(umsAlertLevel);
            String moniResult = rawAlert.getEventId().equals(map.get("alertId")) ? MONI_RESULT_ACTIVE : MONI_RESULT_REVOKE;
            model.setMoniResult(moniResult);
//			model.setOldEventId(map.get("alertId"));
//			model.setActionId(rawAlert.getActionId());
//			model.setPrefix(this.alertIndex);
            model.setSource(SOURCE_ZABBIX);
            resultList.add(model);
        }
        // 更新缓存中的 扫描游标
        scanIdxHolder.updateScanIndex(startScanIndex);
        popupExtraInfos(resultList);
        return resultList;
    }

    private void popupExtraInfos(final List<AlertModel> alertList) {
        Set<Integer> itemIdList = alertList.stream().map(alert -> {
            return alert.getZbxItemId();
        }).collect(Collectors.toSet());

        Map<Integer, String> itemMapMoniObj = new HashMap<>();
        List<List<Integer>> groupList = groupItemIds(new ArrayList<Integer>(itemIdList));
        groupList.stream().forEach(group -> {
            List<ItemIdMapMoniObj> itemIdMapMoniObj = dao.fetchMoniObjByItemIdArr(group);
            itemIdMapMoniObj.stream().forEach(moniObj -> {
                itemMapMoniObj.put(moniObj.getItemId(), moniObj.getMoniObject());
            });
        });

        alertList.stream().forEach(alert -> {
            alert.setMonitorObject(itemMapMoniObj.get(alert.getZbxItemId()));
        });
    }

    private List<List<Integer>> groupItemIds(List<Integer> itemIdList) {
        List<List<Integer>> groupList = new ArrayList<List<Integer>>();
        if (CollectionUtils.isEmpty(itemIdList)) {
            return groupList;
        }

        int startIdx = 0;
        int endIdx = 0;
        int groupCount = 200;

        while (true) {
            if (startIdx + groupCount <= itemIdList.size()) {
                endIdx = startIdx + groupCount;
            } else if (startIdx < itemIdList.size()) {
                endIdx = itemIdList.size();
            }
            if (endIdx > startIdx) {
                groupList.add(itemIdList.subList(startIdx, endIdx));
                startIdx = endIdx;
                continue;
            }
            break;
        }
        return groupList;
    }

    private Map<String, String> convertMsgContent2Map(String msg) {
        Map<String, String> keyValMap = new HashMap<String, String>();
        if (StringUtils.isBlank(msg)) {
            return keyValMap;
        }

        // MYSQL中 每一个键值对为一行, 此处转换成ORALCE中相同的方式，即key1:value1&key2:value2&...
        if (msg.indexOf("&") < 0) {
            StringBuilder sb = new StringBuilder();
            // 把换行替换成&
            Matcher lineMatch = Pattern.compile("(?m)^.*$").matcher(msg);
            while (lineMatch.find()) {
                String line = lineMatch.group();
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                sb.append(line).append("&");
            }
            msg = sb.toString();
        }

        msg = "&" + msg;
        String regex = "&(";
        if (StringUtils.isNotEmpty(ignoreAlertKey)) {
            regex += ignoreAlertKey;
        }
        regex += "\\w+):";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(msg);

        List<String> keyList = new ArrayList<String>();
        List<String> valList = new ArrayList<String>();

        int valBeginIdx = -1;
        while (matcher.find()) {
            String key = matcher.group(1);
            keyList.add(key);

            if (valBeginIdx >= 0) {
                String val = resolveLineWrap(msg.substring(valBeginIdx, matcher.start()), "&");
                valList.add(val);
            }

            valBeginIdx = matcher.end();
        }

        if (valBeginIdx > 0) {
            String val = resolveLineWrap(msg.substring(valBeginIdx), "&");
            valList.add(val);
        }

        for (int i = 0; i < keyList.size(); i++) {
            keyValMap.put(keyList.get(i), valList.get(i));
        }
        return keyValMap;
    }

    private String resolveLineWrap(String valText, String wrapMark) {
        return valText.replaceAll(wrapMark, "\n").trim();
    }

    private String getMoniIndex(Map<String, String> map) {
        String device = map.get("device");
        String moniIndex = map.get("moniIndex");
        if (null != device && null != moniIndex) {
            return moniIndex.replace(device, "").trim();
        }
        return moniIndex;
    }

    /**
     * 生成umsAlertId. 注意: 之前的zabbix.jar, 生成umsAlertId时,
     * 需要append自动消警次数, 产品化后, 此逻辑移入上层alert_service服务的逻辑中 <br/>
     * <p>
     * 生成规则: 	     样例: XXG_7_11772_4 <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param alertIndex
     * @param itemId
     * @param alertLevel
     * @return
     */
    private String createUmsAlertId(String alertIndex, String actionId, String itemId, String alertLevel) {
        return alertIndex + "_" + actionId + "_" + itemId + "_" + alertLevel;
    }

//    public static void main(String[] args) {
//        String message = "alertId:448640331\n" +
//                "device:10.193.106.120\n" +
//                "businessSystem:Hrb_Proxy_215\n" +
//                "moniIndex:10.193.106.120  TRAP告警-南向设备配置未保存\n" +
//                "alertDesc:\n" +
//                "alertStartTime:2019.09.11 10:18:39\n" +
//                "curMoniValue:10:18:38 2019/09/11 PDU INFO:\n" +
//                "AlarmOID:                 0x80001370010ac170611c031877\n" +
//                "AlarmDesc:               TRAP\n" +
//                "  version                        3\n" +
//                "  receivedfrom                   UDP: [10.193.106.120]:1061->[10.193.14.215]:162\n" +
//                "  errorstatus                    0\n" +
//                "  messageid                      1595655780\n" +
//                "  securitylevel                  1\n" +
//                "  securityEngineID               0x80001370010ac170611c031877\n" +
//                "  securityName                   snmptrap\n" +
//                "  contextName                    \n" +
//                "  securitymodel                  3\n" +
//                "  transactionid                  47235\n" +
//                "  errorindex                     0\n" +
//                "  requestid                      1965197367\n" +
//                "VARBINDS:\n" +
//                "  DISMAN-EVENT-MIB::sysUpTimeInstance type=67 value=Timeticks: (43406407) 5 days, 0:34:24.07\n" +
//                "  SNMPv2-MIB::snmpTrapOID.0      type=6  value=OID: SNMPv2-SMI::enterprises.2011.5.119.3.13.12.2.1\n" +
//                "  SNMPv2-SMI::enterprises.2011.5.119.3.13.12.1.1.0 type=4  value=STRING: \"a63cb213-6ff7-384c-aa87-4f626ca5d062\"\n" +
//                "  SNMPv2-SMI::enterprises.2011.5.119.3.13.12.1.2.0 type=4  value=STRING: \"HRB-PCRP21-POD4P-CE6855-90\"\n" +
//                "  SNMPv2-SMI::enterprises.2011.5.119.3.13.12.1.3.0 type=64 value=IpAddress: 10.192.207.42\n" +
//                "curMoniTime:2019.09.11 10:18:21\n" +
//                "moniIndexValue:*UNKNOWN*\n" +
//                "indexMoniResult:1\n" +
//                "alertLevel:4\n" +
//                "trrigerid:2144716\n" +
//                "itemid:4169771\n" +
//                "hostname:SDN-POD4-Trap  10.193.106.120";
//        ZabbixBiz z = new ZabbixBiz();
//        Map<String, String> map = z.convertMsgContent2Map(message);
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + "---长度：" + entry.getValue().length() + ",-----:" + entry.getValue());
//        }
//    }
}
