package com.aspire.mirror.theme.server.biz.handler.themeData;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.theme.server.dao.BizThemeDao;
import com.aspire.mirror.theme.server.dao.BizThemeDimDao;
import com.aspire.mirror.theme.server.dao.po.BizTheme;
import com.aspire.mirror.theme.server.dao.po.BizThemeDim;
import com.google.common.collect.Maps;
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
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.process
 * 类名称:    SimpleThemeCacheHolder.java
 * 类描述:
 * 创建人:    JinSu
 * 创建时间:  2019/6/25 17:02
 * 版本:      v1.0
 */
@Component
@ConditionalOnProperty(value = "middleware.configuration.switch.redis", havingValue = "false")
public class SimpleThemeCacheHolder extends ThemeCacheHolder{
    private static volatile String THEME_FRESHTIME = "";

    @Autowired
    private BizThemeDao bizThemeDao;

    @Autowired
    private BizThemeDimDao bizThemeDimDao;


    @Override
    public void initLoad() {
//        Map<String, BizTheme> themeMap = (Map<String, BizTheme>) redissonClient.getBucket
//                (Constant.KEY_THEME_LIST).get();
//        Map<String, List<BizThemeDim>> themeDimMap = (Map<String, List<BizThemeDim>>) redissonClient.getBucket
//                (Constant.KEY_THEMEDIM_LIST).get();
//
//        this.updateThemeAndDimMap(themeMap, themeDimMap);
        flush();
    }

    @Override
    public void flush() {
        try {
            Date now = new Date();
            BizTheme param = new BizTheme();
            // 过滤条件：业务系统
//        param.setObjectId(sysCode);
            param.setObjectType("2");
            // 过滤条件：为日志类型主题
            param.setUpType("1");
            // 过滤条件：主题上报开关开启
            param.setUpSwitch("0");
            if (!StringUtils.isEmpty(THEME_FRESHTIME)) {
                param.setUpdateTime(DateUtil.getDate(THEME_FRESHTIME, DateUtil.DATE_TIME_FORMAT));
            } else {
                // 没有存更新时间，首次加载，只取正式数据
                param.setDeleteFlag("0");
            }
            List<BizTheme> bizThemeList = bizThemeDao.select(param);

            if (!CollectionUtils.isEmpty(bizThemeList)) {
//                RBucket<Map<String, BizTheme>> themeFlag = redissonClient.getBucket
//                        (Constant.KEY_THEME_LIST);
                Map<String, BizTheme> mapTheme = this.themeMap;
//                RBucket<Map<String, List<BizThemeDim>>> themeDimFlag = redissonClient.getBucket(Constant
//                        .KEY_THEMEDIM_LIST);
                Map<String, List<BizThemeDim>> themeDimMap = this.themeDimMap;
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
                this.updateThemeAndDimMap(mapTheme, themeDimMap);
            }

            // 设置刷新时间
            THEME_FRESHTIME = DateUtil.format(now, DateUtil.DATE_TIME_FORMAT);
        } catch (Throwable e) {
            LOGGER.error("ThemeCacheHolder[fresh] Error to sync theme list.", e);
        } finally {
//            W_LOCK.unlock();
        }
    }
}
