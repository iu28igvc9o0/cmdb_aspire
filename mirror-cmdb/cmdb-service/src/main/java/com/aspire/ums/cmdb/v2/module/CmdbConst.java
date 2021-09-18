package com.aspire.ums.cmdb.v2.module;

import com.aspire.ums.cmdb.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbConst
 * Author:   hangfang
 * Date:     2019/5/21 13:53
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class CmdbConst {
    //模型表名称前缀
    public static final String CMDB_MODULE_TABLE_V3_PREFIX = "cmdb_v3_";

    //模型表名称前缀
    //public static final String CMDB_MODULE_TABLE_PREFIX = "cmdb_instance_";

    //模型表名称后缀（被删除的表数据）
    public static final String CMDB_MODULE_TABLE_SUFFIX = "_his";

    //模型表名称后缀（被删除的表数据）
    public static final String CMDB_INSTANCE_MAIN_TABLE = "cmdb_instance";

    //默认模型 主机资源 id 的字典类型
    public static final String CMDB_DEFAULT_CI_MODULE_ID = "default_ci_module_id"; // 主机资源
    public static final String CMDB_DEFAULT_BUSINESS_MODULE_ID = "default_business_module_id"; // 业务资源
    public static final String CMDB_DEFAULT_DICT_MODULE_ID = "default_dict_module_id"; // 数据资源
    public static final String CMDB_DEFAULT_PRODUCE_MODULE_ID = "default_produce_module_id"; // 厂家信息
    public static final String CMDB_DEFAULT_BUSINESS_SYSTEM_ID = "default_business_system_module_id"; // 业务系统模型ID

    public static final String CMDB_MODULE_TYPE_HOST = "host";
    public static final String CMDB_MODULE_TYPE_BUSINESS = "business";
    public static final String CMDB_MODULE_TYPE_DICT = "dict";

    public static final String getModuleTable(String catalogCode) {
        return CMDB_MODULE_TABLE_V3_PREFIX + catalogCode + "_";
    }

    public static final String getConditionSql(List<Map<String, String>> querys) {
        StringBuilder condition = new StringBuilder();
        for (Map<String, String> query : querys) {
            condition.append(" and ");
            // 处理查询条件
            String filed = query.get("field");
            String value = query.get("value");
            String operator = query.get("operator");
            switch (operator) {
                case ">":
                    condition.append(filed + " >  '" + value + "' + 0 ");
                    break;
                case ">=":
                    condition.append(filed + " >=  '" + value + "' + 0 ");
                    break;
                case "<":
                    condition.append(filed + " <  '" + value + "' + 0 ");
                    break;
                case "<=":
                    condition.append(filed + " <=  '" + value + "' +0 ");
                    break;
                case "!=":
                    if (StringUtils.isEmpty(value)) {
                        condition.append("IFNULL(" + filed + ", '') != '' ");
                    } else {
                        condition.append(filed + " != '" + value + "' ");
                    }
                    break;
                case "=":
                    if (StringUtils.isEmpty(value)) {
                        condition.append("IFNULL(" + filed + ", '') = '' ");
                    } else {
                        condition.append(filed + " = '" + value + "' ");
                    }
                    break;
                case "like":
                    condition.append(filed + " like '%" + value + "%' ");
                    break;
                case "not in":
                    condition.append(filed + " not in ('" + value.replaceAll(",", "','") + "')");
                    break;
                case "in":
                    condition.append(filed + " in ('" +  value.replaceAll(",", "','") + "')");
                    break;
                case "between":
                    condition.append(filed + " between '" +  value.split(",")[0] + "' and '" + value.split(",")[1] + "' ");
                    break;
                default:
                    condition.append(filed + " = '" + value + "' ");
                    break;
            }
        }
        return condition.toString();
    }
}
