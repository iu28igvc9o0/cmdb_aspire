package com.aspire.ums.cmdb.v2.process.report;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.report.playload.*;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportService;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportSettingService;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportTableService;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.cache.CacheConst;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: Cmdb31ProvinceReportImportFactory
 * Author:   hangfang
 * Date:     2020/4/09
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Slf4j
@NoArgsConstructor
public class Cmdb31ProvinceReportImportFactory extends ImportFactory {

    private ICmdb31ProvinceReportSettingService reportSettingService;
    private ICmdb31ProvinceReportService reportService;
    private ICmdb31ProvinceReportTableService reportTableService;
    private Map<String, Cmdb31ProvinceReportSetting> reportSettingMap;

    @Override
    public void initBasic() {
        super.initBasic();
    }

    private void calcValid(Map<String, Object> valueMap, List<Cmdb31ProvinceReportSetting> settingList) {
        for (Cmdb31ProvinceReportSetting setting : settingList) {
            if (!valueMap.containsKey(setting.getId())) {
                continue;
            }
            if (StringUtils.isNotEmpty(setting.getCalcValid())) {
                String calcValid = setting.getCalcValid();
                JSONObject jsonObject = JSONObject.parseObject(calcValid);
                String anotherId = jsonObject.getString("anotherId");
                // 如果不是和另一字段对比的验证则跳过
                if (StringUtils.isEmpty(anotherId)) {
                    continue;
                }
                String operator = jsonObject.getString("operator");
                String tip = jsonObject.getString("tip");
                Object curValue = StringUtils.isNotEmpty(valueMap.get(setting.getId())) ? valueMap.get(setting.getId()) : 0;
                Object anotherValue = StringUtils.isNotEmpty(valueMap.get(anotherId)) ? valueMap.get(anotherId) : 0;
                String validSql = "";
                if (setting.getResourceControlType().equals("datetime")) {
                    validSql = "select '" + curValue + "' " + operator + " '" + anotherValue + "' as flag";
                } else {
                    validSql = "select " + curValue + " " + operator + " " + anotherValue + " as flag";
                }
                if (super.getSchemaService() == null) {
                    super.setSchemaService(SpringUtils.getBean(SchemaService.class));
                }
                List<Map<String, Object>> result = super.getSchemaService().executeSql(validSql);
                if (result.size() > 0 && result.get(0).get("flag").toString().equals("0")) {
                    throw new RuntimeException(tip);
                }
            }
        }
    }

    private void validTable(Map<String, String> dataMap, List<Cmdb31ProvinceReportSetting> tableSettingList) {
        for (Cmdb31ProvinceReportSetting setting : tableSettingList) {
            if (!dataMap.containsKey(setting.getResourceType())) {
                throw new RuntimeException("列 ["+ setting.getResourceType() +"] 不存在，请检查模板是否正确");
            }
        }
    }

    private Object transformReportValue(Cmdb31ProvinceReportSetting setting, String reportValue) {
        if (StringUtils.isEmpty(reportValue)) {
            String controlType = setting.getResourceControlType();
            if ("int".equals(controlType) || "double".equals(controlType)) {
                return 0;
            }
            return reportValue;
        }
        try {
            Object resultValue = reportValue;
            String valueValid = setting.getValueValid();
            String controlType = setting.getResourceControlType();
            switch (controlType) {
                case "int" :
                    validValue(reportValue,valueValid);
                    resultValue = Integer.parseInt(reportValue);
                    break;
                case "double" :
                    validValue(reportValue,valueValid);
                    resultValue = String.format("%.2f",Double.parseDouble(reportValue));
                break;
                case "datetime" :
                    SimpleDateFormat sdf = new SimpleDateFormat(valueValid);
                    resultValue = sdf.format(DateUtils.parseDate(reportValue, new String[]{valueValid}));
                    break;
            }

            return resultValue;
        } catch (Exception e) {
            throw new RuntimeException("列[" + setting.getResourceType() + "] 非法输入");
        }
    }

