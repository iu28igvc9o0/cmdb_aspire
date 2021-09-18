package com.aspire.ums.cmdb.sync.service.impl;

import com.aspire.ums.cmdb.common.StringUtils;
import com.aspire.ums.cmdb.sync.client.CmdbServiceClient;
import com.aspire.ums.cmdb.sync.entity.EIPDept;
import com.aspire.ums.cmdb.sync.service.EipSyncService;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("eipDeptSyncToUmsService")
public class EipDeptSyncToUmsServiceImpl implements EipSyncService<EIPDept> {

    private static final Logger logger = LoggerFactory.getLogger(EipDeptSyncToUmsServiceImpl.class);
    private static final String EIP_ORG_TYPE = "部门";
    private static final String EIP_ORG_REMARK = "EIP";

    @Autowired
    private CmdbServiceClient cmdbServiceClient;

    private OrgManager toUmsOrg(EIPDept dept) {
        OrgManager org = new OrgManager();
        if (StringUtils.isNotEmpty(dept.getDeptId())) {
            org.setId(dept.getDeptId());
        }
        if (StringUtils.isNotEmpty(dept.getDeptName())) {
            org.setOrgName(dept.getDeptName());
        }
        if (StringUtils.isNotEmpty(dept.getParentId())) {
            org.setPid(dept.getParentId());
        }
        org.setOrgType(EIP_ORG_TYPE);
        org.setRemark(EIP_ORG_REMARK);
        return org;
    }

    @Override
    public void add(List<EIPDept> list) {
        for (EIPDept dept : list) {
            cmdbServiceClient.insert(toUmsOrg(dept));
        }
    }

    @Override
    public void modify(List<EIPDept> list) {
        for (EIPDept dept : list) {
            cmdbServiceClient.update(toUmsOrg(dept));
        }
    }

    @Override
    public void delete(List<EIPDept> list) {
        for (EIPDept dept : list) {
            cmdbServiceClient.deleteOrg(dept.getDeptId());
        }
    }
}
