package com.aspire.ums.cmdb.report.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aspire.ums.cmdb.report.mapper.Cmdb31ProvinceReportMapper;
import com.aspire.ums.cmdb.report.playload.*;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportService;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportSettingService;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportTableService;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportValueService;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* 描述：
* @author
* @date 2020-03-24 10:40:25
*/
@Service
@Slf4j
public class Cmdb31ProvinceReportServiceImpl implements ICmdb31ProvinceReportService {

    @Autowired
    private Cmdb31ProvinceReportMapper mapper;
    @Autowired
    private ICmdb31ProvinceReportValueService valueService;
    @Autowired
    private ICmdb31ProvinceReportSettingService settingService;
    @Autowired
    private ICmdb31ProvinceReportTableService reportTableService;
    @Autowired
    private SchemaService schemaService;

    final static List<String> EXP_STRING = Arrays.asList(new String[] {"(", ")", "+", "-", "*", "%", "/",","});

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<Cmdb31ProvinceReport> list() {
        return mapper.list();
    }

    @Override
    public List<Map<String, Object>> listOrderReportData(String submitMonth) {
        List<Map<String, Object>> list =  mapper.listOrderReportData(submitMonth);
        return mapper.listOrderReportData(submitMonth);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public Cmdb31ProvinceReport get(Cmdb31ProvinceReport entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(Cmdb31ProvinceReport entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(Cmdb31ProvinceReport entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(Cmdb31ProvinceReport entity) {
        mapper.delete(entity);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
    public void save(String userName, Map<String,Object> insertDataBox) {
        // 1.拆箱数据
        if (!insertDataBox.containsKey("provinceName")){
            throw new RuntimeException("未传参数[Params.provinceName]");
        }
        if (!insertDataBox.containsKey("resourceOwner")) {
            throw new RuntimeException("未传参数[Params.resourceOwner]");
        }
        if (!insertDataBox.containsKey("submitMonth")) {
            throw new RuntimeException("未传参数[Params.submitMonth]");
        }
        String provinceName = insertDataBox.get("provinceName").toString();
        String resourceOwner = insertDataBox.get("resourceOwner").toString();
        String submitMonth = insertDataBox.get("submitMonth").toString();
        Map<String, Object> insertVOListMap = (Map<String,Object>) insertDataBox.get("data");
        // 用来存在当前省份-月份-归属类下所有reportId,来和数据库里的id对比，当前不存在的则删除
        List<String> currReportIdList = new ArrayList<>();
        // 行数据
        List<Cmdb31ProvinceReport> currReportList = new ArrayList<>();
        // 列数据
        List<Cmdb31ProvinceReportValue> currReportValueList = new ArrayList<>();
        // 2.循环处理表
        for (String tableName: insertVOListMap.keySet()) {
            List<Cmdb31ProvinceInsertVO> insertVOS = JSONArray.parseArray(JSON.toJSONString(insertVOListMap.get(tableName)), Cmdb31ProvinceInsertVO.class);
            if (insertVOS.size() == 0) {
               continue;
            }
            // 获取此表的所有配置数据
            Cmdb31ProvinceTable table = reportTableService.getByName(tableName, null);
            // 获取此表唯一列
            List<Cmdb31ProvinceReportSetting> uniqueSettingList = settingService.listByTableUnique(table.getId());
            Map<String, List<String>> uniqueValues = new HashMap<>();
            Map<String, Cmdb31ProvinceReportSetting> uniqueSettingMap = new HashMap<>();
            if (uniqueSettingList.size() > 0) {
                for  (Cmdb31ProvinceReportSetting unique : uniqueSettingList) {
                    uniqueValues.put(unique.getId(), new ArrayList<>());
                    uniqueSettingMap.put(unique.getId(), unique);
                }
            }
            // 循环处理表行数据
            for (Cmdb31ProvinceInsertVO insertVO : insertVOS) {
//                Cmdb31ProvinceReport report = insertVO.getProvinceReport();
                Cmdb31ProvinceReport report = new Cmdb31ProvinceReport();
                report.setResourceOwner(resourceOwner);
                report.setSubmitMonth(submitMonth);
                report.setProvinceName(provinceName);
                // 2.2.1 如果行id不存在则补全数据
                if (StringUtils.isEmpty(report.getId())) {
                    report.setId(UUIDUtil.getUUID());
                    report.setSubmitTime(new Date());
                }
                report.setApproveStatus("临时保存");
                report.setTableId(table.getId());
                report.setUpdateTime(new Date());
                report.setUpdatePerson(userName);
                currReportIdList.add(report.getId());
                currReportList.add(report);
                // 处理列数据
                List<Cmdb31ProvinceInsertVO.Cmdb31ProvinceValue> valueList= insertVO.getValueList();
                for (Cmdb31ProvinceInsertVO.Cmdb31ProvinceValue value : valueList) {
                    List<String> uniqueValueList = uniqueValues.get(value.getSettingId());
                    if (uniqueValueList!= null) {
                        if (uniqueValueList.contains(value.getReportValue())) {
                            throw new RuntimeException("表 " + tableName + "存在相同 [" + uniqueSettingMap.get(value.getSettingId()).getResourceType() + "]:" + value.getReportValue());
                        }
                        uniqueValues.get(value.getSettingId()).add(value.getReportValue());
                    }
                    Cmdb31ProvinceReportValue reportValue = new Cmdb31ProvinceReportValue();
                    reportValue.setId(UUIDUtil.getUUID());
                    reportValue.setReportId(report.getId());
                    reportValue.setSettingId(value.getSettingId());
                    reportValue.setReportValue(value.getReportValue());
                    currReportValueList.add(reportValue);
                }
            }
        }
        // 删除界面删除数据
        if (currReportList.size() > 0) {
            valueService.deleteByCurrReportIds(currReportList.get(0), currReportIdList);
            mapper.deleteByCurrIds(currReportList.get(0), currReportIdList);
            // 批量保存
            mapper.insertByBatch(currReportList);
            valueService.insertByBatch(currReportValueList);
            triggerToCalcExp(currReportList.get(0), "as count");
            triggerToCalcExp(currReportList.get(0), "as total");
            triggerToCalcExp(currReportList.get(0), "provincename");
        } else {
            Cmdb31ProvinceReport report = new Cmdb31ProvinceReport();
            report.setProvinceName(provinceName);
            report.setSubmitMonth(submitMonth);
            report.setResourceOwner(resourceOwner);
            mapper.deleteByProvinceAndMonth(report);
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                // 先获取需要计算数据 // {reportid, province, idctype, settingid, calc_handler_class}
//                for (Map<String, Object> data : new ArrayList<>()) {
//                    AbstractValidFactory factory = new EmailValidHandler();
//                    double value = factory.calc(data);
//                    // 获取刀计算的值, 入库
//                    save();
//                }

//            }
//        }).start();
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
    public void saveByImport(String userName, Cmdb31ProvinceInsertReq insertReq) {
        // 1.取数据
        List<Cmdb31ProvinceTable> tableList = insertReq.getTableList();
        List<Cmdb31ProvinceReport> reportList = new ArrayList<>();
        List<Cmdb31ProvinceReportValue> reportValueList = new ArrayList<>();
        Cmdb31ProvinceReport provinceReport = new Cmdb31ProvinceReport();
        try {
            BeanUtils.copyProperties(provinceReport, insertReq);
        } catch (Exception e) {
            throw  new RuntimeException(e.getMessage());
        }
        for (Cmdb31ProvinceTable table : tableList) {
            // 获取此表唯一列
            List<Cmdb31ProvinceReportSetting> uniqueSettingList = settingService.listByTableUnique(table.getId());
            Map<String, List<String>> uniqueValues = new HashMap<>();
            Map<String, Cmdb31ProvinceReportSetting> uniqueSettingMap = new HashMap<>();
            if (uniqueSettingList.size() > 0) {
                for  (Cmdb31ProvinceReportSetting unique : uniqueSettingList) {
                    List<String> baseUniqueValues = valueService.listUniqueValues(provinceReport, unique.getId());
                    uniqueValues.put(unique.getId(), baseUniqueValues == null ? new ArrayList<>() : baseUniqueValues);
                    uniqueSettingMap.put(unique.getId(), unique);
                }
            }
            // 循环处理
            for (Cmdb31ProvinceReport report :  table.getReportList()) {
                if (StringUtils.isEmpty(report.getId())) {
                    report.setId(UUIDUtil.getUUID());
                    report.setSubmitTime(new Date());
                }
                report.setApproveStatus("临时保存");
                report.setUpdateTime(new Date());
                report.setUpdatePerson(userName);
                reportList.add(report);
                for (Cmdb31ProvinceReportValue value : report.getReportValueList()) {
                    List<String> uniqueValueList = uniqueValues.get(value.getSettingId());
                    if (uniqueValueList!= null) {
                        if (uniqueValueList.contains(value.getReportValue())) {
                            throw new RuntimeException("存在相同 [" + uniqueSettingMap.get(value.getSettingId()).getResourceType() + "]:" + value.getReportValue());
                        }
                        uniqueValues.get(value.getSettingId()).add(value.getReportValue());
                    }
                    value.setId(UUIDUtil.getUUID());
                    value.setReportId(report.getId());
                    reportValueList.add(value);
                }
            }
            if (reportList.size() > 0) {
                mapper.insertByBatch(reportList);
                valueService.insertByBatch(reportValueList);
            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    // 先获取需要计算数据 // {reportid, province, idctype, settingid, calc_handler_class}
//                for (Map<String, Object> data : new ArrayList<>()) {
//                    AbstractValidFactory factory = new EmailValidHandler();
//                    double value = factory.calc(data);
//                    // 获取刀计算的值, 入库
//                    save();
//                }
                    triggerToCalcExp(provinceReport, "as count");
                    triggerToCalcExp(provinceReport, "as total");
                    triggerToCalcExp(provinceReport, "provincename");

//                }
//            }).start();
        }
    }

    private void triggerToCalcExp(Cmdb31ProvinceReport provinceReport, String calcType) {
        //单表计算
        List<Cmdb31ProvinceTable> provinceTableList  = reportTableService.listByOwnerAndPage(provinceReport.getResourceOwner(), null);
        Cmdb31ProvinceReportReq reportReq = new Cmdb31ProvinceReportReq();
        try {
            BeanUtils.copyProperties(reportReq,provinceReport);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, List<Map<String, Object>>> settingData = getSettingData(reportReq);
        for (Cmdb31ProvinceTable table: provinceTableList) {
            List<Map<String, Object>> tableSettingData = settingData.get(table.getName());
            for (Cmdb31ProvinceReportSetting setting : table.getSettingList()) {
                if (StringUtils.isEmpty(setting.getCalcExp())) {
                    continue;
                }
                if (calcType.equals("as count") && setting.getCalcExp().contains("as count")) {
                    for (Map<String, Object> rowData : tableSettingData) {
                        Cmdb31ProvinceReportValue queryValue = new Cmdb31ProvinceReportValue();
                        Cmdb31ProvinceReportValue reportValue = new Cmdb31ProvinceReportValue();
                        queryValue.setReportId(rowData.get("id").toString());
                        queryValue.setSettingId(setting.getId());
                        queryValue = valueService.get(queryValue);
                        if (queryValue == null) {
                            reportValue.setId(UUIDUtil.getUUID());
                        } else {
                            reportValue.setId(queryValue.getId());
                        }
//                        Cmdb31ProvinceReportValue reportValue = new Cmdb31ProvinceReportValue();
//                        reportValue.setId(UUIDUtil.getUUID());
                        reportValue.setSettingId(setting.getId());
                        reportValue.setReportId(rowData.get("id").toString());
                        reportValue.setReportValue(calcReportValue(provinceReport, setting, rowData));
                        rowData.put(setting.getResourceType(), reportValue.getReportValue());
                        valueService.insert(reportValue);
                    }
                } else if (calcType.equals("as total") && setting.getCalcExp().contains("as total"))  {
                    Cmdb31ProvinceReport report = new Cmdb31ProvinceReport();
                    try {
                        BeanUtils.copyProperties(report, provinceReport);
                        report.setId("");
                        report.setTableId(table.getId());
                        Cmdb31ProvinceReport queryReport = mapper.get(report);
                        if (queryReport == null) {
                            report.setId(UUIDUtil.getUUID());
                            report.setSubmitTime(new Date());
                        } else {
                            report.setId(queryReport.getId());
                        }
                        report.setUpdateTime(new Date());
                        report.setApproveStatus("临时保存");
                        mapper.insert(report);
                        Cmdb31ProvinceReportValue reportValue = new Cmdb31ProvinceReportValue();
                        reportValue.setId(UUIDUtil.getUUID());
                        reportValue.setSettingId(setting.getId());
                        reportValue.setReportId(report.getId());
                        reportValue.setReportValue(calcTableTotal(setting, provinceReport));
                        valueService.insert(reportValue);
                    } catch (Exception e) {
                        throw new RuntimeException(e.toString());
                    }

                }  else if (calcType.equals("provincename") && setting.getCalcExp().contains("provincename"))  {
                    Cmdb31ProvinceReport report = new Cmdb31ProvinceReport();
                    try {

                        BeanUtils.copyProperties(report, provinceReport);
                        report.setId("");
                        report.setTableId(table.getId());
                        Cmdb31ProvinceReport queryReport = mapper.get(report);
                        if (queryReport == null) {
                            report.setId(UUIDUtil.getUUID());
                            report.setSubmitTime(new Date());
                        } else {
                            report.setId(queryReport.getId());
                        }
                        report.setUpdateTime(new Date());
                        report.setApproveStatus("临时保存");
                        mapper.insert(report);
                        Cmdb31ProvinceReportValue reportValue = new Cmdb31ProvinceReportValue();
                        reportValue.setId(UUIDUtil.getUUID());
                        reportValue.setSettingId(setting.getId());
                        reportValue.setReportId(report.getId());
                        reportValue.setReportValue(provinceReport.getProvinceName());
                        valueService.insert(reportValue);
                    } catch (Exception e) {
                        throw new RuntimeException(e.toString());
                    }
                }

            }
        }
    }

    private String calcTableTotal(Cmdb31ProvinceReportSetting setting, Cmdb31ProvinceReport provinceReport) {
        List<Cmdb31ProvinceReportSetting> allSettingList = settingService.listByOwner(provinceReport.getResourceOwner());
        Map<String, Object> totalCondition = new HashMap<>();
        totalCondition.put("resourceOwner", provinceReport.getResourceOwner());
        totalCondition.put("submitMonth", provinceReport.getSubmitMonth());
        totalCondition.put("provinceName", provinceReport.getProvinceName());
        totalCondition.put("settingList", allSettingList);
        if (setting.getCalcExp().contains("[month]")) {
            setting.setCalcExp(setting.getCalcExp().replace("[month]", provinceReport.getSubmitMonth()));
        }
        totalCondition.put("calcExp", setting.getCalcExp());
        List<Map<String, String>> result = mapper.executeToCountTotal(totalCondition);
        Object r = result.get(0).get("total");
        if (r == null) {
            return  "0";
        } else {
            return r.toString();
        }
    }

    @Override
    public Map<String, List<Map<String, Object>>> getSettingData(Cmdb31ProvinceReportReq req) {
        if (StringUtils.isEmpty(req.getResourceOwner())) {
            throw new RuntimeException("Query resourceOwner is require.");
        }
        Map<String, List<Map<String, Object>>> resultReport = new LinkedHashMap<>();
        List<Cmdb31ProvinceTable> tableList = reportTableService.listByOwnerAndPage(req.getResourceOwner(), req.getType());
        for (Cmdb31ProvinceTable table : tableList) {
            req.setResourceClass(table.getName());
            resultReport.put(table.getName(), mapper.getSettingData(req, table.getSettingList()));
        }
        return resultReport;
    }


    @Override
    public Map<String, String> submitApprove(Map<String, String> queryParams) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            if (!queryParams.containsKey("resourceOwner")) {
                throw new RuntimeException("Query params.resourceOwner is require.");
            }
            if (!queryParams.containsKey("provinceName")) {
                throw new RuntimeException("Params.provinceName is require.");
            }
            if (!queryParams.containsKey("submitMonth")) {
                throw new RuntimeException("Params.submitMonth is require.");
            }
            if (!queryParams.containsKey("approveStatus")) {
                throw new RuntimeException("Params.approveStatus is require.");
            }
            String approveStatus = queryParams.get("approveStatus");
            if (!approveStatus.equals("待审核") && !approveStatus.equals("审核驳回") && !approveStatus.equals("审核通过")
                    && !approveStatus.equals("临时保存")) {
                throw new RuntimeException("Not support approve result [" + approveStatus + "].");
            }
            Cmdb31ProvinceReport approveReport = new Cmdb31ProvinceReport();
            approveReport.setUpdateTime(new Date());
            approveReport.setApproveStatus(approveStatus);
            approveReport.setResourceOwner(queryParams.get("resourceOwner"));
            approveReport.setProvinceName(queryParams.get("provinceName"));
            approveReport.setSubmitMonth(queryParams.get("submitMonth"));
            mapper.updateApproveStatus(approveReport);
            returnMap.put("success", "true");
        } catch (Exception e) {
            returnMap.put("success", "false");
            returnMap.put("message", e.getMessage());
        }
        return returnMap;
    }

    @Override
    public Map<String, String> submitCheck(Map<String, String> queryParams) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            if (!queryParams.containsKey("resourceOwner")) {
                throw new RuntimeException("Params.resourceOwner is require.");
            }
            if (!queryParams.containsKey("provinceName")) {
                throw new RuntimeException("Params.provinceName is require.");
            }
            if (!queryParams.containsKey("submitMonth")) {
                throw new RuntimeException("Params.submitMonth is require.");
            }
            List<Map<String, String>> checkResult = mapper.submitCheck(queryParams);
            if (checkResult != null && checkResult.size() > 0) {
                StringBuilder errorBuilder = new StringBuilder();
                for (Map<String, String> result : checkResult) {
                    if (!errorBuilder.toString().equals("")) {
                        errorBuilder.append(",");
                    }
                    if (StringUtils.isNotEmpty(result.get("resource_type"))) {
                        errorBuilder.append(result.get("resource_type"));
                    }
                }
                if (!errorBuilder.toString().equals("")) {
                    errorBuilder.append("等指标数据尚未上报.");
                    returnMap.put("success", "false");
                    returnMap.put("message", errorBuilder.toString());
                    return returnMap;
                }
            }
            returnMap.put("success", "true");
        } catch (Exception e) {
            returnMap.put("success", "false");
            returnMap.put("message", e.getMessage());
        }
        return returnMap;
    }


    // 处理单行数据计算
    private String calcReportValue(Cmdb31ProvinceReport report, Cmdb31ProvinceReportSetting setting, Map<String, Object> rowData) {
        String calcExpSource = setting.getCalcExp();
        try {
            if (calcExpSource.contains("[")) {
                Pattern pattern = Pattern.compile("\\[.*?\\]");
                Matcher m = pattern.matcher(calcExpSource);
                while (m.find()) {
                    String oldExp = m.group();
                    String exp = oldExp.replace("[", "").replace("]", "");
                    String expValue = null;
                    if (exp.equals("month")) {
                        expValue = report.getSubmitMonth();
                        calcExpSource = calcExpSource.replace(oldExp, expValue);
                        break;
                    }
                    String settingId = settingService.getById(exp.trim()).getId();
                    expValue = rowData.get(settingId) == null ? "0" : rowData.get(settingId).toString();
                    calcExpSource = calcExpSource.replace(oldExp, expValue);
                }
                List<Map<String, Object>> result = schemaService.executeSql(calcExpSource);
                Object r = result.get(0).get("count");
                if (r == null) {
                    return "0";
                }
                return r.toString();
            }

        } catch (Exception e) {
            log.error("计算公式失败, 公式 -> {} error {}", calcExpSource, e.getStackTrace());
        }
        return null;
    }

    @Override
    public List<Map<String, String>> getProvinceStatus(Map<String, String> queryParam) {
        if (!queryParam.containsKey("resourceOwner")) {
            throw new RuntimeException("Params.resourceOwner is require.");
        }
        if (!queryParam.containsKey("submitMonth")) {
            throw new RuntimeException("Params.submitMonth is require.");
        }
        return mapper.getProvinceStatus(queryParam);
    }
}