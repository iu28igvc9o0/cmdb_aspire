package com.aspire.ums.cdn.dao.cdn;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cdn.model.CdnDeviceServerDTO;
import com.aspire.ums.cdn.model.LinkPercentReportData;

@Mapper
public interface CdnDao {
	public List<LinkPercentReportData> getLinkPercentReportData();
	public List<CdnDeviceServerDTO> fetchAllDeviceServerList();
}
