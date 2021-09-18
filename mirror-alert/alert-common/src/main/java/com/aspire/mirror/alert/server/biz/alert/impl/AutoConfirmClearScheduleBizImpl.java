package com.aspire.mirror.alert.server.biz.alert.impl;

import com.aspire.mirror.alert.server.biz.alert.AutoConfirmClearScheduleBiz;
import com.aspire.mirror.alert.server.biz.helper.AlertsHandleV2Helper;
import com.aspire.mirror.alert.server.clientservice.CmdbDictClient;
import com.aspire.mirror.alert.server.dao.common.AlertScheduleIndexDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsRecordDao;
import com.aspire.mirror.alert.server.dao.alert.AutoConfirmClearDao;
import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsRecord;
import com.aspire.mirror.alert.server.vo.common.AlertScheduleIndexVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsOperationRequestVo;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.MapUtils;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.alert.AlertsBizV2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class AutoConfirmClearScheduleBizImpl implements AutoConfirmClearScheduleBiz {

    @Autowired
    private AutoConfirmClearDao autoConfirmClearDao;
    @Autowired
    private AlertsDao alertsDao;
    @Autowired
    private AlertsRecordDao alertsRecordDao;
    @Autowired
    private AlertsHandleV2Helper alertHandleHelper;
    @Autowired
    private CmdbDictClient cmdbDictClient;
    @Autowired
    private AlertScheduleIndexDao alertScheduleIndexDao;
    @Autowired
    private AlertsBizV2 alertsBizV2;

    @Override
    public void autoConfirm() {
        try {
            // 1.根据开始时间和结束时间查询符合条件的告警数据和规则数据
//            List<ConfigDict> alertAutoConfirmClearTime =
//                    cmdbDictClient.getDictsByType("alert_auto_confirm_time" , null, null, null );
//            if (CollectionUtils.isEmpty(alertAutoConfirmClearTime)) {
//                log.error("The Value Of type alert_auto_confirm_clear Dict Config In CMDB is empty");
//                return;
//            }
//            ConfigDict configDict = alertAutoConfirmClearTime.get(0);
            List<AlertScheduleIndexVo> alertAutoConfirmTimeList =
                    alertScheduleIndexDao.getAlertScheduleIndexDetail("alert_auto_confirm_time");
            AlertScheduleIndexVo alertAutoConfirmTime;
            if (null == alertAutoConfirmTimeList) {
                alertAutoConfirmTime = insertAlertScheduleIndex("confirm");
            } else {
                alertAutoConfirmTime = alertAutoConfirmTimeList.get(0);
            }
//            String startTime = "2019-04-12 15:36:00";
//            String endTime = "2019-05-14 21:19:26";
            String startTime = alertAutoConfirmTime.getIndexValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date endDate = DateUtils.getSpecifiedDay(sdf.parse(startTime),5);
            String endTime = sdf.format(endDate);
            // 判断结束时间是否超过系统当前时间
            boolean num = endDate.getTime() - new Date().getTime() > 0;
            if (!num) {
                List<Map<String, Object>> alertsByCreateTime = alertsDao.getAlertsByCreateTime(startTime, endTime,"1","confirm");
                log.info("[Auto Confirm]>>>>> Alert Count is {}", CollectionUtils.isEmpty(alertsByCreateTime) ? 0 : alertsByCreateTime.size());
                // 2.根据类型匹配相应的服务
                Map<String,Object> confirm = confirm(alertsByCreateTime);
                // 3.更新字典表中数据值
                if ("success".equals(String.valueOf(confirm.get("flag")))) {
//                    Dict dict = new Dict();
//                    dict.setDictId(configDict.getId());
//                    dict.setDictCode(configDict.getName());
//                    dict.setColName(configDict.getType());
//                    dict.setDictNote();
//                    dict.setUpDict(configDict.getPid());
//                    dict.setDescription(configDict.getDescription());
//                    dict.setPname(configDict.getPname());
//                    dict.setCreate_date(configDict.getCreate_date());
//                    cmdbDictClient.updateCfgDict(dict);
                    List<Map<String, Object>> result = (List<Map<String, Object>>)confirm.get("result");
                    log.info("auto confirm count is {}", CollectionUtils.isEmpty(result) ? 0 : result.size());
                    AlertScheduleIndexVo alertScheduleIndexVo = new AlertScheduleIndexVo();
                    alertScheduleIndexVo.setId(alertAutoConfirmTime.getId());
//                    alertScheduleIndexDTO.setIndexValue(CollectionUtils.isEmpty(alertsByCreateTime) || CollectionUtils.isEmpty(result)
//                            ? endTime : String.valueOf(alertsByCreateTime.get(alertsByCreateTime.size() - 1).get("alert_start_time")));
                    alertScheduleIndexVo.setIndexValue(endTime);
                    alertScheduleIndexDao.updateAlertScheduleIndex(alertScheduleIndexVo);
                }

            }
        } catch (Exception e) {
            log.error("Auto Schedule of Confirm And Clear error is {}", e);
        }
    }

    private AlertScheduleIndexVo insertAlertScheduleIndex(String type) {
        AlertScheduleIndexVo alertScheduleIndexVo = new AlertScheduleIndexVo();
        alertScheduleIndexVo.setId( UUID.randomUUID().toString());
        alertScheduleIndexVo.setIndexName(type.equals("confirm")?"告警自动确认时间":"告警自动清除时间");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        alertScheduleIndexVo.setIndexValue(sdf.format(new Date()));
        alertScheduleIndexVo.setIndexType(type.equals("confirm")?"alert_auto_confirm_time":"alert_auto_clear_time");
        alertScheduleIndexVo.setRemark("不可更改不可删除");
        alertScheduleIndexDao.insertAlertScheduleIndex(alertScheduleIndexVo);
        return alertScheduleIndexVo;
    }

    @Override
    public void autoClear() {

        List<AlertScheduleIndexVo> alertAutoClearTimeList =
                alertScheduleIndexDao.getAlertScheduleIndexDetail("alert_auto_clear_time");
        AlertScheduleIndexVo alertAutoClearTime;
        if (null == alertAutoClearTimeList) {
            alertAutoClearTime = insertAlertScheduleIndex("clear");
        } else {
            alertAutoClearTime = alertAutoClearTimeList.get(0);
        }
        String endTime = null;
        try {
            // 1.根据开始时间和结束时间查询符合条件的告警数据和规则数据
//            List<ConfigDict> alertAutoConfirmClearTime =
//                    cmdbDictClient.getDictsByType("alert_auto_clear_time" , null, null, null );
//            if (CollectionUtils.isEmpty(alertAutoConfirmClearTime)) {
//                log.error("The Value Of type alert_auto_confirm_clear Dict Config In CMDB is empty");
//                return;
//            }
//            ConfigDict configDict = alertAutoConfirmClearTime.get(0);
            String startTime = alertAutoClearTime.getIndexValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date endDate = DateUtils.getSpecifiedDay(sdf.parse(startTime),5);
            endTime = sdf.format(endDate);
            // 判断结束时间是否超过系统当前时间
            boolean num = endDate.getTime() - new Date().getTime() > 0;
            if (!num) {
                List<Map<String, Object>> alertsByCreateTime = alertsDao.getAlertsByCreateTime(startTime, endTime,"2",null);
                log.info("[Auto Clear]>>>>> Alert Count is {}", CollectionUtils.isEmpty(alertsByCreateTime) ? 0 : alertsByCreateTime.size());
                // 2.根据类型匹配相应的服务
//                Map<String,Object> clear = clear(alertsByCreateTime);
                for (Map<String, Object> map : alertsByCreateTime) {
                    AlertsOperationRequestVo request = new AlertsOperationRequestVo();
                    request.setAlertIds(MapUtils.getString(map,"alert_id"));
                    request.setContent(MapUtils.getString(map,"content"));
                    request.setNamespace(MapUtils.getString(map,"operator"));
                    request.setAutoType(-1);
                    alertsBizV2.manualClear(request);
                }

            } else {
                endTime = null;
            }
        } catch (Exception e) {
            log.error("Auto Schedule of Confirm And Clear error is {}", e);
        } finally {
            if (!StringUtils.isEmpty(endTime)) {
                // 3.更新字典表中数据值
                AlertScheduleIndexVo alertScheduleIndexVo = new AlertScheduleIndexVo();
                alertScheduleIndexVo.setId(alertAutoClearTime.getId());
//                    alertScheduleIndexDTO.setIndexValue(CollectionUtils.isEmpty(alertsByCreateTime) || CollectionUtils.isEmpty(result)
//                            ? endTime : String.valueOf(alertsByCreateTime.get(alertsByCreateTime.size() - 1).get("alert_start_time")));
                alertScheduleIndexVo.setIndexValue(endTime);
                alertScheduleIndexDao.updateAlertScheduleIndex(alertScheduleIndexVo);
            }
        }
    }

    private Map<String,Object> confirm(List<Map<String, Object>> alertsByCreateTime) {
        Map<String,Object> res = Maps.newHashMap();
        try {
            List<Map<String, Object>> result = Lists.newArrayList();
            for (Map<String, Object> map : alertsByCreateTime) {
                // 1.根据告警规则查询规则
                result.add(map);
                Alerts alerts = new Alerts();
                alerts.setAlertId(String.valueOf(map.get("alert_id")));
                alerts.setOperateStatus(1);
                // 2.修改告警操作状态为已确认状态
                int index = alertsDao.updateByPrimaryKey(alerts);
                AlertsRecord alertsRecord = new AlertsRecord();
                alertsRecord.setAlertId(alerts.getAlertId());
                alertsRecord.setUserName(String.valueOf(map.get("operator")));
                alertsRecord.setOperationType("1");
                alertsRecord.setContent(String.valueOf(map.get("content")));
                if (index == 1) {
                    alertsRecord.setOperationStatus("1");
                } else {
                    alertsRecord.setOperationStatus("0");
                }
                // 3.录入操作记录表
                alertsRecordDao.insert(alertsRecord);
            }
            res.put("flag","success");
            res.put("result",result);
        } catch (Exception e) {
            log.error("Auto Confirm Schedule Error is {}", e);
            res.put("flag","error");
        }
        return res;
    }

    @Override
    public void delete() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        autoConfirmClearDao.deleteRule(sdf.format(new Date()));
    }
}
