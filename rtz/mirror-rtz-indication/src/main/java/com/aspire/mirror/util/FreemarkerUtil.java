package com.aspire.mirror.util;

import com.aspire.mirror.controller.IndicationController;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateCache;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@Slf4j
public class FreemarkerUtil {
    private static Configuration configuration = null;
    @PostConstruct
    public static void initConfig() {
        log.info("Start setting freemarker configuration .");
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        //设置模板目录
        TemplateLoader classLoad = new ClassTemplateLoader(IndicationController.class.getClassLoader(),"/freemarker/");
        configuration.setTemplateLoader(classLoad);
        TemplateCache templateCache = new TemplateCache(classLoad, configuration);
        templateCache.setDelay(0L);
        log.info("End setting freemarker configuration .");
    }

    /**
     * 获取模板数据
     * @param templateName
     * @param params
     * @return
     */
    public static String formatTemplate(String templateName, Map<String, Object> params) {
        Writer out = null;
        Template template;
        try {
            out = new StringWriter();
            template = getConfiguration().getTemplate(templateName);
            template.process(params, out);
            return out.toString();
        } catch (IOException e) {
            log.error("Get freemarker template -> {} error.", templateName, e);
            return null;
        } catch (TemplateException e) {
            log.error("Format freemarker template -> {}, values -> {} error.", templateName, JSONObject.fromObject(params));
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("Close StringWriter error.", e);
                }
            }
        }
    }

    public static Configuration getConfiguration() {
        return configuration;
    }
}
