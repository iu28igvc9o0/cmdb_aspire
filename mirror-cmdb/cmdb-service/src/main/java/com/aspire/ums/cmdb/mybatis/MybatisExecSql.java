package com.aspire.ums.cmdb.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class MybatisExecSql {
    private String sql;
    private List<String> params;  //占位符的参数名  map中的 key
    private Map<String, Object> metaParams;

    public MybatisExecSql(String sql, List<String> params, Map<String, Object> metaParams){
        this.sql = sql;
        this.params = params;
        this.metaParams = metaParams;
    }
}
