package com.aspire.mirror.alert.server.biz.alert.impl;

import com.aspire.mirror.alert.server.aspect.RequestAuthContext;
import com.aspire.mirror.alert.server.biz.alert.AlertHomeBiz;
import com.aspire.mirror.alert.server.biz.helper.CmdbHelper;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.dao.alert.AlertHomeDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsRecordDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsStatisticDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsTransferDao;
import com.aspire.mirror.alert.server.dao.alert.po.*;
import com.aspire.mirror.alert.server.dao.notify.AlertVoiceNotifyDao;
import com.aspire.mirror.alert.server.dao.notify.po.AlertVoiceNotifyDetail;
import com.aspire.mirror.alert.server.dao.notify.po.AlertNotifyConfigLogDetail;
import com.aspire.mirror.alert.server.vo.alert.AlertStatisticSummaryVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsQueryVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsHisVo;
import com.aspire.mirror.alert.server.helper.AuthHelper;
import com.aspire.mirror.alert.server.util.AlertFilterCommonUtil;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.common.util.PageUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Service
@Transactional
public class AlertHomeBizImpl implements AlertHomeBiz {

    private static final String ALERT_LEVEL_TIP = "1";
    private static final String ALERT_LEVEL_LOW = "2";
    private static final String ALERT_LEVEL_MEDIUM = "3";
    private static final String ALERT_LEVEL_HIGH = "4";
    private static final String ALERT_LEVEL_SERIOUS = "5";
    private static final int ALERT_OPERATE_STATUS_CONFIRMED = 1;

    @Autowired
    private AlertsRecordDao alertsRecordDao;
    @Autowired
    private AlertsTransferDao alertsTransferDao;
    @Autowired
    private AlertHomeDao alertHomeDao;
    @Autowired
    private AuthHelper authHelper;
    
   

