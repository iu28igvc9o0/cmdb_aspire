package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import lombok.Data;

/**
 * 设备资产-存储
 *
 * @author jiangxuwen
 * @date 2020/5/13 11:08
 */
@Data
public class CmdbStorageInfoDTO implements Serializable {

    private static final long serialVersionUID = 6097989507658467776L;

    private String id;

    // 设备id
    private String deviceId;

    // 存储类型
    private String storageType;

    // 厂家
    private String factoryName;

    // 磁盘容量
    private String diskCapacity;

    // 挂载目录
    private String mountDir;

    // 磁盘使用率
    private String diskUtilization;
}
