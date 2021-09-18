package com.aspire.ums.cmdb.resource.mapper;

import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface AssetsMapper {

    List<Map> getAssets();

    List<Map<String, Object>>  advancedSearchDeviceDistributionList(Map<String, Object> params, RowBounds rowBounds);

    Object advancedSearchDeviceDistributionList_count(Map<String, Object> params);

    List<Map<String, Object>> getAllDeviceAssetList();

    List<Map<String, Object>> cmdbAdvancedSearchDeviceIdList(Map<String, Object> params);
}
