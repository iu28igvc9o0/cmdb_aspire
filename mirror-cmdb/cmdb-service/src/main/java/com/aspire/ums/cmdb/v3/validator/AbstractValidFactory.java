package com.aspire.ums.cmdb.v3.validator;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: AbstractValidFactory
 * Author:   zhu.juwang
 * Date:     2020/3/19 15:19
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public abstract class AbstractValidFactory {
    /**
     * 验证方法
     * @param cmdbCode 码表对象
     * @param codeValidate 验证对象
     * @param value 验证值
     * @return
     */
    public abstract Map<String, String> valid(final CmdbCode cmdbCode, final CmdbV3CodeValidate codeValidate, final Object value);

}
