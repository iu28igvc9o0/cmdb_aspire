package com.aspire.ums.cmdb.v2.process.maintenance;

import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfo;
import com.aspire.ums.cmdb.maintenance.service.ICmdbComponentInfoService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import com.aspire.ums.cmdb.v2.process.validate.NumberValidator;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * FileName: MaintenComponentImportFactory
 * Author:   luowenbo
 * Date:     2020/02/12
 * Description: 维保部件导入工厂类
 */
@NoArgsConstructor
public class MaintenComponentImportFactory extends ImportFactory {
    private ICmdbComponentInfoService componentInfoService;

    @Override
    public void initSpringBean() {
        if (componentInfoService == null) {
            componentInfoService = SpringUtils.getBean(ICmdbComponentInfoService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        CmdbComponentInfo project = new CmdbComponentInfo();
        String columnValue;
        BigDecimal newMoney;
        for (String key : dataMap.keySet()) {
            columnValue = dataMap.get(key);
            if (key.contains("必填")) {
                EmptyValidator.notEmpty(key, columnValue);
            }
            if ("规格型号".equals(key)) {
                project.setSpecificModel(columnValue);
            }
            if ("配置描述".equals(key)) {
                project.setConfigDesc(columnValue);
            }
            if ("部件序列号".equals(key)) {
                project.setSerialNumber(columnValue);
            }
            if ("单位".equals(key)) {
                project.setUnit(columnValue);
            }
            if ("数量".equals(key)) {
                NumberValidator.validatePositiveInteger(key,columnValue);
                project.setQuantity(columnValue);
            }
            if ("不含税单价".equals(key)) {
                // 小数点只取6位
                int spotIndex = columnValue.indexOf(".");
                if(spotIndex != -1 && columnValue.substring(spotIndex+1).length() > 6) {
                    String tmpStr = columnValue.substring(0,spotIndex+7);
                    newMoney = new BigDecimal(tmpStr);
                } else {
                    newMoney = new BigDecimal(columnValue);
                }
                project.setUnitNotTax(newMoney);
            }
            if ("不含税价格".equals(key)) {
                // 小数点只取6位
                int spotIndex = columnValue.indexOf(".");
                if(spotIndex != -1 && columnValue.substring(spotIndex+1).length() > 6) {
                    String tmpStr = columnValue.substring(0,spotIndex+7);
                    newMoney = new BigDecimal(tmpStr);
                } else {
                    newMoney = new BigDecimal(columnValue);
                }
                project.setMoneyNotTax(newMoney);
            }
            if ("增值税率或征收率".equals(key)) {
                NumberValidator.validateWithScope(key,columnValue,1,100);
                project.setAddTax(columnValue);
            }
            if ("增值税税额".equals(key)) {
                // 小数点只取6位
                int spotIndex = columnValue.indexOf(".");
                if(spotIndex != -1 && columnValue.substring(spotIndex+1).length() > 6) {
                    String tmpStr = columnValue.substring(0,spotIndex+7);
                    newMoney = new BigDecimal(tmpStr);
                } else {
                    newMoney = new BigDecimal(columnValue);
                }
                project.setAddAmount(newMoney);
            }
            if ("含税价格".equals(key)) {
                // 小数点只取6位
                int spotIndex = columnValue.indexOf(".");
                if(spotIndex != -1 && columnValue.substring(spotIndex+1).length() > 6) {
                    String tmpStr = columnValue.substring(0,spotIndex+7);
                    newMoney = new BigDecimal(tmpStr);
                } else {
                    newMoney = new BigDecimal(columnValue);
                }
                project.setMoneyWithTax(newMoney);
            }
            try {
                this.componentInfoService.save(project);
            } catch (Exception e) {
                throw new RuntimeException("新增维保部件失败:" + e.getMessage(), e);
            }
        }
    }
}
