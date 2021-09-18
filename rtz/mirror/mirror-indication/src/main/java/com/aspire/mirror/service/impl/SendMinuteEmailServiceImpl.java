package com.aspire.mirror.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.common.EnvConfigProperties;
import com.aspire.mirror.service.IIndicationDataService;
import com.aspire.mirror.service.SendMinuteEmailService;
import com.aspire.mirror.util.EmailUtil;
import com.aspire.mirror.util.IndicationConst;
import com.cmcc.family.alertagent.AlertAgent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Slf4j
public class SendMinuteEmailServiceImpl implements SendMinuteEmailService {

    @Autowired
    private IIndicationDataService dataService;

    @Autowired
    private EnvConfigProperties envConfigProperties;

    @Override
    public String autoSendMinuteEmail(String indicationOwner, String indicationCatalog, String indicationCycle,
            String indicationFrequency, String dateTime) {
		//获取异常数据
		/*
		 * Date time = new Date(); try { time = DateUtils.parseDate(dateTime,
		 * new String[]
		 * {TimeUtil.DATE_FORMAT_YYYYMMDDHHMM,TimeUtil.DATE_FORMAT_YYYYMMDD,
		 * TimeUtil.DATE_FORMAT_YYYY_MM_DD}); } catch (ParseException e) {
		 * log.error("时间格式错误{}",dateTime,e); } Calendar calendar =
		 * Calendar.getInstance(); calendar.setTime(time);
		 * calendar.add(Calendar.MINUTE, -1); String start =
		 * DateFormatUtils.format(calendar.getTime(),
		 * TimeUtil.DATE_FORMAT_YYYYMMDDHHMM);
		 */
		JSONObject exceptionData = dataService.getExceptionData(indicationOwner, indicationCatalog, indicationCycle, indicationFrequency, dateTime, null, null, null);;
		String result = sendMinuteEmail(indicationOwner, exceptionData,dateTime,indicationCatalog);
		return String.format("[%s]平台-[%s]-邮件发送结果:%s", indicationOwner,indicationCatalog, result);
	}
    
    private String sendMinuteEmail(String indicationOwner, JSONObject exceptionData, String dateTime, String type) {
        log.info("开始发送{}-{}邮件...", dateTime, type);
        return getMinuteEmailIndicationBuffer(exceptionData,dateTime);
    }
    
    
    @SuppressWarnings("unchecked")
    private String getMinuteEmailIndicationBuffer(JSONObject obj,String dateTime) {
        JSONArray allCountryIndicationData = obj.getJSONArray("全国14天");

        for (int i = 0; i < allCountryIndicationData.size(); i++) {
            //组装每个全国指标的邮件内容
            StringBuffer indicationBuffer = new StringBuffer();
            Map<String,Object> indicationData = Maps.newHashMap();
            indicationData = (Map<String,Object>)allCountryIndicationData.get(i);
            String indicationId = String.valueOf(indicationData.get("indicationId"));
            List<Map<String,Object>> data = (List<Map<String,Object>>)indicationData.get("data");
            if(null == data || data.size() ==0) {
            	continue;
            }
            Map<String,Object> firstData = data.get(0);
            String cause = (String)firstData.get("异常原因");
            //cause = "";
            if(StringUtils.isNotBlank(cause)) {
            	if(!EmailUtil.isContinueAlarm(indicationId, dateTime)) {
            		continue;
            	}else {
            		  //拼装省份邮件
	        		 List<Map<String,Object>> list = Lists.newArrayList();
	                 list.add(firstData);
                    String title = IndicationConst.KADUN_MINUTE_COUNTRY_TITLE_TEMPLATE;
                    String model = IndicationConst.KADUN_MINUTE_COUNTRY_CONTENT_TEMPLATE;
                    String year = dateTime.substring(0,4);
                    String month = dateTime.substring(4,6);
                    String day = dateTime.substring(6,8);
                    String hour = dateTime.substring(8,10);
                    String minute = dateTime.substring(10,12);
                    StringBuffer sb = new StringBuffer();
                    sb.append(year).append("年").append(month).append("月").append(day).append("日").
                    append(hour).append("时").append(minute).append("分");
                    indicationBuffer.append(this.getTable(list,sb.toString()));
                    hour = hour.startsWith("0") ? hour.substring(1,2) : hour;
                    title = String.format(title, year, month,day,hour,minute);
                    model = String.format(model, year, month,day,hour,minute,indicationBuffer.toString());
                    log.info("由kafka将信息通过Email发送  请求 ---> 开始");
                    log.info("发送邮件内容:"+model);
                    final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
                    alert.sendAlert(envConfigProperties.getRealNationalWeb().getAutoSendTopic(), title, model, false);
                    log.info("由kafka将信息通过Email发送  请求 ---> 结束");
                    return "SUCCESS";
            	}
            }else{
            	if(EmailUtil.isRestoreAlarm(indicationId, dateTime)) {
            		 //拼装省份邮件
                    String title = IndicationConst.KADUN_MINUTE_COUNTRY_RESTORE_TITLE_TEMPLATE;
                    String model = IndicationConst.KADUN_MINUTE_COUNTRY_RESTORE_CONTENT_TEMPLATE;
                    String year = dateTime.substring(0,4);
                    String month = dateTime.substring(4,6);
                    String day = dateTime.substring(6,8);
                    String hour = dateTime.substring(8,10);
                    String minute = dateTime.substring(10,12);
                    hour = hour.startsWith("0") ? hour.substring(1,2) : hour;
                    title = String.format(title, year, month,day,hour,minute);
                    model = String.format(model, year, month,day,hour,minute);
                    log.info("由kafka将信息通过Email发送  请求 ---> 开始");
                    log.info("发送邮件内容:"+model);
                    final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
                    alert.sendAlert(envConfigProperties.getRealNationalWeb().getAutoSendTopic(), title, model, false);
                    log.info("由kafka将信息通过Email发送  请求 ---> 结束");
                    return "SUCCESS";
            	}else {
            		continue;
            	}
            }
          }
        return "无异常数据，无需发送邮件";
    }



