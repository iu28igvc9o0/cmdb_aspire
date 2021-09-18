package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.process;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/8/28
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class BizResourceProcessThread  extends ProcessThread {
    @Override
    protected void setDataMap(Map<String, String> keyValueMap, String key, String value) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(value);
        value = m.replaceAll("");
        if (("bizresourcephy").equals(importType.toLowerCase(Locale.ENGLISH)) || ("bizresourcevm").equals(importType.toLowerCase(Locale.ENGLISH))) {
            keyValueMap.put(key, value);
            keyValueMap.put("importType", importType);
        } else {
            keyValueMap.put(key, value);
        }
    }

    @Override
    protected void setSpecialData(Map<String, Object> importData) {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void valid() {

    }
}
