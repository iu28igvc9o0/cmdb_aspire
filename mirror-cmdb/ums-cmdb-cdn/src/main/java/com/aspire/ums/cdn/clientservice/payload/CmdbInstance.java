package com.aspire.ums.cdn.clientservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 *
 * @author
 * @date 2019-05-20 20:56:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbInstance {
    /**
     * ID
     */
    private String id;
    /**
     * 模型ID
     */
    private String moduleId;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 模型分区ID
     */
    private String modulePartitionId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备一级分类
     */
    private String deviceClass;
    /**
     * 设备二级分类
     */
    private String deviceType;
    /**
     * 设备三级分类
     */
    private String deviceClass3;
    /**
     * 设备厂商
     */
    private String deviceMfrs;
    /**
     * 运行状态
     */
    private String deviceStatus;
    /**
     * 设备系统类型
     */
    private String deviceOsType;
    /**
     * os详细版本
     */
    private String osDetailVersion;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 是否已经分配
     */
    private String isAssigned;
    /**
     * 资源池名称
     */
    private String idcType;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * POD名称
     */
    private String podName;
    /**
     * 机房名称
     */
    private String roomId;
    /**
     * 机柜名称
     */
    private String idcCabinet;
    /**
     * 一级部门
     */
    private String department1;
    /**
     * 二级部门
     */
    private String department2;
    /**
     * 业务系统
     */
    private String bizSystem;
    /**
     * 是否ansible管理
     */
    private String isAnsible;
    /**
     * 是否资源池管理设备
     */
    private String isPool;
    /**
     * 维护部门
     */
    private String deptOperation;
    /**
     * 维护人员
     */
    private String ops;
    /**
     * 是否删除
     */
    private String isDelete;
    /**
     * 录入人
     */
    private String insertPerson;
    /**
     * 录入时间
     */
    private String insertTime;
    /**
     * 最后更新人
     */
    private String updatePerson;
    /**
     * 最后更新时间
     */
    private String updateTime;
    /**
     * 主机名称
     */
    private String hostName;
    /**
     * 设备序列号
     */
    private String deviceSn;
    /**
     * 主机描述
     */
    private String deviceDescription;
    /**
     * 是否重要设备
     */
    private String isImportant;
}