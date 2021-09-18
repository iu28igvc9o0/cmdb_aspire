package com.aspire.aiops.dbconfig;

import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * @author ：Vincent Hu
 * @date ：Created in 6/6/2019 14:51
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
public class MybatisUtils {

    private  static SqlSessionFactory sqlSessionFactory=null;
    private  MybatisUtils() {}

    static{
        String resource="main/resource/MybatisConfig.xml";
        try {
            //读取配置文件 获取sqlsessionFactory
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            Configuration configuration = sqlSessionFactory.getConfiguration();
            System.out.println(configuration);
            Environment environment = configuration.getEnvironment();
            System.out.println(environment);
            DataSource dataSource = environment.getDataSource();
            System.out.println(dataSource);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取sqlSession
     * @return
     */
    public static SqlSession getSqlSession(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }

}
