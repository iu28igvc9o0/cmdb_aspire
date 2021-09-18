package com.aspire.ums.cmdb.ipCollect.service.impl;

import com.aspire.ums.cmdb.ipCollect.enums.EventInstanceModuleEnum;
import com.aspire.ums.cmdb.ipCollect.mapper.CmdbVmwareInstanceLogMapper;
import com.aspire.ums.cmdb.ipCollect.service.CmdbVmwareInstanceLogService;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.CmdbVmwareInstanceLog;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.BaseInstanceRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.BaseInstanceRequestExtInfo;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRequestData;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/19 19:40
 */
@Slf4j
@Service("CmdbVmwareInstanceLogService")
public class CmdbVmwareInstanceLogServiceImpl implements CmdbVmwareInstanceLogService {
    @Autowired
    private CmdbVmwareInstanceLogMapper cmdbVmwareInstanceLogMapper;


    @Override
    public void add(CmdbVmwareInstanceLog cmdbVmwareInstanceLog) {
        cmdbVmwareInstanceLogMapper.insert(cmdbVmwareInstanceLog);
    }

    @Override
    public void saveInstanceLog(BaseInstanceRequest instanceRequest) {
        CmdbVmwareInstanceLog entity = new CmdbVmwareInstanceLog();
        InstanceRequestData data = instanceRequest.getData();
        BaseInstanceRequestExtInfo extInfo = data.getExtInfo();
        boolean valid = false;
        String objectId = extInfo.getObjectId();
        for (EventInstanceModuleEnum element : EventInstanceModuleEnum.values()) {
            if (objectId.equalsIgnoreCase(element.getKey())) {
                valid = true;
            }
        }
        if (!valid) {
            log.warn("不支持的objectId:[{}]", objectId);
            return;
        }
        entity.setId(UUIDUtil.getUUID());
        entity.setEventId(data.getEventId());
        entity.setEventType(data.getEvent());
        entity.setObjectId(extInfo.getObjectId());
        entity.setInstanceId(extInfo.getInstanceId());
        entity.setObjectName(extInfo.getObjectName());
        entity.setObjectVersion(extInfo.getObjectVersion());
        entity.setOperator(data.getOperator());
        entity.setOptContent(instanceRequest.getRequestBody());
        entity.setOptDesc(data.getOptDescription());
        entity.setOptTimestamp(extInfo.getTimestamp());
        entity.setTargetCategory(data.getTargetCategory());
        entity.setTargetId(data.getTargetId());
        entity.setTargetName(data.getTargetName());
        entity.setSystem(instanceRequest.getSystem());
        entity.setVersion(extInfo.getVersion());
        add(entity);
    }
}
