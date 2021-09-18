package com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.service;

import com.aspire.mirror.indexadapt.adapt.IndexDataListAdapter;
import com.aspire.mirror.indexadapt.adapt.IndexDataListAdapter.StandardIndex;
import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model.BizThemeDim;
import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model.Items;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 业务告警业务类    <br/>
 * Project Name:index-proxy
 * File Name:BizMonitorIndexService.java
 * Package Name:com.aspire.mirror.indexadapt.adapt.migubizmonitordb.service
 * ClassName: BizMonitorIndexService <br/>
 * date: 2018年10月9日 下午6:13:01 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Slf4j
@Service
@ConditionalOnProperty("indexAdapter.mirrorbizmonitorDb.switch")
public class MirrorBizMonitorIndexService extends AbstractMirrorBizMonitor {
    private static ExecutorService threadService = initExecutorService();

    private static ExecutorService initExecutorService() {
        return new ThreadPoolExecutor(50, 100, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5000),
                new ThreadFactory() {
                    private final AtomicInteger inc = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, "businessTriggerTask_" + inc.getAndIncrement());
                    }
                });
    }


    /**
     * list所有业务指标定义. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @return
     */
    public List<Items> listAllBizMonitorTriggers() {
        return bizMonitorDao.listActiveBizTriggers();
    }

    /**
     * 执行新版数据检测
     */
    public void toDoCheckNew(Items bizItem, final List<IndexDataListAdapter.StandardIndex> holder, final
    CountDownLatch latch) {
        log.info("=====【{}】指标处理开始======", bizItem.getName());
        try {
            Items item = bizItem;
            if (StringUtils.isEmpty(item.getBizThemeId())) {
                return;
            }
            final String queryField = bizItem.getBizThemeId();
            final String zbxJson = item.getBizCalcObj();
            if (StringUtils.isEmpty(zbxJson)) {
                log.error(item.getName() + "指标项为空！");
                return;
            }
            final JSONArray zbxArray = JSONArray.fromObject(zbxJson);
            if (zbxArray == null || zbxArray.size() == 0) {
                return;
            }

            // 指标计算开始时间
            final Date logStartTime = new Date();
            // 记录指标的计算结果值
            final Map<String, Map<String, Object>> itemValueMap = Maps.newHashMap();
//            // init 指标计算结果值
//            for (String objectId : item.getObjectIds()) {
//                itemValueMap.put(objectId, null);
//            }
            // 分组字段
            JSONArray bizGroup = new JSONArray();
            if (StringUtils.isNotEmpty(item.getBizGroup())) {
                bizGroup = JSONArray.fromObject(item.getBizGroup());
            }
            String itemName = "";
            Long total = 0l;// 所有指标的总条数
            for (int i = 0; i < zbxArray.size(); i++) {
                final JSONObject itemJsonTmp = zbxArray.getJSONObject(i);
                if (itemJsonTmp == null) {
                    log.error("itemJson为空！");
                    return;
                }
                if (i == 0) {
                    itemName = "zbxA";
                } else if (i == 1) {
                    itemName = "zbxB";
                } else if (i == 2) {
                    itemName = "zbxC";
                } else {
                    itemName = "zbxD";
                }
                final JSONObject itemJson = itemJsonTmp.getJSONObject(itemName);
                // 统计字段及方法 0求和 1计数 2最大 3最小 4平均
                final String staticsType = itemJson.getString("staticsType");
                final String staticsField = itemJson.getString("staticsField");
                final JSONArray fieldArray = itemJson.getJSONArray("field");
                final JSONArray dateArray = itemJson.getJSONArray("date");
                if (staticsType == null || fieldArray == null || dateArray == null) {
                    log.error("itemJson 属性为空！");
                    return;
                }
                Map<String, Object> termMap = null;

                if (item.getGroupFlag() == null || item.getGroupFlag().equals("1")) {
                    // Map<String,List<String, Object>>  {obj : [aa:aa, bb:bb, value: map]}
                    termMap = elasticHelper.termsItemMap(item,
                            item.getBizIndex(), null, fieldArray, dateArray, null, queryField, staticsField, bizGroup);
                    log.debug("item id is " + item.getItemId() + "termMap is" +  termMap);
                    for (String key : termMap.keySet()) {
                        // 将每个监控项结果计算出来
                        getItemValueMap(itemName, staticsType, (List<Map<String, Object>>) termMap.get(key));
                        List<Map<String, Object>> listItem = (List<Map<String, Object>>) termMap.get(key);
                        if (listItem != null) {
                            for (Map<String, Object> objValue : listItem) {
                                List<String> groupKeyList = Lists.newArrayList();
                                List<JSONObject> groupDescArray = Lists.newArrayList();
                                for (int j = 0; j < bizGroup.size(); j++) {
                                    String groupItem = bizGroup.getJSONObject(j).getString("code");
                                    groupKeyList.add((String) objValue.get(groupItem));
                                    JSONObject groupDescItem = new JSONObject();
                                    groupDescItem.put("code", bizGroup.getJSONObject(j).get("code"));
                                    groupDescItem.put("name", bizGroup.getJSONObject(j).get("name"));
                                    groupDescItem.put("value", objValue.get(groupItem));
                                    groupDescArray.add(groupDescItem);
                                }
                                String groupKey = Joiner.on("|").join(groupKeyList);
                                if (itemValueMap.get(key) == null) {
                                    Map<String, Object> itemMap = Maps.newHashMap();
                                    Map<String, Object> subItemMap = Maps.newHashMap();
                                    subItemMap.put(itemName, objValue.get(itemName));
                                    subItemMap.put("groupDesc", groupDescArray);
                                    subItemMap.put("theme_content", objValue.get("theme_content"));
                                    itemMap.put(groupKey, subItemMap);
                                    itemValueMap.put(key, itemMap);
                                } else {
                                    Map<String, Object> itemMap = itemValueMap.get(key);
                                    if (itemMap.get(groupKey) != null) {
                                        Map<String, Object> subItemMap = (Map<String, Object>) itemMap.get(groupKey);
                                        subItemMap.put(itemName, objValue.get(itemName));
                                    } else {
                                        Map<String, Object> subItemMap = Maps.newHashMap();
                                        subItemMap.put(itemName, objValue.get(itemName));
                                        subItemMap.put("groupDesc", groupDescArray);
                                        subItemMap.put("theme_content", objValue.get("theme_content"));
                                        itemMap.put(groupKey, subItemMap);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            log.debug("主题指标计算结果Map: {}" + itemValueMap);
//            if (itemValueMap.size() == 0) {
//                final Float result = 0f;
//                elasticHelper.insertEs(item, StringUtils.join(item.getObjectIds(), ","), null, null, null, result,
// logStartTime);
//                if ("0".equals(item.getBizIsZero())) {// 空值监控
//                    holder.add(parse2StandardIdx(item, StringUtils.join(item.getObjectIds(), ","), null, null,
// null, result));
//                }
//            } else {
            for (String key : item.getObjectIds()) {
                key = key.toUpperCase();
                // key objectId
                if (itemValueMap.get(key) == null) {// 没数据
                    final Float result = 0f;
                    if ("0".equals(item.getBizIsZero())) {// 空值监控
                        elasticHelper.insertEs(item, key, null, null, null, result, logStartTime);
                        holder.add(parse2StandardIdx(item, key, null, null, null, result));
                    }
                } else {
                    // {objectId: {groupKey: {zbxA: 12, zbxB: 11, groupDesc: [name: aa, code: bb, value:
                    // cc]，"theme_content"}}
                    for (String itemKey : itemValueMap.get(key).keySet()) {
                        // itemKey groupKey
                        Map<String, Object> itemMap = itemValueMap.get(key);
                        // {zbxA: 12, zbxB: 11, groupDesc: [name: aa, code: bb, value: cc]}
                        Map<String, Object> subItemMap = (Map<String, Object>) itemMap.get
                                (itemKey);
//                        for (String subItemKey : subItemMap.keySet()) {
                        try {
                            String exp = item.getBizCalcExp();
                            if (exp.contains("A")) {
                                exp = exp.replaceAll("A", (String) subItemMap.get("zbxA"));
                            }
                            if (exp.contains("B")) {
                                exp = exp.replaceAll("B", (String) subItemMap.get("zbxB"));
                            }
                            if (exp.contains("C")) {
                                exp = exp.replaceAll("C", (String) subItemMap.get("zbxC"));
                            }
                            if (exp.contains("D")) {
                                exp = exp.replaceAll("D", (String) subItemMap.get("zbxD"));
                            }
                            final Float result = Float.valueOf(jse.eval(exp).toString());
                            Object groupDesc = subItemMap.get("groupDesc");
//                    saveToDb(item, String.valueOf(result), logStartTime);
//                                if (!CollectionUtils.isEmpty(item.getDimList())) {
//                                   ;
//                                    for (BizThemeDim bizThemeDim : item.getDimList()) {
//                                        bizThemeDim.setValue();
//                                        remarkList.add(bizThemeDim);
//                                    }
//                                }
                            String remark = (String) subItemMap.get("theme_content");
                            elasticHelper.insertEs(item, key, itemKey, groupDesc, remark, String.valueOf(result),
                                    logStartTime);
                            holder.add(parse2StandardIdx(item, key, itemKey, groupDesc, remark, result));
                        } catch (Exception e) {
                            log.error("表达式计算错误", e);
                        }
//                        }
                    }
                }
            }
//            }
            log.info("=====【{}】指标处理结束======", bizItem.getName());
        } catch (final Exception e) {
            log.error(bizItem.getKey() + ",指标计算失败！", e);
        } finally {
            latch.countDown();
        }
    }

    /**
     * 抓取. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param rawIndexList
     * @return
     */
    public List<StandardIndex> fetchAndAdapt2StandardIdxs(List<Items> rawIndexList) {
        // 获取当前时间
        final Calendar c = Calendar.getInstance();
        final Integer day = c.get(Calendar.DAY_OF_YEAR);
        final Integer minute = c.get(Calendar.MINUTE);
        final Integer hour = c.get(Calendar.HOUR);
        final List<StandardIndex> resultList = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(rawIndexList.size());
        for (Items item : rawIndexList) {
            try {
                // 数据到达时
                if ("1".equals(item.getCalcType())) {
                    threadService.execute(new ThreadCheck(item, resultList, latch));
                    continue;
                }
                Integer delay = item.getDelay();
                // 检测单位为小时
                if (delay > 60 && delay < 24 * 60 && delay % 60 == 0) {
                    final Integer lastHourValue = (delay / 60);

                    if (0 == hour && 0 == minute && 24 % lastHourValue == 0) {
                        // 执行任务
                        threadService.execute(new ThreadCheck(item, resultList, latch));
                        continue;
                    }
                    if (0 != hour && 0 == minute && hour % lastHourValue == 0) {
                        // 执行任务
                        threadService.execute(new ThreadCheck(item, resultList, latch));
                        continue;
                    }

                } else if (delay < 60) {
                    // 检测单位为分
                    final Integer lastMinValue = delay;
                    if (0 == hour) {
                        if (0 == minute && 60 % lastMinValue == 0) {
                            // 执行任务
                            threadService.execute(new ThreadCheck(item, resultList, latch));
                            continue;
                        }
                        if (0 != minute && minute % lastMinValue == 0) {
                            // 执行任务
                            threadService.execute(new ThreadCheck(item, resultList, latch));
                            continue;
                        }
                    } else {
                        if (minute % lastMinValue == 0) {
                            // 执行任务
                            threadService.execute(new ThreadCheck(item, resultList, latch));
                            continue;
                        }
                    }
                } else if (delay > 24 * 60 && delay % (24 * 60) == 0) {
                    // 检测单位为天day
                    final Integer lastMinValue = delay / (24 * 60);
                    if (0 == hour && 0 == minute && day % lastMinValue == 0) {
                        // 执行任务
                        threadService.execute(new ThreadCheck(item, resultList, latch));
                        continue;
                    }
                }
                // 如果以上条件都不满足, 仍然需要count down
                latch.countDown();
            } catch (Throwable e) {
                log.error("Exeception when adapt business items to StandardIdxs.", e);
                latch.countDown();    // count down, 防止线程资源无法释放
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("Exeception when fetchAndAdapt business items to StandardIdxs.", e);
        }
        return resultList;
    }

    private class ThreadCheck implements Runnable {
        private final Items bizItem;
        private final List<IndexDataListAdapter.StandardIndex> holder;
        private final CountDownLatch latch;

        public ThreadCheck(final Items bizItem, final List<IndexDataListAdapter.StandardIndex> holder, final
        CountDownLatch latch) {
            this.bizItem = bizItem;
            this.holder = holder;
            this.latch = latch;
        }

        @Override
        public void run() {
            if (bizItem.getSysType().equals("THEME")) {
                toDoCheckNew(bizItem, holder, latch);
            } else {
                latch.countDown();
            }
        }
    }


    /**
     * 计算指标值
     *
     * @return
     */
//    private String getItemValue(String staticsType, Map<String, Long> termMap) {
//        String itemValue = "";
//        final Long totalResult = termMap.get("response.hits.total");
//        if (null == totalResult) {
//            return itemValue;
//        }
//        // 求和
//        if ("0".equals(staticsType)) {
//            final Set<String> set = termMap.keySet();
//            Float sum = 0f;
//            for (final String value : set) {
//                try {
//                    sum += Float.valueOf(value) * Float.valueOf(termMap.get(value));
//                } catch (final Exception e) {
//                    continue;
//                }
//            }
//            itemValue = String.valueOf(sum);
//        }
//        // 计数
//        if ("1".equals(staticsType)) {
//            itemValue = String.valueOf(totalResult);
//        }
//
//        // 最大
//        if ("2".equals(staticsType)) {
//            Float maxValue = 0f;
//            final Set<String> set = termMap.keySet();
//            final List<Float> valueList = new ArrayList<Float>();
//            for (final String value : set) {
//                try {
//                    valueList.add(Float.valueOf(value));
//                } catch (final Exception e) {
//                    continue;
//                }
//            }
//            if (!valueList.isEmpty()) {
//                maxValue = Collections.max(valueList);
//            }
//            itemValue = String.valueOf(maxValue);
//        }
//
//        // 最小
//        if ("3".equals(staticsType)) {
//            Float minValue = 0f;
//            final Set<String> set = termMap.keySet();
//            final List<Float> valueList = new ArrayList<Float>();
//            for (final String value : set) {
//                try {
//                    valueList.add(Float.valueOf(value));
//                } catch (final Exception e) {
//                    continue;
//                }
//            }
//            if (!valueList.isEmpty()) {
//                minValue = Collections.min(valueList);
//            }
//            itemValue = String.valueOf(minValue);
//        }
//        // 平均
//        if ("4".equals(staticsType)) {
//            Float totalValue = 0f;
//            String avgValue = "0";
//            final Set<String> set = termMap.keySet();
//            for (final String value : set) {
//                try {
//                    final float floatValue = Float.valueOf(value);
//                    totalValue += floatValue * termMap.get(value);
//                } catch (final Exception e) {
//                    continue;
//                }
//            }
//            if (0L != totalResult) {
//                final DecimalFormat decimalFormat = new DecimalFormat("0.00");
//                avgValue = decimalFormat.format(totalValue / (float) totalResult);
//            }
//            itemValue = String.valueOf(avgValue);
//        }
//        return itemValue;
//    }

}
