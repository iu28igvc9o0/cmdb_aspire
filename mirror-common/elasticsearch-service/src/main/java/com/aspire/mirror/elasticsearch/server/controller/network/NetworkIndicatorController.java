package com.aspire.mirror.elasticsearch.server.controller.network;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.elasticsearch.api.dto.network.NetworkIndicatorRequest;
import com.aspire.mirror.elasticsearch.api.dto.network.QueryField;
import com.aspire.mirror.elasticsearch.api.enums.ResultErrorEnum;
import com.aspire.mirror.elasticsearch.api.service.network.IEsNetworkIndicatorService;
import com.aspire.mirror.elasticsearch.server.controller.monitor.MonitorCommonController;
import com.aspire.mirror.elasticsearch.server.enums.Constants;
import com.aspire.mirror.elasticsearch.server.util.DateUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/5 9:23
 */
@RestController
@Slf4j
public class NetworkIndicatorController extends MonitorCommonController implements IEsNetworkIndicatorService {


    @Autowired
    private TransportClient client;

    private static final String DEVICE_CLASS = "网络设备";

    /**
     * 网络端口设备列表查询
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, String>> queryNetworkPortDeviceList(@RequestBody NetworkIndicatorRequest request) {

        if (Objects.isNull(request)) {
            log.info("req is null");
            throw new RuntimeException(String.valueOf(ResultErrorEnum.BAD_REQUEST));
        }
        List<QueryField> params = request.getParams();
        //日期转换
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();
        String startIndex = null;
        String endIndex = null;
        try {
            startIndex = new SimpleDateFormat("yyyyMMdd").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(startTime));
            endIndex = new SimpleDateFormat("yyyyMMdd").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(endTime));
        } catch (Exception e) {
            log.error("date format is wrong ", e.getMessage());
        }
        //查询当前时间的数据
        if (startTime.equals(endTime)) {
            //查询默认时间
            List<Map<String, String>> maps = queryIndicatorData(request, Constants.monitor_index_1 + endIndex);
            maps.stream().sorted(Comparator.comparing(map -> map.get(request.getSortName()))).collect(Collectors.toList());
            return maps;
        }
        //查询开始时间
        List<Map<String, String>> startList = queryIndicatorData(request, Constants.monitor_index_1 + startIndex);
        //查询结束时间
        List<Map<String, String>> endList = queryIndicatorData(request, Constants.monitor_index_1 + endIndex);

        for (Map<String, String> endMap : endList) {
            for (Map<String, String> startMap : startList) {
                boolean exit = (endMap.get(Constants.PORT).equals(startMap.get(Constants.PORT)) && (endMap.get(Constants.DEVICE_IP).equals(startMap.get(Constants.DEVICE_IP)) && (endMap.get(Constants.RESOURCE_ID).equals(startMap.get(Constants.RESOURCE_ID)))));
                if (exit) {
                    for (QueryField param : params) {
                        boolean flag = endMap.containsKey(param.getFieldName());
                        if (flag) {
                            endMap.put(param.getFieldName(), String.valueOf((Integer.valueOf(endMap.get(param.getFieldName())) - Integer.valueOf(startMap.get(param.getFieldName())))));
                        }
                    }
                }
            }
        }
        endList.stream().sorted(Comparator.comparing(map -> map.get(request.getSortName()))).collect(Collectors.toList());
        return endList;
    }

    private List<Map<String, String>> queryIndicatorData(NetworkIndicatorRequest request, String index) {
        //设置查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //过滤条件
        boolQueryBuilder.filter(QueryBuilders.termQuery(Constants.DEVICE_CLASS + ".keyword", DEVICE_CLASS))
                .filter(QueryBuilders.termQuery(Constants.IDC_TYPE + ".keyword", request.getIdcType()))
                .filter(QueryBuilders.termQuery(Constants.ROOM_ID + ".keyword", request.getRoomId()))
                .filter(QueryBuilders.prefixQuery(Constants.ITEM + ".keyword", request.getSortName()));
        if (!request.getEndTime().equals(request.getStartTime())) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery(Constants.DATE_TIME).gte(request.getStartTime()).lte(request.getEndTime()));
        }
        //查询排序指标数据
        List<Map<String, Object>> list = getList(request, boolQueryBuilder, index);
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer name1 = Integer.valueOf(o1.get(Constants.CLOCK).toString());
                Integer name2 = Integer.valueOf(o2.get(Constants.CLOCK).toString());
                return name2.compareTo(name1);
            }
        });
        if (CollectionUtils.isNotEmpty(list) && list.size() > request.getPageSize()) {
            list.subList(0, request.getPageSize());
        }
        List<List<Map<String, Object>>> otherList = new ArrayList<>();
        //获取设备id
        List<String> resourceIds = list.stream().map(map -> map.get(Constants.RESOURCE_ID).toString()).collect(Collectors.toList());
        //获取ip地址
        List<String> ips = list.stream().map(map -> map.get(Constants.DEVICE_IP).toString()).collect(Collectors.toList());
        //端口名称item
        List<String> itemList = list.stream().map(map -> map.get(Constants.ITEM).toString()).collect(Collectors.toList());
        List<QueryField> params = request.getParams();
        for (QueryField param : params) {
            if (param.getFieldValue().equals(request.getSortName())) {
                break;
            }
            //指标合一  todo
            List<String> items = itemList.stream().map(r -> r.replaceFirst(request.getSortName(), param.getFieldValue())).collect(Collectors.toList());
            //查询其余指标数据
            otherList.add(queryOtherIndicatorData(request, items, index, ips, resourceIds));
        }
        //进行数据处理
        List<Map<String, String>> arrayList = getMaps(request, list, otherList);

        return arrayList;
    }

    private List<Map<String, String>> getMaps(NetworkIndicatorRequest request, List<Map<String, Object>> list, List<List<Map<String, Object>>> otherList) {
        //获取默认排序字段的需要的数据
        List<Map<String, String>> arrayList = new ArrayList<>();
        for (Map<String, Object> listMap : list) {
            Map<String, String> map = new HashMap<>(16);
            map.put(Constants.DEVICE_IP, listMap.get(Constants.DEVICE_IP).toString());
            map.put(Constants.PORT, listMap.get(Constants.ITEM).toString().substring(listMap.get(Constants.ITEM).toString().indexOf("[") + 1, listMap.get(Constants.ITEM).toString().indexOf("]")));
            map.put(Constants.RESOURCE_ID, listMap.get(Constants.RESOURCE_ID).toString());
            map.put(Constants.ITEM, request.getSortName());
            map.put(request.getSortName(), listMap.get(Constants.CLOCK).toString());
            arrayList.add(map);
        }
        //获取其他指标数据
        for (List<Map<String, Object>> maps : otherList) {
            //获取其他指标的需要的数据
            List<Map<String, String>> others = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                Map<String, String> hashMap = new HashMap<>(16);
                hashMap.put(Constants.DEVICE_IP, map.get(Constants.DEVICE_IP).toString());
                hashMap.put(Constants.PORT, map.get(Constants.ITEM).toString().substring(map.get(Constants.ITEM).toString().indexOf("[") + 1, map.get(Constants.ITEM).toString().indexOf("]")));
                hashMap.put(Constants.CLOCK, map.get(Constants.CLOCK).toString());
                hashMap.put(Constants.RESOURCE_ID, map.get(Constants.RESOURCE_ID).toString());
                hashMap.put(Constants.ITEM, map.get(Constants.ITEM).toString().substring(map.get(Constants.ITEM).toString().indexOf(0) + 1, map.get(Constants.ITEM).toString().indexOf("[")));
                others.add(hashMap);
            }
            for (Map<String, String> other : others) {
                for (Map<String, String> map : arrayList) {
                    boolean exit = (other.get(Constants.PORT).equals(map.get(Constants.PORT)) && (other.get(Constants.DEVICE_IP).equals(map.get(Constants.DEVICE_IP)) && (other.get(Constants.RESOURCE_ID).equals(map.get(Constants.RESOURCE_ID)))));
                    if (exit) {
                        map.put(other.get(Constants.ITEM), other.get(Constants.CLOCK));
                    }
                }
            }
        }
        return arrayList;
    }

    private List<Map<String, Object>> queryOtherIndicatorData(NetworkIndicatorRequest request, List<String> items, String index, List<String> ips, List<String> resourceIds) {

        //设置查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //过滤条件
        boolQueryBuilder.filter(QueryBuilders.termQuery(Constants.DEVICE_CLASS + ".keyword", DEVICE_CLASS))
                .filter(QueryBuilders.termQuery(Constants.IDC_TYPE + ".keyword", request.getIdcType()))
                .filter(QueryBuilders.termQuery(Constants.ROOM_ID + ".keyword", request.getRoomId()))
                .filter(QueryBuilders.termsQuery(Constants.ITEM + ".keyword", items))
                .filter(QueryBuilders.termsQuery(Constants.RESOURCE_ID + ".keyword", resourceIds))
                .filter(QueryBuilders.termsQuery(Constants.DEVICE_IP + ".keyword", ips));
        if (!request.getEndTime().equals(request.getStartTime())) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery(Constants.DATE_TIME).gte(request.getStartTime()).lte(request.getEndTime()));
        }
        //查询其他指标
        List<Map<String, Object>> list = getList(request, boolQueryBuilder, index);

        return list;
    }

    private List<Map<String, Object>> getList(NetworkIndicatorRequest request, BoolQueryBuilder boolQueryBuilder, String index) {

        // 聚合条件
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("group_agg").field(Constants.ITEM + ".keyword").size(10000);
        AggregationBuilder agg = AggregationBuilders.terms("agg").field(Constants.RESOURCE_ID + ".keyword").size(10000);
        AggregationBuilder topScoreAggBuilder = AggregationBuilders.topHits("hits")
                .sort(Constants.CLOCK, SortOrder.DESC)
                .size(1);
        aggregationBuilder.subAggregation(topScoreAggBuilder);
        agg.subAggregation(aggregationBuilder);

        //索引条件
        String[] clusterIndex = getClusterIndex(request, index);
        // 查询结果
        SearchResponse response = client.prepareSearch(clusterIndex)
                .setQuery(boolQueryBuilder)
                .addAggregation(agg)
                .get();

        Aggregations aggregations = response.getAggregations();
        List<Map<String, Object>> list = Lists.newArrayList();
        if (aggregations == null) {
            log.warn("aggregations are null!");
            return list;
        }
        List<Aggregation> aggregationList = aggregations.asList();
        for (Aggregation aggregation : aggregationList) {
            String s = aggregation.toString();
            JSONObject jsonObject = JSON.parseObject(s);
            getTopHis(jsonObject, list);
        }
//
        return list;
    }


    /**
     * 数据包详情查询
     *
     * @param idcType
     * @param roomId
     * @param item
     * @param host
     * @param port
     * @param resourceId
     * @param startTime
     * @param endTime
     * @param pageSize
     * @param pageNum
     * @return List<Map < String, String>>
     */
    @Override
    public List<Map<String, Object>> queryDataPageDetail(@RequestParam(value = "idcType", required = false) String idcType,
                                                         @RequestParam(value = "roomId", required = false) String roomId,
                                                         @RequestParam(value = "item", required = false) String item,
                                                         @RequestParam(value = "host", required = false) String host,
                                                         @RequestParam(value = "port", required = false) String port,
                                                         @RequestParam(value = "resourceId", required = false) String resourceId,
                                                         @RequestParam(value = "startTime", required = false) String startTime,
                                                         @RequestParam(value = "endTime", required = false) String endTime,
                                                         @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<Map<String, Object>> list = queryExportDataPageDetail(idcType, roomId, item, host, port, resourceId, startTime, endTime, pageSize, pageNum);
        if (CollectionUtils.isEmpty(list)) {
            log.warn("Collection is empty");
            return list;
        }
        //进行分页
        List<Map<String, Object>> maps = pageBySubList(list, pageSize, pageNum);

        return maps;
    }

