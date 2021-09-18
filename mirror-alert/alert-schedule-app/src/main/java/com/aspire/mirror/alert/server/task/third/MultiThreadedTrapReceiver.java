
package com.aspire.mirror.alert.server.task.third;

import com.aspire.mirror.alert.server.biz.helper.CmdbHelper;
import com.aspire.mirror.alert.server.util.MapUtils;
import com.aspire.mirror.alert.server.biz.helper.AlertsHandleV2Helper;
import com.aspire.mirror.alert.server.vo.alert.AlertsV2Vo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本类用于监听代理进程的Trap信息
 *
 * @author zhanjia
 */
@Component
public class MultiThreadedTrapReceiver implements CommandResponder {

    private MultiThreadedMessageDispatcher dispatcher;
    private Snmp snmp = null;
    private Address listenAddress;
    private ThreadPool threadPool;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Value("${threadPool.size: 10}")
    private Integer threadPoolSize;

    @Value("${snmp4j.listenAddress: 0.0.0.0/8362}")
    private String snmp4jListenAddress;
    @Value("${snmp4j.openFlag: true}")
    private boolean snmp4jOpenFlag;

    @Autowired
    private AlertsHandleV2Helper alertHandleHelper;
    @Autowired
    private CmdbHelper cmdbHelper;

    private final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();



    public MultiThreadedTrapReceiver() { // BasicConfigurator.configure();
    }

    private void init() throws IOException {
        threadPool = ThreadPool.create("Trap", threadPoolSize);
        dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
        listenAddress = GenericAddress.parse(snmp4jListenAddress); // 本地IP与监听端口
        TransportMapping transport;
        // 对TCP与UDP协议进行处理
        if (listenAddress instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
        } else {
            transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
        }
        snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        snmp.listen();
    }

    @PostConstruct
    public void run() {
        if (!snmp4jOpenFlag) {
            return;
        }
        try {
            logger.info("启动监听Trap信息。。。");
            init();
            initCAlertMap();
            snmp.addCommandResponder(this);
            logger.info("开始监听Trap信息!!!!!!");
        } catch (Exception ex) {
            logger.error("启动监听Trap失败,", ex);
        }
    }

    /**
     * 实现CommandResponder的processPdu方法, 用于处理传入的请求、PDU等信息 当接收到trap时，会自动进入这个方法
     *
     * @param respEvnt
     */

    public void processPdu(CommandResponderEvent respEvnt) { // 解析Response
        if (respEvnt != null && respEvnt.getPDU() != null) {
            Vector<? extends VariableBinding> recVBs = respEvnt.getPDU().getVariableBindings();
            Map<String, Object> newMap = new HashMap<String, Object>();
            for (int i = 0; i < recVBs.size(); i++) {
                VariableBinding recVB = recVBs.elementAt(i);
                String os = "";
                if (null != recVB.getVariable() && !recVB.getVariable().equals("")) {
                    os = getTransBase(recVB.getVariable().toString());
                }
//                logger.info("告警同步数据： Key = " + recVB.getOid() + ", Value = " + os);
                Iterator<Entry<String, String>> entries = map.entrySet().iterator();
                while (entries.hasNext()) {
                    Entry<String, String> entry = entries.next();
                    if (recVB.getOid().toString().equals(entry.getKey())) {
                        newMap.put(entry.getValue(), os);
                    }
                }
            }
            if (null == newMap.get("sourceIP")) {
                logger.info("告警没有ip，忽略数据");
                return;
            }
            logger.info("运行提示:告警信息同步开始...");
            logger.info("告警数据：" + newMap);

//            List<AlertFieldRequestDTO> alertFieldList = alertHandleHelper.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT);

            // 这里也可以使用构造方法
            if (null != MapUtils.getString(newMap, "notifyType")) {
                int result = 0;
                boolean flag = true;
                try { // 新增告
                    AlertsV2Vo alert = installData(newMap);
                    if (StringUtils.isNotEmpty(alert.getAlertType())) {
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        String jsonString = "{}";
//                        try {
//                            jsonString = objectMapper.writeValueAsString(alert);
//                        } catch (JsonProcessingException e) {
//                        }
//                        JSONObject alertJson = JSONObject.parseObject(jsonString);
//                        //处理cmdb数据
//                        if (com.aspire.mirror.alert.server.util.StringUtils.isNotEmpty(alert.getDeviceIp())){
//                            // 根据  机房 + IP, 查找设备
//                            cmdbHelper.queryDeviceForAlertV2(alertJson, alertFieldList,alertHandleHelper.getModelField(AlertConfigConstants.REDIS_MODEL_DEVICE_INSTANCE));
//                        }
                        alertHandleHelper.handleAlert(alert);
                    } else {
                        logger.error("数据格式有误,同步告警表信息失败.");
                    }
                } catch (Exception e) {
                    flag = false;
                    logger.error("运行提示:告警流水号:[" +
                            newMap.get("serialNumber").toString() + "],同步告警表信息失败.");
                    logger.error("", e);
                }
            } else {
                logger.error("运行提示:告警流水号:[" + newMap.get("serialNumber").toString() +
                        "],缺少关键数据.");
            }
        }
    }

