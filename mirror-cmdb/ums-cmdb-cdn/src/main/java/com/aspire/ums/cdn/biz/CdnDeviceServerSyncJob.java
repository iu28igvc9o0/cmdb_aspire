/**
 *
 * 项目名： ums-cmdb-cdn 
 * <p/> 
 *
 * 文件名:  CdnDeviceServerSyncJob.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.ums.cdn.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cdn.clientservice.CmdbServiceClient;
import com.aspire.ums.cdn.clientservice.payload.CmdbCode;
import com.aspire.ums.cdn.clientservice.payload.CmdbInstance;
import com.aspire.ums.cdn.dao.cdn.CdnDao;
import com.aspire.ums.cdn.exception.CmdbCdnAdaptException;
import com.aspire.ums.cdn.model.CdnDeviceServerDTO;
import com.aspire.ums.cdn.model.Module;

/** 
 *
 * 项目名称: ums-cmdb-cdn 
 * <p/>
 * 
 * 类名: CdnDeviceServerSyncJob
 * <p/>
 *
 * 类功能描述: CDN设备信息同步接口
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Service
@Transactional("transactionManager")
class CdnDeviceServerSyncJob {
	private static final Logger		LOG			= LoggerFactory.getLogger(CdnDeviceServerSyncJob.class);

	@Autowired(required = false)
	private CdnDao					dao;

	@Autowired
	private CmdbServiceClient		cmdbClient;

	private final ThreadPoolExecutor	TASK_POOL	
				= new ThreadPoolExecutor(5, 5, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	
	
	@Transactional(value="transactionManager", readOnly=true)
	@Scheduled(fixedDelayString = "${cdn.deviceserver.sync.interval}")
	public void runJob() {
		List<CdnDeviceServerDTO> serverList = dao.fetchAllDeviceServerList();
		if (CollectionUtils.isEmpty(serverList)) {
			LOG.warn("There is no cdn server list to sync.");
			return;
		}
		serverList.parallelStream().forEach(item -> {
			item.refreshStatusLabels();
			if (StringUtils.isBlank(item.getDeviceStatusLabel())) {
				System.out.println(item.getHostname() + "|" + item.getMCardIP() + "|" + item.getDeviceStatusCode());
			}
		});
		
		Pair<Module, CmdbCode> cmdbDef = fetchCmdbModuleCode();
		if (cmdbDef == null || cmdbDef.getLeft() == null) {
			LOG.error("There is no module with code of 'cdn', the cdn server data sync job will ignore.");
			return;
		}
		
		int index = 0;
		int taskSize = TASK_POOL.getMaximumPoolSize();
		while (true) {
			int turnSize = index + taskSize > serverList.size() ? serverList.size() - index : taskSize;
			List<CdnDeviceServerDTO> splitServerList = serverList.subList(index, index + turnSize);
			CountDownLatch latch = new CountDownLatch(splitServerList.size());
			
			splitServerList.stream().forEach(server -> {
				TASK_POOL.submit(constructSyncThread(latch, cmdbDef.getLeft(), cmdbDef.getRight(), server));
			});
			try {
				latch.await();
			} catch (Exception e) {
				throw new CmdbCdnAdaptException(e);
			}
			
			index += turnSize;
			if (index >= serverList.size()) {
				break;
			}
		}
	}
	
	private Pair<Module, CmdbCode> fetchCmdbModuleCode() {
		Module queryParam = new Module();
		queryParam.setCode("cdn");
		List<Module> queryList = cmdbClient.queryModuleByParams(queryParam);
		if (CollectionUtils.isEmpty(queryList)) {
			return null;
		}
		
		List<CmdbCode> codeList = cmdbClient.getCodeListByModuleId(queryList.get(0).getId());
		CmdbCode matchCode = null;
		if (codeList != null) {
			Optional<CmdbCode> first = codeList.stream().filter(item -> {
				return "ssh_ip".equals(item.getFiledCode());
			}).findFirst();
			if (first.isPresent()) {
				matchCode = first.get();
			}
		}
		return Pair.of(queryList.get(0), matchCode);
	}
	
	private Runnable constructSyncThread(final CountDownLatch latch, 
				final Module cdnModule, final CmdbCode ipCode, final CdnDeviceServerDTO server) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, Object> requestData = new HashMap<>();
					requestData.put("module", cdnModule);
					
					CmdbInstance exist = cmdbClient.queryDeviceByRoomIdAndIP(server.getDeviceOwnerName(), server.getMCardIP());
					if (exist == null) {
						requestData.put("instanceData", server.resolveCmdbUpdateInstance(ipCode, null));
						cmdbClient.addInstance(requestData);
					} else {
						requestData.put("instanceData", server.resolveCmdbUpdateInstance(ipCode, exist.getId()));
						cmdbClient.updateInstance(exist.getId(), requestData);
					}
				} catch (Exception e) {
					LOG.error(null, e);
				} finally {
					latch.countDown();
				}
			}
		};
	}
}
