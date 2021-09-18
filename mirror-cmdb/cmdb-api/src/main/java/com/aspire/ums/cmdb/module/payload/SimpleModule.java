package com.aspire.ums.cmdb.module.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: SimpleModule
 * Author:   zhu.juwang
 * Date:     2020/3/17 18:34
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SimpleModule {
    /**
     * 模型ID
     */
    private String id;
    /**
     * 模型名称
     */
    private String name;
    /**
     * 模型编码
     */
    private String code;
    /**
     * 模型编码
     */
    private String catalogId;

    /**
     * 是否需要审核
     */
    private Integer enableApprove;
    /**
     * 模型编码
     */
    private Integer isVice;
}
