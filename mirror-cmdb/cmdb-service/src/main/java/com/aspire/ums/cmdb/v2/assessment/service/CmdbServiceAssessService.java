package com.aspire.ums.cmdb.v2.assessment.service;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.assessment.payload.CmdbServiceAssess;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.v2.assessment.service
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/15 15:28
 * 版本: v1.0
 */
public interface CmdbServiceAssessService {
    
    Map<String,Object> insert ();
    
    Result<CmdbServiceAssess> queryAllData(int pageNum, int pageSize, String province, String device_type);
    
    Map<String,Object> saveScore (List<CmdbServiceAssess> list);
    
}
