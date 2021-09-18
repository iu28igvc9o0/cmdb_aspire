package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.schema;

import com.aspire.mirror.composite.service.cmdb.schema.ICompSchemaAPI;
import com.aspire.ums.cmdb.schema.payload.ColumnInfo;
import com.aspire.ums.cmdb.schema.payload.TableInfo;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.schema.CmdbSchemaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSchemaController
 * Author:   zhu.juwang
 * Date:     2019/5/30 22:57
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbSchemaController implements ICompSchemaAPI {

    @Autowired
    private CmdbSchemaClient schemaClient;

    @Override
    public List<TableInfo> getTableList() {
        return schemaClient.getTableList();
    }

    @Override
    public List<ColumnInfo> getColumnsByTableName(@PathVariable("tableName") String tableName) {
        return schemaClient.getColumnsByTableName(tableName);
    }

    @Override
    public Map<String, String> checkSql(@RequestBody Map<String, String> params) {
        return schemaClient.checkSql(params);
    }
}
