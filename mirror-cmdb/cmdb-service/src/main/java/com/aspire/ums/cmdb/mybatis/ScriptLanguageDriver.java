package com.aspire.ums.cmdb.mybatis;

import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;

import java.util.Map;

/**
 * @desc:
 * @author: zhangliu
 * @date: 2019/7/19
 */
public class ScriptLanguageDriver {

    private final Configuration configuration = new Configuration();
    private final ScriptBuilder scriptBuilder;

    public ScriptLanguageDriver(String script) {
        StringBuilder sb = new StringBuilder("<script> ");
        sb.append(script).append(" </script>");
        XPathParser parser = new XPathParser(sb.toString(), false, configuration.getVariables(), new XMLMapperEntityResolver());
        XNode xNode = parser.evalNode("/script");
        this.scriptBuilder = new ScriptBuilder(configuration, xNode, Map.class);
    }

//    /**
//     * 获取解析sql的参数
//     * @return
//     */
//    public Set<String> parseScriptParams() {
//        return scriptBuilder.parseScriptParams();
//    }

    /**
     * 获取可以执行的sql
     * @return
     */
    public SqlSource createSqlSource() {
        return scriptBuilder.parseScriptNode();
    }


}
