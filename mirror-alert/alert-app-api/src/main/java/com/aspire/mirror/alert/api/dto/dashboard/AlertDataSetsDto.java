package com.aspire.mirror.alert.api.dto.dashboard;

import java.io.Serializable;
import java.util.List;

public class AlertDataSetsDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	List<AlertDataSet> dataSets;

	public List<AlertDataSet> getDataSets() {
		return dataSets;
	}

	public void setDataSets(List<AlertDataSet> dataSets) {
		this.dataSets = dataSets;
	}
}
