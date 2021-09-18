package com.aspire.mirror.composite.payload.dashboard;

import java.io.Serializable;
import java.util.List;

public class ComAlertDataSetsDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	List<ComAlertDataSet> dataSets;

	public List<ComAlertDataSet> getDataSets() {
		return dataSets;
	}

	public void setDataSets(List<ComAlertDataSet> dataSets) {
		this.dataSets = dataSets;
	}
}
