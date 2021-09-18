package com.aspire.ums.cmdb.v2.process.validate;

import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.SpringUtils;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: UnqiueValidator
 * Author:   zhu.juwang
 * Date:     2019/8/5 18:03
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class UniqueValidator {
    private static SchemaService schemaService;
    public static SchemaService getSchemaService() {
        if (schemaService == null) {
            schemaService = SpringUtils.getBean(SchemaService.class);
        }
        return schemaService;
    }

    public static void validate(String columnName, String sql, Object ... values) {
        List<Map<String, Object>> list = null;
        try {
            String newSql = replace(sql, 0, values);
            list = getSchemaService().executeSql(newSql);
        } catch (Exception e) {
            throw new RuntimeException("列[" + columnName + "]唯一检测语句[" + replace(sql, 0, values) + "]失败.");
        }
        if (list != null && list.size() > 0) {
            throw new RuntimeException("列[" + columnName + "]不唯一.");
        }
    }

    private static String replace(String sql, int index, Object ... values) {
        if (sql.contains("\\?")) {
            sql.replace("\\?", values[index].toString());
            replace(sql, index++, values);
        }
        return sql;
    }
}
