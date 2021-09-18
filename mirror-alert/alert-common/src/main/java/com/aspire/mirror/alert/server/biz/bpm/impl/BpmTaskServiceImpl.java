package com.aspire.mirror.alert.server.biz.bpm.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.biz.bpm.IBpmTaskService;
import com.aspire.mirror.alert.server.biz.helper.CmdbHelper;
import com.aspire.mirror.alert.server.vo.bpm.AlertBpmCallBack;
import com.aspire.mirror.alert.server.clientservice.payload.AlertUserVO;
import com.aspire.mirror.alert.server.clientservice.process.RbacClient;
import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.dao.bpm.AlertBpmZycStaffDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsRecordDao;
import com.aspire.mirror.alert.server.dao.bpm.po.AlertBpmZycStaff;
import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsRecord;
import com.aspire.mirror.alert.server.biz.alert.AlertsHisBizV2;
import com.aspire.mirror.alert.server.constant.OrderStatusEnum;
import com.aspire.mirror.alert.server.dao.isolate.AlertIsolateAlertsV2Mapper;
import com.aspire.mirror.alert.server.dao.bpm.AlertOrderHandleMapper;
import com.aspire.mirror.alert.server.dao.bpm.po.AlertOrderHandle;
import com.aspire.mirror.common.constant.Constant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis.client.Call;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
public class BpmTaskServiceImpl implements IBpmTaskService {
    @Autowired
    private CmdbHelper cmdbHelper;
    @Autowired
    private AlertsDao alertsDao;
    @Autowired
    private AlertsRecordDao alertsRecordDao;
    @Autowired
    private AlertsHisBizV2 alertsHisBizV2;
    @Autowired
    private RbacClient rbacClient;
    @Autowired
    private AlertOrderHandleMapper alertOrderHandleMapper;
    @Autowired
    private AlertIsolateAlertsV2Mapper alertIsolateAlertsV2Mapper;
    @Value("${bpm.runtime.url}")
    private String runtimeUrl;
    @Value("${bpm.closing.url}")
    private String checkClosingUrl;
    @Value("${bpm.token}")
    private String bpmToken;
    @Value("${bpm.alert.Url}")
    private String url;
    @Value("${bpm.alert.gjFlowkey}")
    private String gjFlowkey;
    @Value("${bpm.alert.gzFlowkey}")
    private String gzFlowkey;
    @Value("${bpm.alert.wbFlowkey}")
    private String wbFlowkey;
    @Value("${bpm.alert.tuningFlowkey}")
    private String tuningFlowKey;
    @Value("${bpm.alert.acount}")
    private String account;
    @Value("${bpm.alert.method}")
    private String method;
    @Value("${bpm.alert.namepace}")
    private String space;
    @Value("${bpm.alert.gjModel}")
    private String gjModel;
    @Value("${bpm.alert.gzModel}")
    private String gzModel;
    @Value("${bpm.alert.wbModel}")
    private String wbModel;
    @Value("${bpm.alert.bpmModel:webservice}")
    private String bpmModel;

