package com.aspire.mirror.elasticsearch.server.controller.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.elasticsearch.server.controller.CommonController;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.server.controller.monitor
 * @Author: baiwenping
 * @CreateTime: 2020-05-06 15:56
 * @Description: ${Description}
 */
public class MonitorCommonController extends CommonController{

    /**
     * 递归处理聚合结果
     * @param jsonObject
     * @param list
     * @param map
     */
    protected void generateAggs (JSONObject jsonObject, List<Map<String, Object>> list, Map<String, Object> map) {
        if (list == null) {
            list = Lists.newArrayList();
        }
        Set<String> keySet = jsonObject.keySet();
        boolean flag = false;
        for (String key: keySet) {
            Object oo = jsonObject.get(key);
            if (!(oo instanceof JSONObject)) {
                if (key.indexOf("|") > -1) {
                    // 此处是为了适配特殊格式的脚本
                    String[] keySplits = key.split("\\|");
                    String[] valueSplits = oo.toString().split("\\|");
                    for (int i = 0; i < keySplits.length; i++) {
                        String keySplit = keySplits[i];
                        if (valueSplits.length > i) {
                            map.put(keySplit, valueSplits[i]);
                        } else {
                            break;
                        }
                    }
                }
                map.put(key, oo);
                continue;
            }
            flag = true;
            JSONObject object = jsonObject.getJSONObject(key);
            Set<String> keySet1 = object.keySet();
            // 底层数据
            if (!keySet1.contains("buckets")) {
                Map<String, Object> mapEnd = Maps.newHashMap();
                for (String keyEnd: keySet1) {
                    Object o = object.get(keyEnd);
                    if (o == null) {
                        mapEnd.put(keyEnd, null);
                    } else if (o instanceof JSONObject) {
                        mapEnd.putAll(object.getJSONObject(keyEnd));
                    } else {
                        if (keyEnd.indexOf("|") > -1) {
                            // 此处是为了适配特殊格式的脚本
                            String[] keySplits = keyEnd.split("\\|");
                            String[] valueSplits = o == null? null : o.toString().split("\\|");
                            for (int i = 0; i < keySplits.length; i++) {
                                String keySplit = keySplits[i];
                                if (valueSplits != null && valueSplits.length > i) {
                                    mapEnd.put(keySplit, valueSplits[i]);
                                } else {
                                    break;
                                }
                            }
                        }
                        mapEnd.put(keyEnd, o);
                    }
                }
                if (map != null) {
                    mapEnd.putAll(map);
                }
                list.add(mapEnd);
                continue;
            }
            JSONArray buckets = object.getJSONArray("buckets");
            int size = buckets.size();
            for (int i = 0; i < size; i++) {
                JSONObject bucket = buckets.getJSONObject(i);
                if (bucket == null) {
                    continue;
                }
                Map<String, Object> mapNew = Maps.newHashMap();
                if (map != null) {
                    mapNew.putAll(map);
                }
                Object keyValue = bucket.get("key");
                if (key.indexOf("|") > -1) {
                    // 此处是为了适配特殊格式的脚本
                    String[] keySplits = key.split("\\|");
                    String[] valueSplits = keyValue == null ? null:keyValue.toString().split("\\|");
                    for (int k = 0; k < keySplits.length; k++) {
                        String keySplit = keySplits[k];
                        if (valueSplits != null && valueSplits.length > k) {
                            mapNew.put(keySplit, valueSplits[k]);
                        } else {
                            break;
                        }
                    }
                }
                mapNew.put(key, bucket.get("key"));
                // 递归处理下一层数据
                generateAggs(bucket, list, mapNew);
            }
        }
        // 如果都没处理，表示最后一层
        if (!flag) {
            Map<String, Object> mapEnd = Maps.newHashMap();
            if (map != null) {
                mapEnd.putAll(map);
            }
            jsonObject.remove("key");
            mapEnd.putAll(jsonObject);
            list.add(mapEnd);
        }
    }

    /**
     * 递归处理聚合结果
     * @param jsonObject
     * @param list
     */
    protected void getTopHis (JSONObject jsonObject, List<Map<String, Object>> list) {
        if (list == null) {
            list = Lists.newArrayList();
        }
        Set<String> keySet = jsonObject.keySet();
        for (String key: keySet) {
            Object oo = jsonObject.get(key);
            if (oo instanceof JSONObject) {
                getTopHis(jsonObject.getJSONObject(key), list);
            } else if (oo instanceof JSONArray) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                if ("hits".equals(key)) {
                    int size = jsonArray.size();
                    for (int i = 0; i < size; i++) {
                        JSONObject hits = jsonArray.getJSONObject(i);
                        if (hits == null) {
                            continue;
                        }
                        Object source = hits.get("_source");
                        if (source == null || !(source instanceof JSONObject)) {
                            continue;
                        }
                        list.add(hits.getJSONObject("_source"));
                    }
                } else {
                    int size = jsonArray.size();
                    for (int i = 0; i < size; i++) {
                        JSONObject hits = jsonArray.getJSONObject(i);
                        if (hits == null) {
                            continue;
                        }
                        getTopHis(hits, list);
                    }
                }
            }
        }
    }
}
