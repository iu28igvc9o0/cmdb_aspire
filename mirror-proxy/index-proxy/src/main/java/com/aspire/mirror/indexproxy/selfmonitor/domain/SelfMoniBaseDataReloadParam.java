package com.aspire.mirror.indexproxy.selfmonitor.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: SelfMoniBaseDataReloadParam
 * <p/>
 *
 * 类功能描述: 自研监控基础数据重载参数
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年10月25日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
public final class SelfMoniBaseDataReloadParam {
	@Getter
	private final Integer shardingIndex;			
	
	@Getter
	private final Integer shardingTotal;
	
	@JsonIgnore
	private final Map<String, Object> extraParamMap = new HashMap<>();
	
	public SelfMoniBaseDataReloadParam(Integer shardingIndex, Integer shardingTotal) {
		this.shardingIndex = shardingIndex;
		this.shardingTotal = shardingTotal;
	}
	
	@JsonAnySetter
	public void addExtraParam(String paramKey, Object paramVal) {
		if (paramKey == null) {
			log.warn("The extra paramKey is invalid as it is null.");
			return;
		} 
		extraParamMap.put(paramKey, paramVal);
	}
	
	@JsonAnyGetter
	public Object getExtraParam(String paramKey) {
		if (paramKey == null) {
			return null;
		}
		return extraParamMap.get(paramKey);
	}
	
	/** 
	 * 功能描述: 与指定的对象比较sharding信息是否存在变化
	 * <p>
	 * @param other
	 * @return
	 */
	public boolean isShardingInfoDiff(SelfMoniBaseDataReloadParam other) {
		return !this.getShardingIndex().equals(other.getShardingIndex())
				|| !this.getShardingTotal().equals(other.getShardingTotal());
	}
	
	/** 
	 * @see java.lang.Object#equals(java.lang.Object) 
	 */
	@Override
	public boolean equals(Object obj) {
		if (!this.getClass().isInstance(obj)) {
			return false;
		}
		SelfMoniBaseDataReloadParam other = getClass().cast(obj);
		if (!this.getShardingIndex().equals(other.getShardingIndex())
				|| !this.getShardingTotal().equals(other.getShardingTotal())) {
			return false;
		}
		if (this.getSortExtraParamEntryList().size() != other.getSortExtraParamEntryList().size()) {
			return false;
		}
		List<String> thisSortExtraList = getSortExtraParamEntryList();
		List<String> otherSortExtraList = getSortExtraParamEntryList();
		for (int i = 0; i < thisSortExtraList.size(); i++) {
			if (!thisSortExtraList.get(i).equals(otherSortExtraList.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	/** 
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		int hash = 37;
		hash = hash + shardingIndex + 17 * shardingTotal;
		for (String paramEntry : getSortExtraParamEntryList()) {
			hash += 17 * paramEntry.hashCode();
		}
		return hash;
	}
	
	private List<String> getSortExtraParamEntryList() {
		List<String> result = new ArrayList<>();
		for (Map.Entry<String, Object> entry : extraParamMap.entrySet()) {
			result.add(entry.getKey() + "=" + String.valueOf(entry.getValue()));
		}
		Collections.sort(result);
		return result;
	}
}
