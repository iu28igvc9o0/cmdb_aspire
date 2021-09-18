package com.aspire.ums.cmdb.ipAudit.service.impl;

import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.service.IpAuditConfigDictService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: huanggongrui
 * @Description:
 * @Date: create in 2020/5/30 11:21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IpAuditConfigDictServiceImpl implements IpAuditConfigDictService {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private IpAuditIntranetIpMapper mapper;

    @Override
    public List<Map<String, String>> getDictByType(String type, String parentId) {
        List<Map<String, String>> rtn = Lists.newArrayList();
        if (StringUtils.isNotEmpty(type)) {
            HashMap<String, String> param = Maps.newHashMap();
            param.put("type", type);
            param.put("pid", parentId);
            switch (type) {
                case "business1":
                    rtn = mapper.getBusinessConfigDict(param);
                    break;
                case "business2":
                    if (StringUtils.isNotEmpty(parentId))
                        rtn = mapper.getBusinessConfigDict(param);
                    break;
                case "networkSegment":
                    rtn = mapper.getNetworkSegmentConfigDict();
                    break;
                case "networkSegmentSub":
                    rtn = mapper.getNetworkSegmentSubConfigDict(parentId);
                    break;
                case "financialBusiness":
                    rtn = mapper.getFinancialBusinessConfigDict();
                    break;
                case "deviceStatus":
                    rtn = mapper.getDeviceStatusConfigDict();
                    break;
                default: break;
            }
        }
        return rtn;
    }

    @Override
    public void updateIpRepositoryInfo(IpAuditUpdateRequest param) {
        mapper.updateIpRepositoryInnerIpInfo(param);
        mapper.updateIpRepositoryInnerSegmentInfo(param);
    }
}
