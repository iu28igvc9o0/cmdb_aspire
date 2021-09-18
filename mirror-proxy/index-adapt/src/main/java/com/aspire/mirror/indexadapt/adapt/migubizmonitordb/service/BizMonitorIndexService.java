package com.aspire.mirror.indexadapt.adapt.migubizmonitordb.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.indexadapt.adapt.IndexDataListAdapter.StandardIndex;
import com.aspire.mirror.indexadapt.adapt.migubizmonitordb.dao.MiguBizMonitorIndexDao;
import com.aspire.mirror.indexadapt.adapt.migubizmonitordb.elasticsearch.ElasticSearchHelper;
import com.aspire.mirror.indexadapt.adapt.migubizmonitordb.model.BusinessTrigger;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
@Transactional("migubizmonitorDb_TransactionManager")
@ConditionalOnProperty("indexAdapter.migubizmonitorDb.switch")
public class BizMonitorIndexService {
    private static ExecutorService threadService = initExecutorService();
    private static final ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
    @Autowired
    private MiguBizMonitorIndexDao bizMonitorDao;
    @Autowired
    private ElasticSearchHelper elasticHelper;
    @Value("${local.roomId}")
    private String roomId;

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
    public List<BusinessTrigger> listAllBizMonitorTriggers() {
        return bizMonitorDao.listActiveBizTriggers();
    }

    /**
     * 抓取. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param rawIndexList
     * @return
     */
    public List<StandardIndex> fetchAndAdapt2StandardIdxs(List<BusinessTrigger> rawIndexList) {
        // 获取当前时间
        final Calendar c = Calendar.getInstance();
        final Integer day = c.get(Calendar.DAY_OF_YEAR);
        final Integer minute = c.get(Calendar.MINUTE);
        final Integer hour = c.get(Calendar.HOUR);
        final List<StandardIndex> resultList = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(rawIndexList.size());

        for (BusinessTrigger businessTrigger : rawIndexList) {
            try {
                // 数据到达时
                if ("1".equals(businessTrigger.getCalculationType())) {
                    threadService.execute(new ThreadCheck(businessTrigger, resultList, latch));
                    continue;
                }
                // 检测单位为小时
                if (businessTrigger.getCycleType().equals("1")) {
                    final Integer lastHourValue = Integer.valueOf(businessTrigger.getCycleValue()) % 24;
                    if (0 == lastHourValue) {
                        if (0 == hour && 0 == minute) {
                            // 零点执行任务
                            threadService.execute(new ThreadCheck(businessTrigger, resultList, latch));
                            continue;
                        }
                    } else {
                        if (0 == hour && 0 == minute && 24 % lastHourValue == 0) {
                            // 执行任务
                            threadService.execute(new ThreadCheck(businessTrigger, resultList, latch));
                            continue;
                        }
                        if (0 != hour && 0 == minute && hour % lastHourValue == 0) {
                            // 执行任务
                            threadService.execute(new ThreadCheck(businessTrigger, resultList, latch));
                            continue;
                        }
                    }
                } else if (businessTrigger.getCycleType().equals("0")) {
                    // 检测单位为分
                    final Integer lastMinValue = Integer.valueOf(businessTrigger.getCycleValue()) % (60 * 24);
                    if (0 == lastMinValue) {
                        if (0 == hour && 0 == minute) {
                            // 零点执行任务
                            threadService.execute(new ThreadCheck(businessTrigger, resultList, latch));
                            continue;
                        }
                    } else {
                        if (0 == hour) {
                            if (0 == minute && (60 * 24) % lastMinValue == 0) {
                                // 执行任务
                                threadService.execute(new ThreadCheck(businessTrigger, resultList, latch));
                                continue;
                            }
                            if (0 != minute && minute % lastMinValue == 0) {
                                // 执行任务
                                threadService.execute(new ThreadCheck(businessTrigger, resultList, latch));
                                continue;
                            }
                        } else {
                            if ((hour * 60 + minute) % lastMinValue == 0) {
                                // 执行任务
                                threadService.execute(new ThreadCheck(businessTrigger, resultList, latch));
                                continue;
                            }
                        }
                    }
                } else if (businessTrigger.getCycleType().equals("2")) {
                    // 检测单位为天day
                    final Integer lastMinValue = Integer.valueOf(businessTrigger.getCycleValue());
                    if (0 == lastMinValue) {
                        if (0 == hour && 0 == minute) {
                            // 零点执行任务
                            threadService.execute(new ThreadCheck(businessTrigger, resultList, latch));
                            continue;
                        }
                    } else {
                        if (0 == hour && 0 == minute && day % lastMinValue == 0) {
                            // 执行任务
                            threadService.execute(new ThreadCheck(businessTrigger, resultList, latch));
                            continue;
                        }
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
            log.error("InterruptedException when await business items to adapt to StandardIdxs.", e);
        }
        return resultList;
    }

    private class ThreadCheck implements Runnable {
        private final BusinessTrigger businessTrigger;
        private final List<StandardIndex> holder;
        private final CountDownLatch latch;

        public ThreadCheck(final BusinessTrigger businessTrigger,
                           final List<StandardIndex> holder, final CountDownLatch latch) {
            this.businessTrigger = businessTrigger;
            this.holder = holder;
            this.latch = latch;
        }

        @Override
        public void run() {
            toDoCheckNew(businessTrigger, holder, latch);
        }
    }

    /**
     * 执行新版数据检测
     */
    public void toDoCheckNew(BusinessTrigger businessTrigger,
                             final List<StandardIndex> holder, final CountDownLatch latch) {
        try {
            final String queryField = "A_" + businessTrigger.getJkzbCode();
            final String zbxJson = businessTrigger.getZbxJson();
            if (StringUtils.isEmpty(zbxJson) || StringUtils.isEmpty(queryField)) {
                log.error(businessTrigger.getTriggerName() + "指标项为空！");
                return;
            }
            final JSONArray zbxArray = JSONArray.fromObject(zbxJson);
            if (zbxArray == null || zbxArray.size() == 0) {
                return;
            }

            // 记录指标的计算结果值
            final List<String> itemValueList = new ArrayList<String>();
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
                final JSONArray fieldArray = itemJson.getJSONArray("field");
                final JSONArray dateArray = itemJson.getJSONArray("date");
                if (staticsType == null || fieldArray == null || dateArray == null) {
                    log.error("itemJson 属性为空！");
                    return;
                }
                final Map<String, Long> termMap = elasticHelper.termsItemMap(businessTrigger,
                        businessTrigger.getTriggerIndex(), null, fieldArray, dateArray, null, queryField);
                total = total + termMap.get("response.hits.total");
                final String itemValue = getItemValue(staticsType, termMap);
                itemValueList.add(itemValue);
            }

            String exp = businessTrigger.getMonitorStatics();
            if (exp.contains("A")) {
                exp = exp.replaceAll("A", itemValueList.get(0));
            }
            if (exp.contains("B")) {
                exp = exp.replaceAll("B", itemValueList.get(1));
            }
            if (exp.contains("C")) {
                exp = exp.replaceAll("C", itemValueList.get(2));
            }

            if (total == 0) {// 没数据
                final Float result = 0f;
                if ("0".equals(businessTrigger.getIsZero())) {// 空值监控
                    holder.add(parse2StandardIdx(businessTrigger, result));
                }
            } else {
                final Float result = Float.valueOf(jse.eval(exp).toString());
                holder.add(parse2StandardIdx(businessTrigger, result));
            }
        } catch (final Exception e) {
            log.error(businessTrigger.getKeyName() + ",指标计算失败！", e);
        } finally {
            latch.countDown();
        }
    }

    private StandardIndex parse2StandardIdx(BusinessTrigger businessTrigger, Object value) {
        StandardIndex standardIndex = new StandardIndex();
        standardIndex.setObjectType("2");
        standardIndex.setObjectId(businessTrigger.getSysCode());
        standardIndex.setRoomId(roomId);
        standardIndex.setItemId(businessTrigger.getItem_id());
        standardIndex.setClock(Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())).intValue());
        standardIndex.setValue(String.valueOf(value));
        return standardIndex;
    }

