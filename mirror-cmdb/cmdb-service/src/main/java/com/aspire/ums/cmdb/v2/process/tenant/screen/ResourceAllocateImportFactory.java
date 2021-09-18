package com.aspire.ums.cmdb.v2.process.tenant.screen;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenResourceAllocation;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenService;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import com.aspire.ums.cmdb.v2.process.validate.NumberValidator;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * FileName: ResourceAllocateImportFactory
 * Author:   luowenbo
 * Date:     2020/02/26
 * Description: 利用率大屏——资源总量分配导入
 */
@NoArgsConstructor
public class ResourceAllocateImportFactory extends AbstractTenantScreenFactory {
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
        ScreenResourceAllocation resourceAllocation = new ScreenResourceAllocation();
        // 封装实体对象
        resourceAllocation.setSystemTitleId(systemTitle);
        resourceAllocation.setMonthlyTime(monthlyTime);
        List<ScreenResourceAllocation> paramList = new ArrayList<>();
        String columnValue;
        for (String key : dataMap.keySet()) {
            columnValue = dataMap.get(key).trim();
            if (key.contains("必填")) {
                EmptyValidator.notEmpty(key, columnValue);
            }
            if("业务系统[必填]".equals(key)) {
                resourceAllocation.setBizSystem(columnValue);
                continue;
            }
            if("二级部门".equals(key)) {
                resourceAllocation.setDepartment2(columnValue);
                continue;
            }
            if("一级部门".equals(key)) {
                resourceAllocation.setDepartment1(columnValue);
                continue;
            }
            if("资源池[必填]".equals(key)) {
                DictValidator.validator(key,columnValue,this.idcTypeList);
                resourceAllocation.setResourcePool(columnValue);
                continue;
            }
            if("POD池[必填]".equals(key)) {
                DictValidator.validator(key,columnValue, this.podList);
                resourceAllocation.setPod(columnValue);
                continue;
            }
            // 转化的单元格值
            String tranColumnValue = "".equals(columnValue.trim())?"0":columnValue;
            // ### 使用量 ###
            if("GPU使用量(台)".equals(key) || "物理机使用量(台)".equals(key) || "虚拟机使用量(台)".equals(key)) {
                NumberValidator.validatePositiveInteger(key,tranColumnValue);
                resourceAllocation.setUseAllocation(tranColumnValue);
                continue;
            }
            // ### 免考核量 ###
            if("物理机免考核量(台)".equals(key) || "虚拟机免考核量(台)".equals(key) || "GPU免考核量(台)".equals(key)) {
                NumberValidator.validatePositiveInteger(key,tranColumnValue);
                resourceAllocation.setNotInspectCount(tranColumnValue);
                continue;
            }
            if("FC-SAN使用量(T)".equals(key) || "IP-SAN使用量(T)".equals(key)|| "块存储使用量(T)".equals(key)
                    || "文件存储使用量(T)".equals(key) || "对象存储使用量(T)".equals(key) || "备份存储使用量(T)".equals(key) ) {
                NumberValidator.validatePositive(key,tranColumnValue);
                resourceAllocation.setUseAllocation(tranColumnValue);
                continue;
            }
            // ### 本月申请资源 ###
            if("GPU本月申请资源(台)".equals(key) || "物理机本月申请资源(台)".equals(key) || "虚拟机本月申请资源(台)".equals(key)) {
                NumberValidator.validatePositiveInteger(key,tranColumnValue);
                resourceAllocation.setApplyCount(tranColumnValue);
                continue;
            }
            if("FC-SAN本月申请资源(T)".equals(key) || "IP-SAN本月申请资源(T)".equals(key)|| "块存储本月申请资源(T)".equals(key)
                    || "文件存储本月申请资源(T)".equals(key) || "对象存储本月申请资源(T)".equals(key) || "备份存储本月申请资源(T)".equals(key) ) {
                NumberValidator.validatePositive(key,tranColumnValue);
                resourceAllocation.setApplyCount(tranColumnValue);
                continue;
            }
            // ### 本月回收资源 ###
            if("GPU本月回收资源(台)".equals(key) || "物理机本月回收资源(台)".equals(key) || "虚拟机本月回收资源(台)".equals(key)) {
                NumberValidator.validatePositiveInteger(key,tranColumnValue);
                resourceAllocation.setRecycleCount(tranColumnValue);
                // 添加到对象列表
                ScreenResourceAllocation tmpObj = new ScreenResourceAllocation(resourceAllocation);
                paramList.add(tmpObj);
                continue;
            }
            if("FC-SAN本月回收资源(T)".equals(key) || "IP-SAN本月回收资源(T)".equals(key)
                    || "块存储本月回收资源(T)".equals(key) || "文件存储本月回收资源(T)".equals(key) || "对象存储本月回收资源(T)".equals(key)
                    || "备份存储本月回收资源(T)".equals(key)) {
                NumberValidator.validatePositive(key,tranColumnValue);
                resourceAllocation.setRecycleCount(tranColumnValue);
                // 添加到对象列表
                ScreenResourceAllocation tmpObj = new ScreenResourceAllocation(resourceAllocation);
                paramList.add(tmpObj);
                continue;
            }
            // 虚拟机的特殊属性（内存和CPU核数）
            if("虚拟机回收内存(G)".equals(key)) {
                NumberValidator.validatePositive(key,tranColumnValue);
                resourceAllocation.setMemorySize(tranColumnValue);
                continue;
            }
            if("虚拟机回收CPU核数(个)".equals(key)) {
                NumberValidator.validatePositiveInteger(key,tranColumnValue);
                resourceAllocation.setCpuNum(tranColumnValue);
                continue;
            }
            if("物理机未部署采集点数量".equals(key) || "虚拟机未部署采集点数量".equals(key)) {
                NumberValidator.validatePositiveInteger(key,tranColumnValue);
                resourceAllocation.setUndeployPoint(tranColumnValue);
                continue;
            }
            if("物理机已分配总量(台)".equals(key)) {
                NumberValidator.validatePositiveInteger(key,columnValue);
                resourceAllocation.setModuleType("计算资源");
                resourceAllocation.setAmount(tranColumnValue);
                resourceAllocation.setUnit("台");
                resourceAllocation.setDeviceType("物理机");
                continue;
            }
            if("虚拟机已分配总量(台)".equals(key)) {
                NumberValidator.validatePositiveInteger(key,columnValue);
                resourceAllocation.setModuleType("计算资源");
                resourceAllocation.setAmount(tranColumnValue);
                resourceAllocation.setUnit("台");
                resourceAllocation.setDeviceType("虚拟机");
                continue;
            }
            if("GPU已分配总量(台)".equals(key)) {
                NumberValidator.validatePositiveInteger(key,columnValue);
                resourceAllocation.setModuleType("计算资源");
                resourceAllocation.setAmount(tranColumnValue);
                resourceAllocation.setUnit("台");
                resourceAllocation.setDeviceType("GPU");
                continue;
            }
            if("FC-SAN已分配总量(T)".equals(key)) {
                NumberValidator.validatePositive(key,columnValue);
                resourceAllocation.setModuleType("存储资源");
                resourceAllocation.setAmount(tranColumnValue);
                resourceAllocation.setUnit("T");
                resourceAllocation.setDeviceType("FC-SAN");
                continue;
            }
            if("IP-SAN已分配总量(T)".equals(key)) {
                NumberValidator.validatePositive(key,columnValue);
                resourceAllocation.setModuleType("存储资源");
                resourceAllocation.setAmount(tranColumnValue);
                resourceAllocation.setUnit("T");
                resourceAllocation.setDeviceType("IP-SAN");
                continue;
            }
            if("块存储已分配总量(T)".equals(key)) {
                NumberValidator.validatePositive(key,columnValue);
                resourceAllocation.setModuleType("存储资源");
                resourceAllocation.setAmount(tranColumnValue);
                resourceAllocation.setUnit("T");
                resourceAllocation.setDeviceType("块存储");
                continue;
            }
            if("文件存储已分配总量(T)".equals(key)) {
                NumberValidator.validatePositive(key,columnValue);
                resourceAllocation.setModuleType("存储资源");
                resourceAllocation.setAmount(tranColumnValue);
                resourceAllocation.setUnit("T");
                resourceAllocation.setDeviceType("文件存储");
                continue;
            }
            if("对象存储已分配总量(T)".equals(key)) {
                NumberValidator.validatePositive(key,columnValue);
                resourceAllocation.setModuleType("存储资源");
                resourceAllocation.setAmount(tranColumnValue);
                resourceAllocation.setUnit("T");
                resourceAllocation.setDeviceType("对象存储");
                continue;
            }
            if("备份存储已分配总量（T）".equals(key)) {
                NumberValidator.validatePositive(key,columnValue);
                resourceAllocation.setModuleType("存储资源");
                resourceAllocation.setAmount(tranColumnValue);
                resourceAllocation.setUnit("T");
                resourceAllocation.setDeviceType("备份存储");
                continue;
            }
        }
        try {
            // 先删除原先的数据，在进行新增
            ScreenResourceAllocation first = paramList.get(0);
            ItCloudScreenRequest req = new ItCloudScreenRequest();
            // 封装删除方法的条件
            req.setDepartment1(first.getDepartment1());
            req.setDepartment2(first.getDepartment2());
            req.setMonthlyTime(first.getMonthlyTime());
            req.setBizSystem(first.getBizSystem());
            req.setResourcePool(first.getResourcePool());
            req.setPod(first.getPod());
            req.setTableType("screen_resource_allocation");
            this.itCloudScreenService.deleteOldData(req);
            this.itCloudScreenService.insertBatchResourceAllocate(paramList);
        } catch (Exception e) {
            throw new RuntimeException("新增资源分配信息:" + e.getMessage(), e);
        }
    }
}
