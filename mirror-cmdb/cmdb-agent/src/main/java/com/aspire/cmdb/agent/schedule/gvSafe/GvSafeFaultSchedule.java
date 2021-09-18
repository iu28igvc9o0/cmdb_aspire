package com.aspire.cmdb.agent.schedule.gvSafe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.cmdb.agent.helper.SmSendHelper;
import com.aspire.cmdb.agent.util.CommonHttpResponse;
import com.aspire.cmdb.agent.util.HttpUtils;
import com.aspire.cmdb.agent.util.YearlyVactionAttendanceUtil;
import com.aspire.ums.cmdb.helper.BpmTokenHelper;
import com.aspire.ums.cmdb.helper.MailSendHelper;
import com.aspire.ums.cmdb.sqlManage.service.CmdbSqlManageService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.restful.service.ICommonRestfulService;
import com.aspire.ums.cmdb.v2.restful.service.ISlaMagRecordService;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BizsystemUrgeSchedule
 * Author:   longfeng
 * Date:     2020/11/2
 * Description: 扫描业务系统，根据上线考核时间前15，5天发送催办工单
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@EnableScheduling
@Slf4j
@Component
public class GvSafeFaultSchedule {

    @Autowired
    ICmdbInstanceService instanceService;
    @Autowired
    ICommonRestfulService iCommonRestfulService;

    @Autowired
    ICmdbConfigService configService;
    
    @Autowired
    ISlaMagRecordService  iSlaMagRecordService;
    
    @Autowired
    SmSendHelper SmSendHelper;
    
    @Autowired
    private YearlyVactionAttendanceUtil yearlyVactionAttendanceUtil;
    
    @Value("${GvSafeFaultSchedule.scene:故障跟踪上报}")
    String scene;
    
    @Value("${bpm.sla.url:}")
    String bpmUrl;
    
    @Value("${GvSafeFaultSchedule.nodeId:UserTask1}")
    String nodeId;
    
    @Value("${GvSafeFaultSchedule.procDefKey:gzgzsb}")
    String procDefKey;
    
    @Value("${GvSafeFaultSchedule.isHtml:true}")
    boolean isHtml;
    @Value("${GvSafeFaultSchedule.pageSize:500}")
    private int  pageSize;
    
    @Value("${GvSafeFaultSchedule.userName:admin}")
    private String  userName;
    
    @Autowired
    private BpmTokenHelper bpmTokenHelper;
    
    private String LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    @Autowired
    private CmdbSqlManageService sqlManageService;
    
	/*
	 * @Autowired private EmailSendHelper emailSendHelper;
	 */
    
    @Autowired
    private MailSendHelper emailSendHelper;
    
    
   
