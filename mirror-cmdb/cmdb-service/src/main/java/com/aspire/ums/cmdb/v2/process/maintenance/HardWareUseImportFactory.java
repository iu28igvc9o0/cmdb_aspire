package com.aspire.ums.cmdb.v2.process.maintenance;

import com.aspire.ums.cmdb.maintenance.payload.HardwareUse;
import com.aspire.ums.cmdb.maintenance.service.IHardWareService;
import com.aspire.ums.cmdb.maintenance.service.IHardWareUseService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 类名称: HardWareImportFactory
 * 类描述: 硬件维保使用导入 (新增数据)
 * 创建人: PJX
 * 创建时间: 2019/8/1 16:17
 * 版本: v1.0
 */
@Slf4j
@NoArgsConstructor
public class HardWareUseImportFactory extends ImportFactory {

    private IHardWareUseService hardWareUseService;
    private IHardWareService hardWareService;

    public int getHourSpace(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24) + 1;
        return Integer.parseInt(String.valueOf(between_days)) * 24;
    }

    @Override
    public void initSpringBean() {
        if (hardWareUseService == null) {
            hardWareUseService = SpringUtils.getBean(IHardWareUseService.class);
        }
        if (hardWareService == null) {
            hardWareService = SpringUtils.getBean(IHardWareService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        HardwareUse hardwareUse = new HardwareUse();
        String deviceSerialNumber = dataMap.get("设备序列号[必填]");
        if (StringUtils.isEmpty(deviceSerialNumber)) {
            throw new RuntimeException("列[设备序列号]必填");
        }
        String warrantyDate = dataMap.get("出保时间[必填]");
        if (StringUtils.isEmpty(warrantyDate)) {
            throw new RuntimeException("列[出保时间]必填");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            hardwareUse.setWarrantyDate(sdf.parse(warrantyDate));
        } catch (Exception e) {
            throw new RuntimeException("列[出保时间]日期格式不正确格式：年-月-日");
        }

        String serverPerson = dataMap.get("服务人员[必填]");
        if (StringUtils.isNotEmpty(serverPerson))
            hardwareUse.setServerPerson(serverPerson);

        String serverLevel = dataMap.get("服务级别[必填]");
        if (StringUtils.isEmpty(serverLevel)) {
            throw new RuntimeException("列[服务级别]必填");
        } else {
            if (!"一级".equals(serverLevel) &&
                    !"二级".equals(serverLevel)&&
                    !"三级".equals(serverLevel)&&
                    !"四级".equals(serverLevel)) {
                throw new RuntimeException("列[服务级别] 不在可选范围");
            } else {
                hardwareUse.setServerLevel(serverLevel);
            }
        }

        String startTime = dataMap.get("开始时间[必填]");
        if (StringUtils.isEmpty(startTime)) {
            throw new RuntimeException("列[开始时间]必填");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            hardwareUse.setStartDate(sdf.parse(startTime));
        } catch (Exception e) {
            throw new RuntimeException("列[开始时间]日期格式不正确格式：年-月-日");
        }

        String endTime = dataMap.get("结束时间[必填]");
        if (StringUtils.isEmpty(endTime)) {
            throw new RuntimeException("列[开始时间]必填");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            hardwareUse.setEndDate(sdf.parse(endTime));
        } catch (Exception e) {
            throw new RuntimeException("列[结束时间]日期格式不正确格式：年-月-日");
        }

        int hours = getHourSpace(hardwareUse.getStartDate(), hardwareUse.getEndDate());
        hardwareUse.setProcessTime(String.valueOf(hours));

        String actualManDay = dataMap.get("实际人天[必填]");

        if (StringUtils.isNotEmpty(actualManDay))
            hardwareUse.setActualManDay(actualManDay);
        else
            throw new RuntimeException("列[实际人天]必填");

        String mobileApprover = dataMap.get("移动审批人");
        if (StringUtils.isNotEmpty(mobileApprover))
            hardwareUse.setMobileApprover(mobileApprover);

        String operateApprover = dataMap.get("运维审批人");
        if (StringUtils.isNotEmpty(operateApprover))
            hardwareUse.setOperateApprover(operateApprover);

        String creater = dataMap.get("创建人");
        if (StringUtils.isNotEmpty(creater))
            hardwareUse.setCreater(creater);

        // 根据设备序列号和出保时间查询项目(以及相关属性)是否存在
        Map<String,Object> tmpMp = hardWareService.queryIsExist(deviceSerialNumber,warrantyDate);
        String projectInstanceId = tmpMp.get("message").toString();
        if (StringUtils.isNotEmpty(projectInstanceId)) {
            hardwareUse.setProjectInstanceId(projectInstanceId);
            String hardWareId = hardWareService.queryIdByProjectInstanceId(projectInstanceId);
            if (StringUtils.isNotEmpty(hardWareId)) {
                hardwareUse.setHardWareId(hardWareId);
                hardWareUseService.addHardwareUse(hardwareUse);
            } else {
                throw new RuntimeException("根据[设备序列号:" + deviceSerialNumber + ", 出保时间:" + warrantyDate + "]查询不到对应的硬件维保ID");
            }
        } else {
            throw new RuntimeException("根据[设备序列号:" + deviceSerialNumber + ", 出保时间:" + warrantyDate + "]查询不到对应的项目");
        }
    }
}
