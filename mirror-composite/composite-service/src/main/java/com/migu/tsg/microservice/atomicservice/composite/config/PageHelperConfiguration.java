package com.migu.tsg.microservice.atomicservice.composite.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.pagehelper.PageHelper;

/**
* 项目名称: rbac-service
* 包: com.migu.tsg.microservice.atomicservice.rbac.config
* 类名称: PageHelperConfiguration.java
* 类描述: mybatis分页插件配置
* 创建人: WangSheng
* 创建时间: 2017年7月31日上午10:29:49
* 版本: v1.0
*/
@Configuration
public class PageHelperConfiguration {

    
    /**
    * pageHelper:(分页). <br/>
    *
    * 作者： WangSheng
    * @return
    */
    @Bean
    public PageHelper pageHelper() {
        final PageHelper pageHelper = new PageHelper();
        final Properties props = new Properties();
        props.setProperty("offsetAsPageNum", "true");
        props.setProperty("rowBoundsWithCount", "true");
        props.setProperty("reasonable", "true");
        //通过设置pageSize=0或者RowBounds.limit = 0就会查询出全部的结果。
        props.setProperty("pageSizeZero", "true");
        pageHelper.setProperties(props);
        return pageHelper;
    }
}