    @SuppressWarnings("unchecked")
  	@Scheduled(cron = "${GvSafeFaultSchedule.cron:0 2 5 * * ?}")
	public void scanFault() throws Exception  {
    	log.info("------GvSafeFaultSchedule start {}------", System.currentTimeMillis());
    	Date curTime = new Date();
    	
    	//调用bpm接口获取规则配置数据
		
    	String token = bpmTokenHelper.getUserToken(userName);
    	if(StringUtils.isBlank(token)) {
    		log.error("调用bpm获取token失败");
    		throw new Exception("调用bpm获取token失败");
    	}
    	//token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYxNjAzMzgwMywiaWF0IjoxNjE1OTQ3NDAzfQ.2-LGor0h3jRo_8kKsGeGFDQ1ly5FVoBoT8Nhu-wfw-BkC35dcZfkJQAoq6hHWiJYX32yhNlEiFK1hRqLkK2voA";
    	
    	//procDefKey = "gzgzsb";
    	String url = bpmUrl +"?procDefKey="+procDefKey+"&nodeId="+nodeId;
    	CommonHttpResponse res = HttpUtils.httpGet(url, "Bearer " + token, "Authorization");
    	int status = res.getStatus();
    	if(status!=200) {
    		log.error("调用bpm获取sla配置数据失败"+res.getContent());
    		throw new Exception("调用bpm获取sla配置数据失败"+res.getContent());
    	}
		/*
		 * String sss = "[\r\n" + "  {\r\n" + "    \"id\":\"规则配置ID\",\r\n" +
		 * "    \"name\":\"规则配置名称\",\r\n" + "    \"isEnable\":\"1\",\r\n" +
		 * "    \"isWork\":\"1\",\r\n" + "    \"isMultiNode\":\"是否是多节点汇合\",\r\n" +
		 * "    \"nodeId\":\"节点Id（存在多个）\",\r\n" + "    \"proDefName\":\"流程名称\",\r\n" +
		 * "    \"proDefKey\":\"流程KEY\",\r\n" +
		 * "    \"filterCondition\":\"配置的查询条件，JSON格式\",\r\n" +
		 * "    \"ruleTypeList\":[\r\n" + "      {\r\n" + "        \"id\":\"规则ID\",\r\n"
		 * + "        \"title\":\"邮件标题\",\r\n" + "        \"actionName\":\"规则名称\",\r\n"
		 * + "        \"actionType\":\"2\",\r\n" +
		 * "        \"content\":\"[故障级别]{gzgzsbdx.fault_level}[故障发现时间]{gzgzsbdx.fault_start_time}[业务恢复时间]{gzgzsbdx.bizSystem_recovery_time}\",\r\n"
		 * + "        \"otherUser\":[\r\n" + "    {\r\n" +
		 * "        \"account\": \"用户账号\",\r\n" + "        \"typename\": \"类型名称\",\r\n"
		 * + "        \"id\": \"用户ID\",\r\n" + "        \"name\": \"用户名称\",\r\n" +
		 * "        \"email\": \"415954178@qq.com\",\r\n" +
		 * "        \"mobile\": \"18959809931\",\r\n" + "        \"type\": 1\r\n" +
		 * "    }\r\n" + "],\r\n" + "        \"type\":\"1\",\r\n" +
		 * "        \"minuend\":\"超时计算公式的被减数\",\r\n" +
		 * "        \"reduction\":\"超时计算公司的减数\",\r\n" + "        \"duration\":\"5\"\r\n"
		 * + "      }\r\n" + "    ]\r\n" + "  }\r\n" + "]";
		 */
		JSONArray jsArray = JSON.parseArray(res.getContent());
		
		for(int i=0;i<jsArray.size();i++) {
			
			//查询故障上报数据
			JSONObject ob = jsArray.getJSONObject(i);
			String enable = ob.getString("isEnable");
			if(enable.equals("0")) {
				continue;
			}
			String isWork = ob.getString("isWork");//1是0否
			JSONArray typesList = ob.getJSONArray("ruleTypeList");
			for(int j=0;j<typesList.size();j++) {
				JSONObject typeOb = typesList.getJSONObject(j);
				String type = typeOb.getString("type");
				String ruleId = typeOb.getString("id");
				//String msg = typeOb.getString("content");
				String duration = typeOb.getString("duration");
				Double endTime = Double.valueOf(duration)*60*60;
				JSONArray userList = typeOb.getJSONArray("otherUser");
				String smsType = typeOb.getString("actionType");
				List<String> smsList = getSmsList(userList,smsType);
				String scanType = "";
				if(type.equals("1")) {
					scanType = "标准超时";
				}else if(type.equals("2")) {
					scanType = "标准预警";
				}else {
					log.error("目前只支持标准超时和标准预警");
					continue;
				}
				
				Map<String, Object> params2 = Maps.newHashMap();
		        int k =0;
		        int pageSize = this.pageSize;
				params2.put("scanType", scanType);//超时
				params2.put("begin", k*pageSize);
				params2.put("pageSize", pageSize);
				params2.put("scene", scene);
				params2.put("ruleId", ruleId);
				params2.put("endTime", endTime);
				params2.put("curTime",  new SimpleDateFormat(LONG_DATE_FORMAT).format(curTime));
				//entity.setParams(params2);
				//entity.setResponseType("list");
				List<Map<String, Object>> faultlist = sqlManageService.queryList("query_gvSafeFault_sla",params2);
				//List<Map<String, Object>> faultlist = (List<Map<String, Object>>) value;
				sendmsg(faultlist,typeOb,isWork,scanType,endTime,smsList);
				while(faultlist.size()==pageSize) {
					k++;
					params2.put("begin", k*pageSize);
					faultlist = sqlManageService.queryList("query_gvSafeFault_sla",params2);
					// faultlist = (List<Map<String, Object>>) value;
					 sendmsg(faultlist,typeOb,isWork,scanType,endTime,smsList);//调用发短信
				}
			}
			
			
		}
	}

