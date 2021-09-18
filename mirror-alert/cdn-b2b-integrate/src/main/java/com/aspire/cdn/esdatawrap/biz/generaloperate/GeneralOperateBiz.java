package com.aspire.cdn.esdatawrap.biz.generaloperate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.cdn.esdatawrap.biz.generaloperate.model.AppClientInfo;
import com.aspire.cdn.esdatawrap.biz.generaloperate.model.HostPingRequestBody;
import com.aspire.cdn.esdatawrap.biz.generaloperate.model.HostPingResult;
import com.aspire.cdn.esdatawrap.common.GeneralResponse;
import com.aspire.cdn.esdatawrap.util.CmdExecuteUtil;
import com.aspire.cdn.esdatawrap.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: GeneralOperateBiz
 * <p/>
 *
 * 类功能描述: 一般操作业务类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年9月2日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Service
public class GeneralOperateBiz {
	private static final String IDX_APP_CLIENT_INFO	= "app_client_info";
	@Autowired
	private RestHighLevelClient	restClient;
	private static final String TELNET_CMD	= "echo \"\" | telnet %s 80 ";
	private static final String PING_CMD 	= "ping -c 2 -w 2 %s";
	private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 10, 5L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
	
	/** 
	 * 功能描述: ping检测主机  
	 * <p>
	 * @param reqBody
	 * @return
	 */
	public List<HostPingResult> pingHostList(HostPingRequestBody reqBody) {
		List<String> hostList = reqBody.resolveHostList();
		final CountDownLatch latch = new CountDownLatch(hostList.size());
		final List<HostPingResult> resultList = new ArrayList<>();
		for (String host : hostList) {
			executor.submit(new Runnable() {
				@Override
				public void run() {
					asycExecutePing(host, latch, resultList);
				}
			});
		}
		try {
			latch.await();
		} catch (Exception e) {
			String tip = "Error when invoke pingHostList method with params: " + reqBody.toString();
			throw new RuntimeException(tip, e);
		}
		return resultList;
	}
	
	private void asycExecutePing(final String host, final CountDownLatch latch, final List<HostPingResult> resultList) {
		try {
			String cmd = String.format(TELNET_CMD, host);
			Pair<Boolean, String> feedback = CmdExecuteUtil.executeCommandNonBlockingWithFeedback(cmd, null, null);
			if (feedback.getLeft()) {
				resultList.add(new HostPingResult(host, feedback.getLeft()));
				return;
			}
			cmd = String.format(PING_CMD, host);
			feedback = CmdExecuteUtil.executeCommandNonBlockingWithFeedback(cmd, null, null);
			resultList.add(new HostPingResult(host, feedback.getLeft()));
		} catch (Throwable e) {
			log.error("Error when invoke asycExecutePing for host: {}.", host, e);
			resultList.add(new HostPingResult(host, false));
		} finally {
			latch.countDown();
		}
	}
	
	public GeneralResponse pushAppClientInfo(@RequestBody AppClientInfo clientInfo) {
		log.info("Got the pushAppClientInfo request with detail: " + JsonUtil.toJacksonJson(clientInfo));
		IndexRequest indexReq = new IndexRequest(IDX_APP_CLIENT_INFO, "doc", clientInfo.getClientId());
		indexReq.source(JsonUtil.toJacksonJson(clientInfo), XContentType.JSON);
		try {
			restClient.index(indexReq, RequestOptions.DEFAULT);
		} catch (Exception e) {
			return new GeneralResponse(false, e.getMessage());
		}
		return new GeneralResponse(true);
	}
}
