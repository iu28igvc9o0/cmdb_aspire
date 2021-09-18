package com.aspire.code.generator.service.impl;

import com.aspire.code.generator.config.DBUtils;
import com.aspire.code.generator.config.FreemarkerUtil;
import com.aspire.code.generator.service.ICodeGeneratorService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CodeGeneratorService
 * Author:   zhu.juwang
 * Date:     2019/4/28 17:18
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CodeGeneratorServiceImpl implements ICodeGeneratorService {
    @Override
    public List<String> generatorCode(String databaseUrl, String databaseUser, String databasePwd, List<String> tableNames, String packagePath, String localPath) {
        List<String> returnList = new ArrayList<>();
        for (String tableName : tableNames) {
            List<Map<String, String>> filedList = getTableColumn(databaseUrl, databaseUser, databasePwd, tableName);
            if (filedList.size() == 0) {
                returnList.add("表名->" + tableName + "不存在或表结构为空.");
                continue;
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            dataMap.put("entityName", DBUtils.formatNameToClassName(tableName));
            dataMap.put("tableName", tableName);
            dataMap.put("filedList", filedList);
            String entityPath = packagePath+".entity";
            String entityName = DBUtils.formatNameToClassName(tableName);
            String servicePath = packagePath+".service";
            String serviceName = "I" + DBUtils.formatNameToClassName(tableName) + "Service";
            String serviceImplPath = packagePath+".service.impl";
            String serviceImplName = DBUtils.formatNameToClassName(tableName) + "ServiceImpl";
            String mapperPath = packagePath+".mapper";
            String mapperName = DBUtils.formatNameToClassName(tableName) + "Mapper";
            dataMap.put("entityPackage", entityPath);
            dataMap.put("entityName", entityName);
            dataMap.put("servicePackage", servicePath);
            dataMap.put("serviceName", serviceName);
            dataMap.put("implPackage", serviceImplPath);
            dataMap.put("implName", serviceImplName);
            dataMap.put("mapperPackage", mapperPath);
            dataMap.put("mapperName", mapperName);

            //生成文件
            this.generatorEntity(localPath, entityPath, entityName, dataMap, "java", "entity.ftl");
            this.generatorEntity(localPath, servicePath, serviceName, dataMap, "java", "service.ftl");
            this.generatorEntity(localPath, serviceImplPath, serviceImplName, dataMap, "java", "impl.ftl");
            this.generatorEntity(localPath, mapperPath, mapperName, dataMap, "java", "mapper.ftl");
            this.generatorEntity(localPath, mapperPath, mapperName, dataMap, "xml", "mapperXML.ftl");
            returnList.add(localPath + File.separator + entityPath.replace(".", File.separator) + File.separator + entityName + ".java");
            returnList.add(localPath + File.separator + servicePath.replace(".", File.separator) + File.separator + serviceName + ".java");
            returnList.add(localPath + File.separator + serviceImplPath.replace(".", File.separator) + File.separator + serviceImplName + ".java");
            returnList.add(localPath + File.separator + mapperPath.replace(".", File.separator) + File.separator + mapperName + ".java");
            returnList.add(localPath + File.separator + mapperPath.replace(".", File.separator) + File.separator + mapperName + ".xml");
        }
        return returnList;
    }

    @Override
    public String generatorCURD(String databaseUrl, String databaseUser, String databasePwd, List<String> tableNames, String operator) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String tableName : tableNames) {
            List<Map<String, String>> filedList = getTableColumn(databaseUrl, databaseUser, databasePwd, tableName);
            if (filedList.size() == 0) {
                stringBuilder.append("----table name -> [" + tableName + "]---------------------------------------------------");
                stringBuilder.append("表名->" + tableName + "不存在或表结构为空.");
                continue;
            }
            /*
                处理字段
                1. 实体类字段都处理为骆驼命名法
                2. 实体类数据类型转化
             */
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("operator", operator.toLowerCase());
            dataMap.put("tableName", tableName);
            dataMap.put("filedList", filedList);

            //开始生成文件
            Configuration configuration = FreemarkerUtil.getConfiguration();
            Template emailTemplate;
            try {
                emailTemplate = configuration.getTemplate("curd.ftl");
                StringWriter out = new StringWriter();
                emailTemplate.process(dataMap,out);
                String sqlString = out.getBuffer().toString();
                if (!stringBuilder.toString().equals("")) {
                    stringBuilder.append("\r\n");
                }
                stringBuilder.append("----table name -> [" + tableName + "]---------------------------------------------------");
                stringBuilder.append("\r\n");
                stringBuilder.append(sqlString);
                out.close();
            } catch (IOException | TemplateException e) {
                log.error("Generator file error.", e);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public List<String> connection(String databaseUrl, String databaseUser, String databasePwd) throws Exception {
        JdbcTemplate jdbcTemplate = DBUtils.getTemplate(databaseUrl, databaseUser, databasePwd);
        if (!databaseUrl.contains("mysql")) {
            throw new Exception("Connection url " + databaseUrl + "isn't support!");
        }
        List<String> tables;
        try {
            String tableOwner = databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1, databaseUrl.indexOf("?"));
            String querySql = "select TABLE_NAME from information_schema.`TABLES` where TABLE_SCHEMA=? order by TABLE_NAME";
            Object[] params = new Object[] {tableOwner};
            tables = jdbcTemplate.query(querySql, params, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("TABLE_NAME");
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("Execute query sql error." + e.getMessage(), e);
        }
        return tables;
    }

    private List<Map<String, String>> getTableColumn (String databaseUrl, String databaseUser, String databasePwd, String tableName) {
        JdbcTemplate jdbcTemplate = DBUtils.getTemplate(databaseUrl, databaseUser, databasePwd);
        String querySql = "select * from information_schema.`COLUMNS` where LOWER(TABLE_NAME)=LOWER('" + tableName + "')";
        //获取表结构
        List<Map<String, Object>> columnList = jdbcTemplate.queryForList(querySql);
        List<Map<String, String>> filedList = new ArrayList<>();
        for (Map<String, Object> column : columnList) {
            Map<String, String> filed = new HashMap<>();
            filed.put("javaFiled", DBUtils.formatNameToCamelCase(column.get("COLUMN_NAME").toString()));
            filed.put("javaFiledType", DBUtils.formatDBTypeToJavaType(column.get("DATA_TYPE").toString()));
            filed.put("dbColumn", column.get("COLUMN_NAME").toString());
            filed.put("dbColumnType", DBUtils.formatDBType(column.get("DATA_TYPE").toString()));
            filed.put("columnComment", column.get("COLUMN_COMMENT").toString());
            if (column.get("COLUMN_KEY").equals("PRI")) {
                filed.put("isKey", "true");
            }
            filedList.add(filed);
        }
        return filedList;
    }

    /**
     * 生成entity实体类
     * @param localPath 本地路径
     * @param packagePath 包路径
     * @param fileName 文件名称
     * @param dataMap 数据集
     * @param suffix 文件后缀
     * @param ftlName 模板名称
     */
    private void generatorEntity(String localPath, String packagePath, String fileName, Map<String, Object> dataMap, String suffix, String ftlName) {
        FreemarkerUtil.generatorFile(localPath, packagePath, fileName + "." + suffix, dataMap, ftlName);
    }
}
