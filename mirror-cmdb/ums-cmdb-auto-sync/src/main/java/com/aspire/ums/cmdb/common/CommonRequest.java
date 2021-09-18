package com.aspire.ums.cmdb.common;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.aspire.ums.cmdb.vo.AjaxResponseObject;

public class CommonRequest {
	
	private static final Logger logger = Logger.getLogger(CommonRequest.class);
	
	public static final int statusCode = 300;

	public static final String jsonrpc = "2.0";

	private static final String ENCODING_UTF8 = "UTF-8";

	public static AjaxResponseObject get(String url) throws Exception {
		CloseableHttpResponse rsp = RestClient.getInstance().get(url);
		StatusLine line = rsp.getStatusLine();
		if (line.getStatusCode() > statusCode) {
			logger.error("请求[URL="+url+"]异常，返回码："+line.getStatusCode());
			return new AjaxResponseObject(false,"请求异常，返回码："+line.getStatusCode()); 
		}

		String result = EntityUtils.toString(rsp.getEntity(), ENCODING_UTF8);
		if (null == result || result.trim().equals("")) {
			logger.error("请求[URL="+url+"]返回数据为空！");
			return new AjaxResponseObject(false,"请求返回数据为空！"); 
		} 
		return new AjaxResponseObject(true,result);
	}
}
