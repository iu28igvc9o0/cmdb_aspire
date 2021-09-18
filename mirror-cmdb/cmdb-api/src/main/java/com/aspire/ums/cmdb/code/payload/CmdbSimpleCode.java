package com.aspire.ums.cmdb.code.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbSimpleCode
 * Author:   zhu.juwang
 * Date:     2020/3/12 15:03
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbSimpleCode {
    /**
     * ID
     */
    private String codeId;
    /**
     * 字段编码
     */
    private String filedCode;
    /**
     * 字段显示名称
     */
    private String filedName;
    /**
     * 控件类型
     */
    private String controlTypeId;
    /**
     * 归属模型
     */
    private String ownerModuleId;
    /**
     * 字段归属模型分组
     */
    private String moduleCatalogId;
}