    /**
     * 计算指标值
     *
     * @return
     */
    private String getItemValue(String staticsType, Map<String, Long> termMap) {
        String itemValue = "";
        final Long totalResult = termMap.get("response.hits.total");
        if (null == totalResult) {
            return itemValue;
        }
        // 求和
        if ("0".equals(staticsType)) {
            final Set<String> set = termMap.keySet();
            Float sum = 0f;
            for (final String value : set) {
                try {
                    sum += Float.valueOf(value) * Float.valueOf(termMap.get(value));
                } catch (final Exception e) {
                    continue;
                }
            }
            itemValue = String.valueOf(sum);
        }
        // 计数
        if ("1".equals(staticsType)) {
            itemValue = String.valueOf(totalResult);
        }

        // 最大
        if ("2".equals(staticsType)) {
            Float maxValue = 0f;
            final Set<String> set = termMap.keySet();
            final List<Float> valueList = new ArrayList<Float>();
            for (final String value : set) {
                try {
                    valueList.add(Float.valueOf(value));
                } catch (final Exception e) {
                    continue;
                }
            }
            if (!valueList.isEmpty()) {
                maxValue = Collections.max(valueList);
            }
            itemValue = String.valueOf(maxValue);
        }

        // 最小
        if ("3".equals(staticsType)) {
            Float minValue = 0f;
            final Set<String> set = termMap.keySet();
            final List<Float> valueList = new ArrayList<Float>();
            for (final String value : set) {
                try {
                    valueList.add(Float.valueOf(value));
                } catch (final Exception e) {
                    continue;
                }
            }
            if (!valueList.isEmpty()) {
                minValue = Collections.min(valueList);
            }
            itemValue = String.valueOf(minValue);
        }
        // 平均
        if ("4".equals(staticsType)) {
            Float totalValue = 0f;
            String avgValue = "0";
            final Set<String> set = termMap.keySet();
            for (final String value : set) {
                try {
                    final float floatValue = Float.valueOf(value);
                    totalValue += floatValue * termMap.get(value);
                } catch (final Exception e) {
                    continue;
                }
            }
            if (0L != totalResult) {
                final DecimalFormat decimalFormat = new DecimalFormat("0.00");
                avgValue = decimalFormat.format(totalValue / (float) totalResult);
            }
            itemValue = String.valueOf(avgValue);
        }
        return itemValue;
    }

}
