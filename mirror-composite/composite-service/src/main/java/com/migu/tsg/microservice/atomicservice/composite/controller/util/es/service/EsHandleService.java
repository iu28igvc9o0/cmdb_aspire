package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant.Enviroment;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.dto.BucketInfoDTO;

import io.searchbox.client.JestResult;
import io.searchbox.core.Count;
import io.searchbox.core.CountResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.sort.Sort;
import io.searchbox.core.search.sort.Sort.Sorting;
import io.searchbox.indices.IndicesExists;

/**
 * Es handle 项目名称: 微服务运维平台（log-service 模块） 包:
 * com.migu.tsg.microservice.monitor.log.biz.impl 类名称: EsHandleService.java 类描述:
 * Es handle 创建人: sunke 创建时间: 2017年8月12日 上午10:07:27
 */
@Service
public class EsHandleService {

    private static final Logger LOG = LoggerFactory.getLogger(EsHandleService.class);

    @Autowired
    private JestService jestService;

   /* @Autowired
    private RegionServiceClient regionServiceClient;*/

    private static final Gson GSON = new Gson();

    /**
     * 查询总记录数
     * 
     * @param json
     *            查询dsl语句
     * @param indexName
     *            index名称
     * @param type
     *            type名称
     * @return 总记录数
     */
    public int count(String json, ArrayList<String> indexName, String type, String regionId) {
        try {
            LOG.info("count +++ indexName:{}, type:{}", indexName, type);
            ArrayList<String> indexNameNew = new ArrayList<>();
            for (int i = 0; i < indexName.size(); i++) {
                // 移除不存在的索引
                String indexNameTmp = indexName.get(i);
                if (indicesExists(indexNameTmp, regionId)) {
                    indexNameNew.add(indexNameTmp);
                }
            }
            final CountResult result = jestService.getInstance(regionId, false)
                    .execute(new Count.Builder().query(json).addIndex(indexNameNew).addType(type).build());
            LOG.info("count --- result.getCount():{}", result.getCount());
            if (result.getCount() != null) {
                return result.getCount().intValue();
            }

        } catch (IOException e) {
            LOG.error("count exception; " + e.getMessage(), e);
        }

        LOG.info("count --- return 0");
        return 0;
    }

    /**
     * 列表查询
     * 
     * @param json
     *            查询dsl语句
     * @param indexName
     *            index名称
     * @param type
     *            type名称
     * @param cls
     *            返回列表对象类型
     * @return 列表数列
     */
    @SuppressWarnings("rawtypes")
    public List list(final String json, ArrayList<String> indexName, final String type, final Type cls, String regionId) {
        List resultList = new ArrayList();
        try {
            // LOG.info("list dsl:{}", json.toString());
            LOG.info("list +++ indexName:{}, type:{}", indexName, type);
            ArrayList<String> indexNameNew = new ArrayList<>();
            for (int i = 0; i < indexName.size(); i++) {
                // 移除不存在的索引
                String indexNameTmp = indexName.get(i);
                if (indicesExists(indexNameTmp, regionId)) {
                    indexNameNew.add(indexNameTmp);
                }
            }
            // 默认按时间倒序，瞎写的，大神们看到请优化
            Sort sort = new Sort("time", Sorting.DESC);
            final Search search = new Search.Builder(json).addIndex(indexNameNew).addType(type).addSort(sort).build();
            LOG.info("list search:{}");

            final SearchResult result = jestService.getInstance(regionId, false).execute(search);

            LOG.debug("list 1 result:{}", result);

            final List<String> list = result.getSourceAsStringList();
            if (list == null || list.isEmpty()) {
                return resultList;
            }
            resultList = GSON.fromJson(list.toString(), cls);
        } catch (IOException e) {
            LOG.error("list exception; " + e.getMessage(), e);
        }

        LOG.info("list --- resultList:{}", resultList.size());
        return resultList;
    }

    /**
     * 分组查询后只获取分组标签数列
     * 
     * @param json
     *            查询dsl语句
     * @param indexName
     *            index名称
     * @param type
     *            type名称
     * @return 标签数列
     */
    public List<String> group(String json, String indexName, String type, String regionId) {
        final List<String> tags = new ArrayList<String>();
        try {
            LOG.info("group +++ indexName:{}, type:{}", indexName, type);
            // LOG.info("group dsl:{}", json.toString());

            final Search search = new Search.Builder(json).addIndex(indexName).addType(type).build();

            LOG.info("group search:{}", search);

            final SearchResult result = jestService.getInstance(regionId, false).execute(search);

            LOG.info("group result:{}", result.getTotal());

            final TermsAggregation terms = result.getAggregations().getTermsAggregation(Enviroment.AGGLABEL);

            List<io.searchbox.core.search.aggregation.TermsAggregation.Entry> list = terms.getBuckets();

            LOG.info("group list.size:{}", list.size());
            for (final io.searchbox.core.search.aggregation.TermsAggregation.Entry e : list) {
                // LOG.debug("listTags key:{};value:{}", e.getKeyAsString(), e.getCount());
                tags.add(e.getKeyAsString());
            }
        } catch (IOException e) {
            LOG.error("group exception; " + e.getMessage(), e);
        }
        LOG.info("group --- tags:{}", tags);
        return tags;
    }

