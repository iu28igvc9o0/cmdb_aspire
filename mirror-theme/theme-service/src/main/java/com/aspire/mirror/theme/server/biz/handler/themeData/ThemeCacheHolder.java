package com.aspire.mirror.theme.server.biz.handler.themeData;

import com.aspire.mirror.theme.server.dao.po.BizTheme;
import com.aspire.mirror.theme.server.dao.po.BizThemeDim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 扫描游标hodler类   <br/>
 * Project Name:zabbix-integrate
 * File Name:AlertScanIndexHolder.java
 * Package Name:com.aspire.mirror.zabbixintegrate.biz
 * ClassName: AlertScanIndexHolder <br/>
 * date: 2018年10月17日 下午7:23:59 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
public abstract class ThemeCacheHolder {
    Logger LOGGER = LoggerFactory.getLogger(ThemeCacheHolder.class);


    //    private volatile Map<String, String> flagMap = new HashMap<>();        // 扫描游标是否更改标记
    protected volatile Map<String, BizTheme> themeMap = new HashMap<>();       // 系统中缓存的扫描游标

    protected volatile Map<String, List<BizThemeDim>> themeDimMap = new HashMap<>();       // 系统中缓存的扫描游标
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock R_LOCK = LOCK.readLock();
    private static final Lock W_LOCK = LOCK.writeLock();


    /**
     * 从数据库初始化游标缓存值. <br/>
     * <p>
     * 作者： pengguihua
     */
    @PostConstruct
    public abstract void initLoad() ;

    /**
     * 获取当前游标值. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @return
     */
    public Map<String, BizTheme> getThemeMap() {
        R_LOCK.lock();
        try {
            return this.themeMap;
        } finally {
            R_LOCK.unlock();
        }
    }

    public Map<String, List<BizThemeDim>> getThemeDimMap() {
        R_LOCK.lock();
        try {
            return this.themeDimMap;
        } finally {
            R_LOCK.unlock();
        }
    }

    /**
     * 更新游标. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param map
     */
    public void updateThemeAndDimMap(Map<String, BizTheme> map, Map<String, List<BizThemeDim>> dimMap) {
        W_LOCK.lock();
        try {
            this.themeMap = map;
            this.themeDimMap = dimMap;
//                flagMap = map;
        } finally {
            W_LOCK.unlock();
        }
    }

    /**
     * 定时刷新到 数据库. <br/>
     * <p>
     * 作者： pengguihua
     */
    @PreDestroy
    @Scheduled(cron = "${genThemeCache.cron}")
    public abstract void flush();
}
