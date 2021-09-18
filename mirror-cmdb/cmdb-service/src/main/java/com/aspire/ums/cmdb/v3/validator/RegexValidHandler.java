package com.aspire.ums.cmdb.v3.validator;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.util.ResultUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;

import java.util.Map;
import java.util.regex.Pattern;

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
public class RegexValidHandler extends AbstractValidFactory {
    @Override
    public Map<String, String> valid(CmdbCode cmdbCode, final CmdbV3CodeValidate codeValidate, Object value) {
        String regex = codeValidate.getValidTypeExpression();
        if (StringUtils.isNotEmpty(value)) {
            try {
                Pattern pattern = Pattern.compile(regex);
                pattern.pattern();
            } catch (Exception e) {
                return ResultUtils.parseMap(ResultUtils.ERROR, "无效的正则表达式[" + regex + "].");
            }
            if (!String.valueOf(value).matches(regex)) {
                return ResultUtils.parseMap(ResultUtils.ERROR, "数据格式不正确.");
            }
        }
        return ResultUtils.parseMap(ResultUtils.SUCCESS, ResultUtils.SUCCESS);
    }
}
