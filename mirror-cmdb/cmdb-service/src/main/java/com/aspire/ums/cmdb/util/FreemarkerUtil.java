package com.aspire.ums.cmdb.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: FreemarkerUtil
 * Author:   zhu.juwang
 * Date:     2020/6/16 17:48
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class FreemarkerUtil {

    /**
     * 获取freemarker cfg
     * @return
     * @throws IOException
     */
    public static Configuration getConfiguration() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
        cfg.setClassForTemplateLoading(FreemarkerUtil.class,"/freemarker");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    public static String generatorFile(String querySQL, Object modelData) {
        //开始生成文件
        Configuration configuration = getConfiguration();
        Template emailTemplate = null;
        try {
//            String content = "select * from aaa where 1=1 <#if filedList!=null and filedList != ''> and idcType = #{idcType} </#if>";
//            Map<String, Object> mapData = new HashMap<>();
//            mapData.put("filedList", "ccc");
//            mapData.put("idcType", "南方基地");

            emailTemplate = new Template("template", new StringReader(querySQL), configuration);
            StringWriter out = new StringWriter();
            emailTemplate.process(modelData,out);
            String outString = out.getBuffer().toString();
            log.info(outString);
            out.close();
            return outString;
        } catch (IOException | TemplateException e) {
            log.error("Generator file error.", e);
            throw new RuntimeException("Generator file error.");
        }
    }
}