    /**
     * 设置邮件表格
     * @param list
     * @return
     */
    private StringBuffer getTable(List<Map<String,Object>> list,String dateStr) {
        //表格部分
        StringBuffer tableBuffer = new StringBuffer();
        
        tableBuffer.append("<table style=\"border-collapse:collapse; width:100%\">");
		/*
		 * StringBuffer thBuffer = new StringBuffer(); thBuffer.
		 * append("<th style=\"border: 1px solid #000; text-align:center; font-size:13px\">"
		 * ) .append("省份").append("</th>"); thBuffer.
		 * append("<th style=\"border: 1px solid #000; text-align:center; font-size:13px\">"
		 * ) .append("发生异常时间").append("</th>"); thBuffer.
		 * append("<th style=\"border: 1px solid #000; text-align:center; font-size:13px\">"
		 * ) .append("卡顿告警个数").append("</th>"); thBuffer.
		 * append("<th style=\"border: 1px solid #000; text-align:center; font-size:13px\">"
		 * ) .append("异常原因").append("</th>");
		 */

		
		 //设置表头 
        StringBuffer thBuffer = new StringBuffer(); 
        for (String thKey : list.get(0).keySet()) { 
        	if (!"indicationId".equals(thKey)) {
        		if(thKey.equals("省份") || thKey.equals("异常时间点") ||thKey.equals("告警数") ||thKey.equals("异常原因") ) {
        			thBuffer.append("<th style=\"border: 1px solid #000; text-align:center; font-size:13px\">"
        					  ) .append(thKey).append("</th>"); } }
        		}
        		
		 
        tableBuffer.append("<tr>").append(thBuffer).append("</tr>");
        //设置正文
        for (Map<String,Object> trData : list) {
            StringBuffer trBuffer = new StringBuffer();
            for (String trKey : trData.keySet()) {
                if (!"indicationId".equals(trKey)) {
                	if(trKey.equals("省份") || trKey.equals("异常时间点") ||trKey.equals("告警数") ||trKey.equals("异常原因") ) {
                		String val = trData.get(trKey).toString();
                		if(trKey.equals("告警数")) {
                			val = val.split("\\.")[0];
            			}
                		if(trKey.equals("异常时间点")) {
                			trBuffer.append("<td style=\"border: 1px solid #000; text-align:center; font-size:13px\">")
                            .append(dateStr).append("</td>");
                		}else {
                			trBuffer.append("<td style=\"border: 1px solid #000; text-align:center; font-size:13px\">")
                            .append(val).append("</td>");
                		}
                	}
                }
            }
            tableBuffer.append("<tr>").append(trBuffer).append("</tr>");
        }
        tableBuffer.append("</table>").append("<br/>");
        return tableBuffer;
    }
}
