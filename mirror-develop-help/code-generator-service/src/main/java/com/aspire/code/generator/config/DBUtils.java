package com.aspire.code.generator.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DBUtils
 * Author:   zhu.juwang
 * Date:     2019/4/28 17:08
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class DBUtils {

    private static JdbcTemplate jdbcTemplate;
    private static String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";

    /**
     * 获取JdbcTemplate连接
     * @param databaseUrl 数据库连接
     * @param userName 用户名
     * @param passWord 密码
     * @return JdbcTemplate 操作类
     */
    public static JdbcTemplate getTemplate(String databaseUrl, String userName, String passWord) {
        jdbcTemplate = SpringUtils.getBean(JdbcTemplate.class);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(MYSQL_DRIVER_CLASS);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(passWord);
        jdbcTemplate.setDataSource(dataSource);
        dataSource = null;
        return jdbcTemplate;
    }

    public static String formatNameToCamelCase(String filedName) {
        if (filedName == null || !filedName.contains("_")) {
            return filedName;
        }
        String[] fileds = filedName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String filed : fileds) {
            if (!StringUtils.isEmpty(sb.toString())) {
                sb.append(filed.substring(0,1).toUpperCase()).append(filed.substring(1));
            } else {
                sb.append(filed);
            }
        }
        return sb.toString();
    }

    public static String formatNameToClassName(String filedName) {
        String[] fileds = filedName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String filed : fileds) {
            sb.append(filed.substring(0,1).toUpperCase()).append(filed.substring(1));
        }
        return sb.toString();
    }

    public static String formatDBTypeToJavaType(String filedType) {
        String javaType = "String";
        if (filedType.toLowerCase().contains("int")) {
            javaType = "Integer";
        }
        if (filedType.toLowerCase().contains("time") || filedType.toLowerCase().contains("date")) {
            javaType = "Date";
        }
        if (filedType.toLowerCase().contains("double") || filedType.toLowerCase().contains("decimal")) {
            javaType = "Double";
        }
        if (filedType.toLowerCase().contains("float")) {
            javaType = "Float";
        }
        return javaType;
    }

    public static String formatDBType(String filedType) {
        String javaType = "VARCHAR";
        if (filedType.toLowerCase().contains("int")) {
            javaType = "TINYINT";
        } else if (filedType.toLowerCase().contains("date")) {
            javaType = "DATE";
        } else if (filedType.toLowerCase().contains("timestamp")) {
            javaType = "TIMESTAMP";
        } else if (filedType.toLowerCase().contains("time")) {
            javaType = "TIME";
        } else if (filedType.toLowerCase().contains("double") || filedType.toLowerCase().contains("decimal")) {
            javaType = "DECIMAL";
        } else if (filedType.toLowerCase().contains("float")) {
            javaType = "FLOAT";
        }
        return javaType;
    }
}
