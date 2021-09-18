package com.aspire.ums.cmdb.sqlManage;

import lombok.Data;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSqlManage
 * Author:   hangfang
 * Date:     2020/6/17
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class CmdbSqlManage {

    /**
     * id
     */
    private String id;
    /**
     * 方法名
     */
    private String name;
    /**
     * 权限归属模型ID
     */
    private String authModuleId;
    /**
     * 权限归属, 与权限分组对应
     */
    private String authModule;
    /**
     * 图表类型
     */
    private String chartType;
    /**
     * 图表名
     */
    private String chartTitle;
    /**
     * sql
     */
    private String chartSql;
    /**
     * 是否需要权限过滤
     */
    private Integer needAuth;
    /**
     * 描述
     */
    private String describe;

    public CmdbSqlManage () {}

    public CmdbSqlManage (String chartSql, String authModule, Integer needAuth) {
        this.chartSql = chartSql;
        this.authModule = authModule;
        this.needAuth = needAuth;
    }

    public CmdbSqlManage (String chartSql, String authModuleId, String authModule, Integer needAuth) {
        this.chartSql = chartSql;
        this.authModuleId = authModuleId;
        this.authModule = authModule;
        this.needAuth = needAuth;
    }
}
