package com.aspire.ums.cmdb.resource.service.impl;//package com.aspire.ums.cmdb.resource.service.impl;

import com.aspire.ums.cmdb.resource.entity.ResBuildDetail;
import com.aspire.ums.cmdb.resource.mapper.ResourceBuildDetailMapper;
import com.aspire.ums.cmdb.resource.service.ResBuildDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResBuildDetailServiceImpl implements ResBuildDetailService {

    @Autowired
    private ResourceBuildDetailMapper resourceBuildDetailMapper;

    @Override
    public void addResourceBuildDetail(ResBuildDetail resBuildDetail) {
        resourceBuildDetailMapper.addResourceBuildDetail(resBuildDetail);
    }

    @Override
    public void updateResourceBuildDetail(ResBuildDetail resBuildDetail) {
        resourceBuildDetailMapper.updateResourceBuildDetail(resBuildDetail);
    }
}
