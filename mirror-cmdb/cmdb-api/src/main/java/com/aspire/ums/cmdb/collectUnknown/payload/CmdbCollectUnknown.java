package com.aspire.ums.cmdb.collectUnknown.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/10/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbCollectUnknown {

    /**
     * ID
     */
    private String id;
    /**
     * 设备id（如已经维护回填数据）
     */
    private String instanceId;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 资源池
     */
    private String idcType;
    /**
     * 资源池名称
     */
    private String idcTypeName;

    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备分类
     */
    private String deviceClass;
    /**
     * 设备模型id
     */
    private String moduleId;
    /**
     * 设备类型名称
     */
    private String deviceTypeName;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 虚拟机名称
     */
    private String vmName;
    /**
     * 宿主机IP地址列
     */
    private String existIp;
    /**
     * 其他IP地址
     */
    private String otherIp;
    /**
     * 数据来源（监控上报/自动采集/苏研对接/自动发现）
     */
    private String dataFrom;

    /**
     * 处理状态 (0:待处理,1:已维护,2:已屏蔽)
     */
    private Integer handleStatus;

    /**
     * 提交人
     */
    private String commitUser;

    /**
     * 提交时间
     */
    private Date commitTime;

    /**
     * 处理人
     */
    private String handleUser;

    /**
     * 处理时间
     */
    private Date handleTime;

    /**
     * 备注
     */
    private String remark;
}
