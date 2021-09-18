package com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.service;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model.Items;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 业务主题计算指标服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.service
 * 类名称:    BizThemeCalcItemService.java
 * 类描述:    暴露给第三方的业务系统调用主题计算使用接口
 * 创建人:    JinSu
 * 创建时间:  2019/1/25 14:56
 * 版本:      v1.0
 */
@RestController
@RequestMapping("/v1/themeCalcItem")
@Slf4j
@ConditionalOnProperty("indexAdapter.mirrorbizmonitorDb.switch")
public class BizThemeCalcItemService extends AbstractMirrorBizMonitor {
    private static ExecutorService threadService = initExecutorService();


    private static ExecutorService initExecutorService() {
        return new ThreadPoolExecutor(50, 100, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5000),
                new ThreadFactory() {
                    private final AtomicInteger inc = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, "themeCalcTask_" + inc.getAndIncrement());
                    }
                });
    }

    @RequestMapping("/calcItem/{item_id}")
    public Map<String, String> updateForms(@PathVariable(name = "item_id") String itemId, @RequestParam(value =
            "group_time") String groupTime) {
        Map<String, String> result = Maps.newHashMap();
        result.put("success", "0");
        if (StringUtils.isEmpty(itemId)) {
            result.put("reason", "监控项ID为空");
            return result;
        }
        if (StringUtils.isEmpty(groupTime)) {
            result.put("reason", "分组时间为空");
            return result;
        }
        String[] groupTimeArray = groupTime.split(",");
        List<Date> groupTimeList = Lists.newArrayList();
        for (String itemGroupTimeStr : groupTimeArray) {
            Date itemGroupTime = DateUtil.getDate(itemGroupTimeStr, DateUtil.DATE_TIME_FORMAT);
            if (itemGroupTime != null) {
                groupTimeList.add(itemGroupTime);
            }
        }
        if (groupTimeList.size() == 0) {
            result.put("reason", "请检查分组时间格式");
            return result;
        } else {
            final CountDownLatch latch = new CountDownLatch(1);
//            threadService.execute(new BizThemeCalcItemService.ThreadCheck(itemId, groupTimeList, result, latch));
            Future future = threadService.submit(new ThreadCheck(itemId, groupTimeList, result, latch));
            try {
                if (future.get() == null) {
                    return result;
                }
            } catch (InterruptedException e) {
                log.error("Exeception when exec theme calculate items.", e);
            } catch (ExecutionException e) {
                log.error(e.getCause().getMessage());
            }
//            result.put("success", "1");
            latch.countDown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                log.error("Exeception when fetchAndAdapt theme calculate items.", e);
            }
        }
        return result;
    }

    private class ThreadCheck implements Runnable {
        private final String itemId;
        private final List<Date> groupTimeList;
        private final  Map<String, String> result;
        private final CountDownLatch latch;

        public ThreadCheck(final String itemId, final List<Date> groupTimeList, Map<String, String> result, final CountDownLatch latch) {
            this.itemId = itemId;
            this.groupTimeList = groupTimeList;
            this.result = result;
            this.latch = latch;
        }

        @Override
        public void run() {
            Items item = bizMonitorDao.selectByPrimaryKey(itemId);
            Date now = new Date();
            List<Date> groupTime = Lists.newArrayList();
            groupTime.add(now);
            toDoThemeCalc(item, groupTimeList, result, latch);
        }
    }


    /**
     * 指标计算
     */
    public void toDoThemeCalc(Items item, List<Date> groupTime, Map<String, String> result, CountDownLatch latch) {
        try {
            if (StringUtils.isEmpty(item.getBizThemeId())) {
                result.put("reason", item.getName() + "指标的主题ID为空！");
                return;
            }
            final String queryField = item.getBizThemeId();
            final String zbxJson = item.getBizCalcObj();
            if (StringUtils.isEmpty(zbxJson)) {
                result.put("reason", item.getName() + "指标项为空！");
                log.error(item.getName() + "指标项为空！");
                return;
            }
            final JSONArray zbxArray = JSONArray.fromObject(zbxJson);
            if (zbxArray == null || zbxArray.size() == 0) {
                result.put("reason", item.getName() + "指标项为空！");
                return;
            }
            // 指标计算开始时间
            final Date logStartTime = new Date();
            // 记录指标的计算结果值
            Map<String, List<Map<String, Object>>> itemValueMap = Maps.newHashMap();
            Map<String, JSONArray> staticsNameGroupMap = Maps.newHashMap();
            final JSONArray bizGroup = JSONArray.fromObject(item.getBizGroup());
            for (Date itemGroupTime : groupTime) {
                for (int i = 0; i < zbxArray.size(); i++) {
                    final JSONObject itemJson = zbxArray.getJSONObject(i);
                    if (itemJson == null) {
                        log.error("itemJson为空！");
                        result.put("reason", item.getName() + "指标指标项解析出错！");
                        return;
                    }
                    // 指标项名称
                    final String staticsName = itemJson.getString("staticsName");
                    // 统计字段及方法 0求和 1计数 2最大 3最小 4平均
                    final String staticsType = itemJson.getString("staticsType");
                    final String staticsField = itemJson.getString("staticsField");
                    final JSONArray fieldArray = itemJson.getJSONArray("field");
                    final JSONArray dateArray = itemJson.getJSONArray("date");
                    // 分组字段
                    final JSONArray bizGroupArray = itemJson.getJSONArray("bizGroup");
                    if (staticsType == null || fieldArray == null || dateArray == null) {
                        log.error("itemJson 属性为空！");
                        result.put("reason", item.getName() + "指标指标项属性解析为空！");
                        return;
                    }
                    staticsNameGroupMap.put(staticsName, bizGroupArray);
                    List<Map<String, Object>> termMap = elasticHelper.termsThemeCalcItemMap(item,
                            item.getBizIndex(), null, fieldArray, dateArray, null, queryField, staticsField,
                            bizGroupArray, itemGroupTime);

                    getItemValueMap(staticsName, staticsType, termMap);
                    itemValueMap.put(staticsName, termMap);
                }

                // 生成监控项Map 默认第一个监控项结果map 其他结果值塞入
                List<Map<String, Object>> calcMapList = Lists.newArrayList();
                String firstStaticsName = zbxArray.getJSONObject(0).getString("staticsName");
                calcMapList.addAll(itemValueMap.get(firstStaticsName));
                // 循环所有监控项结果map， 匹配放入一个map，方便做计算
                for (String staticsName : itemValueMap.keySet()) {
                    List<Map<String, Object>> mapList = itemValueMap.get(staticsName);
                    JSONArray itemBizGroupMap = staticsNameGroupMap.get(staticsName);
                    for (Map<String, Object> singleCalcMap : mapList) {
                        for (Map<String, Object> map : calcMapList) {
                            boolean compareFlag = true;
                            for (Object o : itemBizGroupMap) {
                                String itemBizGroup = (String) o;
                                if (!map.get(itemBizGroup).equals(singleCalcMap.get(itemBizGroup))) {
                                    compareFlag = false;
                                    break;
                                }
                            }
                            if (compareFlag) {
                                map.put(staticsName, singleCalcMap.get(staticsName));
                            }
                        }
                    }
                }
                String bizThemeExp = item.getBizThemeExp();
                JSONArray expArray = JSONArray.fromObject(bizThemeExp);
                // 先删除指标数据 生成计算结果map 入es
                elasticHelper.deleteEsData(item.getItemId(), itemGroupTime);
                for (Map<String, Object> calcMap : calcMapList) {
                    // 存放入es的结果map
                    Map<String, Object> map = Maps.newHashMap();
                    // 存放入es的数据对应类型
                    Map<String, String> typeMap = Maps.newHashMap();
                    for (int i = 0; i < bizGroup.size(); i++) {
                        String groupItem = bizGroup.getJSONObject(i).getString("code");
                        map.put(groupItem, calcMap.get(groupItem));
                    }
                    for (Object obj : expArray) {
                        JSONObject jsonObject = (JSONObject) obj;
                        String name = jsonObject.getString("code");
                        String exp = jsonObject.getString("exp");
                        String type = jsonObject.getString("type");


                        for (String staticsName : calcMap.keySet()) {
                            exp = exp.replace(staticsName, calcMap.get(staticsName).toString());
                        }

                        String[] expSplit = exp.split(";");
                        List<String> rsList = Lists.newArrayList();
                        for (String expItem : expSplit) {
                            String resultString = "";
                            if (type.equals("0")) {
                                try {
                                    Float floatRes = Float.valueOf(jse.eval(expItem).toString());
                                    if (!floatRes.isNaN()) {
                                        BigDecimal b = new BigDecimal(floatRes);
                                        floatRes = b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
                                        resultString = floatRes.toString();
                                    }
                                } catch (ScriptException e) {
                                    log.error(item.getKey() + "指标，计算项： " + name + "表达式项：" + expItem + "计算失败");
                                }
                            } else {
                                try {
                                    resultString = jse.eval(expItem).toString();
                                } catch (ScriptException e) {
                                    log.error(item.getKey() + "指标，计算项： " + name + "表达式项：" + expItem + "计算失败");
                                }
                            }
                            if (!StringUtils.isEmpty(resultString)) {
                                rsList.add(resultString);
                            }
                        }
                        map.put(name, Joiner.on(",").join(rsList));
                        typeMap.put(name, type);
                    }
                    elasticHelper.insertEs(item, map, typeMap, itemGroupTime, logStartTime);
                }
            }
            result.put("success", "1");
        } catch (Exception e) {
            log.error(item.getKey() + ",指标计算失败！", e);
        } finally {
            latch.countDown();
        }
    }


}