    /**
     * 数据包详情导出查询
     *
     * @param idcType
     * @param roomId
     * @param item
     * @param host
     * @param port
     * @param resourceId
     * @param startTime
     * @param endTime
     * @param pageSize
     * @param pageNum
     * @return List<Map < String, String>>
     */
    @Override
    public List<Map<String, Object>> queryExportDataPageDetail(@RequestParam(value = "idcType", required = false) String idcType,
                                                               @RequestParam(value = "roomId", required = false) String roomId,
                                                               @RequestParam(value = "item", required = false) String item,
                                                               @RequestParam(value = "host", required = false) String host,
                                                               @RequestParam(value = "port", required = false) String port,
                                                               @RequestParam(value = "resourceId", required = false) String resourceId,
                                                               @RequestParam(value = "startTime", required = false) String startTime,
                                                               @RequestParam(value = "endTime", required = false) String endTime,
                                                               @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        //日期转换
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
            //设置查询条件
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            //过滤条件
            boolQueryBuilder.must(QueryBuilders.termQuery(Constants.DEVICE_CLASS + ".keyword", DEVICE_CLASS))
                    .must(QueryBuilders.termQuery(Constants.DEVICE_IP + ".keyword", host))
                    .must(QueryBuilders.termQuery(Constants.RESOURCE_ID + ".keyword", resourceId))
                    .must(QueryBuilders.termQuery(Constants.IDC_TYPE + ".keyword", idcType))
                    .must(QueryBuilders.termQuery(Constants.ROOM_ID + ".keyword", roomId))
                    .must(QueryBuilders.termQuery(Constants.ITEM + ".keyword", item + "[" + port + "]"))
                    .must(QueryBuilders.rangeQuery(Constants.DATE_TIME).gte(startTime).lte(endTime));
            // 聚合条件
            AggregationBuilder aggregationBuilder = AggregationBuilders.terms("group_agg").field(Constants.ITEM + ".keyword").size(10000);
            AggregationBuilder agg = AggregationBuilders.terms("agg").field(Constants.RESOURCE_ID + ".keyword").size(10000);
            AggregationBuilder topScoreAggBuilder = AggregationBuilders.topHits("hits")
                    .sort(Constants.VALUE, SortOrder.DESC)
                    .size(1);
            aggregationBuilder.subAggregation(topScoreAggBuilder);
            agg.subAggregation(aggregationBuilder);
            //索引条件
            List<String> indexList = DateUtil.getIndexList(startDate, endDate, Constants.monitor_index_1);
            String[] clusterIndex = getClusterIndex(null, indexList.toArray((new String[indexList.size()])));
            // 查询结果
            SearchResponse response = client.prepareSearch(clusterIndex)
                    .setQuery(boolQueryBuilder)
                    .addAggregation(agg)
                    .get();

            Aggregations aggregations = response.getAggregations();
            List<Map<String, Object>> list = Lists.newArrayList();

            if (aggregations == null) {
                log.warn("aggregations are null!");
                return mapList;
            }
            List<Aggregation> aggregationList = aggregations.asList();
            for (Aggregation aggregation : aggregationList) {
                String s = aggregation.toString();
                JSONObject jsonObject = JSON.parseObject(s);
                getTopHis(jsonObject, list);
            }
            //数据处理
            for (Map<String, Object> map : list) {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put(Constants.VALUE, map.get(Constants.VALUE));
                hashMap.put(Constants.DATE_TIME, map.get(Constants.DATE_TIME));
                mapList.add(hashMap);
            }

        } catch (Exception e) {
            log.error("queryExportDataPageDetail is error", e.getMessage());
        }
        return mapList;
    }

    public static List<Map<String, Object>> pageBySubList(List list, int pagesize, int currentPage) {

        int totalcount = list.size();

        int pagecount = 0;

        List<Map<String, Object>> subList;

        int m = totalcount % pagesize;

        if (m > 0) {

            pagecount = totalcount / pagesize + 1;

        } else {

            pagecount = totalcount / pagesize;

        }

        if (m == 0) {

            subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));

        } else {

            if (currentPage == pagecount) {

                subList = list.subList((currentPage - 1) * pagesize, totalcount);

            } else {

                subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));

            }

        }

        return subList;

    }


}
