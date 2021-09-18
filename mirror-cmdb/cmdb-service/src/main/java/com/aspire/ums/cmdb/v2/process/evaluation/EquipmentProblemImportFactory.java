package com.aspire.ums.cmdb.v2.process.evaluation;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceTypeModel;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.assessment.entity.EquipmentProblem;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceTypeModelService;
import com.aspire.ums.cmdb.v2.assessment.service.IEquipmentProblemService;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.util.Arrays;
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
public class EquipmentProblemImportFactory extends ImportFactory {

    private IEquipmentProblemService equipmentProblemService;
    private ICmdbDeviceTypeModelService typeModelService;
    private List<Map<String, Object>> deviceTypeList;
    private List<Map<String, Object>> deviceProduceList;
    private List<Map<String, Object>> levelList;

    @Override
    public void initSpringBean() {
        if (equipmentProblemService == null) {
            equipmentProblemService = SpringUtils.getBean(IEquipmentProblemService.class);
        }
        if (typeModelService == null) {
            typeModelService = SpringUtils.getBean(ICmdbDeviceTypeModelService.class);
        }
    }

    @Override
    public void initBasic() {
        super.initBasic();
        deviceProduceList = super.getDictList("select  id `key`, name `value` from cmdb_device_produce");
        deviceTypeList = super.getDictList("select  id `key`, name `value` from cmdb_device_type where type = 'class'");
        levelList = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'event_level'");
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
    public void execute(Map<String, String> dataMap) {
        EquipmentProblem equipmentProblem = new EquipmentProblem();
        EmptyValidator.notEmpty("问题名称", dataMap.get("问题名称"));
        equipmentProblem.setProblemName(dataMap.get("问题名称"));
        EmptyValidator.notEmpty("级别", dataMap.get("级别"));
        DictValidator.validator("级别", dataMap.get("级别"), getLevelList());
        equipmentProblem.setProblemLevel(dataMap.get("级别"));
        EmptyValidator.notEmpty("发生地市", dataMap.get("发生地市"));
        equipmentProblem.setCity(dataMap.get("发生地市"));
        EmptyValidator.notEmpty("厂家", dataMap.get("厂家"));
        DictValidator.validator("厂家", dataMap.get("厂家"), getDeviceProduceList());
        equipmentProblem.setProduce(dataMap.get("厂家"));
        EmptyValidator.notEmpty("设备类型", dataMap.get("设备类型"));
        EmptyValidator.notEmpty("设备型号", dataMap.get("设备型号"));
        CmdbDeviceTypeModel typeModel = getTypeModelService().getDeviceTypeByModel(dataMap.get("设备型号"));
        if (null != typeModel) {
            String deviceType = typeModel.getDeviceType();
            DictValidator.validator("设备类型", deviceType, getDeviceTypeList());
            equipmentProblem.setDeviceName(dataMap.get("设备型号"));
            equipmentProblem.setDeviceType(deviceType);
        } else {
            String deviceType = dataMap.get("设备类型");
            DictValidator.validator("设备类型", deviceType, getDeviceTypeList());
            if (deviceType.contains("交换机")) {
                throw new RuntimeException("根据设备型号未查到设备类型");
            } else {
                equipmentProblem.setDeviceName(dataMap.get("设备型号"));
                equipmentProblem.setDeviceType(deviceType);
            }
        }
        equipmentProblem.setConfigType(dataMap.get("典配类型"));
        if (StringUtils.isNotEmpty(dataMap.get("发生时间"))) {
            try {
                Date date = DateUtils.parseDate(dataMap.get("发生时间"), new String[]{Constants.DATE_PATTERN_YYYY_MM_DD, Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM,
                        Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS, Constants.DATE_PATTERN_YYYY_MM_DD_01});
                equipmentProblem.setBeginDate(date);
            }  catch (Exception e) {
                throw new RuntimeException("日期" + dataMap.get("发生时间") + "输入有误, 日期格式不正确, 支持['yyyy-mm-dd','yyyy-mm-dd HH:mm', 'yyyy-mm-dd HH:mm:ss','yyyy/mm/dd']格式;");
            }
        }
        if (StringUtils.isNotEmpty(dataMap.get("解决时间"))) {
            try {
                Date date = DateUtils.parseDate(dataMap.get("解决时间"), new String[]{Constants.DATE_PATTERN_YYYY_MM_DD, Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM,
                        Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS, Constants.DATE_PATTERN_YYYY_MM_DD_01});
                equipmentProblem.setSolveDate(date);
            }  catch (Exception e) {
                throw new RuntimeException("日期" + dataMap.get("解决时间") + "输入有误, 日期格式不正确, 支持['yyyy-mm-dd','yyyy-mm-dd HH:mm', 'yyyy-mm-dd HH:mm:ss','yyyy/MM/dd']格式;");
            }
        }
        equipmentProblem.setProblemDemand(dataMap.get("问题/需求"));
        equipmentProblem.setProblemPerson(dataMap.get("问题提出人及电话"));
        if (StringUtils.isNotEmpty(dataMap.get("状态")))
            DictValidator.validator("状态", dataMap.get("状态"), Arrays.asList(new String[]{"OPEN", "CLOSED"}));
        equipmentProblem.setStatus(dataMap.get("状态"));
        equipmentProblem.setDetail(dataMap.get("问题报告word版"));
        equipmentProblem.setPerson(dataMap.get("create_username"));
        equipmentProblem.setPhone(dataMap.get("create_user_phone"));
        equipmentProblem.setQuarter(dataMap.get("quarter"));
        equipmentProblem.setProvince(dataMap.get("province"));
        equipmentProblem.setAssessStatus("2");
        try {
            getEquipmentProblemService().insertEquipmentProblem(equipmentProblem);
        } catch (Exception e) {
            throw new RuntimeException("导入失败" + e.getMessage());
        }
    }
}
