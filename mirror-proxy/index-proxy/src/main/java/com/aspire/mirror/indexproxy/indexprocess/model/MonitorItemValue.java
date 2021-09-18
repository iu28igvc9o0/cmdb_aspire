package com.aspire.mirror.indexproxy.indexprocess.model;

import org.springframework.beans.BeanUtils;

import com.aspire.mirror.indexproxy.domain.MonitorItemRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class MonitorItemValue extends MonitorItemRecord {
	private static final long serialVersionUID = -6983262251336106852L;
	
    private String value;
	
	
	public static MonitorItemValue from(MonitorItemRecord itemRecord, String value) {
		MonitorItemValue itemVal = new MonitorItemValue();
		BeanUtils.copyProperties(itemRecord, itemVal);
		itemVal.setValue(value);
		return itemVal;
	}
}