    @Override
    public PageResponse<AlertsVo> select(PageRequest pageRequest, String alertType) {
        PageResponse<AlertsVo> pageResponse = new PageResponse<AlertsVo>();
        try {
        	 Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
        	 Map<String,String> authMap = authHelper.packAlertCondition(resFilterMap);
       
            Page page = PageUtil.convert(pageRequest);
            page.getParams().put("authFilter", authMap);
            
            int count = 0;
            List<Alerts> alerts = Lists.newArrayList();
            if ("activity".equals(alertType)) {
                alerts.addAll(alertHomeDao.getHomeActivityViewCount(page));
            } else if ("confirm".equals(alertType)) {
                alerts.addAll(alertHomeDao.getHomeConfirmViewCount(page));
            }
            if (!CollectionUtils.isEmpty(alerts)) {
                count += alerts.size();
            }
            pageResponse.setCount(count);
            if (count > 0) {
                List<Alerts> listAlerts = Lists.newArrayList();
                if ("activity".equals(alertType)) {
                    listAlerts.addAll(alertHomeDao.getHomeActivityData(page));
                } else if ("confirm".equals(alertType)) {
                    listAlerts.addAll(alertHomeDao.getHomeConfirmData(page));
                }
                List<AlertsVo> listDTO = TransformUtils.transform(AlertsVo.class, listAlerts);
                for (AlertsVo alertHis: listDTO) {
                    String alertId = alertHis.getAlertId();
                    List<AlertsRecord> recordList = alertsRecordDao.selectRecordByPrimaryKey(alertId);

                    List<AlertReportOperateRecord> reportRecord = alertsRecordDao.selectReportOperationRecordByAlertKey(alertId);
                    if (!CollectionUtils.isEmpty(reportRecord)) {
                        alertHis.setReportTime(reportRecord.get(0).getCreateTime());
                        alertHis.setReportStatus(reportRecord.get(0).getStatus());
                        alertHis.setReportType(reportRecord.get(0).getReportType());
                    }
                    if (!CollectionUtils.isEmpty(recordList)) {
                        for (AlertsRecord record: recordList) {
                            String operationType = record.getOperationType();
                            switch (operationType) {
                                case "0":
                                    if (alertHis.getTransTime() == null || alertHis.getTransTime().before(record.getOperationTime())) {
                                        alertHis.setTransUser(record.getUserName());
                                        alertHis.setTransStatus(Integer.parseInt(record.getOperationStatus()));
                                        alertHis.setTransTime(record.getOperationTime());
                                    }
                                    break;
                                case "1":
                                    if (alertHis.getConfirmedTime() == null || alertHis.getConfirmedTime().before(record.getOperationTime())) {
                                        alertHis.setConfirmedUser(record.getUserName());
                                        alertHis.setConfirmedTime(record.getOperationTime());
                                        alertHis.setConfirmedContent(record.getContent());
                                    }
                                    break;
                                case "2":
                                    if (alertHis.getDeliverTime() == null || alertHis.getDeliverTime().before(record.getOperationTime())) {
                                        alertHis.setDeliverTime(record.getOperationTime());
                                        alertHis.setDeliverStatus(Integer.parseInt(record.getOperationStatus()));
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if (org.apache.commons.lang.StringUtils.isEmpty(alertHis.getConfirmedUser())) {
                        List<AlertsTransfer> transferList = alertsTransferDao.selectByAlertId(alertHis.getAlertId());
                        Set<String> nameSet = new HashSet<>();
                        for (AlertsTransfer transfer: transferList) {
                            nameSet.add(transfer.getConfirmUserName());
                        }
                        StringBuilder username = new StringBuilder("");
                        for (String name: nameSet) {
                            username.append(name).append(",");
                        }
                        String toConfirmUser = username.toString();
                        alertHis.setToConfirmUser(toConfirmUser.length() > 0 ? toConfirmUser.substring(0, toConfirmUser.length()-1) : "");
                    }
                }
                pageResponse.setResult(listDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error is {}",e);
        }
        return pageResponse;
    }
    
    @Override
    public List<String> activeAlertSourceList() {
    	return alertHomeDao.getActiveAlertSourceList();
    }

    @Override
    public AlertStatisticSummaryVo getOverview(PageRequest pageRequest, String alertType) {
        AlertStatisticSummaryVo summaryDTO = new AlertStatisticSummaryVo();
        Page page = PageUtil.convert(pageRequest);
        Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
   	   Map<String,String> authMap = authHelper.packAlertCondition(resFilterMap);
    	page.getParams().put("authFilter", authMap);
        page.setBegin(null);
        page.setPageSize(null);
        try {
            List<Alerts> listAlerts = Lists.newArrayList();
            if ("activity".equals(alertType)) {
                listAlerts.addAll(alertHomeDao.getHomeActivityViewCount(page));
            } else if ("confirm".equals(alertType)) {
                listAlerts.addAll(alertHomeDao.getHomeConfirmViewCount(page));
            }
            int tip = 0;
            int low = 0;
            int medium = 0;
            int high = 0;
            int serious = 0;
            int confirmed = 0;
            for (Alerts alert : listAlerts) {
                switch (alert.getAlertLevel()) {
                    case ALERT_LEVEL_TIP:
                        tip += 1;
                        break;
                    case ALERT_LEVEL_LOW:
                        low += 1;
                        break;
                    case ALERT_LEVEL_MEDIUM:
                        medium += 1;
                        break;
                    case ALERT_LEVEL_HIGH:
                        high += 1;
                        break;
                    case ALERT_LEVEL_SERIOUS:
                        serious += 1;
                        break;
                    default:
                        break;
                }
                if (alert.getOperateStatus() != null && alert.getOperateStatus() == ALERT_OPERATE_STATUS_CONFIRMED) {
                    confirmed += 1;
                }
            }
            summaryDTO.setSummary(CollectionUtils.isEmpty(listAlerts) ? 0 : listAlerts.size());
            summaryDTO.setConfirmed(confirmed);
            summaryDTO.setTip(tip);
            summaryDTO.setLow(low);
            summaryDTO.setMedium(medium);
            summaryDTO.setHigh(high);
            summaryDTO.setSerious(serious);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("select Home Alert Overview is error {}",e);
        }
        return summaryDTO;
    }

    @Override
    public PageResponse<AlertsHisVo> selectHis(PageRequest pageRequest) {
        PageResponse<AlertsHisVo> pageResponse = new PageResponse<>();
        try {
            Page page = PageUtil.convert(pageRequest);
            Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
        	   Map<String,String> authMap = authHelper.packAlertCondition(resFilterMap);
         	page.getParams().put("authFilter", authMap);
            int count = 0;
            List<AlertsHis> alertsHis = alertHomeDao.exportHis(page);
            if (!CollectionUtils.isEmpty(alertsHis)) {
                count += alertsHis.size();
            }
            pageResponse.setCount(count);
            if (count > 0) {
                List<AlertsHis> listHisAlerts = alertHomeDao.getHomeAlertHisData(page);
                List<AlertsHisVo> listHisDTO = TransformUtils.transform(AlertsHisVo.class, listHisAlerts);
                for (AlertsHisVo alertHis: listHisDTO) {
                    String alertId = alertHis.getAlertId();
                    List<AlertsRecord> recordList = alertsRecordDao.selectRecordByPrimaryKey(alertId);
                    List<AlertReportOperateRecord> reportRecord = alertsRecordDao.selectReportOperationRecordByAlertKey(alertId);
                    if (!CollectionUtils.isEmpty(reportRecord)) {
                        alertHis.setReportTime(reportRecord.get(0).getCreateTime());
                        alertHis.setReportStatus(reportRecord.get(0).getStatus());
                        alertHis.setReportType(reportRecord.get(0).getReportType());
                    }
                    if (!CollectionUtils.isEmpty(recordList)) {
                        for (AlertsRecord record: recordList) {
                            String operationType = record.getOperationType();
                            switch (operationType) {
                                case "0":
                                    if (alertHis.getTransTime() == null || alertHis.getTransTime().before(record.getOperationTime())) {
                                        alertHis.setTransUser(record.getUserName());
                                        alertHis.setTransStatus(Integer.parseInt(record.getOperationStatus()));
                                        alertHis.setTransTime(record.getOperationTime());
                                    }
                                    break;
                                case "1":
                                    if (alertHis.getConfirmedTime() == null || alertHis.getConfirmedTime().before(record.getOperationTime())) {
                                        alertHis.setConfirmedUser(record.getUserName());
                                        alertHis.setConfirmedTime(record.getOperationTime());
                                        alertHis.setConfirmedContent(record.getContent());
                                    }
                                    break;
                                case "2":
                                    if (alertHis.getDeliverTime() == null || alertHis.getDeliverTime().before(record.getOperationTime())) {
                                        alertHis.setDeliverTime(record.getOperationTime());
                                        alertHis.setDeliverStatus(Integer.parseInt(record.getOperationStatus()));
                                    }
                                    break;
    //                            case "3":
    //                                if (alertHis.getClearTime() == null || alertHis.getClearTime().before(record.getOperationTime())) {
    //                                    alertHis.setClearTime(record.getOperationTime());
    //                                    alertHis.setClearContent(record.getContent());
    //                                    alertHis.setClearUser(record.getUserName());
    //                                }
    //                                break;
                                default:
                                    break;
                            }
                        }
                    }
                    if (org.apache.commons.lang.StringUtils.isEmpty(alertHis.getConfirmedUser())) {
                        List<AlertsTransfer> transferList = alertsTransferDao.selectByAlertId(alertHis.getAlertId());
                        Set<String> nameSet = new HashSet<>();
                        for (AlertsTransfer transfer: transferList) {
                            nameSet.add(transfer.getConfirmUserName());
                        }
                        StringBuilder username = new StringBuilder("");
                        for (String name: nameSet) {
                            username.append(name).append(",");
                        }
                        String toConfirmUser = username.toString();
                        alertHis.setToConfirmUser(toConfirmUser.length() > 0 ? toConfirmUser.substring(0, toConfirmUser.length()-1) : "");
                    }
                }
                pageResponse.setResult(listHisDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("select Home Alert His is error {}",e);
        }
        return pageResponse;
    }

    @Override
    public List<Alerts> export(PageRequest pageRequest,String alertType) {
        Page page = PageUtil.convert(pageRequest);
        Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
    	   Map<String,String> authMap = authHelper.packAlertCondition(resFilterMap);
     	page.getParams().put("authFilter", authMap);
        List<Alerts> listAlerts = Lists.newArrayList();
        if ("activity".equals(alertType)) {
            listAlerts.addAll(alertHomeDao.getHomeActivityViewCount(page));
        } else if ("confirm".equals(alertType)) {
            listAlerts.addAll(alertHomeDao.getHomeConfirmViewCount(page));
        }
        return listAlerts;
    }

    @Override
    public List<AlertsHis> exportHis(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
    	   Map<String,String> authMap = authHelper.packAlertCondition(resFilterMap);
     	page.getParams().put("authFilter", authMap);
        List<AlertsHis> listHisAlerts = alertHomeDao.exportHis(page);
        return listHisAlerts;
    }

    @Autowired
    private AlertVoiceNotifyDao alertVoiceNotifyDao;
    @Autowired
    private AlertsStatisticDao alertsStatisticDao;
    @Autowired
    private CmdbHelper cmdbHelper;
    @Override
    public ResponseEntity<String> getHomeAlertVoiceContent(boolean isUandS, AlertsQueryVo queryRequest) {
        try {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            response.setHeader("Set-Cookie","cookiename=cookievalue; path=/; Domain=domainvaule; Max-age=seconds; HttpOnly");
            AlertVoiceNotifyDetail alertVoiceNotify = alertVoiceNotifyDao.getAlertVoiceNotify(queryRequest.getUserName());
            if (null == alertVoiceNotify) return new ResponseEntity<String>("", HttpStatus.OK);
            Map<String, Object> alertFilterSceneInfo = alertVoiceNotify.getAlertFilterSceneInfo();
            Map<String, Object> condition = Maps.newHashMap();
            String optionCondition = AlertFilterCommonUtil.getCondition(String.valueOf(alertFilterSceneInfo.get("optionCondition")));
            condition.put("condition", optionCondition);
            Page p = new Page();
            p.setParams(condition);
            List<Alerts> listAlerts = alertsStatisticDao.filterPageList(p);
            if (CollectionUtils.isEmpty(listAlerts)) return new ResponseEntity<String>("", HttpStatus.OK);
            if (!isUandS) {
                // 权限字段
                boolean roomFlag = true;
                List<String> authPrecinctList = queryRequest.getAuthPrecinctList();
                boolean deviceTypeFlag = true;
                List<String> authDeviceTypeList = queryRequest.getAuthDeviceTypeList();
                boolean deviceIpFlag = true;
                List<String> authDeviceIdList = queryRequest.getAuthDeviceIdList();
                boolean bizSystemFlag = true;
                List<String> authBizSystemIdList = queryRequest.getAuthBizSystemIdList();
                boolean idcTypeFlag = true;
                List<String> authIdcIdList = queryRequest.getAuthIdcIdList();
                for (int i = 0; i < listAlerts.size(); i++) {
                    Alerts alert = listAlerts.get(i);
                    int queryType = "0".equals(queryRequest.getQueryType()) ? 0 : 1;
                    if (alert.getOperateStatus() != queryType) {
                        listAlerts.remove(i);
                        continue;
                    }
                    if (CollectionUtils.isNotEmpty(authPrecinctList) && !authPrecinctList.contains(alert.getSourceRoom())) roomFlag = false;
                    if (CollectionUtils.isNotEmpty(authDeviceTypeList) && !authDeviceTypeList.contains(alert.getDeviceType())) deviceTypeFlag = false;
                    if (CollectionUtils.isNotEmpty(authDeviceIdList) && !authDeviceIdList.contains(alert.getDeviceIp())) deviceIpFlag = false;
                    if (CollectionUtils.isNotEmpty(authBizSystemIdList) && !authBizSystemIdList.contains(alert.getBizSys())) bizSystemFlag = false;
                    if (CollectionUtils.isNotEmpty(authIdcIdList) && !authIdcIdList.contains(alert.getIdcType())) idcTypeFlag = false;
                    if (!roomFlag && !deviceTypeFlag && !deviceIpFlag && !bizSystemFlag && !idcTypeFlag) listAlerts.remove(i);
                }
            }
            int alertExistTime = alertVoiceNotify.getAlertExistTime();
            Date date = new Date();
            Date startTime = DateUtils.getSpecifiedDay(date,-alertExistTime);
            Calendar beginTime = Calendar.getInstance();
            beginTime.setTime(startTime);
            String content = alertVoiceNotify.getContent();
            List<String> contentList = Lists.newArrayList();
            if (StringUtils.isNotBlank(content)) contentList =  Arrays.asList(content.split(","));
            List<String> alertNotifyConfigLog = alertVoiceNotifyDao.getAlertNotifyConfigLog(alertVoiceNotify.getUuid());
            String contentHand = "您好：\n\t告警语音通知信息如下，请尽快处理以下告警：";
            StringBuffer text = new StringBuffer();
            List<AlertNotifyConfigLogDetail> alertNotifyConfigLogDetails = Lists.newArrayList();
            for (Alerts alert : listAlerts) {
                if (alert.getOperateStatus() != Integer.parseInt(queryRequest.getQueryType())) continue;
                Calendar alertTime = Calendar.getInstance();
                alertTime.setTime(alert.getAlertStartTime());
                boolean startT = alertTime.before(beginTime);
                boolean orderStatus = alert.getOrderStatus().equals("1");
                boolean operationStatus = alert.getOperateStatus() == 0;
                boolean time = date.after(DateUtils.getTimesmorning()) && date.before(DateUtils.getTodayEndTime());
                if (startT && orderStatus && operationStatus && time) {
                    if (alertNotifyConfigLog.contains(alert.getAlertId())) continue;
                    Map<String, Object> cmdbInstance = cmdbHelper.queryDeviceByRoomIdAndIP(alert.getIdcType(), alert.getDeviceIp());
                    if (org.springframework.util.CollectionUtils.isEmpty(contentList) || contentList.contains("curMoniTime"))
                        text.append( DateUtil.format(alert.getCurMoniTime(), "yyyy-MM-dd HH:mm:ss")).append(",");
                    if (org.springframework.util.CollectionUtils.isEmpty(contentList) || contentList.contains("idcType"))
                        text.append("【资源池：").append( StringUtils.isEmpty(alert.getIdcType()) ? "空" :alert.getIdcType()).append("】,");
                    if (org.springframework.util.CollectionUtils.isEmpty(contentList) || contentList.contains("bizSys"))
                        text.append("【").append(StringUtils.isEmpty(alert.getBizSys()) ? "空" : alert.getBizSys()).append("】，");
                    if (org.springframework.util.CollectionUtils.isEmpty(contentList) || contentList.contains("deviceClass"))
                        text.append("【").append(StringUtils.isEmpty(alert.getDeviceClass()) ? "设备" : alert.getDeviceClass()).append("-");
                    if (org.springframework.util.CollectionUtils.isEmpty(contentList) || contentList.contains("deviceType"))
                        text.append(StringUtils.isEmpty(alert.getDeviceType()) ? "空" : alert.getDeviceType());
                    if (org.springframework.util.CollectionUtils.isEmpty(contentList) || contentList.contains("deviceIp"))
                        text.append("：").append(alert.getDeviceIp()).append("】，");
                    if (CollectionUtils.isEmpty(contentList) || contentList.contains("deviceDescription"))
                        text.append("【设备描述：").append(null != cmdbInstance ? cmdbInstance.get("device_description") : "空").append("    ");
                    if (org.springframework.util.CollectionUtils.isEmpty(contentList) || contentList.contains("hostName"))
                        text.append("设备名称：").append(alert.getHostName()).append("】，");
                    if (org.springframework.util.CollectionUtils.isEmpty(contentList) || contentList.contains("alertLevel"))
                        text.append("产生【").append( AlertCommonConstant.alertLevelMap.get(alert.getAlertLevel())).append("】告警");
                    if (org.springframework.util.CollectionUtils.isEmpty(contentList) || contentList.contains("moniIndex"))
                        text.append(alert.getMoniIndex()).append("】，");
                    if (org.springframework.util.CollectionUtils.isEmpty(contentList) || contentList.contains("curMoniValue"))
                        text.append("【告警值：").append(alert.getCurMoniValue()).append("】");

                    AlertNotifyConfigLogDetail detail = new AlertNotifyConfigLogDetail();
                    detail.setAlertNotifyConfigId(alertVoiceNotify.getUuid());
                    detail.setVoiceAlertId(alert.getAlertId());
                    detail.setOperator(alertVoiceNotify.getCreator());
                    alertNotifyConfigLogDetails.add(detail);
                }
            }
            log.info("Voice Notify Content Response is {}", text.toString());
            if (!CollectionUtils.isEmpty(alertNotifyConfigLogDetails)) {
                alertVoiceNotifyDao.insertAlertNotifyConfigLog(alertNotifyConfigLogDetails);
            }
            String result =  StringUtils.isNotBlank(text.toString()) ? contentHand + text.toString() : "";
            return new ResponseEntity<String>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("[ALERT-SERVICE]>>>Get Home Alert Voice Content Error is {}", e);
        }
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @Override
    public AlertStatisticSummaryVo hisOverview(PageRequest pageRequest) {
        AlertStatisticSummaryVo summaryDTO = new AlertStatisticSummaryVo();
        Page page = PageUtil.convert(pageRequest);
        Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
    	   Map<String,String> authMap = authHelper.packAlertCondition(resFilterMap);
     	page.getParams().put("authFilter", authMap);
        page.setBegin(null);
        page.setPageSize(null);
        try {
            List<AlertsHis> alertsHis = alertHomeDao.exportHis(page);
            int tip = 0;
            int low = 0;
            int medium = 0;
            int high = 0;
            int serious = 0;
            int confirmed = 0;
            for (AlertsHis alert : alertsHis) {
                switch (alert.getAlertLevel()) {
                    case ALERT_LEVEL_TIP:
                        tip += 1;
                        break;
                    case ALERT_LEVEL_LOW:
                        low += 1;
                        break;
                    case ALERT_LEVEL_MEDIUM:
                        medium += 1;
                        break;
                    case ALERT_LEVEL_HIGH:
                        high += 1;
                        break;
                    case ALERT_LEVEL_SERIOUS:
                        serious += 1;
                        break;
                    default:
                        break;
                }
                if (alert.getOperateStatus() != null && alert.getOperateStatus() == ALERT_OPERATE_STATUS_CONFIRMED) {
                    confirmed += 1;
                }
            }
            summaryDTO.setSummary(CollectionUtils.isEmpty(alertsHis) ? 0 : alertsHis.size());
            summaryDTO.setConfirmed(confirmed);
            summaryDTO.setTip(tip);
            summaryDTO.setLow(low);
            summaryDTO.setMedium(medium);
            summaryDTO.setHigh(high);
            summaryDTO.setSerious(serious);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[ALERT-SERVICE]>>> Get Home Alert His Overview is error {}",e);
        }
        return summaryDTO;
    }
}