	//发送短信
	private void sendmsg(List<Map<String, Object>> faultlist, JSONObject typeOb ,
			String isWork,String scanType,Double endTime,List<String> smsList) throws Exception {
		Date curTime = new Date();
		String msg = typeOb.getString("content");
		String smsType = typeOb.getString("actionType");
		
		try {
		for(Map<String, Object> fault:faultlist) {
			String optimize_expect_time = fault.get("optimize_expect_time").toString();
			//optimize_expect_time = "2021-02-26 01:00:00";
			Date expect_time;
			
				expect_time = new SimpleDateFormat(LONG_DATE_FORMAT).parse(optimize_expect_time);
				
			if(isWork.equals("1")) {
				if(scanType.equals("标准超时")) {
					//获取非工作日时间：毫秒
					long dealTime = yearlyVactionAttendanceUtil.noWorkTimeMillis(expect_time,curTime);
					
					if((curTime.getTime() - expect_time.getTime())<=(endTime.longValue()*1000+dealTime)) {//未超时
						continue;
					}
				}else {//标准预警
					long dealTime = yearlyVactionAttendanceUtil.noWorkTimeMillis(curTime, expect_time);
					
					if((expect_time.getTime() - curTime.getTime()  )>(endTime.longValue()*1000+dealTime)) {//未超时
						continue;
					}
				}
				
			}
			msg = getMsg(fault,msg);
			Map<String, Object> map = Maps.newHashMap();
			map.put("status", "success");
			String typeTitle = "邮件";
			try {
				if(smsType.equals("1")) {
					typeTitle = "短信";
					log.info("sla开发送法短信");
					SmSendHelper.send(smsList, msg);
				}else {
					log.info("sla开发送法邮件");
					String title= typeOb.getString("title");
					emailSendHelper.sendMail(title, msg, isHtml,smsList.toArray(new String[smsList.size()]));
				}
			}catch(Exception e) {
				map.put("status", "fail");
				map.put("message", e.getMessage().length()<2000?e.getMessage():e.getMessage().substring(0,1999));
				log.error("*********sla"+typeTitle+"失败*********",e);
			}
			
			log.info("开始插入数据库数据");
			
			map.put("scene", scene);
			map.put("scan_type", scanType);
			map.put("rule_id", typeOb.getString("id"));
			map.put("rel_id", fault.get("id"));
			map.put("create_time", new Date());
			iSlaMagRecordService.insert(map);
		
		}
		} catch (Exception e) {
			throw e;
		}
	}
	
	private  String getMsg(Map<String, Object> fault,String msg) {
        String pattern = "\\{[^}]*\\}";
        
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(msg);
        while (m.find()) {
            String g = m.group();
            String[] targetArr = g.substring(1, g.length() - 1).split("\\.");//去掉花括号
            String target = targetArr[0];
            if(targetArr.length>1) {
            	target = targetArr[1];
            }
          
            msg = msg.replace(g,fault.get(target)==null?"":fault.get(target).toString());
        }
        
       return msg;
    }

	private List<String> getSmsList(JSONArray userList,String actionType) {
		List<String> smsList = Lists.newArrayList();
		
		for(int i=0;i<userList.size();i++) {
			JSONObject ob = userList.getJSONObject(i);
			if(actionType.equals("1")) {
				String mobile = ob.getString("mobile");
				String MOBILE_PATTERN = "(^1(3[4-9]|47|5[0-27-9]|65|78|8[2-478]|98)\\d{8}$)|(^170[356]\\d{7}$)";
				//String MOBILE_PATTERN2 = "^(134|135|136|137|138|139|147|158|151|152|157|181|182|183|187|188)\\d{8}$";
				//Pattern.matches("^(134|135|136|137|138|139|147|150|151|152|157|181|182|183|187|188)\\d{8}$", mobile )
				if(Pattern.matches(MOBILE_PATTERN, mobile )) {
					smsList.add(mobile);
				}
			}else  if(actionType.equals("2")){
				String email = ob.getString("email");
				smsList.add(email);
			}else {
				log.error("{}通知方式不支持",actionType);
				continue;
			}
			
		}
		return smsList;
	}

  
}
