package com.aspire.mirror.alert.server.biz.helper;

import com.aspire.mirror.alert.server.dao.common.AlertScheduleIndexDao;
import com.aspire.mirror.alert.server.vo.common.AlertScheduleIndexVo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AlertScheduleIndexHelper {
    private final ConcurrentHashMap<String, Map<String, String>> scheduleIndexCache = new ConcurrentHashMap<>();

    @Resource
    private AlertScheduleIndexDao alertScheduleIndexDao;

    /**
     * 给告警转换枚举数据
     * @param alert
     */
    public void pushDictAlert (Map<String, Object> alert) {
        if (CollectionUtils.isEmpty(alert)) {
            return;
        }
        if (scheduleIndexCache.isEmpty()) {
            getAllScheduleIndexs();
        }

        for (Map.Entry<String, Object> entry: alert.entrySet()) {
            String key = entry.getKey();
            String alertValue = MapUtils.getString(alert, key);
            if (StringUtils.isEmpty(alertValue)) {
                continue;
            }
            for (Map.Entry<String, Map<String, String>> entryCache: scheduleIndexCache.entrySet()) {
                String cacheKey = entryCache.getKey();
                if (key.equalsIgnoreCase(cacheKey)) {
                    Map<String, String> cacheValue = entryCache.getValue();
                    if (!CollectionUtils.isEmpty(cacheValue) && cacheValue.containsKey(alertValue)) {
                        alert.put(key, cacheValue.get(alertValue));
                    }
                    break;
                }
            }

        }
    }

    /**
     * 查询所有枚举数据
     */
    private void getAllScheduleIndexs () {
        List<AlertScheduleIndexVo> list = alertScheduleIndexDao.getALLScheduleIndex();
        for (AlertScheduleIndexVo alertScheduleIndexVo : list) {
            String indexType = alertScheduleIndexVo.getIndexType();
            Map<String, String> dictMap = scheduleIndexCache.get(indexType);
            if (dictMap == null) {
                dictMap = Maps.newHashMap();
                scheduleIndexCache.put(indexType, dictMap);
            }
            dictMap.put(alertScheduleIndexVo.getIndexValue(), alertScheduleIndexVo.getIndexName());
        }
    }

    /**
     * 每小时刷新一次数据
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void flushCache () {
        scheduleIndexCache.clear();
    }
}
