package com.aspire.mirror.alert.server.helper;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.mirror.alert.server.dao.alert.AlertRestfulDao;
import com.aspire.mirror.alert.server.dao.alert.po.AuthFieldConfig;
import com.google.common.collect.Maps;

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
public class AuthHelper {
    @Autowired
    private AlertRestfulDao AlertRestfulDao;


   public Map<String,String>  getField(Integer type){
	   List<AuthFieldConfig>  data = AlertRestfulDao.selectAuthField(type);
	   return packMap(data);
    }
   
   
 
   Map<String,String> packMap(List<AuthFieldConfig> data){
	   Map<String,String> map = Maps.newHashMap();
	   for(AuthFieldConfig d:data) {
		   map.put(d.getName(), d.getValue());
	   }
	   return  map;
   }
   
   public Map<String,List<String>>  packCondition(Map<String, List<String>> resFilterMap,Integer type){
	   log.debug("权限数据：{}",resFilterMap);
	   if(null==resFilterMap || resFilterMap.size()==0) {
		   log.info("没有权限数据：{}",resFilterMap);
		   return null;
	   }
	   Map<String,String> map = getField(type);
	   Map<String,List<String>> fieldMap = Maps.newHashMap();
	   for(Entry<String, List<String>> entry:resFilterMap.entrySet()) {
		   String name = entry.getKey();
		   List<String> value= entry.getValue();
		   String field = map.get(name);
		   
		   if(null!=field && null!=value && value.size()>0) {
			   fieldMap.put(field, value);
		   }
	   }
	   if(fieldMap.size()>0) {
		   return fieldMap;
	   }else {
		   return null;
	   }
   }
   
   public Map<String,String> packSqlConditionByType(Map<String, List<String>> resFilterMap,Integer type){
	   log.debug("权限数据：{}",resFilterMap);
	   if(null==resFilterMap || resFilterMap.size()==0) {
			/*
			 * Map<String,String> conditionMap = Maps.newHashMap(); String con =
			 * ")auth where 1=1 and idc_type in('信息港资源池','南方基地','呼和浩特资源池')" ;
			 * conditionMap.put("pre", "select * from("); // conditionMap.put("flag",
			 * "true"); conditionMap.put("condition1", con);
			 */
		   log.info("没有权限数据：{}",resFilterMap);
		   return null;
	   }
	   StringBuffer sb = new StringBuffer();
	   Map<String,String> map = getField(type);
	   for(Entry<String, List<String>> entry:resFilterMap.entrySet()) {
		   String name = entry.getKey();
		   List<String> value= entry.getValue();
		   String field = map.get(name);
		   
		   if(null!=field && null!=value && value.size()>0) {
			   sb.append(" and ").append(field).append(" in (");
			   for(int i=0;i<value.size();i++) {
				   if(i==0) {
					 sb.append("'").append(value.get(i)).append("'");
				   }else {
					   sb.append(",'").append(value.get(i)).append("'");
				   }
				   
			   }
			   sb.append(")");
		   }
	   }
	 //  "select * from(";
	   if(sb.length()==0) {
		   return null;
	   }
	   Map<String,String> conditionMap = Maps.newHashMap();
//	   String con = ")auth where 1=1" + sb.toString();
	   String con = " and 1 = 1 " + sb.toString() + ") auth ";
	   conditionMap.put("condition", sb.toString());
	   conditionMap.put("pre", "select * from(");
	  // conditionMap.put("flag", "true");
	   conditionMap.put("condition1", con);
	   return conditionMap;
	   
   }
   
  public  Map<String,String>  packAlertCondition(Map<String, List<String>> resFilterMap){
	  return packSqlConditionByType(resFilterMap,1);
   }
}
