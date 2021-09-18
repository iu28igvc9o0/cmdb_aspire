package com.aspire.ums.cmdb.v2.assessment.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.assessment.payload.CmdbProblemEvent;

import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.problemEvent.service
 * 类名称:
 * 类描述: 故障事件信息
 * 创建人: PJX
 * 创建时间: 2019/6/25 20:34
 * 版本: v1.0
 */
public interface ICmdbProblemEventService {
    
    Result<CmdbProblemEvent> getAllData(int pageNum, int pageSize, String province,String quarter,String createUsername);
    
    Map<String, Object> insertOrUpdate(JSONObject data);
    
    Map<String, Object> delete(String id);
    
    Map<String, Object> updateStatus(String province,int status, String quarter);
}
