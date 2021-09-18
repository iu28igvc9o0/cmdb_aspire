package com.aspire.ums.cmdb.v2.process.tenant.screen;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenStoreLowUltRequest;
import com.aspire.ums.cmdb.index.payload.ScreenBizSystemNotInspect;
import com.aspire.ums.cmdb.index.payload.ScreenStoreLowUtilization;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenBizSystemService;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenService;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenStoreLowUtilizationService;
import com.aspire.ums.cmdb.v2.process.validate.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName StoreRsLowUtilizationImportFactory
 * @Description 存储资源低利用率导入工厂类
 * @Author luowenbo
 * @Date 2020/4/20 18:16
 * @Version 1.0
 */
@NoArgsConstructor
public class StoreRsLowUtilizationImportFactory extends AbstractTenantScreenFactory {
    private ItCloudScreenStoreLowUtilizationService screenStoreLowUtilizationService;
    private List<Map<String, Object>> storeTypes;

    // 覆盖
    public void initBasic() {
        super.initBasic();
        storeTypes = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'screen_store_type'");
    }

    @Override
    public void initSpringBean() {
        if (screenStoreLowUtilizationService == null) {
            screenStoreLowUtilizationService = SpringUtils.getBean(ItCloudScreenStoreLowUtilizationService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        // 获取导入传入的参数
        String systemTitle = super.getProcessParams().get("systemTitle");
        String monthlyTime = super.getProcessParams().get("monthlyTime");
        ScreenStoreLowUtilization screenStoreLowUtilization = new ScreenStoreLowUtilization();
        // 封装参数
        screenStoreLowUtilization.setSystemTitleId(systemTitle);
        screenStoreLowUtilization.setMonthlyTime(monthlyTime);
        String columnValue;
        for (String key : dataMap.keySet()) {
            columnValue = dataMap.get(key).trim();
            if (key.contains("必填")) {
                EmptyValidator.notEmpty(key, columnValue);
            }
            if("业务系统[必填]".equals(key)) {
                screenStoreLowUtilization.setBizSystem(columnValue);
                continue;
            }
            if("二级部门".equals(key)) {
                screenStoreLowUtilization.setDepartment2(columnValue);
                continue;
            }
            if("一级部门".equals(key)) {
                screenStoreLowUtilization.setDepartment1(columnValue);
                continue;
            }
            if("资源池[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.idcTypeList);
                screenStoreLowUtilization.setResourcePool(columnValue);
                continue;
            }
            if("POD池[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.podList);
                screenStoreLowUtilization.setPod(columnValue);
                continue;
            }
            if ("存储类型".equals(key)) {
                DictValidator.validator(key, columnValue, this.storeTypes);
                screenStoreLowUtilization.setStoreType(columnValue);
                continue;
            }
            if ("存储资源利用率".equals(key)) {
                // 当单元格为空的时候，默认填写0
                String value = "".equals(columnValue.trim()) ? "0" : columnValue;
                NumberValidator.validatePositiveLimitN(key,value,2,"0","100");
                screenStoreLowUtilization.setUtilization(value);
                continue;
            }
        }
        try {
            // 先删除原先的数据，在进行新增
            ItCloudScreenStoreLowUltRequest req = new ItCloudScreenStoreLowUltRequest();
            // 封装删除方法的条件
            req.setDepartment1(screenStoreLowUtilization.getDepartment1());
            req.setDepartment2(screenStoreLowUtilization.getDepartment2());
            req.setMonthlyTime(screenStoreLowUtilization.getMonthlyTime());
            req.setBizSystem(screenStoreLowUtilization.getBizSystem());
            req.setResourcePool(screenStoreLowUtilization.getResourcePool());
            req.setPod(screenStoreLowUtilization.getPod());
            req.setStoreType(screenStoreLowUtilization.getStoreType());
            this.screenStoreLowUtilizationService.delete(req);
            // 新增数据
            this.screenStoreLowUtilizationService.insert(screenStoreLowUtilization);
        } catch (Exception e) {
            throw new RuntimeException("新增存储资源低利用率信息:" + e.getMessage(), e);
        }
    }
}
