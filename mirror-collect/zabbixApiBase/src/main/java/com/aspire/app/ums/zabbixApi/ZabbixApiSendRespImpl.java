package com.aspire.app.ums.zabbixApi;

import com.aspire.app.ums.http.HttpClientService;

import net.sf.json.JSONObject;

/** 
*
* 项目名称: pentaho-zabbix 
* <p/>
* 
* 类名: ZabbixApiSendRespImpl
* <p/>
*
* 类功能描述: zabbixApi请求发送接收接口默认实现
* <p/>
*
* @author	pengguihua
*
* @date	2016年4月21日  
*
* @version	V1.0 
* <br/>
*
* <b>Copyright(c)</b> 2016 卓望公司-版权所有 
*
*/
class ZabbixApiSendRespImpl implements IZabbixApiSendResp {
	
	@Override
	public JSONObject request(String targetURL, String jsonText) throws Exception {
		HttpClientService httpClient = new HttpClientService(targetURL);
		return httpClient.postJson(jsonText, HttpClientService.getResponseJsonHandler());
	}
	
	@Override
	public JSONObject request(String targetURL, JSONObject queryJson) throws Exception {
		String jsonText = queryJson == null ? null : queryJson.toString();
		return request(targetURL, jsonText);
	}
	
	@Override
	public String request(String targetURL, String jsonText, String key) throws Exception {
		JSONObject respObj = request(targetURL, jsonText);
		if (respObj == null) {
			return null;
		}
		Object val = respObj.get(key);
		if (val == null) {
			return null;
		}
		return val.toString();
	}
	
	@Override
	public String request(String targetURL, JSONObject queryJson, String key) throws Exception {
		String jsonText = queryJson == null ? null : queryJson.toString();
		return request(targetURL, jsonText, key);
	}

}
