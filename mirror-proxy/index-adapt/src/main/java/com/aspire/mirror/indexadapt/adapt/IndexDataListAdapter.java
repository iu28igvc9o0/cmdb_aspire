package com.aspire.mirror.indexadapt.adapt;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* 指标值收集-适配接口    <br/>
* Project Name:index-proxy
* File Name:IndexDataListAdapter.java
* Package Name:com.aspire.mirror.indexadapt.adapt
* ClassName: IndexDataListAdapter <br/>
* date: 2018年8月6日 下午3:25:06 <br/>
* @author pengguihua
* @version @param <T>
* @since JDK 1.6
*/ 
public interface IndexDataListAdapter<T> {
	
	public String getAdapterIdentity();					// 获取适配器全局唯一身份
	
	public boolean preHandleAdapt();					// 适配之前的处理, 如果返回false, 则不继续后续的 适配处理
	
	public List<T> fetchRawIndexDataList();				// 获取原始指标数据
	
	public List<StandardIndex> adapt2StandardIndex();	// 适配成标准指标数据
	
	public void postHandleAdapt();						// 适配完成后处理	
	
	public void handleException();						// 适配过程中出现异常时的处理
	
	@Data
	@JsonInclude(Include.NON_NULL)
	public static class StandardIndex {

		@JsonProperty("object_type")
		private String objectType;
		
		@JsonProperty("object_id")
	    private String objectId;

		@JsonProperty("room_id")
		private String roomId;
		
		@JsonProperty("item_id")
		private String itemId;
		
		@JsonProperty
		private Integer clock;
		
		@JsonProperty
		private String value;
		
		@JsonProperty("pre_value")
		private String preValue;

		@JsonProperty("group_key")
		private String groupKey;

		@JsonProperty("group_desc")
		private Object groupDesc;

		@JsonProperty("remark")
		private String remark;

		@JsonProperty("extend_obj")
		private String extendObj;		// 扩展业务对象   比如为  巡检report_id, 或其它自定义的业务对象， 这个属性由业务定制
	}
}
