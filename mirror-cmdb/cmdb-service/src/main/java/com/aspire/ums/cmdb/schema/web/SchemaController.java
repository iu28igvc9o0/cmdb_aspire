package com.aspire.ums.cmdb.schema.web;

import com.aspire.ums.cmdb.schema.ISchemaAPI;
import com.aspire.ums.cmdb.schema.payload.ColumnInfo;
import com.aspire.ums.cmdb.schema.payload.TableInfo;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SchemaController
 * Author:   zhu.juwang
 * Date:     2019/5/29 8:33
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class SchemaController implements ISchemaAPI {

    @Autowired
    private SchemaService schemaService;

    @Override
    public List<TableInfo> getTableList() {
        return schemaService.getTableList();
    }

    @Override
    public List<ColumnInfo> getColumnsByTableName(@PathVariable("tableName") String tableName) {
        return schemaService.getColumnListByTableName(tableName);
    }

    @Override
    public Map<String, String> checkSql(@RequestBody Map<String, String> params) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            List<Map<String, String>> list = schemaService.checkSql(params);
            returnMap.put("flag", "success");
        } catch (Exception e) {
            returnMap.put("flag", "error");
            returnMap.put("msg", e.getMessage());
        }
        return returnMap;
    }
}
