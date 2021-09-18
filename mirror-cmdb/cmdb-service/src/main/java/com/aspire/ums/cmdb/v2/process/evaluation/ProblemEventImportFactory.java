package com.aspire.ums.cmdb.v2.process.evaluation;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceTypeModel;
import com.aspire.ums.cmdb.assessment.payload.CmdbProblemEvent;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceTypeModelService;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbProblemEventService;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/7/18
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Slf4j
@NoArgsConstructor
public class ProblemEventImportFactory extends ImportFactory {

    private ICmdbProblemEventService problemEventService;
    private ICmdbDeviceTypeModelService typeModelService;
    private List<Map<String, Object>> deviceTypeList;
    private List<Map<String, Object>> deviceProduceList;
    private List<Map<String, Object>> levelList;
    private List<Map<String, Object>> maintenOwnerList;
    private SchemaService schemaService;

    private List<Map<String, Object>> getLevelList() {
        if (levelList == null) {
            levelList = new ArrayList<>();
            String sql = "select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'event_level'";
            List<Map<String, Object>> list = getSchemaService().executeSql(sql);
            if (list != null && list.size() > 0) {
                levelList.addAll(list);
            }
        }
        return levelList;
    }

    private List<Map<String, Object>> getMaintenOwnerList() {
        if (maintenOwnerList == null) {
            maintenOwnerList = new ArrayList<>();
            String sql = "select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'mainten_owner'";
            List<Map<String, Object>> list = getSchemaService().executeSql(sql);
            if (list != null && list.size() > 0) {
                maintenOwnerList.addAll(list);
            }
        }
        return maintenOwnerList;
    }


    private List<Map<String, Object>> getDeviceTypeList() {
        if (deviceTypeList == null) {
            deviceTypeList = new ArrayList<>();
            String sql = "select  id `key`, name `value` from cmdb_device_type where type = 'class'";
            List<Map<String, Object>> list = getSchemaService().executeSql(sql);
            if (list != null && list.size() > 0) {
                deviceTypeList.addAll(list);
            }
        }
        return deviceTypeList;
    }
    private List<Map<String, Object>> getProduceList() {
        deviceProduceList = new ArrayList<>();
        String sql = "select  id `key`, name `value` from cmdb_device_produce";
        List<Map<String, Object>> list = getSchemaService().executeSql(sql);
        if (list != null && list.size() > 0) {
            deviceProduceList.addAll(list);
        }
        return deviceProduceList;
    }

