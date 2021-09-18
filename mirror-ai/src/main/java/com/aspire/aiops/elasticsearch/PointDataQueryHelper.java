package com.aspire.aiops.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspire.aiops.utils.ResourceUtil;
import com.aspire.aiops.utils.TimeUtil;


public class PointDataQueryHelper {

    private EsTransportClient transportClient;
    private static final Logger log = LoggerFactory.getLogger(PointDataQueryHelper.class);
    private static Properties props = ResourceUtil.loadResource("es.properties");
    private static String[] index = props.getProperty("es.index").split(",");
    private static String type = props.getProperty("es.type");

    public SearchResponse query(Map<String,Long> timeMap, List<Integer> itemList) throws Exception{
        log.info("es query params: "+ timeMap.toString()+" "+itemList.toString());

        //String index = "uint";
        //String type = "values";
        SearchResponse response = null;

        /*String precinctName = params.getString("precinctName");
        String meteName = params.getString("meteName");
        List<String> itemsList = (List)params.get("items");*/

        try {
            Long startTime = timeMap.get("startTime");
            Long endTime = timeMap.get("endTime");
            System.out.println("========================================================================");
            System.out.println("startTime: "+ TimeUtil.seconds2Time(startTime));
            System.out.println("endTime: "+TimeUtil.seconds2Time(endTime));

            if (startTime!=null && endTime != null && itemList.size()>0) {
                QueryBuilder queryBuilder = QueryBuilders
                        .boolQuery()
                        .must(QueryBuilders.termsQuery("itemid",
                                itemList))
                        .must(QueryBuilders.rangeQuery("clock")
                                .gte(startTime)
                                .lte(endTime));

                if(null == transportClient) {
                    transportClient = new EsTransportClient();
                }
                
                response = transportClient.getClient()
                        .prepareSearch(index)
                        .setTypes(type)
//                        .setSearchType(SearchType.DEFAULT)
                        .setSize(10000)
                        //.addSort("clock", SortOrder.DESC)
                        .setQuery(queryBuilder)
                        .execute().actionGet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // for test
        /*if(response==null){
            QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
            if(null == transportClient) {
                transportClient = new EsTransportClient();
            }
            response = transportClient.getClient()
                    .prepareSearch(index)
                    .setTypes(type)
                    .setSearchType(SearchType.DEFAULT)
                    .setSize(5)
                    //.addSort("clock", SortOrder.DESC)
                    .setQuery(queryBuilder)
                    .execute().actionGet();
        }*/
        return response;
    }

    public List<Map<String, Object>> responseParser(SearchResponse response){

        List<Map<String, Object>> list = new ArrayList();
        try {
            if(response!=null){
                for (SearchHit hit :response.getHits()) {
                    try {
                        Map<String, Object> map = hit.getSource();
                        list.add(map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //log.info("query result size: "+list.size());
        /*if(list.size()==0){
            Map<String, Object> defaultMap = new HashMap();
            defaultMap.put("itemid","1");
            defaultMap.put("clock","2");
            defaultMap.put("value","3");
            list.add(defaultMap);
        }*/
        log.info("es---------------------------------------------------------------");
        log.info("query result size: "+list.size());
        return list;
    }

    public SearchResponse querySample(){

        SearchResponse response = null;

        try {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%get random%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
            if(null == transportClient) {
                transportClient = new EsTransportClient();
            }
            response = transportClient.getClient()
                    .prepareSearch(index)
                    .setTypes(type)
//                    .setSearchType(SearchType.DEFAULT)
                    .setSize(5)
                    //.addSort("clock", SortOrder.DESC)
                    .setQuery(queryBuilder)
                    .execute().actionGet();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }

}
