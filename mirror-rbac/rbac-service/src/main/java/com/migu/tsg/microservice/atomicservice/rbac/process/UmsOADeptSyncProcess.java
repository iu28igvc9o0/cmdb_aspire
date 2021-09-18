package com.migu.tsg.microservice.atomicservice.rbac.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.migu.tsg.microservice.atomicservice.rbac.biz.UserDepmentSyncEipBiz;

@Component
public class UmsOADeptSyncProcess {
	
	@Autowired
	UserDepmentSyncEipBiz userDepmentSyncEipBiz;
	
	private static final Logger logger = LoggerFactory.getLogger(UmsOADeptSyncProcess.class);
	
	@Scheduled(cron = "0 */30 * * * ? ")
	public void oaDeptSyncProcess(){
		try {
			userDepmentSyncEipBiz.userDeptSyncProcess();
		} catch (Exception e) {
			logger.error("同步UMS用户组织失败" , e);
		}
		
	}
}
