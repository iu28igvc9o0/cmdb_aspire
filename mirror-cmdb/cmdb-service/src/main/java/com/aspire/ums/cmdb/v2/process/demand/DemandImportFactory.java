package com.aspire.ums.cmdb.v2.process.demand;

import com.aspire.ums.cmdb.demand.entity.CmdbDemandManager;
import com.aspire.ums.cmdb.demand.entity.CmdbDemandResourceType;
import com.aspire.ums.cmdb.demand.entity.CmdbDemandResourceTypeValue;
import com.aspire.ums.cmdb.demand.mapper.CmdbDemandResourceTypeMapper;
import com.aspire.ums.cmdb.demand.service.ICmdbDemandManagerService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.v2.process
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/12 10:10
 * 版本: v1.0
 */
@Data
@Slf4j
@NoArgsConstructor
public class DemandImportFactory extends ImportFactory {
    private ICmdbDemandManagerService demandManagerService;
    private CmdbDemandResourceTypeMapper typeMapper;
    private ModuleService moduleService;
    private List<CmdbDemandResourceType> resourceTypeList = null;
    private List<Map<String, Object>> whetherList;
    private List<Map<String, Object>> idcTypeList;

    private void valid(Map<String, String> dataMap) {
        EmptyValidator.notEmpty("部门[必填]", dataMap.get("部门[必填]"));
        EmptyValidator.notEmpty("需求接口人[必填]", dataMap.get("需求接口人[必填]"));
        EmptyValidator.notEmpty("需求接口人电话[必填]", dataMap.get("需求接口人电话[必填]"));
        EmptyValidator.notEmpty("应用系统[必填]", dataMap.get("应用系统[必填]"));
        EmptyValidator.notEmpty("资源需求提出时间[必填]", dataMap.get("资源需求提出时间[必填]"));
    }

    @Override
    public void initBasic() {
        super.initBasic();
        idcTypeList = super.getDictList("select idc_code `key`, idc_code `value` from cmdb_idc_manager order by sort_index");
        whetherList = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'whether'");
    }

    @Override
    public void initSpringBean() {
        if (demandManagerService == null) {
            demandManagerService = SpringUtils.getBean(ICmdbDemandManagerService.class);
        }
        if (typeMapper == null) {
            typeMapper = SpringUtils.getBean(CmdbDemandResourceTypeMapper.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        valid(dataMap);
        CmdbDemandManager demand = new CmdbDemandManager();
        demand.setDepartment(dataMap.get("部门[必填]"));
        demand.setTenant(dataMap.get("需求接口人[必填]"));
        demand.setTenantPhone(dataMap.get("需求接口人电话[必填]"));
        demand.setBizSystem(dataMap.get("应用系统[必填]"));
        DictValidator.validator("是否立项", dataMap.get("是否立项"), getWhetherList());
        demand.setIsProject(dataMap.get("是否立项"));
        DateValidator.validate("立项时间", dataMap.get("立项时间"));
        demand.setProjectTime(dataMap.get("立项时间"));
        DateValidator.validate("资源需求提出时间[必填]", dataMap.get("资源需求提出时间[必填]"));
        demand.setSubmitTime(dataMap.get("资源需求提出时间"));
        demand.setCycleTime(dataMap.get("需求满足周期"));
        DictValidator.validator("是否主机代维", dataMap.get("是否主机代维"), getWhetherList());
        demand.setIsHostMaintenance(dataMap.get("是否主机代维"));
        DictValidator.validator("是否有容灾高可用", dataMap.get("是否有容灾高可用"), getWhetherList());
        demand.setIsDisaster(dataMap.get("是否有容灾高可用"));
        DictValidator.validator("容灾类型", dataMap.get("容灾类型"), Arrays.asList(new String[]{"双活", "异地灾备"}));
        demand.setDisasterType(dataMap.get("容灾类型"));
        demand.setWlanRequirement(dataMap.get("宽度要求"));
        DateValidator.validate("资源预期投产时间", dataMap.get("资源预期投产时间"));
        demand.setCommissionTime(dataMap.get("资源预期投产时间"));
        DictValidator.validator("部署资源池要求", dataMap.get("部署资源池要求"), Arrays.asList(new String[]{"无", "有"}));
        demand.setIsIdcRequirement(dataMap.get("部署资源池要求"));
        if (dataMap.get("部署资源池要求").equals("有")) {
            EmptyValidator.notEmpty("资源池要求", dataMap.get("资源池要求"));
            DictValidator.validator("资源池要求", dataMap.get("资源池要求"), getIdcTypeList());
        }
        demand.setIdcRequirement(dataMap.get("资源池要求"));
        List<CmdbDemandResourceTypeValue> typeValueList = new ArrayList<>();
        if (resourceTypeList == null) {
            throw new RuntimeException("初始化资源类型失败, resourceTypeList为空");
        }
        for (CmdbDemandResourceType resourceType : resourceTypeList) {
            String resourceCode = resourceType.getResourceType();
            if (dataMap.containsKey(resourceCode) && dataMap.get(resourceCode) != null) {
                CmdbDemandResourceTypeValue typeValue = new CmdbDemandResourceTypeValue();
                typeValue.setResourceTypeId(resourceType.getResourceTypeId());
                typeValue.setResourceCount(dataMap.get(resourceCode));
                if (dataMap.containsKey(resourceCode + "使用场景描述") && StringUtils.isNotEmpty(dataMap.get(resourceCode + "使用场景描述"))) {
                    typeValue.setResourceScene(dataMap.get(resourceCode + "使用场景描述"));
                }
                if (dataMap.containsKey(resourceCode + "资源用途") && StringUtils.isNotEmpty(dataMap.get(resourceCode + "资源用途"))) {
                    typeValue.setResourceUse(dataMap.get(resourceCode + "资源用途"));
                }
                typeValueList.add(typeValue);
            }
        }
        if (typeValueList.size() > 0) {
            demandManagerService.insertExcelData(demand,typeValueList);
        }
    }
}
