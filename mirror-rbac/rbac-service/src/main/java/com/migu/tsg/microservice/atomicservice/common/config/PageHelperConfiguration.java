package com.migu.tsg.microservice.atomicservice.common.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.pagehelper.PageHelper;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.common.config <br>
 * 类名称: PageHelperConfiguration.java <br>
 * 类描述: mybatis分页插件配置 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月31日上午10:29:49 <br>
 * 版本: v1.0
 */
@Configuration
public class PageHelperConfiguration {

    /**
     * 配置分页
     * @return return
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        // 通过设置pageSize=0或者RowBounds.limit = 0就会查询出全部的结果。
        p.setProperty("pageSizeZero", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
