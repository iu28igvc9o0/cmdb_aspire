package com.aspire.mirror.alert.server.biz.notify.impl;

import com.aspire.mirror.alert.server.biz.notify.AlertVoiceNotifyBiz;
import com.aspire.mirror.alert.server.biz.helper.CmdbHelper;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.dao.notify.AlertVoiceNotifyDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsStatisticDao;
import com.aspire.mirror.alert.server.dao.notify.po.AlertVoiceNotifyDetail;
import com.aspire.mirror.alert.server.dao.notify.po.AlertVoiceNotifyReqDTO;
import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.vo.notify.AlertVoiceNotifyContentVo;
import com.aspire.mirror.alert.server.util.AlertFilterCommonUtil;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.MapUtils;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Service
@Transactional
public class AlertVoiceNotifyBizImpl implements AlertVoiceNotifyBiz {

    @Autowired
    private AlertVoiceNotifyDao alertVoiceNotifyDao;
    @Autowired
    private AlertsStatisticDao alertsStatisticDao;
    @Autowired
    private CmdbHelper cmdbHelper;

    @Override
    public String createdAlertVoiceNotify(AlertVoiceNotifyReqDTO request) {
        try {
            log.info("Create Alert Voice Notify Request is {}", request);
            AlertVoiceNotifyDetail alertVoiceNotify = alertVoiceNotifyDao.getAlertVoiceNotify(request.getCreator());
            if (null != alertVoiceNotify) {
                alertVoiceNotifyDao.deleteVoiceNotifyByCreator(request.getCreator());
            }
            String uuid = UUID.randomUUID().toString();
            request.setUuid(uuid);
            alertVoiceNotifyDao.createdAlertVoiceNotify(TransformUtils.transform(AlertVoiceNotifyReqDTO.class,request));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Create Alert Voice Notify Error is {}", e);
            return "error";
        }
        return "success";
    }

    @Override
    public AlertVoiceNotifyDetail getAlertVoiceNotify(String creator) {
        AlertVoiceNotifyDetail alertVoiceNotify = new AlertVoiceNotifyDetail();
        try {
            log.info("Voice Notify Creator is {}", creator);
            alertVoiceNotify = alertVoiceNotifyDao.getAlertVoiceNotify(creator);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get Alert Voice Notify Error is {}", e);
        }
        return alertVoiceNotify;
    }

