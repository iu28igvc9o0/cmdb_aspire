package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.schema;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cmdb.schema.payload.ColumnInfo;
import com.aspire.ums.cmdb.schema.payload.TableInfo;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSchemaClient
 * Author:   zhu.juwang
 * Date:     2019/5/30 22:55
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbSchemaClient {
    /**
     * 获取表名列表
     */
    @RequestMapping(value = "/cmdb/schema/getTables", method = RequestMethod.GET)
    List<TableInfo> getTableList();

    /**
     * 获取表名列表
     */
    @RequestMapping(value = "/cmdb/schema/getColumns/{tableName}", method = RequestMethod.GET)
    List<ColumnInfo> getColumnsByTableName(@PathVariable("tableName") String tableName);

    /**
     * 检测SQL是否错误
     */
    @RequestMapping(value = "/cmdb/schema/checkSql", method = RequestMethod.POST)
    Map<String, String> checkSql(@RequestBody Map<String, String> params);
}
