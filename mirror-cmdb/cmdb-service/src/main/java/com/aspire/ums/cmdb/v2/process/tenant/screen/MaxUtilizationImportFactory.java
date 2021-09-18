package com.aspire.ums.cmdb.v2.process.tenant.screen;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenCheckScore;
import com.aspire.ums.cmdb.index.payload.ScreenMaxUtilization;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenCheckScoreService;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenService;
import com.aspire.ums.cmdb.v2.index.util.ScreenDataUtil;
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
 * FileName: MaxUtilizationImportFactory
 * Author:   luowenbo
 * Date:     2020/02/26
 * Description: 利用率大屏——最大值利用率导入
 */
@NoArgsConstructor
public class MaxUtilizationImportFactory extends AbstractTenantScreenFactory {
    private ItCloudScreenService itCloudScreenService;
    private ItCloudScreenCheckScoreService itCloudScreenCheckScoreService;

    @Override
    public void initSpringBean() {
        if (itCloudScreenCheckScoreService == null) {
            itCloudScreenCheckScoreService = SpringUtils.getBean(ItCloudScreenCheckScoreService.class);
        }
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
        ScreenMaxUtilization maxUtilization = new ScreenMaxUtilization();
        // 封装实体对象
        maxUtilization.setSystemTitleId(systemTitle);
        maxUtilization.setMonthlyTime(monthlyTime);
        maxUtilization.setHardwareType(hardwareType);
        maxUtilization.setDeviceType(deviceType);
        List<ScreenMaxUtilization> paramList = new ArrayList<>();
        String columnValue;
        int day = SuppleUtil.getDayByMonthAndYear(monthlyTime);
        for (String key : dataMap.keySet()) {
            columnValue = dataMap.get(key).trim();
            if (key.contains("必填")) {
                EmptyValidator.notEmpty(key, columnValue);
            }
            if("业务系统[必填]".equals(key)) {
                maxUtilization.setBizSystem(columnValue);
                continue;
            }
            if("二级部门".equals(key)) {
                maxUtilization.setDepartment2(columnValue);
                continue;
            }
            if("一级部门".equals(key)) {
                maxUtilization.setDepartment1(columnValue);
                continue;
            }
            if("资源池[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.idcTypeList);
                maxUtilization.setResourcePool(columnValue);
                continue;
            }
            if("POD池[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.podList);
                maxUtilization.setPod(columnValue);
                continue;
            }
            int dateColumnValue = Integer.parseInt(key);
            if(dateColumnValue <= day) {
                // 当单元格为空的时候，默认填写0
                String value = "".equals(columnValue.trim()) ? "0" : columnValue;
                NumberValidator.validatePositiveLimitN(key,value,2,"0","100");
                maxUtilization.setMaxUtilization(value);
                String suffix = dateColumnValue > 9 ? dateColumnValue+"": "0" + dateColumnValue;
                maxUtilization.setStatistDate(monthlyTime + "-" + suffix);
                // 创建新对象保存
                ScreenMaxUtilization tmpObj = new ScreenMaxUtilization(maxUtilization);
                paramList.add(tmpObj);
            }
        }
        try {
            // 封装删除方法的条件
            ScreenMaxUtilization first = paramList.get(0);
            ItCloudScreenRequest req = new ItCloudScreenRequest();
            req.setDepartment1(first.getDepartment1());
            req.setDepartment2(first.getDepartment2());
            req.setMonthlyTime(first.getMonthlyTime());
            req.setDeviceType(first.getDeviceType());
            req.setBizSystem(first.getBizSystem());
            req.setResourcePool(first.getResourcePool());
            req.setPod(first.getPod());
            req.setHardwareType(first.getHardwareType());
            req.setTableType("screen_max_utilization");
            // 先删除原先的数据，在进行新增
            this.itCloudScreenService.deleteOldData(req);
            this.itCloudScreenService.insertBatchMaxUtilization(paramList);
        } catch (Exception e) {
            throw new RuntimeException("新增资源均峰值利用率信息:" + e.getMessage(), e);
        }
    }


    /**
     * 所有数据导入完成以后, 回调事件(覆盖)
     */
    public void afterImportHandler() {
        String monthlyTime = super.getProcessParams().get("monthlyTime");
        String hardwareType = super.getProcessParams().get("hardwareType");
        // 当导入的数据是CPU数据，进行一次总量计算，得出租户的cpu均峰值来计算扣分项（物理机和虚拟机做合并）
        if("CPU".equals(hardwareType)) {
            itCloudScreenService.updateCpuMaxList(monthlyTime);
        }
    }
}
