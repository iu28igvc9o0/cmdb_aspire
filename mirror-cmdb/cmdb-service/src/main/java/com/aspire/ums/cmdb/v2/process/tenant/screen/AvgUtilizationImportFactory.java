package com.aspire.ums.cmdb.v2.process.tenant.screen;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenAvgUtilization;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenService;
import com.aspire.ums.cmdb.v2.index.util.SuppleUtil;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import com.aspire.ums.cmdb.v2.process.validate.NumberValidator;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * FileName: AvgUtilizationImportFactory
 * Author:   luowenbo
 * Date:     2020/02/26
 * Description: 利用率大屏——平均值利用率导入
 */
@NoArgsConstructor
public class AvgUtilizationImportFactory extends AbstractTenantScreenFactory {
    private ItCloudScreenService itCloudScreenService;

    @Override
    public void initSpringBean() {
        if (itCloudScreenService == null) {
            itCloudScreenService = SpringUtils.getBean(ItCloudScreenService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        // 获取导入传入的参数
        String systemTitle = super.getProcessParams().get("systemTitle");
        String monthlyTime = super.getProcessParams().get("monthlyTime");
        String hardwareType = super.getProcessParams().get("hardwareType");
        String deviceType = super.getProcessParams().get("deviceType");
        ScreenAvgUtilization avgUtilization = new ScreenAvgUtilization();
        // 封装实体对象
        avgUtilization.setSystemTitleId(systemTitle);
        avgUtilization.setMonthlyTime(monthlyTime);
        avgUtilization.setHardwareType(hardwareType);
        avgUtilization.setDeviceType(deviceType);
        List<ScreenAvgUtilization> paramList = new ArrayList<>();
        String columnValue;
        int day = SuppleUtil.getDayByMonthAndYear(monthlyTime);
        for (String key : dataMap.keySet()) {
            columnValue = dataMap.get(key).trim();
            if (key.contains("必填")) {
                EmptyValidator.notEmpty(key, columnValue);
            }
            if("业务系统[必填]".equals(key)) {
                avgUtilization.setBizSystem(columnValue);
                continue;
            }
            if("二级部门".equals(key)) {
                avgUtilization.setDepartment2(columnValue);
                continue;
            }
            if("一级部门".equals(key)) {
                avgUtilization.setDepartment1(columnValue);
                continue;
            }
            if("资源池[必填]".equals(key)) {
                DictValidator.validator(key,columnValue, this.idcTypeList);
                avgUtilization.setResourcePool(columnValue);
                continue;
            }
            if("POD池[必填]".equals(key)) {
                DictValidator.validator(key,columnValue, this.podList);
                avgUtilization.setPod(columnValue);
                continue;
            }
            int dateColumnValue = Integer.parseInt(key);
            if(dateColumnValue <= day) {
                // 当单元格为空的时候，默认填写0
                String value = "".equals(columnValue.trim()) ? "0" : columnValue;
                NumberValidator.validatePositiveLimitN(key,value,2,"0","100");
                avgUtilization.setAvgUtilization(value);
                String suffix = dateColumnValue > 9 ? dateColumnValue+"": "0" + dateColumnValue;
                avgUtilization.setStatistDate(monthlyTime + "-" + suffix);
                // 封装新对象
                ScreenAvgUtilization tmpObj = new ScreenAvgUtilization(avgUtilization);
                paramList.add(tmpObj);
            }
        }
        try {
            // 先删除原先的数据，在进行新增
            ScreenAvgUtilization first = paramList.get(0);
            ItCloudScreenRequest req = new ItCloudScreenRequest();
            // 封装删除方法的条件
            req.setDepartment1(first.getDepartment1());
            req.setDepartment2(first.getDepartment2());
            req.setMonthlyTime(first.getMonthlyTime());
            req.setBizSystem(first.getBizSystem());
            req.setResourcePool(first.getResourcePool());
            req.setPod(first.getPod());
            req.setDeviceType(first.getDeviceType());
            req.setHardwareType(first.getHardwareType());
            req.setTableType("screen_avg_utilization");
            this.itCloudScreenService.deleteOldData(req);
            this.itCloudScreenService.insertBatchAvgUtilization(paramList);
        } catch (Exception e) {
            throw new RuntimeException("新增资源平均值利用率信息:" + e.getMessage(), e);
        }
    }
}
