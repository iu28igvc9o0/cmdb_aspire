package com.aspire.aiops.dbconfig;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author ：Vincent Hu
 * @date ：Created in 6/6/2019 14:53
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

public class DruidDataSourceFactory  extends UnpooledDataSourceFactory{

    public DruidDataSourceFactory() {
        this.dataSource = new DruidDataSource();
    }

}
