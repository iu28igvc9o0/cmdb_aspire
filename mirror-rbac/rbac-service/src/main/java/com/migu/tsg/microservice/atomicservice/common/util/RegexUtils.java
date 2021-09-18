package com.migu.tsg.microservice.atomicservice.common.util;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.migu.tsg.microservice.atomicservice.common.enums.BadRequestFieldMessageEnum;
import com.migu.tsg.microservice.atomicservice.common.enums.ResultErrorEnum;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.common.util <br>
 * 类名称: RegexUtil.java <br>
 * 类描述: 正则表达式工具类 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月1日上午9:48:22 <br>
 * 版本: v1.0
 */
public class RegexUtils {

    /**
     * 验证数组uuids是否为合法的UUID字符串
     * @param uuids UUID集合
     * @return true则合法,false则不合法
     */
    public static boolean hasMatchesRegexUuids(final String... uuids) {
        if (uuids == null || uuids.length <= 0) {
            return false;
        }
        String pattern = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$";
        return Arrays.stream(uuids).allMatch(uuid -> StringUtils.trimToEmpty(uuid).matches(pattern));
    }

    /**
     * 验证uuids是否为合法的UUID字符串
     * @param uuids UUID集合
     */
    public static void verifyRegexUuids(final String... uuids) {
        if (uuids!=null&&!RegexUtils.hasMatchesRegexUuids(uuids)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "uuids",
                    new String[] { BadRequestFieldMessageEnum.UUID_REQUEST_INVALID.getMessage() });
        }
    }

}
