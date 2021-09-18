package com.aspire.ums.cmdb.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnProperty(name = "mybatis.refresh", havingValue = "true")
@Component
@Slf4j
public class MybatisMapperRefresh {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    private Resource[] mapperLocations;

    private String packageSearchPath = "classpath*:com/aspire/**/mapper/*.xml";

    private boolean isEnable = true; // 是否启用刷新线程

    private int deplaySeconds = 3; // 刷新延迟

    private List<String> changeList;

    private HashMap<String, Long> fileMapping = new HashMap<String, Long>();// 记录文件是否变化

    private static volatile boolean stop = false;

    /**
     * 初始化方法
     */
    @PostConstruct
    public void init() {
        if (!isEnable) {
            return;
        }

        final MybatisMapperRefresh ruRefresh = this;
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!stop) {
                    try {
                        ruRefresh.refreshMapper();
                    } catch (Exception e1) {
                        log.error("刷新失败");
                    }

                    try {
                        Thread.sleep(deplaySeconds * 1000);
                    } catch (InterruptedException e) {
                        log.error("刷新失败");
                    }
                }
            }
        }, "Thread-Mybatis-Refresh").start();

    }

    public void refreshMapper() {
        try {
            Configuration configuration = this.sqlSessionFactory.getConfiguration();

            try {
                this.scanMapperXml();
            } catch (IOException e) {
                log.error("扫描包路径配置错误");
                return;
            }

            if (this.isChanged()) {
                this.removeConfig(configuration);

                for (Resource configLocation : mapperLocations) {
                    InputStream iss = null;
                    try {
                        // hangfang 2020.07.30 资源未释放：流
                        iss = configLocation.getInputStream();
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(iss, configuration,
                                configLocation.toString(), configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                        if (changeList.contains(configLocation.getFilename())) {
                            log.info("[" + configLocation.getFilename() + "] refresh finished");
                        }
                    } catch (IOException e) {
                        log.error("[" + configLocation.getFilename() + "] error");
                        continue;
                    } finally {
                        if (iss != null) {
                            try {
                                iss.close();
                            } catch (IOException e) {
                                e.getStackTrace();
                                log.error("关闭流失败");
                            }

                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPackageSearchPath(String packageSearchPath) {
        this.packageSearchPath = packageSearchPath;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 扫描xml文件所在的路径
     *
     * @throws IOException
     */
    private void scanMapperXml() throws IOException {
        this.mapperLocations = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
    }

    /**
     * 清空Configuration中几个重要的缓存
     *
     * @param configuration
     * @throws Exception
     */
    private void removeConfig(Configuration configuration) throws Exception {
        Class<?> classConfig = configuration.getClass();
        clearMap(classConfig, configuration, "mappedStatements");
        clearMap(classConfig, configuration, "caches");
        clearMap(classConfig, configuration, "resultMaps");
        clearMap(classConfig, configuration, "parameterMaps");
        clearMap(classConfig, configuration, "keyGenerators");
        clearMap(classConfig, configuration, "sqlFragments");

        clearSet(classConfig, configuration, "loadedResources");

    }

    private void clearMap(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
        Field field = classConfig.getDeclaredField(fieldName);
//        field.setAccessible(true);
        ReflectionUtils.makeAccessible(field);
        Map mapConfig = (Map) field.get(configuration);
        mapConfig.clear();
    }

    private void clearSet(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
        Field field = classConfig.getDeclaredField(fieldName);
//        field.setAccessible(true);
        ReflectionUtils.makeAccessible(field);
        Set setConfig = (Set) field.get(configuration);
        setConfig.clear();
    }

    /**
     * 判断文件是否发生了变化
     *
     * @param resource
     * @return
     * @throws IOException
     */
    private boolean isChanged() throws IOException {
        boolean flag = false;
        changeList = new ArrayList<String>();
        for (Resource resource : mapperLocations) {
            String resourceName = resource.getFilename();

            boolean addFlag = !fileMapping.containsKey(resourceName);// 此为新增标识

            // 修改文件:判断文件内容是否有变化
            Long compareFrame = fileMapping.get(resourceName);
            long lastFrame = resource.contentLength() + resource.lastModified();
            boolean modifyFlag = null != compareFrame && compareFrame.longValue() != lastFrame;// 此为修改标识

            // 新增或是修改时,存储文件
            if (addFlag || modifyFlag) {
                fileMapping.put(resourceName, Long.valueOf(lastFrame));// 文件内容帧值
                flag = true;
                changeList.add(resource.getFilename());
            }
        }
        return flag;
    }

    public int getDeplaySeconds() {
        return deplaySeconds;
    }

    public void setDeplaySeconds(int deplaySeconds) {
        this.deplaySeconds = deplaySeconds;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

}
