package com.aspire.mirror.alert.server.biz.alert.impl;

import com.aspire.mirror.alert.server.biz.alert.AlertIntelligentBiz;
import com.aspire.mirror.alert.server.dao.alert.AlertIntelligentDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsRecordDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsTransferDao;
import com.aspire.mirror.alert.server.dao.alert.po.AlertReportOperateRecord;
import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsRecord;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsTransfer;
import com.aspire.mirror.alert.server.vo.alert.AlertStatisticSummaryVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class AlertIntelligentBizImpl implements AlertIntelligentBiz{

    private static final String ALERT_LEVEL_TIP = "1";
    private static final String ALERT_LEVEL_LOW = "2";
    private static final String ALERT_LEVEL_MEDIUM = "3";
    private static final String ALERT_LEVEL_HIGH = "4";
    private static final String ALERT_LEVEL_SERIOUS = "5";
    private static final int ALERT_OPERATE_STATUS_CONFIRMED = 1;

    @Autowired
    private AlertIntelligentDao alertIntelligentDao;
    @Autowired
    private AlertsRecordDao alertsRecordDao;
    @Autowired
    private AlertsTransferDao alertsTransferDao;

    @Override
    public PageResponse<AlertsVo> queryAlertIntelligent(PageRequest pageRequest, String intelligentAlertId) {
        PageResponse<AlertsVo> pageResponse = new PageResponse<AlertsVo>();
        try {
            Page page = PageUtil.convert(pageRequest);
            int count = 0;
            if (StringUtils.isNotEmpty(intelligentAlertId)) {
                Map<String, Object> params = page.getParams();
                params.put("intelligentAlertId",intelligentAlertId);
                page.setParams(params);
            }
            List<Alerts> countList = Lists.newArrayList();
            if (StringUtils.isEmpty(intelligentAlertId)) {
                countList.addAll(alertIntelligentDao.queryAlertIntelligentCount(page));
            } else {
                countList.addAll(alertIntelligentDao.queryAlertCountByIntelligentAlertId(page));
            }
            if (!CollectionUtils.isEmpty(countList)) {
                count += countList.size();
            }
            pageResponse.setCount(count);
            if (count > 0) {
                List<Alerts> listAlerts = Lists.newArrayList();
                if (StringUtils.isEmpty(intelligentAlertId)) {
                    listAlerts.addAll(alertIntelligentDao.queryAlertIntelligentData(page));
                } else {
                    listAlerts.addAll(alertIntelligentDao.queryAlertDataByIntelligentAlertId(page));
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
            log.error("Query Alert Intelligent error is {}",e);
        }
        return pageResponse;
    }

    @Override
    public AlertStatisticSummaryVo alertIntelligentOverview(PageRequest pageRequest) {
        AlertStatisticSummaryVo summaryDTO = new AlertStatisticSummaryVo();
        Page page = PageUtil.convert(pageRequest);
        try {
            List<Alerts> listAlerts = alertIntelligentDao.queryAlertIntelligentCount(page);
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
    public List<AlertsVo> exportAlertIntelligentData(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        List<Alerts> listAlerts = alertIntelligentDao.queryAlertIntelligentCount(page);
        return TransformUtils.transform(AlertsVo.class, listAlerts);
    }

}
