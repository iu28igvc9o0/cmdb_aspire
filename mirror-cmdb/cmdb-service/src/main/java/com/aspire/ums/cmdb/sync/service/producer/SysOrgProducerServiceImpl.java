package com.aspire.ums.cmdb.sync.service.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import com.aspire.ums.cmdb.systemManager.service.OrgManagerService;

/**
 * 组织机构同步.
 *
 * @author jiangxuwen
 * @date 2020/5/13 20:44
 */
@Service("SysOrgProducerServiceImpl")
public class SysOrgProducerServiceImpl extends AbstractKafkaProducerService<OrgManager> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrgManagerService orgManagerService;

    @Override
    public OrgManager getObjectData(String moduleId, String objectId) {
        return orgManagerService.getOrgManagerById(objectId);
    }
}
