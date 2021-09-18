package com.aspire.ums.cmdb.automate.service.Impl;

import com.aspire.ums.cmdb.automate.enums.AutomateInstanceModuleEnum;
import com.aspire.ums.cmdb.automate.mapper.AutomateInstanceLogMapper;
import com.aspire.ums.cmdb.automate.payload.AutomateHostDTO;
import com.aspire.ums.cmdb.automate.payload.AutomateInstanceLog;
import com.aspire.ums.cmdb.automate.payload.easyops.AutomateHostDataDTO;
import com.aspire.ums.cmdb.automate.service.AutomateInstanceLogService;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.BaseInstanceRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.BaseInstanceRequestExtInfo;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRequestData;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-08-24 10:01
 * @description 保存自动化模型日志具体实现
 */
@Slf4j
@Service
public class AutomateInstanceLogServiceImpl implements AutomateInstanceLogService {

    @Resource
    private AutomateInstanceLogMapper logMapper;

    @Override
    public void add(AutomateInstanceLog instanceLog) {
        logMapper.insert(instanceLog);
    }

    @Override
    public void saveInstanceLog(BaseInstanceRequest request,String synLogId) {
        InstanceRequestData data = request.getData();
        BaseInstanceRequestExtInfo extInfo = data.getExtInfo();
        boolean valid = false;
        String objectId = extInfo.getObjectId();
        for (AutomateInstanceModuleEnum element : AutomateInstanceModuleEnum.values()) {
            if (objectId.equalsIgnoreCase(element.getKey())) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            log.warn("不支持的objectId:[{}]", objectId);
            return;
        }
        AutomateInstanceLog entity = buildInstanceLog(request,synLogId);
        add(entity);
    }

    @Override
    public void saveKafkaHostLog(Map<String,Object> synParam) {
        logMapper.saveKafkaHostLog(synParam);
    }

    @Override
    public void updateHostLogStatus(String synLogId) {
        logMapper.updateHostLogStatus(synLogId);
    }

    @Override
    public String getKafkaHostLog(String synLogId) {
        return logMapper.getKafkaHostLog(synLogId);
    }

    @Override
    public void updateKafkaHostLogStatus(Map<String, Object> param) {
        logMapper.updateKafkaHostLogStatus(param);
    }

    private AutomateInstanceLog buildInstanceLog(BaseInstanceRequest request, String synLogId) {
        InstanceRequestData data = request.getData();
        BaseInstanceRequestExtInfo extInfo = data.getExtInfo();
        AutomateInstanceLog entity = new AutomateInstanceLog();
        entity.setId(UUIDUtil.getUUID());
        entity.setEventId(data.getEventId());
        entity.setEventType(data.getEvent());
        entity.setObjectId(extInfo.getObjectId());
        entity.setInstanceId(extInfo.getInstanceId());
        entity.setObjectName(extInfo.getObjectName());
        entity.setObjectVersion(extInfo.getObjectVersion());
        entity.setOperator(data.getOperator());
        entity.setOptDesc(data.getOptDescription());
        entity.setOptTimestamp(extInfo.getTimestamp());
        entity.setTargetCategory(data.getTargetCategory());
        entity.setTargetId(data.getTargetId());
        entity.setTargetName(data.getTargetName());
        entity.setSystem(request.getSystem());
        entity.setVersion(extInfo.getVersion());
        entity.setSynLogId(synLogId);
        return entity;
    }


}
