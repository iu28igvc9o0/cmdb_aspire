package com.aspire.ums.cmdb.mybatis;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @desc:
 * @author: zhangliu
 * @date: 2019/7/19
 */
@Slf4j
public class MybatisScriptHelper {

    /**
     * 获取执行的sql
     */
    public static MybatisExecSql getExecScriptSql(String sql, Map<String, Object> params) {
        final SqlSource sqlSource = new ScriptLanguageDriver(sql).createSqlSource();
        BoundSql boundSql = sqlSource.getBoundSql(params);
        String exec = boundSql.getSql();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        List<String> paramsIndex = new ArrayList<>();
        Map<String, Object> metaParams = new LinkedMap();
        for (ParameterMapping pro : parameterMappings) {
            paramsIndex.add(pro.getProperty());
            Object value;
            if (boundSql.hasAdditionalParameter(pro.getProperty())) {
                value = boundSql.getAdditionalParameter(pro.getProperty());
            } else if (params.containsKey(pro.getProperty())) {
                value = params.get(pro.getProperty());
            } else {
                value = null;
            }
            metaParams.put(pro.getProperty(), value);
        }
        if (log.isInfoEnabled()) {
            log.info("Query SQL -> {}", exec);
            log.info("Query SQL Params -> {}", JSONObject.fromObject(metaParams));
        }
        return new MybatisExecSql(exec, paramsIndex, metaParams);
    }

    /**
     *  验证是否是mybatis适用的脚本
     * @param sql sql脚本
     * @return 是否
     */
    public static boolean checkMybatisScript(String sql) {
        return ScriptBuilder.checkMybatisScript(sql);
    }


}
