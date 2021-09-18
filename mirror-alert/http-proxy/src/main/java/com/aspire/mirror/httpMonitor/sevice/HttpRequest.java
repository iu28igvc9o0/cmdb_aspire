package com.aspire.mirror.httpMonitor.sevice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.httpMonitor.common.MonitorConstant;
import com.aspire.mirror.httpMonitor.dao.po.MonitorHttpConfig;


public class HttpRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequest.class);

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public Map<String, Object> sendGet(String url, String param, MonitorHttpConfig mon) {
        Map<String, Object> responseMap = new HashMap<>();
        StringBuffer result = new StringBuffer();
        BufferedReader in = null;
        int timeOut = 15000;
        try {
            URL realUrl = new URL(getURLString(mon, param, url));
            HttpURLConnection connection = null;
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
            // 获取请求开始时间
            responseMap.put("startTime", df.format(new Date()));
            try {
               connection = (HttpURLConnection) realUrl.openConnection();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            // 设置通用的请求属性
            if (connection != null) {
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                connection.setRequestProperty("Content-Type", mon.getHttp_content_type());
                // 如果为信息头模式，则将数据封装在头部里面
                // 设置超时时间TODO
                timeOut = 15000;
                connection.setConnectTimeout(timeOut);
                if (mon.getTime_out() != null) {
                	timeOut = mon.getTime_out();
                    connection.setReadTimeout(timeOut);
                }
                // 建立实际的连接
                connection.connect();
                // 获取所有响应头字段
                Map<String, List<String>> map = connection.getHeaderFields();
                // 遍历所有的响应头字段
                responseMap.put("responseHead", map);
                int responseCode = connection.getResponseCode();
                responseMap.put("statusCode", String.valueOf(responseCode));
                // 定义 BufferedReader输入流来读取URL的响应
                if (200 == responseCode) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result.append(line);
                    }
                }
                // 获取响应结束时间
                responseMap.put("endTime", df.format(new Date()));
                Date date1 = df.parse(responseMap.get("startTime").toString());
                Date date2 = df.parse(responseMap.get("endTime").toString());
                // 获取响应耗时时间
                responseMap.put("timeCon", (date2.getTime() - date1.getTime()));
                responseMap.put("result", result.toString());
                responseMap.put("timeOut", timeOut);
                responseMap.put("normal", 1);
            }
        } catch (SocketTimeoutException e) {
            responseMap.put("result", MonitorConstant.URL_TIME_OUT + "(" + timeOut + "毫秒)");
            // 设置响应超时字段
            responseMap.put("timeOut", timeOut);
            responseMap.put("normal", 0);
            responseMap.put("conclusion", MonitorConstant.URL_TIME_OUT + "(" + timeOut + "毫秒)");
            LOGGER.error(mon.getId() + "," + mon.getMonitor_name() + "," + e.getMessage(), e);
        } catch (UnknownHostException e) {
            responseMap.put("normal", 2);
            responseMap.put("conclusion", MonitorConstant.URL_UNKNOWN_ERROR);
            LOGGER.error(mon.getId() + "," + mon.getMonitor_name() + "," + e.getMessage(), e);
        } catch (Exception e) {
            responseMap.put("normal", 3);
            responseMap.put("conclusion", MonitorConstant.URL_CONN_ERROR);
            LOGGER.error(mon.getId() + "," + mon.getMonitor_name() + "," + e.getMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return responseMap;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public Map<String, Object> sendPost(String url, String param, MonitorHttpConfig mon) {
        Map<String, Object> responseMap = new HashMap<>();
        StringBuffer result = new StringBuffer();
        BufferedReader in = null;
        int timeOut = 15000;
        try {
            URL realUrl = new URL(getURLString(mon, param, url));
            HttpURLConnection connection = null;
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
            // 获取请求开始时间
            responseMap.put("startTime", df.format(new Date()));
            try {
               connection = (HttpURLConnection) realUrl.openConnection();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            // 设置通用的请求属性
            if (connection != null) {
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                connection.setRequestProperty("Content-Type", mon.getHttp_content_type());//"application/json"
                // 如果为信息头模式，则将数据封装在头部里面
                // 设置超时时间TODO
                connection.setConnectTimeout(timeOut);
                if (mon.getTime_out() != null) {
                	timeOut = mon.getTime_out();
                    connection.setReadTimeout(timeOut);
                }
             // 发送POST请求必须设置如下两行
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false); 
                connection.setRequestMethod("POST");
                // 建立实际的连接
                connection.connect();
                if (mon.getHttp_content_type() != null && mon.getHttp_content_type().contains("json")) {
					/*
					 * OutputStreamWriter writer = new
					 * OutputStreamWriter(connection.getOutputStream()); // 发送JSON参数 if (param !=
					 * null) { writer.write(param); writer.flush(); writer.close(); }
					 */
                    
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    writer.write(param);
                    writer.close();
                }
                // 获取所有响应头字段
                Map<String, List<String>> map = connection.getHeaderFields();
                // 遍历所有的响应头字段
                responseMap.put("responseHead", map);
                int responseCode = connection.getResponseCode();
                responseMap.put("statusCode", String.valueOf(responseCode));
                // 定义 BufferedReader输入流来读取URL的响应
                if (200 == responseCode) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result.append(line);
                    }
                }
                responseMap.put("endTime", df.format(new Date()));
                Date date1 = df.parse(responseMap.get("startTime").toString());
                Date date2 = df.parse(responseMap.get("endTime").toString());
                responseMap.put("timeCon", (date2.getTime() - date1.getTime()));
                responseMap.put("result", result.toString());
                responseMap.put("timeOut", timeOut);
                responseMap.put("normal", 1);
            }
        } catch (SocketTimeoutException e) {
            responseMap.put("result", MonitorConstant.URL_TIME_OUT + "(" + timeOut + "毫秒)");
            // 设置响应超时字段
            responseMap.put("timeOut", timeOut);
            responseMap.put("normal", 0);
            responseMap.put("conclusion", MonitorConstant.URL_TIME_OUT + "(" + timeOut + "毫秒)");
            LOGGER.error(mon.getId() + "," + mon.getMonitor_name() + "," + e.getMessage(), e);
        } catch (UnknownHostException e) {
            responseMap.put("normal", 2);
            responseMap.put("conclusion", MonitorConstant.URL_UNKNOWN_ERROR);
            LOGGER.error(mon.getId() + "," + mon.getMonitor_name() + "," + e.getMessage(), e);
        } catch (Exception e) {
            responseMap.put("normal", 3);
            responseMap.put("conclusion", MonitorConstant.URL_CONN_ERROR);
            LOGGER.error(mon.getId() + "," + mon.getMonitor_name() + "," + e.getMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return responseMap;
    }

    /**
     * 对返回结果进行校验
     *
     * @param Mon
     * @param responseResult
     * @return String
     */
	
    /**
     * 对返回结果进行校验
     * 
     * @param mon
     * @param responseResult
     * @return String
     */
    public String ruleCheck(MonitorHttpConfig mon, String responseResult,String statusCode) {
        String conclusion = MonitorConstant.URL_CONCLUSION_ABNORMAL;
        // 判断是否为200
            if (mon.getResponse_code() ==1) {//200
            	if("200".equals(statusCode)) {
	        		 if (mon.getResponse_type().equals("JSON")) {
	                     conclusion = judgeJson(mon, responseResult);
	                 } else if (mon.getResponse_type().equals("HTML")) {
	                     conclusion = judgeHtml(mon.getHtml_format(), responseResult);
	                 }
            	}else {
            		conclusion = MonitorConstant.CONCLUSION_STATUS;
            	}
               
            } else {
            	if (!"200".equals(statusCode)) {
	            	String[] nonRegulars = mon.getRegular_check().split(",");
	                if (1 < nonRegulars.length) {
	                    StringBuilder conclusions = new StringBuilder();
	                    for (int i = 0; i < nonRegulars.length; i++) {
	                        conclusion = judgeHtml(nonRegulars[i].replace("\\\\", "\\"), statusCode);
	                        conclusions.append(conclusion);
	                    }
	                    if (conclusions.toString().contains(MonitorConstant.CONCLUSION_NORMAL)) {
	                        conclusion = MonitorConstant.CONCLUSION_NORMAL;
	                    } else {
	                        conclusion = MonitorConstant.HTML_CONCLUSION_ABNORMAL;
	                    }
	                }else {
	                    // 非200
	                    conclusion = judgeHtml(mon.getRegular_check().replace("\\\\", "\\"), statusCode);
	                }
            	}else {
                    // 非200
            		 conclusion = MonitorConstant.CONCLUSION_STATUS;
                }
            }
        return conclusion;
    }

    /**
     * 对返回的JSON数据进行校验
     *
     * @param mon
     * @param responseResult
     * @return String
     */
    public String judgeJson(MonitorHttpConfig mon, String responseResult) {
        String conclusion = MonitorConstant.JSON_CONCLUSION_ABNORMAL;
        boolean json = isJson(responseResult);
        try {
            if (json) {
                String checkResult = checkJsonValue(responseResult, mon.getJson_attribute());
                if (checkResult != null) {
                    if (MonitorConstant.JSON_COMPARE_TYPE_LARGE.equals(mon.getJson_operator())) {
                        Integer intJsonAttribute = Integer.parseInt(checkResult);
                        Integer intJsonValue = Integer.parseInt(mon.getJson_value());
                        if (intJsonAttribute >= intJsonValue) {
                            conclusion = MonitorConstant.CONCLUSION_NORMAL;
                        }
                    } else if (MonitorConstant.JSON_COMPARE_TYPE_LARGE_ETC.equals(mon.getJson_operator())) {
                        Integer intJsonAttribute = Integer.parseInt(checkResult);
                        Integer intJsonValue = Integer.parseInt(mon.getJson_value());
                        if (intJsonAttribute < intJsonValue) {
                            conclusion = MonitorConstant.CONCLUSION_NORMAL;
                        }
                    } else if (MonitorConstant.JSON_COMPARE_TYPE_SMALL.equals(mon.getJson_operator())) {
                        Integer intJsonAttribute = Integer.parseInt(checkResult);
                        Integer intJsonValue = Integer.parseInt(mon.getJson_value());
                        if (intJsonAttribute <= intJsonValue) {
                            conclusion = MonitorConstant.CONCLUSION_NORMAL;
                        }
                    } else if (MonitorConstant.JSON_COMPARE_TYPE_SMALL_ETC.equals(mon.getJson_operator())) {
                        if (checkResult.equals(mon.getJson_value())) {
                            conclusion = MonitorConstant.CONCLUSION_NORMAL;
                        }
                    } else if (MonitorConstant.JSON_COMPARE_TYPE_SMALL_CONTAIN.equals(mon.getJson_operator())) {
                        if (checkResult.contains(mon.getJson_value())) {
                            conclusion = MonitorConstant.CONCLUSION_NORMAL;
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            return MonitorConstant.JSON_CONCLUSION_ABNORMAL;
        }
        return conclusion;
    }

    /**
     * 对指定的HTML内容进行正则校验
     *
     * @param regular
     * @param responseResult
     * @return String
     */
    public String judgeHtml(String regular, String responseResult) {
        try {
            String result = MonitorConstant.HTML_CONCLUSION_ABNORMAL;
            Matcher m = Pattern.compile(regular).matcher(responseResult);
            if (m.find()) {
                result = MonitorConstant.CONCLUSION_NORMAL;
            }
            return result;
        } catch (Exception e) {
            return MonitorConstant.HTML_CONCLUSION_ABNORMAL;
        }
    }

    /**
     * 判断是否是json结构
     *
     * @param value
     * @return boolean
     */
    public boolean isJson(String value) {
        try {
            JSONObject.parseObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    /**
     * 验证请求的json数据是否正常
     *
     * @param responseResult
     * @param expression
     * @return
     */
    public String checkJsonValue(String responseResult, String expression) {
        String result = null;
        try {
            JSONObject json = JSONObject.parseObject(responseResult);
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            String[] exSplit = expression.split("\\.");
            for (int i = 0; i < exSplit.length; i++) {
                if (exSplit[i].contains("[")) {
                    String[] ss = exSplit[i].split("\\[");
                    String aa = ss[1].substring(0, 1);

                    map.put("name" + i + "list" + aa, ss[0]); // 判断是否含有List对象,如：'child':[{'name':'222','child':[{'mmmm':'333'}]},{'name':'2221'}]
                } else {
                    if (i < exSplit.length - 1) {
                        map.put("name" + i + "object", exSplit[i]); // 判断是否含有Oject对象，如：'name':{'name':'2aa1'}
                    } else {
                        map.put("name" + i, exSplit[i]);
                    }
                }
            }

            JSONObject flag = json; // 标志，用来存储每次循环后的下一个对象
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().contains("list")) { // 判断是否含有List对象
                    String value = entry.getKey().substring(entry.getKey().length() - 1, entry.getKey().length()); // 截取字符串最后一位
                    JSONArray obj = (JSONArray) flag.get(entry.getValue());
                    if (obj.size() == 0) {
                        return null;
                    }
                    Object objList = obj.get(Integer.parseInt(value));
                    flag = (JSONObject) objList;
                } else if (entry.getKey().contains("object")) { // 判断是否含有Oject对象
                    Object objObject = flag.get(entry.getValue());
                    flag = JSONObject.parseObject(JSONObject.toJSONString(objObject));
                } else {
                    result = flag.get(entry.getValue()).toString();
                }
            }
        } catch (Exception  e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        return result;
    }

    /*public String getToken1(MonitorWebAuth auth){
    	BigDecimal intervalTime = auth.getIntervalTime();
    	//DateFormat df = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS");
    	Date now = new Date();
    	if(auth.getId()!=null){
    		if(BigDecimal.valueOf(0).equals(intervalTime)){//当间隔时间为0
            	String token = getToken(auth);
            	return token;
            }else{//当间隔时间大于0
            	int intValueTime = intervalTime.intValue();
    			int t = intValueTime * 60 * 1000;
            	if(StringUtils.isEmpty(auth.getTokenValue())){
        			String token = getToken(auth);
        			Date afterDate = new Date(now.getTime() + t);
        			auth.setIntervalTime(intervalTime);
            		auth.setTokenValue(token);
            		auth.setValidityTime(afterDate);
            		authMapper.updateByPrimaryKeySelective(auth);
            		return token;
        		}else{
        			Date date = new Date();
        			if(auth.getValidityTime().getTime() - date.getTime()>=0){
        				String tokenValue = auth.getTokenValue();
        				return tokenValue;
        			}else{
        				String token = getToken(auth);
        				Date afterDate = new Date(now .getTime() + t);
        				auth.setIntervalTime(intervalTime);
        				auth.setTokenValue(token);
        				auth.setValidityTime(afterDate);
        				authMapper.updateByPrimaryKeySelective(auth);
        				return token;
        			}
        		}
        	}
            }else{
            	String token = getToken(auth);
            	return token;
            }
    }*/




    /**
     * 获取url的拼接访问地址
     *
     * @param mon
     * @return
     */
    public String getURLString(MonitorHttpConfig mon, String param, String url) {
        String urlNameString = url;
        // 进行正常\异常检测的时候运行的路线
        if (StringUtils.isNotEmpty(param) && mon.getHttp_content_type() != null
                && !mon.getHttp_content_type().contains("json")) {
            urlNameString = url + "?" + param;
        }
        
        return urlNameString;
    }

    private static HttpURLConnection disableSslCheckInUrlConnection(String url) {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);

            return connection;

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