    /**
     * 分组查询后只获取分组标签数列
     * 
     * @param json
     *            查询dsl语句
     * @param indexName
     *            index名称
     * @param type
     *            type名称
     * @return 标签数列
     */
    public List<String> group(String json, ArrayList<String> indexName, String type, String regionId) {
        final List<String> tags = new ArrayList<String>();
        try {
            LOG.info("group +++ indexName:{}, type:{}", indexName, type);
            // LOG.info("group dsl:{}", json.toString());
            ArrayList<String> indexNameNew = new ArrayList<>();
            for (int i = 0; i < indexName.size(); i++) {
                // 移除不存在的索引
                String indexNameTmp = indexName.get(i);
                if (indicesExists(indexNameTmp, regionId)) {
                    indexNameNew.add(indexNameTmp);
                }
            }
            final Search search = new Search.Builder(json).addIndex(indexNameNew).addType(type).build();

            LOG.info("group search:{}", search);

            final SearchResult result = jestService.getInstance(regionId, false).execute(search);

            LOG.debug("group result:{}", result);

            final TermsAggregation terms = result.getAggregations().getTermsAggregation(Enviroment.AGGLABEL);

            List<io.searchbox.core.search.aggregation.TermsAggregation.Entry> list = terms.getBuckets();

            LOG.info("group list.size:{}", list.size());
            for (final io.searchbox.core.search.aggregation.TermsAggregation.Entry e : list) {
                // LOG.debug("listTags key:{};value:{}", e.getKeyAsString(), e.getCount());
                tags.add(e.getKeyAsString());
            }
        } catch (IOException e) {
            LOG.error("group exception; " + e.getMessage(), e);
        }
        LOG.info("group --- tags:{}", tags);
        return tags;
    }

    /**
     * 获取时间直方图
     * 
     * @param json
     *            查询dsl语句
     * @param indexName
     *            index名称
     * @param type
     *            type名称
     * @return 时间直方图数列
     */
    public List<BucketInfoDTO> histogram(String json, ArrayList<String> indexName, String type, String regionId) {
        final List<BucketInfoDTO> resultList = new ArrayList<BucketInfoDTO>();
        BucketInfoDTO dto = null;
        try {
            // LOG.info("histogram +++ dsl:{}", json.toString());
            LOG.info("histogram +++ indexName:{}, type:{}", indexName, type);
            ArrayList<String> indexNameNew = new ArrayList<>();
            for (int i = 0; i < indexName.size(); i++) {
                // 移除不存在的索引
                String indexNameTmp = indexName.get(i);
                if (indicesExists(indexNameTmp, regionId)) {
                    indexNameNew.add(indexNameTmp);
                }
            }
            final Search search = new Search.Builder(json).addIndex(indexNameNew).addType(type).build();

            LOG.info("histogram 1 search=" + search.toString());

            final SearchResult result = jestService.getInstance(regionId, false).execute(search);

            LOG.debug("histogram result:{}", result.toString());

            final TermsAggregation terms = result.getAggregations().getTermsAggregation(Enviroment.AGGLABEL);
            final List<io.searchbox.core.search.aggregation.TermsAggregation.Entry> list = terms.getBuckets();
            LOG.info("histogram list.size:{}", list.size());
            for (final io.searchbox.core.search.aggregation.TermsAggregation.Entry e : list) {
                // LOG.info("histogram key:{};value:{}", e.getKeyAsString(), e.getCount());
                dto = new BucketInfoDTO();
                String s = e.getKeyAsString();
                dto.setTime(s.substring(0, s.length() - 6));
                dto.setCount(e.getCount());
                resultList.add(dto);
            }
        } catch (Exception e) {
            LOG.info("log list chart exception; " + e.getMessage(), e);
        }

        LOG.info("histogram --- resultList:{}"/* , resultList */);
        return resultList;
    }
    /**
     * 获取时间直方图
     * 
     * @param json
     *            查询dsl语句
     * @param indexName
     *            index名称
     * @param type
     *            type名称
     * @return 时间直方图数列
     */
    public List<BucketInfoDTO> histogramForBiz(String json, ArrayList<String> indexName, String type, String regionId) {
        final List<BucketInfoDTO> resultList = new ArrayList<BucketInfoDTO>();
        BucketInfoDTO dto = null;
        try {
            // LOG.info("histogram +++ dsl:{}", json.toString());
            LOG.info("histogram +++ indexName:{}, type:{}", indexName, type);
            ArrayList<String> indexNameNew = new ArrayList<>();
            for (int i = 0; i < indexName.size(); i++) {
                // 移除不存在的索引
                String indexNameTmp = indexName.get(i);
                if (indicesExists(indexNameTmp, regionId)) {
                    indexNameNew.add(indexNameTmp);
                }
            }
            final Search search = new Search.Builder(json).addIndex(indexNameNew).addType(type).build();

            LOG.info("histogram 1 search=" + search.toString());

            final SearchResult result = jestService.getInstance(regionId, false).execute(search);

            LOG.debug("histogram result:{}", result.toString());

            final TermsAggregation terms = result.getAggregations().getTermsAggregation(Enviroment.AGGLABEL);
            final List<io.searchbox.core.search.aggregation.TermsAggregation.Entry> list = terms.getBuckets();
            LOG.info("histogram list.size:{}", list.size());
            for (final io.searchbox.core.search.aggregation.TermsAggregation.Entry e : list) {
                // LOG.info("histogram key:{};value:{}", e.getKeyAsString(), e.getCount());
                dto = new BucketInfoDTO();
                String s = e.getKeyAsString();
                dto.setTime(s.substring(0, s.length() - 3));
                dto.setCount(e.getCount());
                resultList.add(dto);
            }
        } catch (Exception e) {
            LOG.info("log list chart exception; " + e.getMessage(), e);
        }

        LOG.info("histogram --- resultList:{}"/* , resultList */);
        return resultList;
    }

    /**
     * 判断索引是否存在
     * 
     * @return
     */
    private Boolean indicesExists(String indexName, String regionId) throws IOException {
        IndicesExists indicesExists = new IndicesExists.Builder(indexName).build();
        JestResult execute = jestService.getInstance(regionId, false).execute(indicesExists);
        return execute != null && execute.isSucceeded();
    }
}
