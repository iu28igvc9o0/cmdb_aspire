package com.aspire.ums.cmdb.v3.validator;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.util.ResultUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: SegmentValidHandler
 * Author:   zhu.juwang
 * Date:     2020/6/10 19:46
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class SegmentValidHandler extends AbstractValidFactory {
    @Override
    public Map<String, String> valid(CmdbCode cmdbCode, CmdbV3CodeValidate codeValidate, Object value) {
        String regex = "^(?=(\\b|\\D))(((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))(?=(\\b|\\D))/([1-2][0-9]|3[0-2]|[1-9])$";
        if (StringUtils.isNotEmpty(value) && !String.valueOf(value).matches(regex)) {
            return ResultUtils.parseMap(ResultUtils.ERROR, "网段地址格式不正确. 正确格式如:192.168.1.1/24");
        }
        return ResultUtils.parseMap(ResultUtils.SUCCESS, ResultUtils.SUCCESS);
    }
}
