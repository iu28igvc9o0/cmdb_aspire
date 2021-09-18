package com.aspire.ums.cmdb.v3.validator;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.util.ResultUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: EmailValidHandler
 * Author:   zhu.juwang
 * Date:     2020/3/19 17:29
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class PhoneValidHandler extends AbstractValidFactory {
    @Override
    public Map<String, String> valid(CmdbCode cmdbCode, final CmdbV3CodeValidate codeValidate, Object value) {
        String regex = "((0\\d{2,3}-\\d{7,8})|(1[123456789]\\d{9}))";
        if (StringUtils.isNotEmpty(value) && !String.valueOf(value).matches(regex)) {
            return ResultUtils.parseMap(ResultUtils.ERROR, "电话格式不正确. 正确格式如:010-12345678 或 13888888888");
        }
        return ResultUtils.parseMap(ResultUtils.SUCCESS, ResultUtils.SUCCESS);
    }
}
