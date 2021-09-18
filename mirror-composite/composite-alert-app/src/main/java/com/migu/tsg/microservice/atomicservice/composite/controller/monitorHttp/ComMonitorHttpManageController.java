package com.migu.tsg.microservice.atomicservice.composite.controller.monitorHttp;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

import java.util.List;

import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpConfigResponse;
import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpHisResponse;
import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpIdcTypeResponse;
import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpReqObj;
import com.aspire.mirror.composite.payload.monitorHttp.ComMonitorHttpConfigResponse;
import com.aspire.mirror.composite.payload.monitorHttp.ComMonitorHttpHisResponse;
import com.aspire.mirror.composite.payload.monitorHttp.ComMonitorHttpIdcTypeResponse;
import com.aspire.mirror.composite.payload.monitorHttp.ComMonitorHttpReq;
import com.aspire.mirror.composite.service.monitorHttp.IComMonitorHttpManageService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.monitorHttp.MonitorHttpManageClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.common.entity.PageResponse;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;

@RestController
public class ComMonitorHttpManageController implements IComMonitorHttpManageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComMonitorHttpManageController.class);

	@Autowired
	private MonitorHttpManageClient monitorHttpManageClient;

	@Override
	public PageResponse<ComMonitorHttpConfigResponse> pageList(@RequestBody ComMonitorHttpReq pageRequset) {
		LOGGER.info("method[pageList] request body is {}", pageRequset);
		MonitorHttpReqObj alertsPageRequset = PayloadParseUtil.jacksonBaseParse(MonitorHttpReqObj.class, pageRequset);
		PageResponse<MonitorHttpConfigResponse> pageResponse = monitorHttpManageClient.pageList(alertsPageRequset);
		PageResponse<ComMonitorHttpConfigResponse> response = new PageResponse<ComMonitorHttpConfigResponse>();
		response.setCount(pageResponse.getCount());
		List<ComMonitorHttpConfigResponse> panel = jacksonBaseParse(ComMonitorHttpConfigResponse.class,pageResponse.getResult());
		response.setResult(panel);
		return response;
	}

	@Override
	public ComMonitorHttpConfigResponse findByPrimaryKey(@PathVariable("id") String id) {
		LOGGER.info("method[findByPrimaryKey] id is {}", id);
		 if (StringUtils.isEmpty(id)) {
			 LOGGER.warn("ComMonitorHttpManageController method[findByPrimaryKey] param id is empty");
	            return null;
	        }
		 MonitorHttpConfigResponse p = monitorHttpManageClient.findByPrimaryKey(id);
		 ComMonitorHttpConfigResponse panel = jacksonBaseParse(ComMonitorHttpConfigResponse.class,p);
		return panel;
	}

	@Override
	public ResponseEntity<String> create(@RequestBody ComMonitorHttpConfigResponse createRequest) {
		LOGGER.info("method[create] createRequest is {}", createRequest);
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		if (createRequest == null) {
			LOGGER.error("create createRequest is null");
			throw new RuntimeException("create param is null");
		}
		MonitorHttpConfigResponse c = monitorHttpManageClient.findByName(createRequest.getMonitor_name());
		if (null != c) {
			return new ResponseEntity<String>("fail", HttpStatus.OK);
		}
		MonitorHttpConfigResponse panel = PayloadParseUtil.jacksonBaseParse(MonitorHttpConfigResponse.class,createRequest);
		panel.setCreate_staff(authCtx.getUser().getUsername());
		monitorHttpManageClient.create(panel);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	/*
	 * public static ComMonitorHttpHisResponse httpPost(final String param, final
	 * String url) { final CloseableHttpClient httpClient =
	 * HttpClients.createDefault(); HttpResponse httpResponse = null; StringBuffer
	 * result = new StringBuffer(); StringBuffer headerResult = new StringBuffer();
	 * ComMonitorHttpHisResponse his = null; BufferedReader in = null; try { final
	 * HttpPost httpPost = new HttpPost(url); httpPost.setHeader("Content-Type",
	 * "application/json"); final StringEntity entity = new StringEntity(param,
	 * HTTP_CHARSET); httpPost.setEntity(entity); httpResponse =
	 * httpClient.execute(httpPost); his = new ComMonitorHttpHisResponse(); Header[]
	 * headers = httpResponse.getAllHeaders(); for(Header h:headers) {
	 * headerResult.append(h.getName()) ; headerResult.append(":");
	 * headerResult.append(h.getValue()); headerResult.append("\n"); }
	 * 
	 * his.setHead_response(headerResult.toString()); in = new BufferedReader(new
	 * InputStreamReader(httpResponse.getEntity().getContent())); String line; while
	 * ((line = in.readLine()) != null) { result.append(line); } String conclusion =
	 * result.toString(); if(conclusion.contains("正常")) { his.setResult(1); }else {
	 * his.setResult(0); } his.setRequest_result(result.toString());
	 * LOGGER.debug("POST Response Status:" +
	 * httpResponse.getStatusLine().getStatusCode()); } catch
	 * (UnsupportedCharsetException e) { LOGGER.error("send post error:{}", e); }
	 * catch (ClientProtocolException e) { LOGGER.error("send post error:{}", e); }
	 * catch (IOException e) { LOGGER.error("send post error:{}", e); } finally {
	 * try { if (in != null) { in.close(); } } catch (Exception e2) {
	 * e2.printStackTrace(); } IOUtils.closeQuietly(httpClient); } return his; }
	 */

	@Override
	public ResponseEntity<String> update(@RequestBody ComMonitorHttpConfigResponse updateRequest,
			@RequestParam(value = "oldName", required = false) String oldName) {
		LOGGER.info("method[update] updateRequest is {}", updateRequest);
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		if (updateRequest == null) {
			LOGGER.warn("update updateRequest note is null");
			return null;
		}
		if (null != oldName && !oldName.equals(updateRequest.getMonitor_name())) {
			MonitorHttpConfigResponse c = monitorHttpManageClient.findByName(updateRequest.getMonitor_name());
			if (null != c) {
				return new ResponseEntity<String>("fail", HttpStatus.OK);
			}
		}
		MonitorHttpConfigResponse panel = PayloadParseUtil.jacksonBaseParse(MonitorHttpConfigResponse.class,updateRequest);
		panel.setUpdate_staff(authCtx.getUser().getUsername());
		monitorHttpManageClient.update(panel);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> updateStatus(@RequestBody ComMonitorHttpConfigResponse updateRequest) {
		LOGGER.info("method[updateStatus] updateRequest is {}", updateRequest);
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		if (updateRequest == null) {
			LOGGER.warn("update updateRequest note is null");
			return null;
		}
		MonitorHttpConfigResponse panel = PayloadParseUtil.jacksonBaseParse(MonitorHttpConfigResponse.class,updateRequest);
		panel.setUpdate_staff(authCtx.getUser().getUsername());
		monitorHttpManageClient.updateStatus(panel);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> delete(@PathVariable("id") String id) {
		LOGGER.info("method[delete] id is {}", id);
		if (StringUtils.isEmpty(id)) {
			LOGGER.error("delete id is null");
			throw new RuntimeException("delete id is null");
		}
		monitorHttpManageClient.delete(id);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	/*
	 * @Override public ComMonitorHttpConfigResponse
	 * findByName(@PathVariable("name") String name) { if
	 * (StringUtils.isEmpty(name)) { LOGGER.error("findByName name is null"); throw
	 * new RuntimeException("findByName name is null"); }
	 * ComMonitorHttpConfigResponse res = monitorHttpManageBiz.getByName(name); if
	 * (null == res) { return null; } ComMonitorHttpConfigResponse resDTO = new
	 * ComMonitorHttpConfigResponse(); BeanUtils.copyProperties(res, resDTO); return
	 * resDTO; }
	 */

	@Override
	public PageResponse<ComMonitorHttpHisResponse> pageListHis(@RequestBody ComMonitorHttpReq pageRequset) {
		LOGGER.info("method[pageListHis] id pageRequset {}", pageRequset);
		if (pageRequset == null) {
			LOGGER.warn("pageListHis param pageRequset is null");
			return null;
		}
		MonitorHttpReqObj panel = PayloadParseUtil.jacksonBaseParse(MonitorHttpReqObj.class,pageRequset);
		PageResponse<MonitorHttpHisResponse> pageResponse = monitorHttpManageClient.pageListHis(panel);
		
		PageResponse<ComMonitorHttpHisResponse> response = new PageResponse<ComMonitorHttpHisResponse>();
		response.setCount(pageResponse.getCount());
		List<ComMonitorHttpHisResponse> hisList = jacksonBaseParse(ComMonitorHttpHisResponse.class,pageResponse.getResult());
		response.setResult(hisList);
		return response;
	}

	

	@Override
	public ComMonitorHttpHisResponse findHisByPrimaryKey(@PathVariable("id") String id) {
		LOGGER.info("method[findHisByPrimaryKey] id id {}", id);
		if (StringUtils.isEmpty(id)) {
			LOGGER.warn("findHisByPrimaryKey param id is null");
			return null;
		}
		MonitorHttpHisResponse his = monitorHttpManageClient.findHisByPrimaryKey(id);
		ComMonitorHttpHisResponse dto = jacksonBaseParse(ComMonitorHttpHisResponse.class,his);
		return dto;
	}

	
	@Override
	public List<ComMonitorHttpIdcTypeResponse> getIdcTypes() {
		List<MonitorHttpIdcTypeResponse> idcTypes = monitorHttpManageClient.getIdcTypes();
		
		List<ComMonitorHttpIdcTypeResponse> dto = jacksonBaseParse(ComMonitorHttpIdcTypeResponse.class,idcTypes);
		return dto;
	}

	@Override
	public ComMonitorHttpHisResponse testHttp(@RequestBody ComMonitorHttpConfigResponse createRequest) {
		MonitorHttpConfigResponse createRq = PayloadParseUtil.jacksonBaseParse(MonitorHttpConfigResponse.class,createRequest);
		MonitorHttpHisResponse his = monitorHttpManageClient.testHttp(createRq);
		ComMonitorHttpHisResponse dto = jacksonBaseParse(ComMonitorHttpHisResponse.class,his);
		return dto;
	}
}
