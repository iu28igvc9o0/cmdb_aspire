package com.aspire.ums.cmdb.v2.process.evaluation;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceRepairEvent;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceTypeModel;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceRepairEventService;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceTypeModelService;
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
public class RepairEventImportFactory extends ImportFactory {

    private ICmdbDeviceRepairEventService repairEventService;
    private ICmdbDeviceTypeModelService typeModelService;
    private List<Map<String, Object>> deviceTypeList;
    private List<Map<String, Object>> deviceProduceList;
    private List<Map<String, Object>> levelList;
    private List<Map<String, Object>> maintenOwnerList;

    @Override
    public void initBasic() {
        super.initBasic();
        deviceTypeList = super.getDictList("select  id `key`, name `value` from cmdb_device_type where type = 'class'");
        deviceProduceList = super.getDictList("select  id `key`, name `value` from cmdb_device_produce");
        levelList = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'event_level'");
        maintenOwnerList = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'mainten_owner'");
    }

    @Override
    public void formatErrorReason(Map<String, Object> errorLineData) {
        super.formatErrorReason(errorLineData);
        errorLineData.remove("create_username");
        errorLineData.remove("create_user_phone");
        errorLineData.remove("quarter");
        errorLineData.remove("province");
    }

    @Override
    public void initSpringBean() {
        if (repairEventService == null) {
            repairEventService = SpringUtils.getBean(ICmdbDeviceRepairEventService.class);
        }
        if (typeModelService == null) {
            typeModelService = SpringUtils.getBean(ICmdbDeviceTypeModelService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        List<CmdbDeviceRepairEvent> repairEvents = new ArrayList<>();
        CmdbDeviceRepairEvent repairEvent = new CmdbDeviceRepairEvent();
        EmptyValidator.notEmpty("事件名称", dataMap.get("事件名称"));
        repairEvent.setEventName(dataMap.get("事件名称"));
        EmptyValidator.notEmpty("级别", dataMap.get("级别"));
        DictValidator.validator("级别", dataMap.get("级别"), getLevelList());
        repairEvent.setEventLevel(dataMap.get("级别"));
        EmptyValidator.notEmpty("发生地市", dataMap.get("发生地市"));
        repairEvent.setCity(dataMap.get("发生地市"));
        EmptyValidator.notEmpty("厂家", dataMap.get("厂家"));
        DictValidator.validator("厂家", dataMap.get("厂家"), this.deviceProduceList);
        repairEvent.setProduce(dataMap.get("厂家"));
        EmptyValidator.notEmpty("设备类型", dataMap.get("设备类型"));
        EmptyValidator.notEmpty("设备型号", dataMap.get("设备型号"));
        CmdbDeviceTypeModel typeModel = getTypeModelService().getDeviceTypeByModel(dataMap.get("设备型号"));
        if (null != typeModel) {
            String deviceType = typeModel.getDeviceType();
            DictValidator.validator("设备类型", deviceType, this.deviceTypeList);
            repairEvent.setDeviceModel(dataMap.get("设备型号"));
            repairEvent.setDeviceType(deviceType);
        } else {
            String deviceType = dataMap.get("设备类型");
            DictValidator.validator("设备类型", deviceType, this.deviceTypeList);
            if (deviceType.contains("交换机")) {
                throw new RuntimeException("根据设备型号未查到设备类型");
            } else {
                repairEvent.setDeviceModel(dataMap.get("设备型号"));
                repairEvent.setDeviceType(deviceType);
            }
        }
        repairEvent.setDianpeiType(dataMap.get("典配类型"));
        if (StringUtils.isNotEmpty(dataMap.get("维保归属"))) {
            DictValidator.validator("维保归属", dataMap.get("维保归属"), this.maintenOwnerList);
        }
        repairEvent.setRepaireBelong(dataMap.get("维保归属"));
        repairEvent.setProblemPartType(dataMap.get("故障部件类型"));
        repairEvent.setCreateUsername(dataMap.get("create_username"));
        repairEvent.setCreateUserPhone(dataMap.get("create_user_phone"));
        repairEvent.setProvince(dataMap.get("province"));
        repairEvent.setQuarter(dataMap.get("quarter"));
        if (StringUtils.isNotEmpty(dataMap.get("故障开始时间"))) {
            try {
                Date date = DateUtils.parseDate(dataMap.get("故障开始时间"), new String[]{Constants.DATE_PATTERN_YYYY_MM_DD, Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM,
                        Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS, Constants.DATE_PATTERN_YYYY_MM_DD_01});
                repairEvent.setProblemStartTime(new Timestamp(date.getTime()));
            }  catch (Exception e) {
                throw new RuntimeException("日期" + dataMap.get("故障开始时间") + "输入有误, 日期格式不正确, 支持['yyyy-mm-dd','yyyy-mm-dd HH:mm', 'yyyy-mm-dd HH:mm:ss','yyyy/MM/dd']格式;");
            }
        }
        if (StringUtils.isNotEmpty(dataMap.get("故障消除时间"))) {
            try {
                Date date = DateUtils.parseDate(dataMap.get("故障消除时间"), new String[]{Constants.DATE_PATTERN_YYYY_MM_DD, Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM,
                        Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS, Constants.DATE_PATTERN_YYYY_MM_DD_01});
                repairEvent.setProblemEndTime(new Timestamp(date.getTime()));

            }  catch (Exception e) {
                throw new RuntimeException("日期" + dataMap.get("故障消除时间") + "输入有误, 日期格式不正确, 支持['yyyy-mm-dd','yyyy-mm-dd HH:mm', 'yyyy-mm-dd HH:mm:ss','yyyy/卖萌/dd']格式;");
            }
        }
        try {
            if (StringUtils.isNotEmpty(dataMap.get("故障处理次数"))) {
                if (Integer.parseInt(dataMap.get("故障处理次数")) < 0) {
                    throw new RuntimeException("列[故障处理次数] 非法输入");
                }
                repairEvent.setProblemHandleCount(Integer.parseInt(dataMap.get("故障处理次数")));
            }
        } catch (Exception e) {
            throw new RuntimeException("列[故障处理次数] 非法输入");
        }
        try {
            if (StringUtils.isNotEmpty(dataMap.get("备件部件更换次数"))) {
                if (Integer.parseInt(dataMap.get("备件部件更换次数")) < 0) {
                    throw new RuntimeException("列[备件部件更换次数] 非法输入");
                }
                repairEvent.setProblemPartChangeCount(Integer.parseInt(dataMap.get("备件部件更换次数")));
            }
        } catch (Exception e) {
            throw new RuntimeException("列[备件部件更换次数] 非法输入");
        }
        repairEvents.add(repairEvent);
        JSONObject saveMap = new JSONObject();
        saveMap.put("saveData", repairEvents);
        saveMap.put("deleteIds", new ArrayList<>());
        getRepairEventService().insert(saveMap);
    }
}
