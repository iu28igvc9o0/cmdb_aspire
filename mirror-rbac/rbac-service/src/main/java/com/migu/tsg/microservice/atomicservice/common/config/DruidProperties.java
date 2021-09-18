package com.migu.tsg.microservice.atomicservice.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.common.config <br>
 * 类名称: DruidProperties.java <br>
 * 类描述: 连接池属性对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月24日下午7:01:10 <br>
 * 版本: v1.0
 */
@Data
@ConfigurationProperties(prefix = "druid")
public class DruidProperties {
    /**
     * 数据库URL
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 连接池中初始化连接数量
     */
    private int initialSize;

    /**
     * 连接池中最小空闲连接数量
     */
    private int minIdle;

    /**
     * 连接池中最大活动连接数量
     */
    private int maxActive;

    /**
     * 连接超时时间(以毫秒为单位)
     */
    private long maxWait;

    /**
     * 验证连接是否可用的SQL语句
     */
    private String validationQuery;

    /**
     * 是否再连接借出去时测试该连接是否可用
     */
    private boolean testOnBorrow;

}
