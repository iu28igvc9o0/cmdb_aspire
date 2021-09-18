package com.aspire.mirror.indexadapt.adapt.monitordb;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexadapt.adapt.AbstractIndexDataListAdapter;
import com.aspire.mirror.indexadapt.adapt.monitordb.model.MonitorZabbixIndex;

/**
* 从监控zabbix数据库中适配   <br/>
* Project Name:index-proxy
* File Name:MonitorIndexDataListAdapter.java
* Package Name:com.aspire.mirror.indexadapt.adapt.inspectiondb
* ClassName: MonitorIndexDataListAdapter <br/>
* date: 2018年8月6日 下午4:02:51 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Component
@ConditionalOnProperty("indexAdapter.monitorDb.switch")
public class MonitorIndexDataListAdapter extends AbstractIndexDataListAdapter<MonitorZabbixIndex> {
	@Value("${indexAdapter.monitorDb.identity}")
	private String identity;

	@Override
	public String getAdapterIdentity() {
		return identity;
	}
	
	@Override
	public boolean preHandleAdapt() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void postHandleAdapt() {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected List<MonitorZabbixIndex> fetchRawIndexDataList0() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected List<StandardIndex> adapt2StandardIndex0(List<MonitorZabbixIndex> rawIndexList) {
		// TODO Auto-generated method stub
		return null;
	}
}
