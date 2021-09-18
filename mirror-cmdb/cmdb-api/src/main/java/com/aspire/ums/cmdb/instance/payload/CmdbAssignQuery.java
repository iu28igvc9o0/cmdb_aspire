package com.aspire.ums.cmdb.instance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class CmdbAssignQuery {

    /**
     * 资源池
     */
    private String resourcePool;
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
}
