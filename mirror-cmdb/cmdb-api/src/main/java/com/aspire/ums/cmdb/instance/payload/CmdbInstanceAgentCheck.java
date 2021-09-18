package com.aspire.ums.cmdb.instance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CmdbInstanceAgentCheck
 * @Description CMDB的Agent部署设备检查
 * @Author luowenbo
 * @Date 2020/5/25 15:50
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbInstanceAgentCheck {

    /*
    *  ID
    * */
    private String id;
    /*
     *  IP
     * */
    private String ip;
    /*
     *  资源池
     * */
    private String idcType;
    /*
     *  设备类型
     * */
    private String deviceType;
    /*
     *  设备名称
     * */
    private String deviceName;
    /*
     *  同步状态
     * */
    private String syncStatus;
    /*
     *  扫描时间
     * */
    private String scanTime;
    /*
     *  CMDB更新时间
     * */
    private String cmdbUpdateTime;
    /*
     *  新增时间
     * */
    private String insertTime;
}
