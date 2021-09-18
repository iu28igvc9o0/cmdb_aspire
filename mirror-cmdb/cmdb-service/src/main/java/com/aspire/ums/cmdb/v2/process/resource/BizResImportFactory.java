package com.aspire.ums.cmdb.v2.process.resource;

import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.report.playload.BizResReport;
import com.aspire.ums.cmdb.report.service.IBizResReportService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DateValidator;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.v2.process
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/20 16:53
 * 版本: v1.0
 */
@Data
@Slf4j
@NoArgsConstructor
public class BizResImportFactory extends ImportFactory {
    
    private IBizResReportService reportService;
    private ConfigDictService configDictService;
    private ModuleService moduleService;
    private List<Map<String, Object>> department1s;
    private List<Map<String, Object>> department2s;
    private List<Map<String, Object>> bizSystemList;
    private List<Map<String, String>> poolList;

    @Override
    public void initBasic() {
        super.initBasic();
        department1s = super.getDictList("SELECT  id `id`, orgName `key`, orgName `value` FROM cmdb_org_system os WHERE parent_id='0' AND isdel='0'");
        Map<String, Object> queryParams = new HashMap<>();
        List<Map<String, Object>> businessList = moduleService.getModuleData(queryParams,null,"business");
        if (businessList != null && businessList.size() > 0) {
            businessList.forEach((data) -> {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("key", data.get("bizSystem"));
                dataMap.put("value", data.get("bizSystem"));
                bizSystemList.add(dataMap);
            });
        }
    }

    @Override
    public void formatErrorReason(Map<String, Object> errorLineData) {
        super.formatErrorReason(errorLineData);
        errorLineData.remove("importType");
    }

    private List<Map<String, Object>> getDepartment2List(String parentId) {
        if (department2s == null) {
            department2s = new ArrayList<>();
            String sql = "SELECT c.orgName `key` ,c.orgName `value` FROM cmdb_org_system c WHERE c.parent_id='" + parentId + "' AND isdel='0'";
            List<Map<String, Object>> list = getSchemaService().executeSql(sql);
            if (list != null && list.size() > 0) {
                department2s.addAll(list);
            }
        }
        return department2s;
    }

    private List<Map<String, String>> getPoolList() {
        poolList = new ArrayList<>();
        List<ConfigDict> dicts = getConfigDictService().selectDictsByType("idctype", null, null, null);
        dicts.forEach(dict -> {
            Map<String, String> dictMap = new HashMap<>();
            dictMap.put("key",dict.getName());
            dictMap.put("value", dict.getValue());
            poolList.add(dictMap);
        });
        return poolList;
    }

    @Override
    public void initSpringBean() {
        if (configDictService == null) {
            configDictService = SpringUtils.getBean(ConfigDictService.class);
        }
        if (reportService == null) {
            reportService = SpringUtils.getBean(IBizResReportService.class);
        }
        if (moduleService == null) {
            moduleService = SpringUtils.getBean(ModuleService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        BizResReport bizResReport = new BizResReport();
        EmptyValidator.notEmpty("业务系统名称", dataMap.get("业务系统名称"));
        DictValidator.validator("业务系统名称", dataMap.get("业务系统名称"), getBizSystemList());
        bizResReport.setBizSystem(dataMap.get("业务系统名称"));

        EmptyValidator.notEmpty("归属部门（一级）", dataMap.get("归属部门（一级）"));
        DictValidator.validator("归属部门（一级）", dataMap.get("归属部门（一级）"), this.department1s);
        bizResReport.setDepartment1(dataMap.get("归属部门（一级）"));

        if (!StringUtils.isEmpty(dataMap.get("归属部门（二级）"))) {
            String parentId = "";
            for (Map d : department1s) {
                if (d.get("value").equals(bizResReport.getDepartment1())) {
                    parentId = d.get("id").toString();
                }
            }
            DictValidator.validator("归属部门（二级）", dataMap.get("归属部门（二级）"), getDepartment2List(parentId) );
            bizResReport.setDepartment2(dataMap.get("归属部门（二级）"));
        }
        EmptyValidator.notEmpty("设备分类", dataMap.get("设备分类"));
        DictValidator.validator("设备分类", dataMap.get("设备分类"), Arrays.asList(new String[]{"X86服务器", "云主机"}));
        bizResReport.setDeviceClass(dataMap.get("设备分类"));
        EmptyValidator.notEmpty("设备类型", dataMap.get("设备类型"));
        bizResReport.setDeviceType(dataMap.get("设备类型"));
        EmptyValidator.notEmpty("所属资源池", dataMap.get("所属资源池"));
        DictValidator.validator("所属资源池", dataMap.get("所属资源池"), getPoolList());
        bizResReport.setIdcType(dataMap.get("所属资源池"));
        bizResReport.setPodName(dataMap.get("POD名称"));
        EmptyValidator.notEmpty("计划申请总量(台)", dataMap.get("计划申请总量(台)"));
        try {
            Integer.parseInt(dataMap.get("计划申请总量(台)"));
        } catch (Exception e) {
            throw new RuntimeException("列[计划申请总量(台)] 数量输入有误, 请输入整数");
        }
        bizResReport.setTotalPlannedApplication(dataMap.get("计划申请总量(台)"));
        EmptyValidator.notEmpty("已分配设备总量(台)", dataMap.get("已分配设备总量(台)"));
        try {
            Integer.parseInt(dataMap.get("已分配设备总量(台)"));
        } catch (Exception e) {
            throw new RuntimeException("列[已分配设备总量(台)] 数量输入有误, 请输入整数");
        }
        bizResReport.setTotalAllocatedEquipment(dataMap.get("已分配设备总量(台)"));
        try {
            Integer.parseInt(dataMap.get("交付周期（天）"));
        } catch (Exception e) {
            throw new RuntimeException("列[交付周期（天）] 数量输入有误, 请输入整数");
        }
        bizResReport.setDeliveryCycle(dataMap.get("交付周期（天）"));
        if ("bizresourcevm".equals(dataMap.get("importType"))) {
            try {
                Integer.parseInt(dataMap.get("虚拟核数vCPU（个）"));
            } catch (Exception e) {
                throw new RuntimeException("列[虚拟核数vCPU（个）] 数量输入有误, 请输入整数");
            }
            bizResReport.setVcpu(dataMap.get("虚拟核数vCPU（个）"));
        }
        if (StringUtils.isNotEmpty(dataMap.get("总内存（G）"))) {
            try {
                Double.parseDouble(dataMap.get("总内存（G）"));
            } catch (Exception e) {
                throw new RuntimeException("列[总内存（G）] 内存输入有误, 请用数据");
            }
            bizResReport.setTotalMemory(dataMap.get("总内存（G）"));
        }
        EmptyValidator.notEmpty("日期", dataMap.get("日期"));
        DateValidator.validate("日期", dataMap.get("日期"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        bizResReport.setCreateTime(dateFormat.format(DateValidator.validate("日期", dataMap.get("日期"))));
        this.reportService.insert(bizResReport);
    }
}
