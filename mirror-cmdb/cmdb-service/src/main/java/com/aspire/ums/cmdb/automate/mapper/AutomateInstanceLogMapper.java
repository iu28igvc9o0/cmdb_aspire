package com.aspire.ums.cmdb.automate.mapper;

import com.aspire.ums.cmdb.automate.payload.AutomateInstanceLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-08-24 10:20
 * @description
 */
public interface AutomateInstanceLogMapper {
    void insert(AutomateInstanceLog entity);
    void batchInsert(@Param("list") List<AutomateInstanceLog> list);
    void saveKafkaHostLog(Map<String,Object> param);
    void updateHostLogStatus(@Param("synLogId") String synLogId);
    String getKafkaHostLog(@Param("synLogId") String synLogId);
    void updateKafkaHostLogStatus(Map<String,Object> param);
}
