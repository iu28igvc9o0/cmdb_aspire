package com.aspire.mirror.indexadapt.adapt;

import java.util.List;

import com.aspire.mirror.indexadapt.adapt.IndexDataListAdapter.StandardIndex;

/**
* 标准指标数据发布接口    <br/>
* Project Name:index-proxy
* File Name:StandardIndexDataListPublisher.java
* Package Name:com.aspire.mirror.indexadapt.adapt
* ClassName: StandardIndexDataListPublisher <br/>
* date: 2018年8月6日 下午3:37:02 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/
public interface StandardIndexDataListPublisher {
	/**
	* 发布标准指标数据. <br/>
	*
	* 作者： pengguihua
	* @param itemDataList
	*/  
	public void publishStandardIndexDataList(List<StandardIndex> indexDataList, String adapterIdentity);
}
