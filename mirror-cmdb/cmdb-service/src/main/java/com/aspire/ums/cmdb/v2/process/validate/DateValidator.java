package com.aspire.ums.cmdb.v2.process.validate;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DateValidator
 * Author:   zhu.juwang
 * Date:     2019/8/5 17:38
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class DateValidator extends EmptyValidator {

    /**
     * 验证时间格式
     * @param value
     * @throws RuntimeException
     */
    public static Date validate(String columnName, Object value) throws RuntimeException {
        return validate(columnName, value, new String[]{Constants.DATE_PATTERN_YYYY_MM_DD, Constants.DATE_PATTERN_YYYY_MM_DD_01});
    }

    /**
     * 验证时间格式
     * @param value
     * @param pattern 格式
     * @throws RuntimeException
     */
    public static Date validate(String columnName, Object value, String[] pattern) throws RuntimeException {
        try {
            if (value != null && StringUtils.isNotEmpty(value.toString())) {
                return DateUtils.parseDate(String.valueOf(value), pattern);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("列[" + columnName + "] 日期" + value + "输入有误, 日期格式不正确, 支持[" + StringUtils.join(pattern, "、") + "]格式;");
        }
    }

    /**
     * 验证时间格式
     * @param value
     * @throws RuntimeException
     */
    public static Date nonnullValidate(String columnName, Object value) throws RuntimeException {
        notEmpty(columnName, value);
        return validate(columnName, value);
    }

    /**
     * 验证时间格式
     * @param value
     * @param pattern 格式
     * @throws RuntimeException
     */
    public static Date nonnullValidate(String columnName, Object value, String[] pattern) throws RuntimeException {
        notEmpty(columnName, value);
        return validate(columnName, value, pattern);
    }
}
