package com.aspire.ums.cmdb.v3.validator;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.util.IpUtils;
import com.aspire.ums.cmdb.util.ResultUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;

import java.util.Map;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/13 19:55
 */
public class Ipv6ValidHandler extends AbstractValidFactory {
    @Override
    public Map<String, String> valid(CmdbCode cmdbCode, CmdbV3CodeValidate codeValidate, Object value) {
        if (StringUtils.isNotEmpty(value) && !IpUtils.isValidIpv6Addr2(String.valueOf(value))) {
            return ResultUtils.parseMap(ResultUtils.ERROR, "IP地址格式不对. 正确格式如:X:X:X:X:X:X:X:0/24");
        }
        return ResultUtils.parseMap(ResultUtils.SUCCESS, ResultUtils.SUCCESS);
    }
}
