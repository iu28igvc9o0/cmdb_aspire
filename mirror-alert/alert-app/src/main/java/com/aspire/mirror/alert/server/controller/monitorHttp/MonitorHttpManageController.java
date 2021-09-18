package com.aspire.mirror.alert.server.controller.monitorHttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpConfigResponse;
import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpHisResponse;
import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpIdcTypeResponse;
import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpReqObj;
import com.aspire.mirror.alert.api.service.monitorHttp.MonitorHttpManageService;
import com.aspire.mirror.alert.server.biz.monitorHttp.MonitorHttpManageBiz;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.constant.MonitorConstant;
import com.aspire.mirror.alert.server.dao.monitorHttp.MonitorHttpIdcTypeDao;
import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpConfig;
import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpHis;
import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpIdcType;
import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpReq;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;

@RestController
public class MonitorHttpManageController implements MonitorHttpManageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorHttpManageController.class);
	private static final String HTTP_CHARSET = "UTF-8";
	@Autowired
	private MonitorHttpManageBiz monitorHttpManageBiz;

	@Autowired
	private MonitorHttpIdcTypeDao monitorHttpIdcTypeDao;

	@Value("${extranet_addr}")
	private String extranet_addr;

	@Override
	public PageResponse<MonitorHttpConfigResponse> pageList(@RequestBody MonitorHttpReqObj pageRequset) {
		if (pageRequset == null) {
			LOGGER.warn("pageList param pageRequset is null");
			return null;
		}
		MonitorHttpReq req = new MonitorHttpReq();
		BeanUtils.copyProperties(pageRequset, req);
		PageResponse<MonitorHttpConfig> pageResult = monitorHttpManageBiz.pageList(req);

		List<MonitorHttpConfigResponse> listAlert = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(pageResult.getResult())) {
			for (MonitorHttpConfig http : pageResult.getResult()) {
				MonitorHttpConfigResponse res = new MonitorHttpConfigResponse();
				BeanUtils.copyProperties(http, res);
				/*
				 * MonitorHttpHis his = http.getMonitorHttpHis(); if (null != his) {
				 * MonitorHttpHisResponse hisRes = new MonitorHttpHisResponse();
				 * BeanUtils.copyProperties(his, hisRes); res.setMonitorHttpHis(hisRes); }
				 */
				listAlert.add(res);
			}
		}

		PageResponse<MonitorHttpConfigResponse> result = new PageResponse<MonitorHttpConfigResponse>();
		result.setCount(pageResult.getCount());
		result.setResult(listAlert);
		return result;
	}

	@Override
	public MonitorHttpConfigResponse findByPrimaryKey(@PathVariable("id") String id) {
		if (StringUtils.isEmpty(id)) {
			LOGGER.warn("findByPrimaryKey param id is null");
			return null;
		}
		MonitorHttpConfig res = monitorHttpManageBiz.selectByPrimaryKey(id);
		if (null == res) {
			return null;
		}
		MonitorHttpConfigResponse resDTO = new MonitorHttpConfigResponse();
		BeanUtils.copyProperties(res, resDTO);
		return resDTO;
	}

	@Override
	public ResponseEntity<String> create(@RequestBody MonitorHttpConfigResponse createRequest) {
		if (createRequest == null) {
			LOGGER.error("create createRequest is null");
			throw new RuntimeException("create param is null");
		}
		MonitorHttpConfig c = monitorHttpManageBiz.getByName(createRequest.getMonitor_name());
		if (null != c) {
			return new ResponseEntity<String>("fail", HttpStatus.OK);
		}

		MonitorHttpConfig res = new MonitorHttpConfig();
		BeanUtils.copyProperties(createRequest, res);
		res.setCreate_time(new Date());

		String url = createRequest.getIdcTypeUrl();
		if (res.getIsIntranet() == 0) {
			res.setExtranet(extranet_addr);
			url = extranet_addr;
		}
		MonitorHttpConfig config = monitorHttpManageBiz.insert(res);

		// Executors.newSingleThreadExecutor().execute();
		try {
			url += "/httpMonitor/add";
			String param = "configId=" + config.getId();
			url = url + "?" + param;
			httpPost(param, url);
		} catch (Exception e) {
			LOGGER.error("创建监控，请求代理记账失败", e);
		}
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	public static MonitorHttpHisResponse httpPost(final String param, final String url) {
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse httpResponse = null;
		try {
			final HttpPost httpPost = new HttpPost(url);
			// httpPost.setHeader("Content-Type", "application/json");
			final StringEntity entity = new StringEntity(param, HTTP_CHARSET);
			httpPost.setEntity(entity);
			httpResponse = httpClient.execute(httpPost);
			// his = new MonitorHttpHisResponse();
			/*
			 * Header[] headers = httpResponse.getAllHeaders(); for(Header h:headers) {
			 * headerResult.append(h.getName()) ; headerResult.append(":");
			 * headerResult.append(h.getValue()); headerResult.append("\n"); }
			 * 
			 * his.setHead_response(headerResult.toString());
			 */
			/*
			 * in = new BufferedReader(new
			 * InputStreamReader(httpResponse.getEntity().getContent())); String line; while
			 * ((line = in.readLine()) != null) { result.append(line); } String rs =
			 * result.toString(); MonitorHttpHis rsHis = JSON.parseObject(rs,
			 * MonitorHttpHis.class); BeanUtils.copyProperties(rsHis, his);
			 */
			/*
			 * String conclusion = result.toString(); if(conclusion.contains("正常")) {
			 * his.setResult(1); }else { his.setResult(0); }
			 * his.setRequest_result(result.toString());
			 */
			LOGGER.debug("POST Response Status:" + httpResponse.getStatusLine().getStatusCode());
		} catch (UnsupportedCharsetException e) {
			LOGGER.error("send post error:{}", e);
		} catch (ClientProtocolException e) {
			LOGGER.error("send post error:{}", e);
		} catch (IOException e) {
			LOGGER.error("send post error:{}", e);
		} finally {
			IOUtils.closeQuietly(httpClient);
		}
		return null;
	}

	public static MonitorHttpHisResponse httpPostTest(final MonitorHttpConfigResponse mon, final String url) {
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse httpResponse = null;
		StringBuffer result = new StringBuffer();
		// StringBuffer headerResult = new StringBuffer();
		MonitorHttpHisResponse his =  new MonitorHttpHisResponse();
		BufferedReader in = null;
		try {
			final HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json");
			final StringEntity entity = new StringEntity(JSON.toJSONString(mon), HTTP_CHARSET);
			httpPost.setEntity(entity);
			httpResponse = httpClient.execute(httpPost);
			int code = httpResponse.getStatusLine().getStatusCode();
			
			
			
			if(code == 200) {
				in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				String line;
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
				String rs = result.toString();
				his = JSON.parseObject(rs, MonitorHttpHisResponse.class);
			}else {
				 in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                 String line;
                 while ((line = in.readLine()) != null) {
                     result.append(line);
                 }
                 StringBuffer headerResult = new StringBuffer();
				Header[] headers = httpResponse.getAllHeaders();
				for (Header h : headers) {
					headerResult.append(h.getName());
					headerResult.append(":");
					headerResult.append(h.getValue());
					headerResult.append("\n");
				}
     			  
     			  his.setHead_response(headerResult.toString());
     			 
				his.setHead_response(headerResult.toString());
				his.setRequest_result(result.toString());
				his.setResult(0);
				his.setResponse_code(code);
				//his.setRequest_result(request_result);
			}
			
			/*
			 * String conclusion = result.toString(); if(conclusion.contains("正常")) {
			 * his.setResult(1); }else { his.setResult(0); }
			 * his.setRequest_result(result.toString());
			 */
			LOGGER.debug("POST Response Status:" + httpResponse.getStatusLine().getStatusCode());
		 } catch (SocketTimeoutException e) {
			 	String message = String.format("代理机%s(%s毫秒)", MonitorConstant.URL_TIME_OUT , mon.getTime_out());
			 	his.setRequest_result(message);
	            // 设置响应超时字段
	            his.setConclusion(message);
	            his.setResult(0);
	            LOGGER.error(message,e);
	        } catch (UnknownHostException e) {
	        	String message = "代理机"+ MonitorConstant.URL_UNKNOWN_ERROR ;
	        	his.setRequest_result(message);
	            // 设置响应超时字段
	            his.setConclusion(message);
	            his.setResult(0);
	            LOGGER.error(message,e);
	        } catch (Exception e) {
	        	String message = "代理机"+ MonitorConstant.URL_CONN_ERROR ;
	        	his.setRequest_result(message);
	            // 设置响应超时字段
	            his.setConclusion(message);
	            his.setResult(0);
	            LOGGER.error(message,e);
	        } finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			IOUtils.closeQuietly(httpClient);
			
		}
		return his;
	}

	@Override
	public ResponseEntity<String> update(@RequestBody MonitorHttpConfigResponse updateRequest) {

		if (updateRequest == null) {
			LOGGER.warn("update updateRequest note is null");
			return null;
		}

		MonitorHttpConfig res = new MonitorHttpConfig();
		BeanUtils.copyProperties(updateRequest, res);

		String url = updateRequest.getIdcTypeUrl();
		if (res.getIsIntranet() == 0) {
			res.setExtranet(extranet_addr);
			url = extranet_addr;
		}
		monitorHttpManageBiz.update(res);

		if (res.getStatus() == 1) {
			try {
				url += "/httpMonitor/modify";
				String param = "configId=" + res.getId();
				url = url + "?" + param;
				httpPost(param, url);
			} catch (Exception e) {
				LOGGER.error("跟新监控，请求代理记账失败", e);
			}
		}
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> updateStatus(@RequestBody MonitorHttpConfigResponse updateRequest) {

		if (updateRequest == null) {
			LOGGER.warn("update updateRequest note is null");
			return null;
		}
		MonitorHttpConfig res = new MonitorHttpConfig();
		BeanUtils.copyProperties(updateRequest, res);
		monitorHttpManageBiz.updateStatus(res);

		String url = updateRequest.getIdcTypeUrl() + "/httpMonitor/pause";
		if (updateRequest.getIsIntranet() == 0) {
			url = updateRequest.getExtranet() + "/httpMonitor/pause";
		}
		if (updateRequest.getStatus() == 1) {
			url = updateRequest.getIdcTypeUrl() + "/httpMonitor/resume";
		}
		try {
			String param = "configId=" + updateRequest.getId();
			url += "?" + param;
			httpPost(param, url);
		} catch (Exception e) {
			LOGGER.error("暂停或者运行监控，请求代理记账失败", e);
		}
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> delete(@PathVariable("id") String id) {
		if (StringUtils.isEmpty(id)) {
			LOGGER.error("delete id is null");
			throw new RuntimeException("delete id is null");
		}
		MonitorHttpConfig c = monitorHttpManageBiz.selectByPrimaryKey(id);
		monitorHttpManageBiz.delete(id);
		String url = c.getIdcTypeUrl() + "/httpMonitor/delete";
		if (c.getIsIntranet() == 0) {
			url = c.getExtranet() + "/httpMonitor/delete";
		}
		try {
			String param = "configId=" + id + "&name=" + c.getMonitor_name();
			url = url + "?" + param;
			httpPost(param, url);
		} catch (Exception e) {
			LOGGER.error("删除监控，请求代理记账失败", e);
		}
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public MonitorHttpConfigResponse findByName(@PathVariable("name") String name) {
		if (StringUtils.isEmpty(name)) {
			LOGGER.error("findByName name is null");
			throw new RuntimeException("findByName name is null");
		}
		MonitorHttpConfig res = monitorHttpManageBiz.getByName(name);
		if (null == res) {
			return null;
		}
		MonitorHttpConfigResponse resDTO = new MonitorHttpConfigResponse();
		BeanUtils.copyProperties(res, resDTO);
		return resDTO;
	}

	@Override
	public PageResponse<MonitorHttpHisResponse> pageListHis(@RequestBody MonitorHttpReqObj pageRequset) {
		if (pageRequset == null) {
			LOGGER.warn("pageListHis param pageRequset is null");
			return null;
		}
		MonitorHttpReq req = new MonitorHttpReq();
		BeanUtils.copyProperties(pageRequset, req);
		PageResponse<MonitorHttpHis> pageResult = monitorHttpManageBiz.pageListHis(req);

		List<MonitorHttpHisResponse> listAlert = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(pageResult.getResult())) {
			for (MonitorHttpHis http : pageResult.getResult()) {
				MonitorHttpHisResponse res = new MonitorHttpHisResponse();
				BeanUtils.copyProperties(http, res);
				listAlert.add(res);
			}
		}

		PageResponse<MonitorHttpHisResponse> result = new PageResponse<MonitorHttpHisResponse>();
		result.setCount(pageResult.getCount());
		result.setResult(listAlert);
		return result;
	}

	@Override
	public ResponseEntity<String> batchCreateHis(@RequestBody List<MonitorHttpHisResponse> createRequest) {
		if (createRequest == null) {
			LOGGER.error("create createRequest is null");
			throw new RuntimeException("create param is null");
		}
		List<MonitorHttpHis> listAlert = Lists.newArrayList();
		for (MonitorHttpHisResponse val : createRequest) {
			MonitorHttpHis res = new MonitorHttpHis();
			BeanUtils.copyProperties(val, res);
			res.setCreate_time(new Date());
			listAlert.add(res);
		}
		monitorHttpManageBiz.batchInsertHis(listAlert);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public MonitorHttpHisResponse findHisByPrimaryKey(@PathVariable("id") String id) {
		if (StringUtils.isEmpty(id)) {
			LOGGER.warn("findHisByPrimaryKey param id is null");
			return null;
		}
		MonitorHttpHis res = monitorHttpManageBiz.selectHisByPrimaryKey(id);
		if (null == res) {
			return null;
		}
		MonitorHttpHisResponse resDTO = new MonitorHttpHisResponse();
		BeanUtils.copyProperties(res, resDTO);
		return resDTO;
	}

	/**
	 * 监控kafka消费消息
	 */
	// @KafkaListener(id = "batch",topics = {"topic.quick.batch"},containerFactory =
	// "batchContainerFactory")
	private void batchListener(List<String> response) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("topic -> http_monitor_topic get kafka messages -> {}", response);
		}
		List<MonitorHttpHis> hisList = Lists.newArrayList();
		if (response != null) {
			for (String val : response) {
				MonitorHttpHis his = JSONObject.parseObject(val, MonitorHttpHis.class);
				// monitorHttpManageBiz.ins
				String conclution = his.getConclusion();
				if (!conclution.equals("正常")) {
					// TODO
				}
				hisList.add(his);
			}
			monitorHttpManageBiz.batchInsertHis(hisList);
		}

	}

	@Override
	public List<MonitorHttpIdcTypeResponse> getIdcTypes() {
		List<MonitorHttpIdcType> idcTypes = monitorHttpIdcTypeDao.getAll();
		List<MonitorHttpIdcTypeResponse> listAlert = Lists.newArrayList();
		for (MonitorHttpIdcType val : idcTypes) {
			MonitorHttpIdcTypeResponse res = new MonitorHttpIdcTypeResponse();
			BeanUtils.copyProperties(val, res);
			listAlert.add(res);
		}
		return listAlert;
	}

	@Override
	public MonitorHttpHisResponse testHttp(@RequestBody MonitorHttpConfigResponse createRequest) {
		String url = createRequest.getIdcTypeUrl() + "/httpMonitor/testHttp";
		if (createRequest.getIsIntranet() == 0) {
			url = createRequest.getExtranet() + "/httpMonitor/testHttp";
		}

		MonitorHttpHisResponse rs = httpPostTest(createRequest, url);
		return rs;
	}

	public static void main(String[] args) {
		String url = "http://localhost:28503/httpMonitor/add";
		Map<String, String> params = new HashMap<String, String>();
		params.put("configId", "10");
		httpPost(JSON.toJSONString(params), url);
	}

}
