package com.aspire.ums.cmdb.v3.module.event.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.helper.BpmTokenHelper;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IPRepositoryInnerIPOperateLogEvent
 * Author:   柳佳逊
 * Date:     2020/7/26
 * Description: 业务变更去bpm系统发起任务流程
 * 
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Component
public class BusinessSystemChangeToBpmEvent extends AbstractModuleEvent {

	    private BpmTokenHelper bpmTokenHelper;
		private ICmdbInstanceService instanceService;
	    @Override
	    public void initSpringBeans() {
	        if (this.bpmTokenHelper == null) {
	            this.bpmTokenHelper = SpringUtils.getBean(BpmTokenHelper.class);
	        }
	        if (this.instanceService == null) {
	        	this.instanceService = SpringUtils.getBean(ICmdbInstanceService.class);
			}
	    }

	    @Override
	    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
	    	String username = handleData.get("username").toString();
//			Map<String, Object> bizInfo = instanceService.getInstanceDetail(moduleId, instanceId);
//	    	String bizSystem = bizInfo.get("bizSystem").toString();
	        Map<String, Object> returnMap = new HashMap<>();
	        String  token = null;
	        if(StringUtils.isNotEmpty(username)) {
	        	token = bpmTokenHelper.getUserToken(username);
	            if (StringUtils.isEmpty(token)) {
	                log.error("获取BPM token异常 token 为空");
	                returnMap.put("flag", false);
	    	        returnMap.put("msg", "获取BPM token异常 token 为空");
	    	        return returnMap;
	            }
	        }else {
	        	 log.error("获取当前操作者为空");
	                returnMap.put("flag", false);
	    	        returnMap.put("msg", "获取当前操作者为空");
	    	        return returnMap;
	        }
	        JSONObject  params =  new JSONObject();
    		Map<Object,Object> data = new HashMap<Object, Object>();
    		Map<Object,Object> form = new HashMap<Object, Object>();
    		form.put("rwlx", "1");
    		form.put("rwclxgfj", "");
    		form.put("zyc", "");
    		form.put("rwclnr", "");
    		form.put("rwnr", "");
    		form.put("rwmc", username + "提交了业务变更, 请及时处理.");
    		data.put(bpmTokenHelper.getDATAKEY(), form);
    		String  formData = JSON.toJSONString(data);
    		byte[] dataByte = org.apache.commons.codec.binary.Base64.encodeBase64(formData.getBytes());
    		params.put("data", new String(dataByte));
    		params.put("account", username);
    		params.put("flowKey", bpmTokenHelper.getFLOWKEY());
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Authorization", "Bearer "+token);
            requestHeaders.add("Content-Type", "application/json;charset=utf-8");
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(params,
            		requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            log.info("<<<<<< 开始发起bpm任务工单 <<<<<<"+bpmTokenHelper.getBUSINESSSYSTEMCHANGETOBPMURL());
            ResponseEntity<String> result = restTemplate.exchange(bpmTokenHelper.getBUSINESSSYSTEMCHANGETOBPMURL(), HttpMethod.POST, requestEntity,String.class);
            if(result.getStatusCodeValue() == 200) {
            	 log.info("<<<<<< 发起bpm任务工单完成 <<<<<<");
     	        returnMap.put("flag", true);
     	        returnMap.put("msg", "保存成功");
     	        return returnMap;
            } else{
            	log.error("<<<<<< 发起bpm任务工单业务变更失败 <<<<<<"+result.getBody());
      	        returnMap.put("flag", false);
      	        returnMap.put("msg", "发起工单失败");
      	        return returnMap;
            }
	       
	    }
	    
	    
	    public static void main(String[] args) {
	    	    JSONObject  params =  new JSONObject();
	    		Map<Object,Object> data = new HashMap<Object, Object>();
	    		Map<Object,Object> form = new HashMap<Object, Object>();
	    		form.put("rwlx", "1");
	    		form.put("rwclxgfj", "");
	    		form.put("zyc", "");
	    		form.put("rwclnr", "");
	    		form.put("rwnr", "");
	    		form.put("rwmc", "测试测试申请人");
	    		data.put("CMDBrwczdx", form);
	    		String  formData = JSON.toJSONString(data);
	    		byte[] dataByte = org.apache.commons.codec.binary.Base64.encodeBase64(formData.getBytes());
	    		params.put("data", new String(dataByte));
	    		params.put("account", "liujiaxun");
	    		params.put("flowKey", "CMDBrwczlc");
	            HttpHeaders requestHeaders = new HttpHeaders();
	            requestHeaders.add("Authorization", "Bearer "+"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaXVqaWF4dW4iLCJleHAiOjE1OTU5MjA5MzcsImlhdCI6MTU5NTgzNDUzN30.aXwLoC4_dYWh-mPvvUvvjP9Cp0IMH505nneX0x0ZYUZ1kQJWedxKAmLxuUu1b_aS3w5cHMD-L5a1vgkAYtENCQ");
	            requestHeaders.add("Content-Type", "application/json;charset=utf-8");
	            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(params,
	            		requestHeaders);
	            RestTemplate restTemplate = new RestTemplate();
	            ResponseEntity<String> result = restTemplate.exchange("http://10.12.70.37:8086/runtime/instance/v1/start", HttpMethod.POST, requestEntity,String.class);
	            System.out.println("+++"+result.getStatusCodeValue());
			
		}

}
