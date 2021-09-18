package com.aspire.ums.cmdb.instance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbAssign
 * Author:   hangfang
 * Date:     2019/11/13
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbAssign {

    /**
     * ID
     */
    private String id;
    /**
     * 资源池
     */
    private String resourcePool;
    /**
     * 类型
     */
    private String type;
    /**
     * 一级租户
     */
    private String department1;
    /**
     * 二级租户
     */
    private String department2;
    /**
     * 业务系统
     */
    private String bizSystem;
    /**
     * 租户资源需求
     */
    private Integer departNeedCount;
    /**
     * 已建设数据
     */
    private Integer builtCount;
    /**
     * 已分配数量
     */
    private Integer assignedCount;
    /**
     * 预分配数量
     */
    private Integer preAssignCount;
    /**
     * 未建设数量
     */
    private Integer unBuiltCount;
    /**
     * 创建时间
     */
    private Date createTime;

}