    @Override
    public void initSpringBean() {
        if (problemEventService == null) {
            problemEventService = SpringUtils.getBean(ICmdbProblemEventService.class);
        }
        if (schemaService == null) {
            schemaService = SpringUtils.getBean(SchemaService.class);
        }
        if (typeModelService == null) {
            typeModelService = SpringUtils.getBean(ICmdbDeviceTypeModelService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        List<CmdbProblemEvent> problemEvents = new ArrayList<>();
        CmdbProblemEvent problemEvent = new CmdbProblemEvent();
        EmptyValidator.notEmpty("故障名称", dataMap.get("故障名称"));
        problemEvent.setProblemName(dataMap.get("故障名称"));
        EmptyValidator.notEmpty("故障等级", dataMap.get("故障等级"));
        DictValidator.validator("故障等级", dataMap.get("故障等级"), getLevelList());
        problemEvent.setProblemLevel(dataMap.get("故障等级"));
        EmptyValidator.notEmpty("发生地市", dataMap.get("发生地市"));
        problemEvent.setCity(dataMap.get("发生地市"));
        EmptyValidator.notEmpty("厂家", dataMap.get("厂家"));
        DictValidator.validator("厂家", dataMap.get("厂家"), getProduceList());
        problemEvent.setProduce(dataMap.get("厂家"));
        EmptyValidator.notEmpty("设备类型", dataMap.get("设备类型"));
        EmptyValidator.notEmpty("设备型号", dataMap.get("设备型号"));
        CmdbDeviceTypeModel typeModel = getTypeModelService().getDeviceTypeByModel(dataMap.get("设备型号"));
        if (null != typeModel) {
            String deviceType = typeModel.getDeviceType();
            DictValidator.validator("设备类型", deviceType, getDeviceTypeList());
            problemEvent.setDeviceModel(dataMap.get("设备型号"));
            problemEvent.setDeviceType(deviceType);
        } else {
            String deviceType = dataMap.get("设备类型");
            DictValidator.validator("设备类型", deviceType, getDeviceTypeList());
            if (deviceType.contains("交换机")) {
                throw new RuntimeException("根据设备型号未查到设备类型");
            } else {
                problemEvent.setDeviceModel(dataMap.get("设备型号"));
                problemEvent.setDeviceType(deviceType);
            }
        }
        problemEvent.setTypicalType(dataMap.get("典配类型"));
        if (StringUtils.isNotEmpty(dataMap.get("维保归属"))) {
            DictValidator.validator("维保归属", dataMap.get("维保归属"), getMaintenOwnerList());
        }
        problemEvent.setRepaireBelong(dataMap.get("维保归属"));
        problemEvent.setProblemPartType(dataMap.get("故障部件类型"));
        if (StringUtils.isNotEmpty(dataMap.get("故障开始时间"))) {
            try {
                Date date = DateUtils.parseDate(dataMap.get("故障开始时间"), new String[]{Constants.DATE_PATTERN_YYYY_MM_DD, Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM,
                        Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS, Constants.DATE_PATTERN_YYYY_MM_DD_01});
                problemEvent.setProblemStartTime(new Timestamp(date.getTime()));
            }  catch (Exception e) {
                throw new RuntimeException("日期" + dataMap.get("故障开始时间") + "输入有误, 日期格式不正确, 支持['yyyy-mm-dd','yyyy-mm-dd HH:mm', 'yyyy-mm-dd HH:mm:ss','yyyy/MM/dd']格式;");
            }
        }
        if (StringUtils.isNotEmpty(dataMap.get("业务恢复时间"))) {
            try {
                Date date = DateUtils.parseDate(dataMap.get("业务恢复时间"), new String[]{Constants.DATE_PATTERN_YYYY_MM_DD, Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM,
                        Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS, Constants.DATE_PATTERN_YYYY_MM_DD_01});
                problemEvent.setBizRegainTime(new Timestamp(date.getTime()));
            }  catch (Exception e) {
                throw new RuntimeException("日期" + dataMap.get("业务恢复时间") + "输入有误, 日期格式不正确, 支持['yyyy-mm-dd','yyyy-mm-dd HH:mm', 'yyyy-mm-dd HH:mm:ss','yyyy/卖萌/dd']格式;");
            }
        }
        if (StringUtils.isNotEmpty(dataMap.get("故障消除时间"))) {
            try {
                Date date = DateUtils.parseDate(dataMap.get("故障消除时间"), new String[]{Constants.DATE_PATTERN_YYYY_MM_DD, Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM,
                        Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS, Constants.DATE_PATTERN_YYYY_MM_DD_01});
                problemEvent.setProblemRemoveTime(new Timestamp(date.getTime()));
            }  catch (Exception e) {
                throw new RuntimeException("日期" + dataMap.get("故障消除时间") + "输入有误, 日期格式不正确, 支持['yyyy-mm-dd','yyyy-mm-dd HH:mm', 'yyyy-mm-dd HH:mm:ss','yyyy/卖萌/dd']格式;");
            }
        }
        try {
            if (StringUtils.isNotEmpty(dataMap.get("故障处理次数"))) {
                if (Integer.parseInt(dataMap.get("故障处理次数")) < 0) {
                    throw new RuntimeException("列[故障处理次数] 非法输入");
                }
                problemEvent.setProblemHandleCount(dataMap.get("故障处理次数"));
            }
        } catch (Exception e) {
            throw new RuntimeException("列[故障处理次数] 非法输入");
        }
        try {
            if (StringUtils.isNotEmpty(dataMap.get("备件部件更换次数"))) {
                if (Integer.parseInt(dataMap.get("备件部件更换次数")) < 0) {
                    throw new RuntimeException("列[备件部件更换次数] 非法输入");
                }
                problemEvent.setProblemPartChangeCount(dataMap.get("备件部件更换次数"));
            }
        } catch (Exception e) {
            throw new RuntimeException("列[备件部件更换次数] 非法输入");
        }
        problemEvent.setReportDesc(dataMap.get("故障报告及相关证明材料"));
        problemEvent.setCreateUsername(dataMap.get("create_username"));
        problemEvent.setCreateUserPhone(dataMap.get("create_user_phone"));
        problemEvent.setProvince(dataMap.get("province"));
        problemEvent.setQuarter(dataMap.get("quarter"));
        problemEvents.add(problemEvent);
        JSONObject saveMap = new JSONObject();
        saveMap.put("saveData", problemEvents);
        saveMap.put("deleteIds", new ArrayList<>());
        Map<String, Object> result = getProblemEventService().insertOrUpdate(saveMap);
        if (!(boolean)result.get("success")) {
            throw new RuntimeException("导入失败" + result.get("message"));
        }
    }

    @Override
    public void formatErrorReason(Map<String, Object> errorLineData) {
        super.formatErrorReason(errorLineData);
        errorLineData.remove("create_username");
        errorLineData.remove("create_user_phone");
        errorLineData.remove("quarter");
        errorLineData.remove("province");
    }
}
