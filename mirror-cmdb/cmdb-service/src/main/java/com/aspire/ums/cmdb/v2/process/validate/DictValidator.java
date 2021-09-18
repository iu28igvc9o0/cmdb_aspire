package com.aspire.ums.cmdb.v2.process.validate;

import com.alibaba.fastjson.JSONArray;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DictValidator
 * Author:   zhu.juwang
 * Date:     2019/8/5 20:32
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class DictValidator {

    /**
     * 判断字典值
     * @param value
     * @throws RuntimeException
     */
    public static void validator(String columnName, Object value, List sourceList) throws RuntimeException {
        if (StringUtils.isNotEmpty(value)) {
            List<String> valueList = new ArrayList<>();
            if (sourceList != null && sourceList.size() > 0) {
                sourceList.forEach((object) -> {
                    if (object instanceof Map) {
                        Map<String, String> data = (Map<String, String>) object;
                        valueList.add(data.get("value"));
                    }
                    if (object instanceof String) {
                        valueList.add(object.toString());
                    }
                });
                if (!valueList.contains(value.toString())) {
                    throw new RuntimeException("列["+columnName+"]值[" + value + "]不在可选范围. 可选值["+ JSONArray.toJSONString(valueList) +"]");
                }
            } else {
                throw new RuntimeException("列["+columnName+"]值[" + value + "]不在可选范围. 可选值["+ JSONArray.toJSONString(valueList) +"]");
            }
        }
    }

}
