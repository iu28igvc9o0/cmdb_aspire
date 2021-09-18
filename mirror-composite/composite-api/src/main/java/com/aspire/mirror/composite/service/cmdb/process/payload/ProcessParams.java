package com.aspire.mirror.composite.service.cmdb.process.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ProcessParams
 * Author:   zhu.juwang
 * Date:     2019/8/26 17:38
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessParams {
    /**
     * 导入类型
     */
    private String importType;
    /**
     * 内容行索引
     */
    private Integer dataStart;

    /******后评估相关导入参数***********************************************/
    /**
     * 创建用户名称
     */
    private String createUsername;
    /**
     * 创建用户电话
     */
    private String createUserPhone;

    /******维保管理相关导入参数***********************************************/
    /**
     * 维保名称
     */
    private String maintenanceProjectName;

    /**
     * 设备id
     */
    private String instanceId;
    /**
     * 模型ID
     */
    private String moduleId;

    /**
    *  分支机构
    * */
    private String province;

    /**
     *  资源归属类型
     * */
    private String resourceOwner;
    /**
     *  资源上报表名
     * */
    private String resourceClass;

    /**
    *  季度
    * */
    private String quarter;

    /******维保部件清单导入参数***********************************************/
    /**
    *  维保对象ID
    * */
    private String maintenanceId;

    /******维保部件清单导入参数***********************************************/
    /**
    *  月报时间 格式 年-月
    * */
    private String monthlyTime;
    /**
     *  系统标题
     * */
    private String systemTitle;
    /**
     *  资源池
     * */
    private String resourcePool;
    /**
     *  硬件类型 内存|CPU
     * */
    private String hardwareType;
    /*
    *  设备类型 物理机|虚拟机
    * */
    private String deviceType;
    /**
     * 模型类型
     */
    private String moduleType;
    /**
     * 操作用户
     */
    private String operatorUser;
}
