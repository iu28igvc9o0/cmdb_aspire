package com.aspire.ums.cmdb.v2.process.tenant.screen;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenBizSystemNotInspect;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenBizSystemService;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenService;
import com.aspire.ums.cmdb.v2.process.validate.DateValidator;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import com.aspire.ums.cmdb.v2.process.validate.FormateValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BizSystemNotInspectImportFactory
 * @Description 业务系统免考核资源详情导入工厂类
 * @Author luowenbo
 * @Date 2020/3/22 16:40
 * @Version 1.0
 */
public class BizSystemNotInspectImportFactory extends AbstractTenantScreenFactory {
    private ItCloudScreenBizSystemService bizSystemService;
    private ItCloudScreenService itCloudScreenService;

    @Override
    public void initSpringBean() {
        if (bizSystemService == null) {
            bizSystemService = SpringUtils.getBean(ItCloudScreenBizSystemService.class);
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
        String deviceType = super.getProcessParams().get("deviceType");
        ScreenBizSystemNotInspect bizSystemNotInspect = new ScreenBizSystemNotInspect();
        // 封装参数
        bizSystemNotInspect.setSystemTitleId(systemTitle);
        bizSystemNotInspect.setMonthlyTime(monthlyTime);
        bizSystemNotInspect.setDeviceType(deviceType);
        String columnValue;
        for (String key : dataMap.keySet()) {
            columnValue = dataMap.get(key);
            if (key.contains("必填")) {
                EmptyValidator.notEmpty(key, columnValue);
            }
            if("业务系统[必填]".equals(key)) {
                bizSystemNotInspect.setBizSystem(columnValue);
                continue;
            }
            if("二级部门".equals(key)) {
                bizSystemNotInspect.setDepartment2(columnValue);
                continue;
            }
            if("一级部门".equals(key)) {
                bizSystemNotInspect.setDepartment1(columnValue);
                continue;
            }
            if("资源池[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.idcTypeList);
                bizSystemNotInspect.setResourcePool(columnValue);
                continue;
            }
            if("POD池[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.podList);
                bizSystemNotInspect.setPod(columnValue);
                continue;
            }
            if("IP".equals(key)) {
                FormateValidator.isIpFormate(key,columnValue);
                bizSystemNotInspect.setIp(columnValue);
                continue;
            }
            if("资源开通日期".equals(key)) {
                DateValidator.validate(key,columnValue, new String[]{Constants.DATE_PATTERN_YYYY_MM_DD});
                bizSystemNotInspect.setResourceOpenDate(columnValue);
                continue;
            }
            if("开始考核日期".equals(key)) {
                DateValidator.validate(key,columnValue, new String[]{Constants.DATE_PATTERN_YYYY_MM_DD});
                bizSystemNotInspect.setStartAssessDate(columnValue);
                continue;
            }
        }
        try {
            // 先删除原先的数据，在进行新增
            ItCloudScreenRequest req = new ItCloudScreenRequest();
            // 封装删除方法的条件
            req.setDepartment1(bizSystemNotInspect.getDepartment1());
            req.setDepartment2(bizSystemNotInspect.getDepartment2());
            req.setMonthlyTime(bizSystemNotInspect.getMonthlyTime());
            req.setBizSystem(bizSystemNotInspect.getBizSystem());
            req.setResourcePool(bizSystemNotInspect.getResourcePool());
            req.setPod(bizSystemNotInspect.getPod());
            req.setDeviceType(bizSystemNotInspect.getDeviceType());
            req.setIp(bizSystemNotInspect.getIp());
            req.setTableType("screen_bizsystem_not_inspect");
            this.itCloudScreenService.deleteOldData(req);
            // 新增数据
            List<ScreenBizSystemNotInspect> list = new ArrayList<>();
            list.add(bizSystemNotInspect);
            this.bizSystemService.batchInsertBizSystemNotInpect(list);
        } catch (Exception e) {
            throw new RuntimeException("新增业务系统免考核资源信息:" + e.getMessage(), e);
        }
    }
}
