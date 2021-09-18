package com.aspire.mirror.zabbixintegrate.biz.alert;

import java.sql.Timestamp;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.mirror.zabbixintegrate.domain.ZabbixAlertScanIndex;

import lombok.extern.slf4j.Slf4j;

/**
* 扫描游标hodler类   <br/>
* Project Name:zabbix-integrate
* File Name:AlertScanIndexHolder.java
* Package Name:com.aspire.mirror.zabbixintegrate.biz
* ClassName: AlertScanIndexHolder <br/>
* date: 2018年10月17日 下午7:23:59 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Component
final class AlertScanIndexHolder {
	@Autowired
	private ZabbixBiz					zabbixBiz;
	private volatile boolean			flag 					= false;		// 扫描游标是否更改标记
	private volatile Long			scanIndex 				= 0L;			// 系统中缓存的扫描游标

	private static final ReadWriteLock	LOCK					= new ReentrantReadWriteLock();
	private static final Lock			R_LOCK					= LOCK.readLock();
	private static final Lock			W_LOCK					= LOCK.writeLock();
	
	/**
	* 从数据库初始化游标缓存值. <br/>
	*
	* 作者： pengguihua
	*/  
	@PostConstruct
	private void initLoad() {
		ZabbixAlertScanIndex fetchScanIndex = zabbixBiz.fetchDbScanIndex(ZabbixAlertScanIndex.ALERT_ID);
		if (fetchScanIndex == null) {
			ZabbixAlertScanIndex initModel = new ZabbixAlertScanIndex();
			initModel.setId(ZabbixAlertScanIndex.ALERT_ID);
			initModel.setScanIndex(this.scanIndex);
			initModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			zabbixBiz.insertScanIndex(initModel);
			return;
		}
		this.updateScanIndex(fetchScanIndex.getScanIndex());
	}
	
	/**
	* 获取当前游标值. <br/>
	*
	* 作者： pengguihua
	* @return
	*/  
	public Long getScanIndex() {
		R_LOCK.lock();
		try {
			return this.scanIndex;
		} finally {
			R_LOCK.unlock();
		}
	}
	
	/**
	* 更新游标. <br/>
	*
	* 作者： pengguihua 
	* @param scanIndex2Update
	*/  
	public void updateScanIndex(Long scanIndex2Update) {
		W_LOCK.lock();
		try {
			if (scanIndex2Update.longValue() != this.scanIndex.longValue()) {
				this.scanIndex = scanIndex2Update;
				flag = true;
			}
		} finally {
			W_LOCK.unlock();
		}
	}
	
	/**
	* 定时刷新到 数据库. <br/>
	*
	* 作者： pengguihua
	*/ 
	@PreDestroy
	@Scheduled(fixedDelayString = "${alertScan.flush.fixedDelay}")
	private void flush() {
		// 如果未存在更改, 直接返回
		if (!flag) {
			return;
		}
		
		W_LOCK.lock();
		try {
			log.info("update alert scan index,alertid is : {}", this.scanIndex);
			ZabbixAlertScanIndex updateModel = new ZabbixAlertScanIndex();
			updateModel.setId(ZabbixAlertScanIndex.ALERT_ID);
			updateModel.setScanIndex(this.scanIndex);
			updateModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			zabbixBiz.updateScanIndex(updateModel);
			// 重置flag为false
			flag = false;
		} catch (Throwable e) {
			log.error("Error when flush the scan index cache to DB.", e);
		} finally {
			W_LOCK.unlock();
		}
	}
}
