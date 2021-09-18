package com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.service;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.indexadapt.adapt.IndexDataListAdapter;
import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.dao.MirrorBizMonitorIndexDao;
import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.elasticsearch.ElasticsearchHandler;
import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model.Items;
import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model.MirrorBizMonitorExtendObject;
import com.aspire.mirror.indexadapt.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.service
 * 类名称:    AbstractMirrorBizMonitor.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/7/1 17:43
 * 版本:      v1.0
 */
@Transactional("mirrorbizmonitorDb_TransactionManager")
@ConditionalOnProperty("indexAdapter.mirrorbizmonitorDb.switch")
public abstract class AbstractMirrorBizMonitor {

    @Value("${local.roomId}")
    private String roomId;

    @Autowired
    protected MirrorBizMonitorIndexDao bizMonitorDao;

    @Autowired
    protected ElasticsearchHandler elasticHelper;

    protected static final ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
    protected void getItemValueMap(String staticsName, String staticsType, List<Map<String, Object>> termMap) {
        //求和
        if ("0".equals(staticsType)) {
            for (Map<String, Object> map : termMap) {
                Map<String, Long> valueMap = (Map<String, Long>) map.get("value");
                final Set<String> set = valueMap.keySet();
                Float sum = 0f;
                for (final String value : set) {
                    try {
                        sum += Float.valueOf(value) * Float.valueOf(valueMap.get(value));
                    } catch (final Exception e) {
                        continue;
                    }
                }
                map.put(staticsName, String.valueOf(sum));
            }
        }
        // 计数
        if ("1".equals(staticsType)) {
            for (Map<String, Object> map : termMap) {
                Map<String, Long> valueMap = (Map<String, Long>) map.get("value");
                Long tatal = 0l;
                for (final Long value : valueMap.values()) {
                    try {
                        tatal += value;
                    } catch (final Exception e) {
                        continue;
                    }
                }
                map.put(staticsName, String.valueOf(tatal));
            }
        }

        // 最大
        if ("2".equals(staticsType)) {
            for (Map<String, Object> map : termMap) {
                Map<String, Long> valueMap = (Map<String, Long>) map.get("value");
                Float maxValue = 0f;
                final Set<String> set = valueMap.keySet();
                final List<Float> valueList = new ArrayList<>();
                for (final String value : set) {
                    try {
                        valueList.add(Float.valueOf(value));
                    } catch (final Exception e) {
                        continue;
                    }
                }
                if (!set.isEmpty()) {
                    maxValue = Collections.max(valueList);
                }
                map.put(staticsName, String.valueOf(maxValue));
            }

        }

        // 最小
        if ("3".equals(staticsType)) {
            for (Map<String, Object> map : termMap) {
                Map<String, Long> valueMap = (Map<String, Long>) map.get("value");
                Float minValue = 0f;
                final Set<String> set = valueMap.keySet();
                final List<Float> valueList = new ArrayList<>();
                for (final String value : set) {
                    try {
                        valueList.add(Float.valueOf(value));
                    } catch (final Exception e) {
                        continue;
                    }
                }
                if (!set.isEmpty()) {
                    minValue = Collections.min(valueList);
                }
                map.put(staticsName, String.valueOf(minValue));
            }
        }
        // 平均
        if ("4".equals(staticsType)) {
            for (Map<String, Object> map : termMap) {
                Map<String, Long> valueMap = (Map<String, Long>) map.get("value");
                Float totalValue = 0f;
                String avgValue = "0";
                final Set<String> set = valueMap.keySet();
                Long totalResult = 0l;
                for (final String value : set) {
                    try {
                        totalResult += valueMap.get(value);
                        final float floatValue = Float.valueOf(value);
                        totalValue += floatValue * valueMap.get(value);
                    } catch (final Exception e) {
                        continue;
                    }
                }
                if (0L != totalResult) {
                    final DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    avgValue = decimalFormat.format(totalValue / (float) totalResult);
                }
                map.put(staticsName, String.valueOf(avgValue));
            }

        }
    }

    protected IndexDataListAdapter.StandardIndex parse2StandardIdx(Items item, String objectId, String groupKey, Object groupDesc, String remark, Object value) {
        IndexDataListAdapter.StandardIndex standardIndex = new IndexDataListAdapter.StandardIndex();
        standardIndex.setObjectType(item.getMoniType());
        standardIndex.setObjectId(objectId);
        standardIndex.setRoomId(roomId);
        standardIndex.setRemark(remark);
        // TODO
        standardIndex.setItemId(item.getItemId());
        standardIndex.setClock(Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())).intValue());
        standardIndex.setValue(String.valueOf(value));
        // 设置分组key 和分组详情
        standardIndex.setGroupKey(groupKey);
        standardIndex.setGroupDesc(groupDesc);
        MirrorBizMonitorExtendObject extendObj = new MirrorBizMonitorExtendObject();
        extendObj.setBizSys(item.getObjectIds().get(0));
        standardIndex.setExtendObj(JsonUtil.toJacksonJson(extendObj));
        return standardIndex;
    }

}
