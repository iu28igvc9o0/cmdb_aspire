package com.aspire.mirror.alert.server.biz.alert.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.biz.alert.PrometheusAlertsBiz;
import com.aspire.mirror.alert.server.biz.helper.CmdbHelper;
import com.aspire.mirror.alert.server.util.PrometheusUtil;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.biz.helper.AlertsHandleV2Helper;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsV2Vo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * prometheus告警实现类
 * <p>
 * 项目名称：mirror平台
 * 包：com.aspire.mirror.alert.server.biz.impl
 * 类名称：PrometheusAlertsBizimpl.java
 * 类描述：prometheus告警实现类
 * 创建人：zhujiahao
 * 创建时间：2019/1/11 14:00
 * 版本：v1.0
 */
@Service
public class PrometheusAlertsBizV2Impl implements PrometheusAlertsBiz {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrometheusAlertsBizV2Impl.class);

    @Autowired
    private AlertsHandleV2Helper alertsHandleV2Helper;
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Autowired
    private CmdbHelper cmdbHelper;

    @Override
    public String insert(String message) {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(message);
        } catch (IOException e) {
            LOGGER.error("prometheus告警解析json数据异常: " + e.getMessage());
            return "prometheus告警解析json数据异常: " + e.getMessage();
        }

        List<AlertFieldVo> alertFieldList = alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode alerts = node.get("alerts");
        LOGGER.info("the size of prometheus alerts is {}", alerts.size());
        Map<String, AlertsV2Vo> firingMap = new HashMap<>();
        Map<String, AlertsV2Vo> resovledMap = new HashMap<>();
        Map<String, JSONObject> firingJsonMap = new HashMap<>();
        Map<String, JSONObject> resovledJsonMap = new HashMap<>();
        Date now = new Date();
        for (JsonNode alert : alerts) {
            AlertsV2Vo dto = new AlertsV2Vo();

            String ip = (alert.get("labels").get("instance").asText()).split(":")[0];
            //ip
            dto.setDeviceIp(ip);
            //当前监控时间
//            dto.setCurMoniTime(PrometheusUtil.getTime(PrometheusUtil.isNull(alert.get("endsAt"))));
            //prometheus没有告警当前时间，跟叶海全沟通的结果是先传当前系统时间（代替）
            dto.setCurMoniTime(now);
            dto.setAlertStartTime(dto.getCurMoniTime());
            //告警时间不存在的告警丢弃掉
            if (dto.getCurMoniTime() == null) {
                LOGGER.info("prometheus alert has no start time,drop this alert ,{}", alert.asText());
                continue;
            }
            String alertType = "firing".equals(PrometheusUtil.getString(alert.get("status"))) ? AlertsV2Vo.ALERT_ACTIVE : AlertsV2Vo.ALERT_REVOKE;
            dto.setAlertType(alertType);
            //同一个ip下只保留最新的数据，同一批数据是一个监控类型，因此可以这么收敛，其他地方慎用
            if (AlertsV2Vo.ALERT_ACTIVE.equals(alertType)) {
                AlertsV2Vo oldDto = firingMap.get(ip);
                if (oldDto != null) {
                    if (oldDto.getCurMoniTime().after(dto.getCurMoniTime())) {
                        continue;
                    }
                }
            } else if (AlertsV2Vo.ALERT_REVOKE.equals(alertType)) {
                AlertsV2Vo oldDto = resovledMap.get(ip);
                if (oldDto != null) {
                    if (dto.getCurMoniTime().before(oldDto.getCurMoniTime())) {
                        continue;
                    }
                }
            }
            //监控对象
            dto.setMoniObject(PrometheusUtil.getString(alert.get("labels").get("custom_type")));
            dto.setItemId(PrometheusUtil.toPinyin(PrometheusUtil.getString(alert.get("labels").get("custom_type"))));
            dto.setKeyComment(PrometheusUtil.getString(alert.get("labels").get("alertname")));
            dto.setItemKey(PrometheusUtil.getString(alert.get("labels").get("name")));
            //告警类型为空，丢弃数据
            if (dto.getMoniObject() == null) {
                LOGGER.info("prometheus alert has no type(monitor object),drop this alert ,{}", alert.asText());
                continue;
            }
            //监控指标/内容，关联触发器name
            dto.setMoniIndex(PrometheusUtil.getString(alert.get("annotations").get("summary")));
            //告警内容不存在的告警丢弃掉
            if (StringUtils.isEmpty(dto.getMoniIndex())) {
                LOGGER.info("prometheus alert has no cummary(alert message),drop this alert ,{}", alert.asText());
                continue;
            }
            //rAlertId 同样是告警id
            dto.setRAlertId(PrometheusUtil.getAlertId(ip, PrometheusUtil.getString(alert.get("labels").get("alertname"))));
            String pool = PrometheusUtil.getString(alert.get("labels").get("ResourcePool"));
            String idcType = cmdbHelper.getIdc(pool);
            dto.setIdcType(idcType);

            //当前监控值
            dto.setCurMoniValue(PrometheusUtil.getString(alert.get("annotations").get("value")));
            //告警开始时间(因为处理逻辑的缘故,这里不需要设置,会去读取"当前监控时间")
            dto.setAlertStartTime(PrometheusUtil.getTime(PrometheusUtil.isNull(alert.get("startsAt"))));
            //告警级别
            dto.setAlertLevel(PrometheusUtil.getLevel(PrometheusUtil.getString(alert.get("labels").get("level"))));
            //告警结束时间
            //prometheus没有告警当前时间，跟叶海全沟通的结果是先传当前系统时间（代替）
            dto.setAlertEndTime(now);
            //备注
            //应产品要求，备注填空
//            dto.setRemark(PrometheusUtil.getString(alert.get("annotations").get("description")));
            //告警来源 zabbix or rometheus
            dto.setSource("prometheus");
            //1-系统  or 2-业务
            dto.setObjectType("1");
            dto.setOrderStatus("1");
            dto.setAlertCount(1);

            String jsonString = "{}";
            try {
                jsonString = objectMapper.writeValueAsString(dto);
            } catch (JsonProcessingException e) {
            }
            JSONObject alertJson = JSONObject.parseObject(jsonString);
            //处理cmdb数据
            if (com.aspire.mirror.alert.server.util.StringUtils.isNotEmpty(dto.getDeviceIp())){
                // 根据  机房 + IP, 查找设备
                cmdbHelper.queryDeviceForAlertV2(alertJson, alertFieldList,alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_DEVICE_INSTANCE));
            }
            if (AlertsV2Vo.ALERT_ACTIVE.equals(alertType)) {
                firingMap.put(ip, dto);
                firingJsonMap.put(ip, alertJson);
            } else if (AlertsV2Vo.ALERT_REVOKE.equals(alertType)) {
                resovledMap.put(ip, dto);
                resovledJsonMap.put(ip, alertJson);
            }
        }

        for (Map.Entry<String, AlertsV2Vo> entry : firingMap.entrySet()) {
            String ip = entry.getKey();
            AlertsV2Vo dto = entry.getValue();
            alertsHandleV2Helper.handleAlert(dto, firingJsonMap.get(ip), alertFieldList);
        }
        for (Map.Entry<String, AlertsV2Vo> entry : resovledMap.entrySet()) {
            String ip = entry.getKey();
            AlertsV2Vo dto = entry.getValue();
            alertsHandleV2Helper.handleAlert(dto, resovledJsonMap.get(ip), alertFieldList);
        }
        return "success";
    }
}
