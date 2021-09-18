package com.aspire.code.generator.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司 FileName: FreemarkerUtil Author: zhu.juwang Date: 2019/4/28 17:48 Description: ${DESCRIPTION}
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Slf4j
public class FreemarkerUtil {

    /**
     * 获取freemarker cfg
     * 
     * @return
     * @throws IOException
     */
    public static Configuration getConfiguration() {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(
                freemarker.template.Configuration.VERSION_2_3_26);
        cfg.setClassForTemplateLoading(FreemarkerUtil.class, "/freemarker");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    public static void generatorFile(String localPath, String packageName, String fileName, Object modelData, String ftlName) {
        /*
         * 生成package目录
         */
        localPath = localPath + packageName.replace(".", File.separator);
        File rootPath = new File(localPath);
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }
        // 开始生成文件
        Configuration configuration = getConfiguration();
        Template emailTemplate = null;
        FileOutputStream fos = null;
        StringWriter out = new StringWriter();
        try {
            emailTemplate = configuration.getTemplate(ftlName);
            emailTemplate.process(modelData, out);
            File file = new File(localPath + File.separator + fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            String outString = out.getBuffer().toString();
            byte[] bytes = outString.getBytes();
            int b = bytes.length; // 是字节的长度，不是字符串的长度
            fos = new FileOutputStream(file);
            fos.write(bytes, 0, b);
        } catch (IOException | TemplateException e) {
            log.error("Generator file error.", e);
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("关闭文件输出流错误:", e);
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("关闭文件输出流错误:", e);
                }
            }
        }
    }

}
