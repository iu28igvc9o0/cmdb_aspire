package com.aspire.mirror.elasticsearch.server.helper;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * cmdb帮助类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.helper
 * 类名称:    CmdbHelper.java
 * 类描述:    cmdb帮助类
 * 创建人:    JinSu
 * 创建时间:  2018/9/25 10:25
 * 版本:      v1.0
 */
@Component
@Slf4j
public class EsAuthHelper {
  
   
   public void packQueryBuilder(Map<String, List<String>> resFilterMap,BoolQueryBuilder queryBuilder ){
	   if(null==resFilterMap || resFilterMap.size()==0) {
		   return ;
	   }
	 
	   for(Entry<String, List<String>> entry:resFilterMap.entrySet()) {
		   String name = entry.getKey();
		   List<String> value= entry.getValue();
		   QueryBuilder queryTermId = QueryBuilders.termsQuery(name+".keyword", value);
		   queryBuilder.must(queryTermId);
	   }
	  
   }
   
  
}