    private AlertsV2Vo installData(Map<String, Object> newMap) {
        AlertsV2Vo alert = new AlertsV2Vo();
        String notifyType = MapUtils.getString(newMap, "notifyType");
        if ("0".equals(notifyType)) {
            alert.setAlertType(AlertsV2Vo.ALERT_ACTIVE);
        } else if ("1".equals(notifyType)) {
            alert.setAlertType(AlertsV2Vo.ALERT_REVOKE);
        }

        // 时间转换
        String sysTime = StringToTimestamp(MapUtils.getString(newMap, "sysUpTime")).toString();
        Date alarmTime = timestampToString(Integer.valueOf(MapUtils.getString(newMap, "alarmTime")));
        alert.setCurMoniTime(alarmTime);
        alert.setAlertStartTime(alarmTime);
//        alert.setRAlertId(newMap.get("serialNumber").toString()); //
        alert.setRAlertId(MapUtils.getString(newMap, "serialNumber"));
        alert.setItemId(newMap.get("snmpTrapOID").toString());
        String ip = MapUtils.getString(newMap, "sourceIP");
        alert.setDeviceIp(ip);
        alert.setMoniObject(MapUtils.getString(newMap, "alarmType"));
        alert.setMoniIndex(MapUtils.getString(newMap, "alarmContent"));

        String alarmLevel = MapUtils.getString(newMap, "alarmLevel");
        String alarmLevelNew = "2";
        switch (alarmLevel) {
            case "0":
                alarmLevelNew = "5";
                break;
            case "1":
                alarmLevelNew = "4";
                break;
            case "2":
                alarmLevelNew = "3";
                break;
            case "3":
                alarmLevelNew = "2";
                break;
        }
        alert.setAlertLevel(alarmLevelNew);
        alert.setKeyComment(MapUtils.getString(newMap, "alarmTitle"));
        if (StringUtils.isEmpty(alert.getDeviceIp())) {
            alert.setObjectType(AlertsV2Vo.OBJECT_TYPE_BIZ);
        } else {
            alert.setObjectType(AlertsV2Vo.OBJECT_TYPE_DEVICE);
        }
        alert.setRemark(MapUtils.getString(newMap, "alarmImpact"));
        alert.setSource("deepWatch");
        alert.setOrderStatus("1");// 未派单
        return alert;
    }

    private void initCAlertMap() {
        map.put("1.3.6.1.2.1.1.3.0", "sysUpTime");
        map.put("1.3.6.1.6.3.1.1.4.1.0", "snmpTrapOID");
        map.put("1.3.6.1.4.1.49022.2.21.4.1", "resourcePoolID");
        map.put("1.3.6.1.4.1.49022.2.21.4.2", "serialNumber");
        map.put("1.3.6.1.4.1.49022.2.21.4.3", "alarmType");
        map.put("1.3.6.1.4.1.49022.2.21.4.4", "alarmTitleID");
        map.put("1.3.6.1.4.1.49022.2.21.4.5", "alarmTitle");
        map.put("1.3.6.1.4.1.49022.2.21.4.6", "alarmContent");
        map.put("1.3.6.1.4.1.49022.2.21.4.7", "alarmLevel");
        map.put("1.3.6.1.4.1.49022.2.21.4.8", "alarmTime");
        map.put("1.3.6.1.4.1.49022.2.21.4.9", "objectId");
        map.put("1.3.6.1.4.1.49022.2.21.4.10", "locationInfo");
        map.put("1.3.6.1.4.1.49022.2.21.4.11", "deviceType");
        map.put("1.3.6.1.4.1.49022.2.21.4.12", "notifyType");
        map.put("1.3.6.1.4.1.49022.2.21.4.13", "alarmId");
        map.put("1.3.6.1.4.1.49022.2.21.4.14", "systemName");
        map.put("1.3.6.1.4.1.49022.2.21.4.15", "alarmImpact");
        map.put("1.3.6.1.4.1.49022.2.21.4.16", "currentIp");
        map.put("1.3.6.1.4.1.49022.2.21.4.17", "sourceIP");
        map.put("1.3.6.1.4.1.49022.2.21.4.18", "correlateAlarmFlag");
        map.put("1.3.6.1.4.1.49022.2.21.4.19", "alarmOID");
        map.put("1.3.6.1.4.1.49022.2.21.4.20", "parentId");
        map.put("1.3.6.1.4.1.49022.2.21.4.21", "parentType");
        map.put("1.3.6.1.4.1.49022.2.21.4.22", "parentName");
        map.put("1.3.6.1.4.1.49022.2.21.4.23", "deviceModel");
        map.put("1.3.6.1.4.1.49022.2.21.4.24", "vendor");
        map.put("1.3.6.1.4.1.49022.2.21.4.25", "bussiness");
        map.put("1.3.6.1.4.1.49022.2.21.4.26", "tenant");

    }

    /**
     * String(yyyy-MM-dd HH:mm:ss)转10位时间戳
     *
     * @param time
     * @return
     */

    private Integer StringToTimestamp(String time) {

        int times = 0;
        try {
            times = (int) ((Timestamp.valueOf(time).getTime()) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (times == 0) {
            logger.info("String转10位时间戳失败");
        }
        return times;
    }

    /**
     * 10位int型的时间戳转换为String(yyyy-MM-dd HH:mm:ss)
     *
     * @param time
     * @return
     */
    private Date timestampToString(Integer time) {
        //int转long时，先进行转型再进行计算，否则会是计算结束后在转型
        long temp = (long) time * 1000;
        Timestamp ts = new Timestamp(temp);
        String tsStr = "";
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try { //方法一
            tsStr = dateFormat.format(ts);
            date = dateFormat.parse(tsStr);
            System.out.println(tsStr);
        } catch (Exception e) {
            logger.error("", e);
        }
        return date;
    }

    private String getTransBase(String msg) {
        Base64 base64 = new Base64();
        try {
            return new String(base64.decode(msg), "UTF-8");
        } catch (UnsupportedEncodingException e) { // TODO Auto-generated catch block
            logger.error("", e);
            return null;
        }
    }
}
