package com.aspire.ums.cmdb.resource.mapper;//package com.aspire.ums.cmdb.resource.mapper;

import com.aspire.ums.cmdb.resource.entity.*;

import java.util.List;
import java.util.Map;

public interface ResourceBuildMapper {

    List<ResBuildMaData> selectResBuildMaData(ResBuildMaDataRequest request);

    int selectResBuildMaCount(ResBuildMaDataRequest request);

    List<Map<String, Object>> selectResBuildMaCountList(Map<String, String> params);

    List<ResBuildNameResponse> getResourceBuildName(Integer id);

    void addResourceBuild(ResBuild resBuild);

    ResBuildMaData getResourceBuildData(Integer buildId);

    void updateResourceBuild(ResBuild resBuild);

    void updateImportStatus(Map<String, Object> param);

    void updateResourceEstimate(ResBuild resBuild);

    void saveCmdbHostAssetsExcelData(List<CmdbHostAssetsExcelData> excelDataList);

    List<Map<String, String>> getReBuildName();

    List<Map<String, String>> getResourceBuildReType();

    List<Map<String, String>> getReBuildResourPool();

    List<Map<String, String>> getResourceEstimate();

}
