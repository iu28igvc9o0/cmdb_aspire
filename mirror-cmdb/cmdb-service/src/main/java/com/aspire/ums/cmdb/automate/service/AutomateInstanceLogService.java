package com.aspire.ums.cmdb.automate.service;

import com.aspire.ums.cmdb.automate.payload.AutomateHostDTO;
import com.aspire.ums.cmdb.automate.payload.AutomateInstanceLog;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.BaseInstanceRequest;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-08-24 10:00
 * @description 保存自动化模型日志接口
 */
public interface AutomateInstanceLogService {

    void add(AutomateInstanceLog instanceLog);
    void saveInstanceLog(BaseInstanceRequest request,String synLogId);
    void saveKafkaHostLog(Map<String,Object> synParam);
    void updateHostLogStatus(String synLogId);
    String getKafkaHostLog(String synLogId);
    void updateKafkaHostLogStatus(Map<String,Object> param);
}
