package com.aspire.ums.cmdb.v2.process.validate;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: EmptyValidator
 * Author:   zhu.juwang
 * Date:     2019/8/5 17:41
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class EmptyValidator {

    /**
     * 非空判断
     * @param value
     * @throws RuntimeException
     */
    public static void notEmpty(String columnName, Object value) throws RuntimeException {
        if (value == null || value.toString().equals("")) {
            throw new RuntimeException("列[" + columnName + "]不能为空");
        }
    }

}
