package com.aspire.ums.cmdb.v3.validator;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.util.ResultUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;

import java.util.Arrays;
import java.util.List;
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
public class IPValidHandler extends AbstractValidFactory {
    @Override
    public Map<String, String> valid(CmdbCode cmdbCode, final CmdbV3CodeValidate codeValidate, Object value) {
        if (value == null || StringUtils.isEmpty(value.toString().trim())) {
            return ResultUtils.parseMap(ResultUtils.SUCCESS, ResultUtils.SUCCESS);
        }
        String sourceIp = value.toString();
        sourceIp.replace("，", ",");
        List<String> ipList = Arrays.asList(sourceIp.split(","));
        for (String ip : ipList) {
            String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
            if (StringUtils.isNotEmpty(ip) && !String.valueOf(ip).matches(regex)) {
                return ResultUtils.parseMap(ResultUtils.ERROR, "IP地址格式不对. 正确格式如:114.114.114.114");
            }
        }
        return ResultUtils.parseMap(ResultUtils.SUCCESS, ResultUtils.SUCCESS);
    }
}