    @Override
    public ResponseEntity<String> getVoiceContent(AlertVoiceNotifyContentVo alertVoiceNotifyContentReq) {
        try {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            response.setHeader("Set-Cookie","cookiename=cookievalue; path=/; Domain=domainvaule; Max-age=seconds; HttpOnly");
            log.info("Voice Notify Content Request is {}", alertVoiceNotifyContentReq);
            Map<String, Object> alertFilterSceneInfo = alertVoiceNotifyContentReq.getAlertFilterSceneInfo();
            Map<String, Object> condition = Maps.newHashMap();
            String optionCondition = AlertFilterCommonUtil.getCondition(String.valueOf(alertFilterSceneInfo.get("optionCondition")));
            condition.put("condition", optionCondition);
            Page page = new Page();
            page.setBegin(null);
            page.setPageSize(null);
            page.setParams(condition);
            List<Alerts> listAlerts = alertsStatisticDao.filterPageList(page);
            int alertExistTime = alertVoiceNotifyContentReq.getAlertExistTime();
            Date date = new Date();
            Date startTime = DateUtils.getSpecifiedDay(date,-alertExistTime);
            Calendar beginTime = Calendar.getInstance();
            beginTime.setTime(startTime);
            List<Alerts> list = Lists.newArrayList();
            for (Alerts alerts : listAlerts) {
                Calendar alertTime = Calendar.getInstance();
                alertTime.setTime(alerts.getAlertStartTime());
                boolean startT = alertTime.before(beginTime);
                boolean orderStatus = alerts.getOrderStatus().equals("1");
                boolean operationStatus = alerts.getOperateStatus() == 0;
                if (startT && orderStatus && operationStatus) list.add(alerts);
            }
            String content = alertVoiceNotifyContentReq.getContent();
            List<String> contentList = Lists.newArrayList();
            if (StringUtils.isNotBlank(content)) contentList =  Arrays.asList(content.split(","));
            String voiceAlertId = alertVoiceNotifyContentReq.getVoiceAlertId();
            List<String> voiceAlertIds = Lists.newArrayList();
            if (StringUtils.isNotBlank(voiceAlertId)) voiceAlertIds = Arrays.asList(voiceAlertId.split(","));
            String contentHand = "您好：\n\t告警语音通知信息如下，请尽快处理以下告警：";
            StringBuffer text = new StringBuffer();
            List<String> voiceAlertIdStr = Lists.newArrayList();
            // TODO TEST
            for (Alerts alertsDTO : list) {
                voiceAlertIdStr.add(alertsDTO.getAlertId());
                if (voiceAlertIds.contains(alertsDTO.getAlertId())) continue;
                Map<String, Object> cmdbInstance = cmdbHelper.queryDeviceByRoomIdAndIP(alertsDTO.getIdcType(), alertsDTO.getDeviceIp());
                if (CollectionUtils.isEmpty(contentList) || contentList.contains("curMoniTime"))
                    text.append( DateUtil.format(alertsDTO.getCurMoniTime(), "yyyy-MM-dd HH:mm:ss")).append(",");
                if (CollectionUtils.isEmpty(contentList) || contentList.contains("idcType"))
                    text.append("【资源池：").append( StringUtils.isEmpty(alertsDTO.getIdcType()) ? "空" :alertsDTO.getIdcType()).append("】,");
                if (CollectionUtils.isEmpty(contentList) || contentList.contains("bizSys"))
                    text.append("【").append(StringUtils.isEmpty(alertsDTO.getBizSys()) ? "空" : alertsDTO.getBizSys()).append("】，");
                if (CollectionUtils.isEmpty(contentList) || contentList.contains("deviceClass"))
                    text.append("【").append(StringUtils.isEmpty(alertsDTO.getDeviceClass()) ? "设备" : alertsDTO.getDeviceClass()).append("-");
                if (CollectionUtils.isEmpty(contentList) || contentList.contains("deviceType"))
                    text.append(StringUtils.isEmpty(alertsDTO.getDeviceType()) ? "空" : alertsDTO.getDeviceType());
                if (CollectionUtils.isEmpty(contentList) || contentList.contains("deviceIp"))
                    text.append("：").append(alertsDTO.getDeviceIp()).append("】，");
                if (CollectionUtils.isEmpty(contentList) || contentList.contains("deviceDescription"))
                    text.append("【设备描述：").append(null != cmdbInstance ? MapUtils.getString(cmdbInstance, "device_description") : "空").append("    ");
                if (CollectionUtils.isEmpty(contentList) || contentList.contains("hostName"))
                    text.append("设备名称：").append(alertsDTO.getHostName()).append("】，");
                if (CollectionUtils.isEmpty(contentList) || contentList.contains("alertLevel"))
                    text.append("产生【").append(AlertCommonConstant.alertLevelMap.get(alertsDTO.getAlertLevel())).append("】告警");
                if (CollectionUtils.isEmpty(contentList) || contentList.contains("moniIndex"))
                    text.append(alertsDTO.getMoniIndex()).append("】，");
                if (CollectionUtils.isEmpty(contentList) || contentList.contains("curMoniValue"))
                    text.append("【告警值：").append(alertsDTO.getCurMoniValue()).append("】");
            }
            log.info("Voice Notify Content Response is {}", text.toString());
            String alertIdStr = StringUtils.join(voiceAlertIdStr.toArray(),",");
            alertVoiceNotifyDao.updateVoiceAlertId(alertVoiceNotifyContentReq.getCreator(), alertIdStr);
            String result =  StringUtils.isNotBlank(text.toString()) ? contentHand + text.toString() : "";
            return new ResponseEntity<String>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Voice Notify Content Error is {}", e);
            return new ResponseEntity<String>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
