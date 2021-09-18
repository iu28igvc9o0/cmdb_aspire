package com.aspire.mirror.elasticsearch.server.util;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.aspire.mirror.elasticsearch.server.enums.Constants;

/**
 * 时间工具类
 * <p>
 * 项目名称: 咪咕微服务运营平台-CICD 包:
 * com.migu.tsg.msp.microservice.atomicservice.cicd.commom.util 类名称:
 * DateUtil.java 类描述: 时间工具类 创建人: WuFan 创建时间: 2017/08/28 18:52 版本: v1.0
 */
public class FormConditionUtil {

	@Value("${INDEX_VALUE_MAX_NEW:value_max}")
	private static String INDEX_VALUE_MAX;

	private static final Logger log = LoggerFactory.getLogger(FormConditionUtil.class);

	public static void getDeviceType(String deviceType, String item, BoolQueryBuilder queryBuilder) {
		if(StringUtils.isBlank(deviceType)) {
			getDeviceTypeAll(item,queryBuilder);
		}else {
			QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword", deviceType);
			queryBuilder.must(queryTermId);
			
			if (item.equals("cpu")) {
				queryBuilder.must(QueryBuilders.termsQuery("item.keyword", Constants.CPU_ITEM.split(",")));
			} else {
				queryBuilder.must(QueryBuilders.termsQuery("item.keyword", Constants.MEMORY_ITEM.split(",")));
			}
		}
		
	}

	public static void getDeviceTypeAll(String item, BoolQueryBuilder queryBuilder) {
		queryBuilder.must(QueryBuilders.termsQuery("deviceType.keyword", "云主机", "X86服务器"));
		if (item.equals("cpu")) {
			queryBuilder.must(QueryBuilders.termsQuery("item.keyword", Constants.CPU_ITEM.split(",")));
		} else {
			queryBuilder.must(QueryBuilders.termsQuery("item.keyword", Constants.MEMORY_ITEM.split(",")));
		}

	}
	
	
	public static double formatDouble(double d) {
   
        BigDecimal bg =  BigDecimal.valueOf(d).setScale(2, BigDecimal.ROUND_HALF_UP);
        
        return bg.doubleValue();
    }
}