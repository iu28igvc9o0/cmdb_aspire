package com.aspire.mirror.indexadapt.adapt.monitordb.model;

import com.aspire.mirror.indexadapt.adapt.BaseRawIndexData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MonitorZabbixIndex extends BaseRawIndexData {
	private String room;	// 机房, 从配置中获取
	private Integer itemId;
	private String itemKey;
	private String value;
	private String deviceIP;
	private String templateKey;
	
	@Override
	public Integer getIndexSeq() {
		return itemId;
	}
}
