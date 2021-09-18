package com.aspire.ums.cmdb.collect.service.impl;

import com.aspire.ums.cmdb.collect.mapper.AutoDiscoveryLogShieldMapper;
import com.aspire.ums.cmdb.collect.service.AutoDiscoveryLogShieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AutoDiscoveryLogShieldServiceImpl implements AutoDiscoveryLogShieldService {

    @Autowired
    AutoDiscoveryLogShieldMapper logShieldMapper;

    @Override
    public List<String> list(String ruleId) {
        return logShieldMapper.getListByRuleId(ruleId);
    }
}
