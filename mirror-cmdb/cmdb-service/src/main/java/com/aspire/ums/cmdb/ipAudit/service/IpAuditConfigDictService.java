package com.aspire.ums.cmdb.ipAudit.service;

import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: huanggongrui
 * @Description: 获取资源字典表数据
 * @Date: create in 2020/5/30 11:18
 */
public interface IpAuditConfigDictService {
    List<Map<String, String>> getDictByType(String type, String parentId);

    void updateIpRepositoryInfo(IpAuditUpdateRequest param);
}
