package com.aspire.ums.cmdb.collect.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.CollectConst;
import com.aspire.ums.cmdb.collect.entity.*;
import com.aspire.ums.cmdb.collect.mapper.CollectOriginalRecordMapper;
import com.aspire.ums.cmdb.collect.service.CollectChangeLogService;
import com.aspire.ums.cmdb.collect.service.CollectEmployeeService;
import com.aspire.ums.cmdb.collect.service.CollectOriginalRecordService;
import com.aspire.ums.cmdb.collect.service.CollectService;
import com.aspire.ums.cmdb.maintain.entity.FormValue;
import com.aspire.ums.cmdb.maintain.entity.InstanceModel;
import com.aspire.ums.cmdb.maintain.service.FormValueService;
import com.aspire.ums.cmdb.module.entity.Form;
import com.aspire.ums.cmdb.util.HttpUtil;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectService
 * Author:   zhu.juwang
 * Date:     2019/3/12 14:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CollectOriginalRecordServiceImpl implements CollectOriginalRecordService {

    @Autowired
    private CollectOriginalRecordMapper recordMapper;
    @Autowired
    private FormValueService formValueService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private CollectEmployeeService employeeService;
    @Autowired
    private CollectChangeLogService changeLogService;
    @Value("${cmdb.inferface.alert.insert}")
    private String alertInsertInferface;

    @Override
    public Page getCollectRecordsByCollectId(String collectId, Page<Map> page) {
        List<Map> totalMap = recordMapper.getCollectRecordsByCollectId(collectId, null, null);
        page.setTotal(totalMap.size());
        page.setDataList(totalMap);
        return page;
    }

    @Override
    public void deleteVOByCollectId(String collectId) {
        recordMapper.deleteVOByCollectId(collectId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void insertVO(CollectOriginalRecordEntity recordEntity) {
        recordMapper.insertVO(recordEntity);
    }

    @Override
    public List<Map<String, String>> getCollectInfoByTaskId(String taskId) {
        return recordMapper.getCollectInfoByTaskId(taskId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void resolveData(String batchId, Map<String, Map<String, List<JSONObject>>> noticeMap, Map<String, String> taskCollect, String formData) {
        String instanceId = taskCollect.get("instanceId");
        String formCode = taskCollect.get("formCode");
        List<Map<String, String>> formValues = formValueService.getFormValueMapByInstanceId(instanceId);
        String deviceIp = getFormValue(formValues, CollectConst.IP_FILED).get("formValue");
        CollectEntity collectEntity = collectService.getCollectByCollectId(taskCollect.get("collectId"));
        //替换采集值
        String formValueId = null, oldValue = null;
        Map<String, String> formMap = getFormValue(formValues, formCode);
        if (formMap != null && formMap.size() > 0) {
            formValueId = formMap.get("id");
            oldValue = formMap.get("formValue");
        }
        //判断新采集的值 是否与 原有值 一样
        boolean isEqual = compare(oldValue, formData);
        //如果相等则不需要做任何处理
        if (isEqual) {
            return;
        }
        //记录到cmdb_collect_change_log
        CollectChangeLogEntity changeLogEntity = new CollectChangeLogEntity();
        changeLogEntity.setId(UUIDUtil.getUUID());
        changeLogEntity.setBatchId(batchId);
        changeLogEntity.setFormId(taskCollect.get("formId"));
        changeLogEntity.setInstanceId(instanceId);
        changeLogEntity.setOldValue(oldValue);
        changeLogEntity.setCurrValue(formData);
        changeLogEntity.setOperaterType("修改");
        changeLogEntity.setOperator("系统管理员");
        changeLogEntity.setOperatorTime(new Date());
        changeLogService.insertVO(changeLogEntity);

        //todo 需要保留 审批覆盖功能
        InstanceModel instanceModel = new InstanceModel();
        instanceModel.setId(instanceId);
        List<FormValue> formValueList = new ArrayList<>();
        FormValue formValue = new FormValue();
        if (StringUtils.isNotEmpty(formValueId)) {
            formValue.setId(formValueId);
        }
        formValue.setFormId(taskCollect.get("formId"));
        formValue.setFormCode(formCode);
        formValue.setFormValue(formData == null ? "" : formData);
        formValue.setInstanceId(instanceId);
        Form form = new Form();
        if (formCode.equalsIgnoreCase("Y_name")) {
            form.setCode(formCode);
        }
        formValue.setForm(form);
        formValueList.add(formValue);
        instanceModel.setFormValues(formValueList);
        formValueService.update(instanceModel);
        //发送告警通知
        if (collectEntity.getNeedAlarm().equals(CollectConst.WHETHER_YES)) {
            sendAlert(instanceId, deviceIp, String.format("采集配置异常(%s->%s)", oldValue, formData), formData, collectEntity.getAlermLevel());
        }
        //发送邮件通知
        if (collectEntity.getNeedNotice().equals(CollectConst.WHETHER_YES)) {
            List<CollectEmployeeEntity> employeeList = employeeService.getCollectEmployee(collectEntity.getId(), "collect_notice_employee");
            if (employeeList != null && employeeList.size() > 0) {
                //key = device_ip + "_" + employeeName + "_" + alarm_level
                for (CollectEmployeeEntity employeeEntity : employeeList) {
                    String key = deviceIp + "_" + employeeEntity.getEmployeeName() + "_" + collectEntity.getAlermLevel();
                    if (!noticeMap.containsKey(key)) {
                        List<JSONObject> dataList = new ArrayList<>();
                        Map<String, List<JSONObject>> dataMap = new HashMap<>();
                        dataMap.put("data", dataList);
                        noticeMap.put(key, dataMap);
                    }
                    List<JSONObject> dataList = noticeMap.get(key).get("data");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("formName", formMap.get("formName"));
                    jsonObject.put("formCode", formMap.get("formCode"));
                    jsonObject.put("oldValue", oldValue);
                    jsonObject.put("currValue", formData);
                    dataList.add(jsonObject);
                    noticeMap.get(key).put("data", dataList);
                }
            }
        }
    }

    private void sendAlert(String instanceId, String deviceIp, String indexContent, String currValue, String alarmLevel) {
        JSONObject createAlert = new JSONObject();
        createAlert.put("moni_index", indexContent);
        createAlert.put("device_id", instanceId);
        createAlert.put("cur_moni_value", currValue);
        createAlert.put("cur_moni_time", new Date());
        createAlert.put("source","内部告警");
        createAlert.put("alert_start_time", new Date());
        createAlert.put("alert_level", alarmLevel);
        createAlert.put("moni_object","Cmdb DataChange");
        createAlert.put("device_ip", deviceIp);
        createAlert.put("object_type","2");
        List<Map<String, String>> oldFormValues = formValueService.getFormValueMapByInstanceId(instanceId);
        for (Map<String, String> entry : oldFormValues) {
            if (entry.get("formCode").equals(CollectConst.BIZ_SYSTEM_FILED)) {
                createAlert.put("biz_sys", entry.get("formValue"));
            }
            if (entry.get("formCode").equals(CollectConst.IDC_FILED)) {
                createAlert.put("idc_type", entry.get("formValue"));
            }
            if (entry.get("formCode").equals(CollectConst.ROOM_ID_FILED)) {
                createAlert.put("source_room", entry.get("formValue"));
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Send alert url -> {} params -> {}", alertInsertInferface, createAlert.toJSONString());
        }
        HttpUtil.postMethod(alertInsertInferface, null, createAlert);
    }

    private Map<String, String> getFormValue(List<Map<String, String>> oldFormValues, String formCode) {
        for (Map<String, String> entry : oldFormValues) {
            if (entry.get("formCode").equals(formCode)) {
                return entry;
            }
        }
        return new HashMap<>();
    }

    /**
     * 比较两个值是否相等
     * @param oldValue 旧值
     * @param value 新值
     * @return false 不相等 true 相等
     */
    private boolean compare(String oldValue, String value) {
        if (String.valueOf(oldValue).equals(String.valueOf(value))) {
            return true;
        }
        if (oldValue == null || value == null) {
            return false;
        }
        //判断值是否可以转化为json
        JSONObject oldJson = parseJson(oldValue);
        JSONObject newJson = parseJson(value);
        //都是json字符串
        if (oldJson != null && newJson != null) {
            boolean compare1 = compareJson(oldJson, newJson);
            boolean compare2 = compareJson(newJson, oldJson);
            return compare1 && compare2;
        }
        //其中一个是json字符串
        if (oldJson != null || newJson != null) {
            return false;
        }
        //都不是json字符串 直接比较
        if (oldValue.equals(value)) {
            return true;
        }
        return false;
    }

    private JSONObject parseJson(Object jsonString) {
        JSONObject returnJson;
        try {
            returnJson = JSONObject.parseObject(jsonString.toString());
        } catch (Exception e) {
            return null;
        }
        return returnJson;
    }

    private boolean compareJson(JSONObject json1, JSONObject json2) {
        boolean isEqual = true;
        for (String key : json1.keySet()) {
            if (!json2.containsKey(key)) {
                isEqual = false;
            }
            //判断值 是否是json类型
            else if (json1.get(key) instanceof JSONObject) {
                if (json2.get(key) instanceof JSONObject) {
                    if (!compareJson(json1.getJSONObject(key), json2.getJSONObject(key))) {
                        return false;
                    } else if (!compareJson(json2.getJSONObject(key), json1.getJSONObject(key))) {
                        return false;
                    }
                } else {
                    isEqual = false;
                }
            }
            //判断值 是否是String类型
            else if (json1.get(key) instanceof String) {
                if (json2.get(key) instanceof String) {
                    isEqual = String.valueOf(json1.get(key)).equals(String.valueOf(json2.get(key)));
                } else {
                    isEqual = false;
                }
            }
            //其他类型值 转化为String类型 再进行比较
            else {
                isEqual = String.valueOf(json1.get(key)).equals(String.valueOf(json2.get(key)));
            }
            //如果发现不相同 则停止继续检测
            if (!isEqual) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateVOByTaskId(Map<String, Object> recordEntity) {
        recordMapper.updateVOByTaskId(recordEntity);
    }
}
