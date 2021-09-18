package com.aspire.mirror.elasticsearch.api.dto;

import java.io.Serializable;
import java.util.List;

public class DataSetsDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	List<DataSet> dataSets;

	public List<DataSet> getDataSets() {
		return dataSets;
	}

	public void setDataSets(List<DataSet> dataSets) {
		this.dataSets = dataSets;
	}
}
