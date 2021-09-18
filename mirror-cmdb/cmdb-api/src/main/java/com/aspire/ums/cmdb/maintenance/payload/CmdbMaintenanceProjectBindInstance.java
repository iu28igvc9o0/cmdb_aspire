package com.aspire.ums.cmdb.maintenance.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-07-29 22:31:45
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbMaintenanceProjectBindInstance {

    /**
     * ID
     */
    private String id;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 设备序列号
     */
    private String deviceSn;
    /**
     * 设备ID
     */
    private String instanceId;
    /**
     * 是否删除
     */
    private String isDelete;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 资源池
     */
        private String idcType;
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
     * 设备型号
     */
    private String deviceModel;
}