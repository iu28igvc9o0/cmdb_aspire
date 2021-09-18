package com.aspire.mirror.log.server.biz.impl;


import com.alibaba.fastjson.JSON;
import com.aspire.mirror.log.server.biz.CommonBiz;
import com.aspire.mirror.log.server.config.ClusterProperties;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.server.controller
 * @Author: baiwenping
 * @CreateTime: 2020-05-14 19:50
 * @Description: ${Description}
 */
@Slf4j
@Service
@AutoConfigureBefore(ClusterProperties.class)
public class CommonBizImpl implements CommonBiz {

    private static final String SPERATOR = ",";

    @Autowired
    private ClusterProperties clusterProperties;
    /**
     * 生成跨域查询索引
     * @param indices
     * @return
     */
    public String[] getClusterIndex (Object queryParam, String... indices) {
        List<String> indexList = clusterProperties.getIndexs();
        log.debug("cluster index list: {}", indexList);
        if (indices == null || CollectionUtils.isEmpty(indexList)) {
            return indices;
        }
        Set<String> set = Sets.newHashSet();
        Set<String> indexs = Sets.newHashSet();
        // 分解成单个索引
        for (String indice: indices) {
            if (StringUtils.isEmpty(indice)) {
                continue;
            }
            String[] indiceArr = indice.split(SPERATOR);
            for (String str: indiceArr) {
                indexs.add(str);
            }
        }
        if (indexs.isEmpty()) {
            return indices;
        }
        boolean flag;
        for (String indexStr: indexs) {
            if (indexStr.indexOf(":") > -1) {
                set.add(indexStr);
            }
            flag = false;
            for (String indexMatch: indexList) {
                if (indexStr.matches(indexMatch)) {
                    flag = true;
                    // 生成跨集群索引
                    set.addAll(getIndexs(indexStr, queryParam));
                    break;
                }
            }
            if (!flag) {
                set.add(indexStr);
            }
        }
        log.debug("return cluster indexs are: {}", set);
        return set.toArray(new String[set.size()]);
    }

    /**
     * 生成跨集群索引
     * @param index
     * @return
     */
    private Set<String> getIndexs (String index, Object queryParam) {
        Map<String, List<String>> queryKeywordMap = clusterProperties.getQueryKeywords();
        Map<String, List<String>> indexKeywordMap = clusterProperties.getIndexKeywords();
        log.debug("-1queryKeywordMap-{}", queryKeywordMap);
        log.debug("-2indexKeywordMap-{}", indexKeywordMap);

        Set<String> set = Sets.newHashSet();
        String queryString;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //转化成string，如果传入参数为空或者get请求则通过request获取
        if (queryParam != null) {
            if (queryParam instanceof String) {
                queryString = (String)queryParam;
            } else {
                queryString = JSON.toJSONString(queryParam);
            }
        } else {
            queryString = JSON.toJSONString(request.getParameterMap());
        }

        //根据查询条件匹配
        if (!CollectionUtils.isEmpty(queryKeywordMap) && !StringUtils.isEmpty(queryString)) {
            matchIndex(set, queryKeywordMap, queryString, index);
        }

        //根据索引名称关键字配置
        if (!CollectionUtils.isEmpty(indexKeywordMap)) {
            matchIndex(set, indexKeywordMap, index, index);
        }

        // 如果没有匹配，补录"*:"查询所有集群
        if (set.isEmpty()) {
            set.add("*:" + index);
        }

        return set;
    }

    /**
     *
     * @param set
     * @param map
     * @param keyword
     */
    private void matchIndex (Set<String> set, Map<String, List<String>> map, String keyword, String index) {
        for (Map.Entry<String, List<String>> entry: map.entrySet()) {
            String key = entry.getKey();
            List<String> list = entry.getValue();
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            for (String str: list) {
                if (keyword.indexOf(str) > -1) {
                    set.add(key + ":" + index);
                }
            }
        }
    }
}
