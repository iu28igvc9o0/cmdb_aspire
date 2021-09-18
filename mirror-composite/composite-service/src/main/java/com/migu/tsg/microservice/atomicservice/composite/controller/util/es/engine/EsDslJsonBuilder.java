package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.engine;

import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 过查询对象构造es dsl查询语句 description: Copyright: Copyright (c) 2017 Company: 咪咕文化
 * tsg package: com.migu.tsg.microservice.monitor.log.es title:
 * EsDslJsonBuilder.java version: V1.0.0.0 author: sunke date: 2017/9/11 22:42
 */
public class EsDslJsonBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(EsDslJsonBuilder.class);
    /**
     * 可提供的dsl语句模板
     */
    private enum DslType {
        histogram, count, sms_count, list, sms_list, group, biz_list, biz_count, biz_histogram
    }

    /**
     * es 查询dsl 版本文件名规则，由于Velocity设置为读取根目录下的模板文件 该文件命名要求模板文件需要存在根目录的vm下
     */
    private static final String VM_TEMPLATE_FILE = "vm/{0}_dsl.vm";

    /**
     * 生成直方图 dsl
     * 
     * @param obj
     *            转换条件
     * @return dsl语句
     */
    public static String histogram(EsQueryBuilder obj) {
        return createDsl(obj, DslType.histogram.name());
    }

    /**
     * 生成count dsl
     * 
     * @param obj
     *            转换条件
     * @return dsl语句
     */
    public static String histogramBiz(EsQueryBuilder obj) {
        return createDsl(obj, DslType.biz_histogram.name());
    }
	/**
	 * 生成count dsl
	 * @param obj 转换条件
	 * @return dsl语句
	 */
	public static String count(EsQueryBuilder obj) {
		return createDsl(obj, DslType.count.name());
	}
	/**
     * 生成count dsl
     * @param obj 转换条件
     * @return dsl语句
     */
    public static String countBiz(EsQueryBuilder obj) {
        return createDsl(obj, DslType.biz_count.name());
    }
	/**
	 * 生成 sms count dsl
	 * @param obj 转换条件
	 * @return dsl语句
	 */
	public static String smsCount(EsQueryBuilder obj) {
		return createDsl(obj, DslType.sms_count.name());
	}
	/**
	 * 生成group dsl
	 * @param obj 转换条件
	 * @return dsl语句
	 */
	public static String group(EsQueryBuilder obj) {
		return createDsl(obj, DslType.group.name());
	}
	/**
	 * 生成es list dsl查询json
	 * @param obj 条件对象
	 * @return 转换后的dsl查询语句
	 */
	public static String list(EsQueryBuilder obj) {
		return createDsl(obj, DslType.list.name());
	}
	public static String listSms(EsQueryBuilder obj) {
		return createDsl(obj, DslType.sms_list.name());
	}
	/**
     * 生成es list dsl查询json
     * @param obj 条件对象
     * @return 转换后的dsl查询语句
     */
    public static String listBiz(EsQueryBuilder obj) {
        return createDsl(obj, DslType.biz_list.name());
    }

    /**
     * 根据模板生成dsl语句
     * 
     * @param obj
     *            转换条件
     * @return dsl语句
     */
    private static String createDsl(EsQueryBuilder obj, String type) {
        LOG.info("createDsl +++ type:{}, obj:{}", type, obj.toString());

        final Properties properties = new Properties();
        properties.put("file.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(properties);
        final StringWriter writer = new StringWriter();
        try {
            final Template template = Velocity.getTemplate(MessageFormat.format(VM_TEMPLATE_FILE, type));
            final VelocityContext context = new VelocityContext();
            context.put("builder", obj);
            template.merge(context, writer);
        } catch (Exception e) {
            LOG.error("fail writer or merge velocity content " + e.getMessage(), e);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                LOG.error("fail close velocity writer " + ex.getMessage(), ex);
            }
        }
        /*
         * if(type.equals(DslType.histogram.name()) ||
         * type.equals(DslType.count.name())){
         * LOG.info("createDsl --- type={}, write:{}", type, writer.toString()); }
         */
        LOG.info("createDsl --- type={}, write:{}", type, writer.toString());
        /*
         * String statment = writer.toString(); String leftString =
         * statment.substring(0,statment.lastIndexOf(STR_DELIMITE_PARAM)); String
         * rightString = statment.substring(statment.lastIndexOf(STR_DELIMITE_PARAM) +
         * 1, statment.length()); statment = leftString + rightString;
         */
        return writer.toString();
    }
}
