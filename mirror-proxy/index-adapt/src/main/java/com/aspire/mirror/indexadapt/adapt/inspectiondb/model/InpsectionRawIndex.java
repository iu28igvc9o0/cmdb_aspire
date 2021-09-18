package com.aspire.mirror.indexadapt.adapt.inspectiondb.model;

import com.aspire.mirror.indexadapt.adapt.BaseRawIndexData;
import com.aspire.mirror.indexadapt.adapt.IndexDataListAdapter.StandardIndex;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InpsectionRawIndex extends BaseRawIndexData {
	private Integer reportItemId;
	private String objectType;
	private String objectId;
	private String itemId;
	private Integer clock;
	private String preValue;
	private String value;
	private String reportId;
	
	@Override
	public Integer getIndexSeq() {
		return reportItemId;
	}
	
	public StandardIndex toStandardIndex() {
		StandardIndex standardIdx = new StandardIndex();
		standardIdx.setExtendObj(reportId);
		standardIdx.setObjectType(objectType);
		standardIdx.setObjectId(objectId);
		standardIdx.setItemId(itemId);
		standardIdx.setClock(clock);
		standardIdx.setPreValue(preValue);
		standardIdx.setValue(value);
		return standardIdx;
	}
}
