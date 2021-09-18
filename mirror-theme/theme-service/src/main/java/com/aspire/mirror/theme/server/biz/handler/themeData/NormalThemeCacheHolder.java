package com.aspire.mirror.theme.server.biz.handler.themeData;

import com.aspire.mirror.common.constant.Constant;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.theme.server.dao.BizThemeDao;
import com.aspire.mirror.theme.server.dao.BizThemeDimDao;
import com.aspire.mirror.theme.server.dao.po.BizTheme;
import com.aspire.mirror.theme.server.dao.po.BizThemeDim;
import com.google.common.collect.Maps;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 一般处理主题缓存
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.process
 * 类名称:    ComplicateThemeCacheHolder.java
 * 类描述:    一般处理主题缓存
 * 创建人:    JinSu
 * 创建时间:  2019/6/25 16:56
 * 版本:      v1.0
 */
@Component
@ConditionalOnProperty(value = "middleware.configuration.switch.redis", havingValue = "true")
public class NormalThemeCacheHolder extends ThemeCacheHolder {
    @Autowired(required = false)
    private RedissonClient redissonClient;

    @Autowired
    private BizThemeDao bizThemeDao;

    @Autowired
    private BizThemeDimDao bizThemeDimDao;

    @Override
    public void initLoad() {
            Map<String, BizTheme> themeMap = (Map<String, BizTheme>) redissonClient.getBucket
                    (Constant.KEY_THEME_LIST).get();
            Map<String, List<BizThemeDim>> themeDimMap = (Map<String, List<BizThemeDim>>) redissonClient.getBucket
                    (Constant.KEY_THEMEDIM_LIST).get();

            this.updateThemeAndDimMap(themeMap, themeDimMap);
    }

    /**
     * 定时刷新到 数据库. <br/>
     * <p>
     * 作者： pengguihua
     */
//    @PreDestroy
//    @Scheduled(cron = "${genThemeCache.cron}")
    public void flush() {
        // 如果未存在更改, 直接返回

//        W_LOCK.lock();
        try {
            RBucket<String> alarmFlag = redissonClient.getBucket(Constant.KEY_THEME_FRESHTIME);
            LOGGER.info("AutoCacheTask[genThemeCache] freshTime is {}" + alarmFlag.get());
            Date now = new Date();
            BizTheme param = new BizTheme();
            // 过滤条件：业务系统
//        param.setObjectId(sysCode);
            param.setObjectType("2");
            // 过滤条件：为日志类型主题
            param.setUpType("1");
            // 过滤条件：主题上报开关开启
            param.setUpSwitch("0");
            if (!StringUtils.isEmpty(alarmFlag.get())) {
                param.setUpdateTime(DateUtil.getDate(alarmFlag.get(), DateUtil.DATE_TIME_FORMAT));
            } else {
                // 没有存更新时间，首次加载，只取正式数据
                param.setDeleteFlag("0");
            }
            List<BizTheme> bizThemeList = bizThemeDao.select(param);

            if (!CollectionUtils.isEmpty(bizThemeList)) {
                RBucket<Map<String, BizTheme>> themeFlag = redissonClient.getBucket
                        (Constant.KEY_THEME_LIST);
                Map<String, BizTheme> mapTheme = themeFlag.get();
                RBucket<Map<String, List<BizThemeDim>>> themeDimFlag = redissonClient.getBucket(Constant
                        .KEY_THEMEDIM_LIST);
                Map<String, List<BizThemeDim>> themeDimMap = themeDimFlag.get();
                if (mapTheme == null) {
                    mapTheme = Maps.newHashMap();
                }
                if (themeDimMap == null) {
                    themeDimMap = Maps.newHashMap();
                }
                for (BizTheme bizTheme : bizThemeList) {
                    if (bizTheme.getDeleteFlag().equals("0")) {
                        BizThemeDim dimParam = new BizThemeDim();
                        dimParam.setThemeId(bizTheme.getThemeId());
                        dimParam.setMatchFlag("1");
                        List<BizThemeDim> bizThemeDimList = bizThemeDimDao.select(dimParam);
                        themeDimMap.put(bizTheme.getThemeId(), bizThemeDimList);
                        mapTheme.put(bizTheme.getThemeId(), bizTheme);
                    } else {
                        themeDimMap.remove(bizTheme.getThemeId());
                        mapTheme.remove(bizTheme.getThemeId());
                    }
                }
                themeFlag.set(mapTheme);
                themeDimFlag.set(themeDimMap);
                this.updateThemeAndDimMap(mapTheme, themeDimMap);
                LOGGER.info("AutoCacheTask[genThemeCache] themeFlag is {} themeDimFlag is {}", themeFlag.get(),
                        themeDimFlag.get());
            }

            // 设置刷新时间
            alarmFlag.set(DateUtil.format(now, DateUtil.DATE_TIME_FORMAT));
        } catch (Throwable e) {
            LOGGER.error("ThemeCacheHolder[fresh] Error to sync theme list.", e);
        } finally {
//            W_LOCK.unlock();
        }
    }
}
