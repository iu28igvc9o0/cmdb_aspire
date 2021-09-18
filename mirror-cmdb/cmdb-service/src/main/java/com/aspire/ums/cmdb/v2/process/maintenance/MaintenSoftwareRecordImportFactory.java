package com.aspire.ums.cmdb.v2.process.maintenance;

import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecord;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecordQuery;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceSoftwareRecordService;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DateValidator;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/8/8
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
public class MaintenSoftwareRecordImportFactory extends ImportFactory {

    private ICmdbMaintenanceSoftwareRecordService softwareRecordService;
    private List<Map<String, Object>> softTypeList;

    @Override
    public void initBasic() {
        super.initBasic();
        softTypeList = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'server_level'");
    }

    @Override
    public void initSpringBean() {
        if (softwareRecordService == null) {
            softwareRecordService = SpringUtils.getBean(ICmdbMaintenanceSoftwareRecordService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        CmdbMaintenanceSoftwareRecord softwareRecord = new CmdbMaintenanceSoftwareRecord();
        for (String key : dataMap.keySet()) {
            if (key.contains("必填") && StringUtils.isEmpty(dataMap.get(key))) {
                throw new RuntimeException("列[" + key + "] 不能为空");
            }
            if ("数量[必填]".equals(key) || "实际人天".equals(key)) {
                try {
                    Integer.parseInt(dataMap.get(key));
                } catch (Exception e) {
                    throw new RuntimeException("数量输入有误, 请输入整数");
                }
            }
        }
        CmdbMaintenanceSoftwareRecordQuery recordQuery = new CmdbMaintenanceSoftwareRecordQuery();
        recordQuery.setProject(dataMap.get("项目[必填]"));
        recordQuery.setSoftwareName(dataMap.get("软件名称[必填]"));
        List<CmdbMaintenanceSoftwareRecord> records = this.softwareRecordService.listByEntity(recordQuery);
        if (records.size() == 0) {
            throw new RuntimeException("根据所填项目->" + dataMap.get("项目[必填]") +",软件名称:" + dataMap.get("软件名称[必填]") + "<-未查询到相应软件维保信息");
        }
        softwareRecord.setProject(records.get(0).getProject());
        softwareRecord.setSoftwareName(records.get(0).getSoftwareName());
        softwareRecord.setSoftwareId(records.get(0).getSoftwareId());
        softwareRecord.setClassify(records.get(0).getClassify());
        softwareRecord.setCompany(records.get(0).getCompany());
        softwareRecord.setServerStartTime(DateValidator.validate("开始时间[必填]", dataMap.get("开始时间[必填]")));
        softwareRecord.setServerEndTime(DateValidator.validate("结束时间[必填]", dataMap.get("结束时间[必填]")));
        if (softwareRecord.getServerEndTime().getTime() - softwareRecord.getServerStartTime().getTime() < 0) {
            throw new RuntimeException("结束时间要大于开始时间");
        }
        DictValidator.validator("服务级别[必填]", dataMap.get("服务级别[必填]"), this.softTypeList);
        softwareRecord.setServerLevel(dataMap.get("服务级别[必填]"));
        softwareRecord.setServerPerson(dataMap.get("服务人员[必填]"));
        softwareRecord.setDevopsApprover(dataMap.get("移动审批人"));
        softwareRecord.setYidongApprover(dataMap.get("运维审批人"));
        softwareRecord.setCreateUser(dataMap.get("创建人"));
        softwareRecord.setRealDays(dataMap.get("实际人天[必填]"));
        long day = DateUtils.getDatePoor2(softwareRecord.getServerEndTime(), softwareRecord.getServerStartTime()) + 1;
        softwareRecord.setHandleLong(day + "");
        List<CmdbMaintenanceSoftwareRecord> recordList = new ArrayList<>();
        recordList.add(softwareRecord);
        this.softwareRecordService.insert(recordList);
    }
}
