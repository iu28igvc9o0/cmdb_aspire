package com.aspire.aiops.utils;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.SQLException;
import java.util.Properties;


/**
 * @author ：Vincent Hu
 * @date ：Created in 6/5/2019 17:19
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

public class DbPoolUtil {

    private static DbPoolUtil dbPoolUtil = null;
    private static DruidDataSource druidDataSource = null;

    static {
        try {
            //ResourceUtil.loadResource();
            Properties properties = ResourceUtil.loadResource("jdbc.properties");
            druidDataSource = (DruidDataSource)DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized DbPoolUtil getInstance(){
        if (null == dbPoolUtil){
            dbPoolUtil = new DbPoolUtil();
        }
        return dbPoolUtil;
    }

    public DruidPooledConnection getConnection() throws SQLException {
        return druidDataSource.getConnection();
    }

}