    private String operateOrder (String orderId, String content) {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String returnStatus = "ERROR";
        log.info("call doUmsEndProcess to close BpmInstance, orderId is : {}, content is : {}", orderId, content);
        try {
            final HttpPost httpPost = new HttpPost(runtimeUrl + "/runtime/instance/v1/doUmsEndProcess");
            if (StringUtils.isNotEmpty(bpmToken)) {
                httpPost.addHeader("Authorization", bpmToken);
            }
            httpPost.setHeader("Content-Type", "application/json"); // 此处应去除
            String jsonString = "{\"instanId\": \"" + orderId + "\"";
            if (!StringUtils.isEmpty(content)) {
                jsonString = jsonString + ",\"content\":\"" + content + "\"}";
            } else {
                jsonString += "}";
            }
            StringEntity entity = new StringEntity(jsonString, "UTF-8");

            // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("POST Response Status: {}", statusCode);
            HttpEntity responseEntity = response.getEntity();
            String result = "";
            if (null != responseEntity) {
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                log.info("POST /runtime/instance/v1/doUmsEndProcess return : {}", result);
            }
            JSONObject json = JSON.parseObject(result);
            if (statusCode == HttpStatus.OK.value()) {
                returnStatus = "OK";
            }
        } catch (Exception e) {
            log.error("error", e);
        } finally {

            // 释放资源
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                }
            }

        }
        return returnStatus;
    }

    /**
     * 调用工单接口关闭告警工单
     * @param orderId
     * @return
     */
    public String closeBpmInstance(String orderId, String content) {
        return operateOrder(orderId, content);
    }

    @Async
    public void closeBpm(String orderId, String alertType) {
        String operateOrder = operateOrder(orderId, "UMS告警自动消除");
        if ("ERROR".equals(operateOrder)) {
            if ("isolate".equalsIgnoreCase(alertType)) {

            } else {
                alertsHisBizV2.updateAlertHisByOrderId(orderId,Constants.ORDER_ERROR);
            }
        }
    }

    public String alertHandleBpmResult(List<Map<String, Object>> alerts, String type, String user, Integer orderType) {
        Map<String, Object> map = handleBpmResult(alerts, type, user, orderType);
        List<Alerts> alertList = (List<Alerts>) map.get("alertList");
        if (!CollectionUtils.isEmpty(alertList)) {
            for (Alerts alertsUpdate: alertList) {
                alertsDao.updateByPrimaryKey(alertsUpdate);
            }
        }
        return MapUtils.getString(map, "returnStr");
    }

    public String isolateHandleBpmResult(List<Map<String, Object>> alerts, String type, String user, Integer orderType) {
        Map<String, Object> map = handleBpmResult(alerts, type, user, orderType);
        List<Alerts> alertList = (List<Alerts>) map.get("alertList");
        if (!CollectionUtils.isEmpty(alertList)) {
            for (Alerts alertsUpdate: alertList) {
                alertIsolateAlertsV2Mapper.updateByPrimaryKey(alertsUpdate);
            }
        }
        return MapUtils.getString(map, "returnStr");
    }

    /**
     * @param alerts
     * @param orderType
     * @return
     */
    private Map<String, Object> handleBpmResult(List<Map<String, Object>> alerts, String type, String user, Integer orderType) {
        Map<String, Object> map = Maps.newHashMap();
        List<Alerts> alertList = Lists.newArrayList();
        String returnStr = "";
        String flowkey =null;
        String model =null;
        if (orderType.toString().equals(Constants.ORDER_ALERT)){
            flowkey = gjFlowkey;
            model = gjModel;
        }else if (orderType.toString().equals(Constants.ORDER_HITCH)){
            flowkey = gzFlowkey;
            model = gzModel;
        }else {
            flowkey = wbFlowkey;
            model = wbModel;
        }

        //
        int successNum = 0;
        List<String> orderIds = Lists.newArrayList();
        if (alerts != null && alerts.size() > 0) {
            log.info("需要进行工单生成的告警数量为" + alerts.size());
            log.info("请求bpm的webService地址:" + url + ",flowkey:" + flowkey + ",account:" + account + ",method:"
                    + method + ",space:" + space);
            for (Map<String, Object> alert : alerts) {
                //取出告警事件目前关联的告警工单id（有可能为null）,用来在bpm侧人工结束工单
                String oldOrderId = (String) alert.get("order_id");
                String oldOrderType = (String) alert.get("order_type");
                String alertId = alert.get("alert_id").toString();
                String runId = null;
                String messages = "";
                try {
                    String dataStr = model;
                    if (orderType.toString().equals(Constants.ORDER_HITCH)){
                        //根据username查询user
                        String key1 = "user";
                        AlertUserVO userVO = rbacClient.findByLdapId(user);
                        dataStr = dataStr.replaceAll("@\\{" + key1.toLowerCase() + "\\}", user);
                        String key2 = "user_name";
                        dataStr = dataStr.replaceAll("@\\{" + key2.toLowerCase() + "\\}", userVO.getName());
                    }
                    dataStr = getData(alert, dataStr);
//
                    DefaultKeyValue<String, String> keyValue;
                    if ("restful".equalsIgnoreCase(bpmModel)) {
                        keyValue = doStartByRestful(user, dataStr, flowkey, oldOrderId, oldOrderType, alertId);
                    } else {
                        keyValue = doStartByWebService(user, dataStr, flowkey, oldOrderId, oldOrderType, alertId);
                    }
                    if (keyValue == null) {
                        messages = "没有返回";
                        continue;
                    }
                    runId = keyValue.getKey();
                    messages = keyValue.getValue();
                    if (StringUtils.isNotEmpty(runId)) {
                        successNum++;
                        orderIds.add(runId);
                    } else {
                        returnStr += messages;
                    }
                } catch (Exception e) {
                    log.error("告警记录录入失败...", e);
                    returnStr += e.getMessage();
                    messages = e.getMessage();
                } finally {
                    //根据返回的结果，修改告警时间
                    Alerts updateE = new Alerts();
                    updateE.setAlertId(alertId);

                    Date date = new Date();
                    AlertsRecord alertsRecord = new AlertsRecord();
                    alertsRecord.setAlertId(alertId);
                    alertsRecord.setUserName(user);
                    alertsRecord.setOperationType("2");

                    alertsRecord.setOperationStatus("1");
                    alertsRecord.setOperationTime(date);
                    if (!StringUtils.isEmpty(runId)) {
                        // 工单状态
                        AlertOrderHandle alertOrderHandle = new AlertOrderHandle();
                        alertOrderHandle.setStatus(OrderStatusEnum.TO_DO.getCode());
                        alertOrderHandle.setExecTime(date);
                        alertOrderHandle.setOrderId(runId);
                        alertOrderHandleMapper.insert(alertOrderHandle);
                        Integer operateStatus = MapUtils.getInteger(alert, "operate_status");
                        // 只有待确认才会变为已确认
                        if (operateStatus == null || operateStatus == 0) {
                            updateE.setOperateStatus(1);//已确认
                        }

                        updateE.setOrderStatus(Constant.ORDER_DEALING);
                        updateE.setOrderId(runId);
                        updateE.setOrderType(orderType.toString());

                        if (orderType.toString().equals(Constants.ORDER_ALERT)){
                            alertsRecord.setContent(Constant.ALERT_ORDER);
                        }else if (orderType.toString().equals(Constants.ORDER_HITCH)){
                            alertsRecord.setContent(Constant.HITCH_ORDER);
                        }else {
                            alertsRecord.setContent(Constant.MAINTENANCE_ORDER);
                        }
                    } else {
                        if (StringUtils.isEmpty(oldOrderId)) {
                            updateE.setOrderStatus(Constants.ORDER_ERROR);
                        }
                        messages = Constants.ORDER_ERROR_DESC + messages;
                        if (messages.length() > 2000) {
                            messages = messages.substring(0, 1999);
                        }
                        alertsRecord.setContent(messages);
                        alertsRecord.setOperationStatus("0");
                    }
//                    alertsDao.updateByPrimaryKey(updateE);
                    alertList.add(updateE);
                    alertsRecordDao.insert(alertsRecord);
                }
            }
        }
        if (StringUtils.isBlank(returnStr)){
            returnStr = "success:"+successNum;
            if (!CollectionUtils.isEmpty(orderIds)) {
                returnStr =  returnStr + "_" + String.join(",",orderIds);
            }
        }
        map.put("alertList", alertList);
        map.put("returnStr", returnStr);
        return map;
    }

    /**
     * 组装参数
     * @param alert
     * @param dataStr
     * @return
     */
    private String getData (Map<String, Object> alert, String dataStr) {
        Map<String, Object> cmdbInstance = cmdbHelper.queryDeviceByRoomIdAndIP(String.valueOf(alert.get("idc_type")), String.valueOf(alert.get("device_ip")));
        if (cmdbInstance != null) {
            alert.putAll(cmdbInstance);
        }
        Map<String, String> alert1 = Maps.newHashMap();
        for (Map.Entry<String, Object> entry: alert.entrySet()) {
            String key = entry.getKey().toLowerCase(Locale.getDefault());
            if ("alert_level".equals(key) && entry.getValue() != null) {
                String valueStr = entry.getValue().toString();
                switch (valueStr) {
                    case "2":
                        valueStr = "低";
                        break;
                    case "3":
                        valueStr = "中";
                        break;
                    case "4":
                        valueStr = "高";
                        break;
                    case "5":
                        valueStr = "重大";
                        break;
                    default:
                        valueStr = "低";
                        break;
                }
                alert1.put(key, valueStr);
            } else {
                Object alertValue = entry.getValue();
                String valueStr = "";
                if (alertValue instanceof String) {
                    valueStr = (String) alertValue;
                } else if (alertValue instanceof Timestamp) {
                    valueStr = alertValue.toString();
                } else {
                    valueStr = alertValue.toString();
                }
                if (!"restful".equalsIgnoreCase(bpmModel)) {
                    valueStr = valueStr.replaceAll("&", "&amp;")
                            .replaceAll("<", "&lt;")
                            .replaceAll(">", "&gt;")
                            .replaceAll("\\n", " ");
                }
                alert1.put(key, valueStr);
            }
        }
        JSONObject jsonObject = JSON.parseObject(dataStr);
        replaceStr(alert1, jsonObject);

        return jsonObject.toJSONString();
    }

    /**
     * 替换派单报文中的参数
     * @param alert
     * @param jsonObject
     */
    private void replaceStr(Map<String, String> alert, JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                replaceStr(alert, (JSONObject) value);
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.size(); i++) {
                    Object o = array.get(i);
                    if (o instanceof JSONObject) {
                        replaceStr(alert, (JSONObject) o);
                    } else {
                        String str = array.getString(i);
                        if (StringUtils.isNotEmpty(str) && str.indexOf("@{") > -1) {
                            for (Map.Entry<String, String> entry : alert.entrySet()) {
                                String entryValue = entry.getValue();
                                if (StringUtils.isNotEmpty(entryValue) && entryValue.indexOf("$") > -1) {
                                    entryValue = entryValue.replaceAll("\\$", "RDS_CHAR_DOLLAR");
                                    str = str.replaceAll("@\\{" + entry.getKey() + "\\}", entryValue);
                                    str = str.replaceAll("RDS_CHAR_DOLLAR", "\\$");
                                } else {
                                    str = str.replaceAll("@\\{" + entry.getKey() + "\\}", entryValue);
                                }
                            }
                            str = str.replaceAll("@\\{[^\\}.]*\\}", "");
                        }
                    }
                }
            } else {
                String str = jsonObject.getString(key);
                if (StringUtils.isNotEmpty(str) && str.indexOf("@{") > -1) {
                    for (Map.Entry<String, String> entry : alert.entrySet()) {
                        String entryValue = entry.getValue();
                        if (StringUtils.isNotEmpty(entryValue) && entryValue.indexOf("$") > -1) {
                            entryValue = entryValue.replaceAll("\\$", "RDS_CHAR_DOLLAR");
                            str = str.replaceAll("@\\{" + entry.getKey() + "\\}", entryValue);
                            str = str.replaceAll("RDS_CHAR_DOLLAR", "\\$");
                        } else {
                            str = str.replaceAll("@\\{" + entry.getKey() + "\\}", entryValue);
                        }
                    }
                    str = str.replaceAll("@\\{[^\\}.]*\\}", "");
                    jsonObject.put(key, str);
                }
            }
        }
    }

    /**
     * webservice方式调用告警工单接口
     * @param user
     * @param dataStr
     * @param flowkey
     * @param oldOrderId
     * @param oldOrderType
     * @param alertId
     * @return
     * @throws Exception
     */
    private DefaultKeyValue<String, String> doStartByWebService (String user, String dataStr, String flowkey, String oldOrderId, String oldOrderType, String alertId) throws Exception{
        DefaultKeyValue<String, String> keyValue = new DefaultKeyValue<>();
        String xml = generateEnvelop(user, dataStr,flowkey,oldOrderId,oldOrderType,alertId);
        org.apache.axis.client.Service service = new org.apache.axis.client.Service();
        Call call = (Call) service.createCall();
        call.setTargetEndpointAddress(new java.net.URL(url));
        call.setUseSOAPAction(true);
        call.setOperationName(new QName(space, method));// WSDL里面描述的接口名称
        call.addParameter(new QName("xml"), org.apache.axis.encoding.XMLType.XSD_STRING,
                javax.xml.rpc.ParameterMode.IN);// 接口的参数
        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型
        // 调用webservice请求
        String result = (String) call.invoke(new Object[]{xml});
        log.info("返回结果：{}", result);
        Document doc = DocumentHelper.parseText(result); // 将字符串转为XML
        Element rootElt = doc.getRootElement(); // 获取根节点
        String message = "";
        Attribute messageAttr = rootElt.attribute("message");
        if (null != messageAttr) {
            message = messageAttr.getValue();
        }
        Attribute attribute = rootElt.attribute("status");
        if (null == attribute) {
            log.error("告警记录录入失败...{}", alertId);
//            returnStr += message;
            keyValue.setValue(message);
            return keyValue;
        }
        String rs = attribute.getValue();

        // 同步成功
        if ("1".equals(rs)) {
//            successNum++;
            Attribute runAttr = rootElt.attribute("runId");
            String runId = runAttr.getValue();
//            orderIds.add(runId);
            keyValue.setKey(runId);
            log.info("告警记录录入成功...");

        } else {
            keyValue.setValue(message);
            log.error("告警记录录入失败...{}", alertId);
        }
        return keyValue;
    }

    /**
     * restful方式调用告警工单
     * @param user
     * @param dataStr
     * @param flowkey
     * @param oldOrderId
     * @param oldOrderType
     * @param alertId
     * @return
     * @throws Exception
     */
    private DefaultKeyValue<String, String> doStartByRestful (String user, String dataStr, String flowkey, String oldOrderId, String oldOrderType, String alertId) throws Exception {
        DefaultKeyValue<String, String> keyValue = new DefaultKeyValue<>();
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            final HttpPost httpPost = new HttpPost(url);
            if (StringUtils.isNotEmpty(bpmToken)) {
                httpPost.addHeader("Authorization", bpmToken);
            }
            httpPost.setHeader("Content-Type", "application/json"); // 此处应去除
            Map<String, String> params = Maps.newHashMap();
            params.put("account", user);
            params.put("actDefId", null);
            params.put("businessKey", null);
            params.put("data", dataStr);
            params.put("flowKey", flowkey);
            params.put("instId", null);
            params.put("subject", null);
            params.put("vars", null);
            params.put("oldOrderId", oldOrderId);
            params.put("oldOrderType", oldOrderType);
            params.put("alertId", alertId);
            // 新增参数，接入系统名称
            params.put("accessSystem","UMS");
            log.info(JSON.toJSONString(params));
            StringEntity entity = new StringEntity(JSON.toJSONString(params), "UTF-8");

            // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("POST Response Status: {}", statusCode);
            HttpEntity responseEntity = response.getEntity();
            String result = "";
            if (null != responseEntity) {
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                log.info("派单返回 : {}", result);
            }
            JSONObject json = JSON.parseObject(result);
            String runId = json.getString("runId");
            String message = json.getString("message");
            String status = json.getString("status");
            // 派单成功
            if ("1".equals(status)) {
                keyValue.setKey(runId);
            }
            keyValue.setValue(message);
        } catch (Exception e) {
            log.error("error", e);
            keyValue.setValue(e.getMessage());
        } finally {

            // 释放资源
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {

                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {

                }
            }

        }
        return keyValue;
    }

    /**
     * 生成wsdl格式的消息信封
     *
     * @return String类型的wsdl格式的消息信封
     */
    private String generateEnvelop(String account, String data, String flowkey, String oldOrderId, String oldOrderType, String alertId) {
        if (oldOrderId ==null){
            oldOrderId="";
        }
        if (oldOrderType ==null){
            oldOrderType="";
        }
        StringBuffer wsdlStr = new StringBuffer();
        String head = "<req flowKey=\"" + flowkey + "\" oldOrderId=\"" + oldOrderId + "\" oldOrderType=\"" + oldOrderType +"\" alertId=\"" + alertId +"\" subject=\"\" account=\"" + account
                + "\" businessKey=\"\" runId=\"\"><data>";
        wsdlStr.append(head);
        wsdlStr.append(data);
        wsdlStr.append("</data></req>");
        String xmlStr = String.valueOf(wsdlStr);
        return xmlStr;
    }

    @Override
    public AlertBpmCallBack callBpmFlowStart(String user, Map<String, Object> alert, String orderType) {
        String xml = "";
        String dataStr = "";
        String alertId = (String) alert.get("alert_id");
        String oldOrderId = (String) alert.get("order_id");
        String oldOrderType = (String) alert.get("order_type");
        String zyc = alert.get("idc_type") == null ? "" : (String) alert.get("idc_type");
        switch (orderType) {
            case Constants.ORDER_TUNING: // 告警调优工单
                AlertUserVO userVO = rbacClient.findByLdapId(user);
                Map<String, Object> gjdy = new HashMap<>();
                gjdy.put("fqr", userVO.getName()); // 发起人
                gjdy.put("lxdh", userVO.getMobile()); // 联系电话
                gjdy.put("lxyx", userVO.getMail()); // 联系邮箱
                gjdy.put("gjgszyc", zyc); // 告警归属资源池
                gjdy.put("zdlx", ""); // 诊断类型

                Map<String, String> jkrMap = getZycMaintenceStaff(StringUtils.isEmpty(zyc) ? "-1" : zyc);
                gjdy.put("zycjkwhjkr", jkrMap == null ? "" : jkrMap.get("names")); // 资源池监控维护接口人
                gjdy.put("zycjkwhjkrID", jkrMap == null ? "" : jkrMap.get("accounts")); // 资源池监控维护接口人ID

                StringBuilder content = new StringBuilder();
                String deviceIP = alert.get("device_ip") == null ? "" : String.valueOf(alert.get("device_ip"));//设备IP
                String hostName = alert.get("host_name") == null ? "" : String.valueOf(alert.get("host_name"));//主机名
                String deviceClass = alert.get("device_class") == null ? "" : String.valueOf(alert.get("device_class"));//设备分类
                String deviceType = alert.get("device_type") == null ? "" : String.valueOf(alert.get("device_type"));//设备类型
                String sourceRoom = alert.get("source_room") == null ? "" : String.valueOf(alert.get("source_room"));//机房
                String podName = alert.get("pod_name") == null ? "" : String.valueOf(alert.get("pod_name"));//POD池
                content.append("告警设备: ");
                if (StringUtils.isNotEmpty(deviceIP)) {
                    content.append(deviceIP + " ");
                }
                if (StringUtils.isNotEmpty(hostName)) {
                    content.append(hostName + " ");
                }
                if (StringUtils.isNotEmpty(deviceClass) && StringUtils.isNotEmpty(deviceType)) {
                    content.append(deviceClass + "-" + deviceType);
                } else {
                    content.append(StringUtils.isEmpty(deviceClass) ? deviceType : deviceClass);
                }
                content.append(" ");
                if (StringUtils.isNotEmpty(sourceRoom)) {
                    content.append(sourceRoom + " ");
                }
                if (StringUtils.isNotEmpty(podName)) {
                    content.append(podName);
                }
                content.append("\n");
                content.append("告警来源: " + alert.get("source") + "\n");
                content.append("告警内容: " + alert.get("moni_index"));
                gjdy.put("glgjnr", content.toString()); // 关联告警内容
                gjdy.put("jjcs", ""); // 解决措施

                gjdy.put("sssj", ""); // 实施时间
                gjdy.put("sjssr", ""); // 实际实施人
                gjdy.put("sjssrID", ""); // 实际实施人ID
                gjdy.put("ssrlxdh", ""); // 实施人联系电话
                gjdy.put("ssnr", ""); // 实施内容
                gjdy.put("ssfj", ""); // 实施附件
                gjdy.put("yzjg", ""); // 验证结果
                gjdy.put("yznr", ""); // 验证内容
                gjdy.put("initData", Maps.newHashMap());
                Map data = Maps.newHashMap();
                data.put("gjdy", gjdy);
                dataStr = JSON.toJSONString(data);
//                String dataStr = JSON.toJSONString(data);
//                xml = generateEnvelop(user,dataStr,tuningFlowKey,oldOrderId,oldOrderType,alertId);
                break;
            default:
                break;
        }
        AlertBpmCallBack callBack = new AlertBpmCallBack();
        String runId = null;
        String messages = "";
        DefaultKeyValue<String, String> keyValue = new DefaultKeyValue<>();
        try {
            if ("restful".equalsIgnoreCase(bpmModel)) {
                keyValue = doStartByRestful(user, dataStr, tuningFlowKey, oldOrderId, oldOrderType, alertId);
            } else {
                keyValue = doStartByWebService(user, dataStr, tuningFlowKey, oldOrderId, oldOrderType, alertId);
            }
        } catch (Exception e) {
        }

        if (keyValue == null) {
            messages = "没有返回";
            callBack.setMessage(messages);
        }else {
            runId = keyValue.getKey();
            messages = keyValue.getValue();
            callBack.setStatus("1");
            callBack.setRunId(runId);
        }
        return callBack;
//        if (StringUtils.isNotEmpty(runId)) {
//        AlertBpmCallBack callBack = new AlertBpmCallBack();
//        try {
//            org.apache.axis.client.Service service = new org.apache.axis.client.Service();
//            Call call = (Call) service.createCall();
//            call.setTargetEndpointAddress(new java.net.URL(url));
//            call.setUseSOAPAction(true);
//            call.setOperationName(new QName(space, method));// WSDL里面描述的接口名称
//            call.addParameter(new QName("xml"), org.apache.axis.encoding.XMLType.XSD_STRING,
//                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
//            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型
//            // 调用webservice请求
//            log.info("发起工单请求数据：{}", xml);
//            String result = (String) call.invoke(new Object[]{xml});
//            log.info("返回结果：{}", result);
//            Document doc = doc = DocumentHelper.parseText(result); // 将字符串转为XML
//            Element rootElt = doc.getRootElement(); // 获取根节点
//            String message = "";
//            Attribute messageAttr = rootElt.attribute("message");
//            if (null != messageAttr) {
//                message = messageAttr.getValue();
//            }
//            Attribute attribute = rootElt.attribute("status");
//            if (null == attribute) {
//                log.error("告警记录录入失败...{}", alertId);
//                callBack.setMessage(message);
//                return callBack;
//            }
//            String rs = attribute.getValue();
//            if ("1".equals(rs)) {
//                Attribute runAttr = rootElt.attribute("runId");
//                String runId = runAttr.getValue();
//                callBack.setStatus("1");
//                callBack.setRunId(runId);
//            }
//        } catch (Exception e) {
//            callBack.setMessage(e.getMessage());
//        }
//        return callBack;
    }

    @Autowired
    private AlertBpmZycStaffDao alertBpmZycStaffDao;

    /**
     * 根据资源池获取对应的工单监控维护接口人
     * @param zyc
     * @return
     */
    private Map<String, String> getZycMaintenceStaff(String zyc) {
        if (StringUtils.isEmpty(zyc)) {
            return null;
        }
        List<AlertBpmZycStaff> list = alertBpmZycStaffDao.selectByZyc(zyc);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List names = Lists.newArrayList();
        List accounts = Lists.newArrayList();
        for (AlertBpmZycStaff staff : list) {
            names.add(staff.getFullname());
            accounts.add(staff.getAccount());
        }
        Map<String, String> result = new HashMap<>();
        result.put("names", StringUtils.join(names.toArray(), ","));
        result.put("accounts", StringUtils.join(accounts.toArray(), ","));
        return result;
    }

    /**
     * 校验工单是否关闭
     * @param orderList
     * @return
     */
    public List<String> checkOrderIsEnd(List<String> orderList) {
        log.info("call checkOrderIsEnd method start, params are : {}", orderList);
        List<String> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(orderList)) {
            return list;
        }
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            final HttpPost httpPost = new HttpPost(checkClosingUrl);
            if (StringUtils.isNotEmpty(bpmToken)) {
                httpPost.addHeader("Authorization", bpmToken);
            }
            httpPost.setHeader("Content-Type", "application/json"); // 此处应去除
//            Map<String, Object> map = Maps.newHashMap();
//            map.put("orderList", orderList);
            String jsonString = JSON.toJSONString(orderList);
            log.info("call alertOrderIsEnd to check BpmInstance closing status, params are : {}", jsonString);
            StringEntity entity = new StringEntity(jsonString, "UTF-8");

            // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("POST Response Status: {}", statusCode);
            HttpEntity responseEntity = response.getEntity();
            String result = "";
            if (null != responseEntity) {
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                log.info("POST {} return : {}", checkClosingUrl, result);
            }
            JSONObject json = JSON.parseObject(result);
            if (statusCode == HttpStatus.OK.value()) {
                list.addAll(json.getJSONArray("orderList").toJavaList(String.class));
            }
        } catch (Exception e) {
            log.error("error", e);
        } finally {

            // 释放资源
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                }
            }

        }
        return list;
    }
}
