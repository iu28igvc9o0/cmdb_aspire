package com.aspire.ums.cmdb.resource.service.impl;//package com.aspire.ums.cmdb.resource.service.impl;

import com.aspire.ums.cmdb.resource.entity.*;
import com.aspire.ums.cmdb.resource.mapper.ResourceBuildMapper;
import com.aspire.ums.cmdb.resource.service.ResBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ResBuildServiceImpl implements ResBuildService {

    @Autowired
    private ResourceBuildMapper resourceBuildMapper;


    @Override
    public List<ResBuildMaData> selectResBuildMaData(ResBuildMaDataRequest request) {
        return resourceBuildMapper.selectResBuildMaData(request);
    }

    @Override
    public int selectResBuildMaCount(ResBuildMaDataRequest request) {
        return resourceBuildMapper.selectResBuildMaCount(request);
    }

    @Override
    public List<Map<String, Object>> selectResBuildMaCountList(Map<String, String> params) {
        return resourceBuildMapper.selectResBuildMaCountList(params);
    }

    @Override
    public List<ResBuildNameResponse> getResourceBuildName(Integer id) {
        return resourceBuildMapper.getResourceBuildName(id);
    }

    @Override
    public void addResourceBuild(ResBuild resBuild) {
        resourceBuildMapper.addResourceBuild(resBuild);
    }

    @Override
    public ResBuildMaData getResourceBuildData(Integer buildId) {
        return resourceBuildMapper.getResourceBuildData(buildId);
    }

    @Override
    public void updateResourceBuild(ResBuild resBuild) {
        resourceBuildMapper.updateResourceBuild(resBuild);
    }

    @Override
    public void updateImportStatus(Map<String, Object> param) {
        resourceBuildMapper.updateImportStatus(param);
    }

    @Override
    public void updateResourceEstimate(ResBuild resBuild) {
        resourceBuildMapper.updateResourceEstimate(resBuild);
    }

    @Override
    public void saveCmdbHostAssetsExcelData(List<CmdbHostAssetsExcelData> excelDataList) {
         resourceBuildMapper.saveCmdbHostAssetsExcelData(excelDataList);
    }

    @Override
    public List<Map<String, String>> getReBuildName() {
        return resourceBuildMapper.getReBuildName();
    }

    @Override
    public List<Map<String, String>> getResourceBuildReType() {
        return resourceBuildMapper.getResourceBuildReType();
    }

    @Override
    public List<Map<String, String>> getReBuildResourPool() {
        return resourceBuildMapper.getReBuildResourPool();
    }

    @Override
    public List<Map<String, String>> getResourceEstimate() {
        return resourceBuildMapper.getResourceEstimate();
    }
}
