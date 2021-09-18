package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.engine;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;

/**
 * Es list查询条件对象
 * 项目名称: 微服务运维平台（log-service 模块）
 * 包: com.migu.tsg.microservice.monitor.log.es
 * 类名称: EsQueryBuilder.java
 * 类描述: Es list查询条件对象
 * 创建人: sunke
 * 创建时间: 2017年8月10日 下午4:12:54
 */
@Data
public class EsQueryBuilder {
	private Map<String, List<String>> paramsMultiple = new ConcurrentHashMap<String, List<String>>();
	private Map<String, List<String>> paramsShould = new ConcurrentHashMap<String, List<String>>();
	private Map<String, String> paramsQuery = new ConcurrentHashMap<String, String>();
	private String timeColsName;
	private long startTime;
	private long endTime;
	private int from;
	private int size;
	private String groupColsName;
	/**
	 * 生成直方图时，时间间隔，如 一个小时: 1h; 一个分钟 1m, 天: day; 月: month,年： year
	 */
	private String interval;
}
