package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName ScreenResourceAllocation
 * @Description 大屏——资源分配实体
 * @Author luowenbo
 * @Date 2020/2/26 21:03
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenResourceAllocation {
    private String id;
    /*
    *  大屏标题ID
    * */
    private String systemTitleId;
    /*
     *  一级部门
     * */
    private String department1;
    /*
     *  二级部门
     * */
    private String department2;
    /*
     *  业务系统
     * */
    private String bizSystem;
    /*
    *  资源池
    * */
    private String resourcePool;
    /*
     *  POD池
     * */
    private String pod;
    /*
     *  模块类型
     * */
    private String moduleType;
    /*
     *  设备类型
     * */
    private String deviceType;
    /*
     *  单位
     * */
    private String unit;
    /*
     *  数量
     * */
    private String amount;
    /*
     *  使用量
     * */
    private String useAllocation;
    /*
     *  免考核量
     * */
    private String notInspectCount;
    /*
     *  回收资源数量
     * */
    private String recycleCount;
    /*
     *  申请资源数量
     * */
    private String applyCount;
    /*
     *  未部署采集数量
     * */
    private String undeployPoint;
    /*
     *  内存大小(G)
     * */
    private String memorySize;
    /*
     *  CPU核数(个)
     * */
    private String cpuNum;
    /*
     *  月报时间 年-月
     * */
    private String monthlyTime;
    /*
     *  新增时间
     * */
    private Date insertTime;
    /*
    *  是否删除
    * */
    private String isDelete;

    public ScreenResourceAllocation(ScreenResourceAllocation obj) {
        this.id = obj.id;
        this.systemTitleId = obj.getSystemTitleId();
        this.department1 = obj.getDepartment1();
        this.department2 = obj.getDepartment2();
        this.bizSystem = obj.getBizSystem();
        this.resourcePool = obj.getResourcePool();
        this.pod = obj.getPod();
        this.moduleType = obj.getModuleType();
        this.deviceType = obj.getDeviceType();
        this.unit = obj.getUnit();
        this.amount = obj.getAmount();
        this.useAllocation = obj.getUseAllocation();
        this.recycleCount = obj.getRecycleCount();
        this.applyCount = obj.getApplyCount();
        this.monthlyTime = obj.getMonthlyTime();
        this.insertTime = obj.getInsertTime();
        this.isDelete = obj.getIsDelete();

        // GPU 和 存储资源没有未部署点属性
        if("GPU".equals(obj.getDeviceType()) || "存储资源".equals(obj.getModuleType())) {
            this.undeployPoint = "0";
            this.notInspectCount = "0";
        } else {
            this.undeployPoint = obj.getUndeployPoint();
            this.notInspectCount = obj.getNotInspectCount();
        }
        // 虚拟机独有属性
        if("虚拟机".equals(obj.getDeviceType())) {
            this.memorySize = obj.getMemorySize();
            this.cpuNum = obj.getCpuNum();
        } else {
            this.memorySize = "0";
            this.cpuNum = "0";
        }
    }
}