    private void validValue(Object resultValue, String valueValid) {
        JSONObject jsonObject = JSONObject.parseObject(valueValid);
        if (jsonObject == null) {
            return;
        }
        Double curValue  =  Double.parseDouble(resultValue.toString());
        Double start = Double.parseDouble(jsonObject.get("start").toString());
        Double end = Double.parseDouble(jsonObject.get("end").toString());
        if ( curValue < start || curValue > end) {
            throw new RuntimeException(jsonObject.get("tip").toString());
        }
    }

    private void validParams(Map<String, String> processParams) {
        if (!processParams.containsKey("resourceOwner")) {
            throw new RuntimeException("there is no param 'resourceOwner'");
        }
        if (!processParams.containsKey("resourceClass")) {
            throw new RuntimeException("there is no param 'resourceClass'");
        }
        if (!processParams.containsKey("province")) {
            throw new RuntimeException("there is no param 'province'");
        }
        if (!processParams.containsKey("monthlyTime")) {
            throw new RuntimeException("there is no param 'monthlyTime'");
        }
        if (!processParams.containsKey("operatorUser")) {
            throw new RuntimeException("there is no param 'operatorUser'");
        }
    }


    @Override
    public void initSpringBean() {
        if (reportSettingService == null) {
            reportSettingService = SpringUtils.getBean(ICmdb31ProvinceReportSettingService.class);
        }
        if (reportService == null) {
            reportService = SpringUtils.getBean(ICmdb31ProvinceReportService.class);
        }
        if (reportTableService == null) {
            reportTableService = SpringUtils.getBean(ICmdb31ProvinceReportTableService.class);
        }
    }

    @Override
    public void valid() {
        super.valid();
        // 校验资源归属，省份信息等
        this.validParams(super.getProcessParams());
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        // 配置导入数据基本信息
        Cmdb31ProvinceInsertReq insertReq = new Cmdb31ProvinceInsertReq();
        insertReq.setProvinceName(super.getProcessParams().get("province"));
        insertReq.setResourceOwner(super.getProcessParams().get("resourceOwner"));
        insertReq.setSubmitMonth(super.getProcessParams().get("monthlyTime"));
        insertReq.setOperator(super.getProcessParams().get("operatorUser"));
//        insertReq.set
        //新增数据
        String tableName = super.getProcessParams().get("resourceClass");
        // 补全省份
        dataMap.put("省份名称", super.getProcessParams().get("province"));
        // 查表详情
        Cmdb31ProvinceTable table = getReportTableService().getByName(tableName, "update");
        // 检验表头
        validTable(dataMap, table.getSettingList());
        Cmdb31ProvinceReport report = new Cmdb31ProvinceReport();
        Map<String, Object> valueMap = new HashMap<>();
        report.setTableId(table.getId());
        report.setProvinceName(insertReq.getProvinceName());
        report.setUpdatePerson(insertReq.getOperator());
        report.setSubmitMonth(insertReq.getSubmitMonth());
        report.setResourceOwner(insertReq.getResourceOwner());
        List<Cmdb31ProvinceReportValue> reportValueList = new ArrayList<>();
        for (Cmdb31ProvinceReportSetting setting : table.getSettingList()) {
            Cmdb31ProvinceReportValue reportValue = new Cmdb31ProvinceReportValue();
            reportValue.setSettingId(setting.getId());
            Object value = transformReportValue(setting, dataMap.get(setting.getResourceType()));
            if (value != null) {
                reportValue.setReportValue(value.toString());
            }
            reportValueList.add(reportValue);
            valueMap.put(setting.getId(), value);
        }
        calcValid(valueMap, table.getSettingList());
        report.setReportValueList(reportValueList);
        List<Cmdb31ProvinceReport> reportList = new ArrayList<>();
        reportList.add(report);
        List<Cmdb31ProvinceTable> tableList = new ArrayList<>();
        table.setReportList(reportList);
        tableList.add(table);
        insertReq.setTableList(tableList);
        getReportService().saveByImport(insertReq.getOperator(), insertReq);
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
