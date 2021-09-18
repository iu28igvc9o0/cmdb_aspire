package com.aspire.ums.cmdb.mybatis;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @desc: 获取mybatis的sql解析结果
 * @author: zhangliu
 * @date: 2019/7/19
 */
public class ScriptBuilder extends XMLScriptBuilder {

//    //自定义参数
//    private Set<String> paramNames;

    //脚本
//    private String script;
//    private XPathParser parser;

    public ScriptBuilder(Configuration configuration, XNode context) {
        this(configuration, context, null);
    }

    public ScriptBuilder(Configuration configuration, XNode context, Class<?> parameterType) {
        super(configuration, context, parameterType);
    }

//    public ScriptBuilder parser(){
//        Objects.requireNonNull(script);
//        Objects.requireNonNull(configuration);
//        this.parser = new XPathParser(addScript(script), false, configuration.getVariables(), new XMLMapperEntityResolver());
//        return this;
//    }

//    public ScriptBuilder script(String script){
//        this.script = script;
//        return this;
//    }

    /**
     * 得到全部的参数列表
     * @return 参数列表
     */
//    public Set<String> parseScriptParams() {
//        //解析标签
//        this.parseDynamicTags(this.context);
//        //解析占位符
//        this.dollarDynamicParamNameParser(this.paramNames, this.script);
//        this.poundDynamicParamNameParser(this.paramNames, this.script);
//        return this.paramNames;
//    }

    /**
     * 检查是否是mybatis适用的sql语句
     * @return 是否
     */
    public static boolean checkMybatisScript(String script) {

        Set<String> paramNames =  new HashSet<>();
        //解析占位符
        dollarDynamicParamNameParser(paramNames, script);
        poundDynamicParamNameParser(paramNames, script);

        if(paramNames.size() > 0) {
            return true;
        }

        final String noBlank = script.replaceAll("\\s", "");
        return check(noBlank.toLowerCase(), new String[]{"<!","<where","<if","<choose","<when","<otherwise", "<foreach"});
    }

    private static boolean check(String noBlank, String[] tags) {
        for(String tag : tags) {
            if(noBlank.indexOf(tag) != -1) {
                return true;
            }
        }
        return false;
    }

    private static class  DynamicParamNameTokenParser implements TokenHandler {
        private Set<String> paramNames;
        DynamicParamNameTokenParser(Set<String> paramNames) {
            this.paramNames = paramNames;
        }
        public String handleToken(String content) {
            checkParamsAndAdd(paramNames, content);
            return content;
        }
    }

    public static void dollarDynamicParamNameParser(Set<String> paramNames, String text) {
        DynamicParamNameTokenParser checker = new DynamicParamNameTokenParser(paramNames);
        GenericTokenParser parser = dollarParser(checker);
        parser.parse(text);
    }

    public static void poundDynamicParamNameParser(Set<String> paramNames, String text) {
        DynamicParamNameTokenParser checker = new DynamicParamNameTokenParser(paramNames);
        GenericTokenParser parser = poundParser(checker);
        parser.parse(text);
    }

    private static GenericTokenParser dollarParser(TokenHandler handler) {
        return new GenericTokenParser("${", "}", handler);
    }
    private static GenericTokenParser poundParser(TokenHandler handler) {
        return new GenericTokenParser("#{", "}", handler);
    }

    private static void checkParamsAndAdd(Set<String> paramNames, String paramName) {
        Objects.requireNonNull(paramName);
        paramNames.add(paramName);
    }

}
