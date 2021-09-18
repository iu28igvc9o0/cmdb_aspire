package com.aspire.ums.cmdb.instance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbQueryInstance
 * Author:   zhu.juwang
 * Date:     2019/5/21 14:59
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbQueryInstance {
    /**
     * 页码
     */
    private Integer pageNumber;
    /**
     * 每页数量
     */
    private Integer pageSize;
    /**
     * 查询类型 普通主表查询(normal)/指定模型查询(module)
     */
    private String queryType;
    /**
     * 排序字段
     */
    private String sortColumn;
    /**
     * 排序类型 asc/desc
     */
    private String sortType;
    /**
     * 模型ID
     */
    private String moduleId;
    /**
     * 模型名称
     */
    private String moduleName;
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
     * IP地址
     */
    private String ip;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 所属资源池
     */
    private String idcType;
    /**
     * pod名称
     */
    private String pod;
    /**
     * 所属机房位置
     */
    private String room;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 所属型号
     */
    private String deviceModel;
    /**
     * 录入时间
     */
    private String insertTime;
    /**
     * 录入开始时间
     */
    private String startInsertTime;
    /**
     * 录入计算时间
     */
    private String endInsertTime;
    /**
     * 设备分类
     */
    private String deviceClass;
    /**
     * 设备三级分类
     */
    private String deviceClass3;
    /**
     * 设备序列号
     */
    private String deviceSN;
    /**
     * 所属项目
     */
    private String deviceProject;
    /**
     * 设备运行状态
     */
    private String deviceStatus;
    /**
     * 维保厂家
     */
    private String maintenanceFactory;
    /**
     * 品牌
     */
    private String mfrFactory;
    /**
     * 维护部门
     */
    private String deptOperation;
    /**
     * 维护人员
     */
    private String ops;
    /**
     * 是否变更记录
     */
    private String approval;
}
