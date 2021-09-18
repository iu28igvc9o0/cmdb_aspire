package com.aspire.mirror.theme.server.service.impl;

import com.aspire.mirror.theme.server.dao.InstanceMapper;
import com.aspire.mirror.theme.server.entity.InstanceBaseColumn;
import com.aspire.mirror.theme.server.service.InstanceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InstanceServiceImpl implements InstanceService {

    @Autowired
    private InstanceMapper instanceMapper;

    @Override
    public Object getInstanceBaseInfoListByFreshTime(String freshTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("freshTime", freshTime);
        if (!StringUtils.isEmpty(freshTime)) {
            map.put("includeDelete", 1);
        }
        List<InstanceBaseColumn> list = instanceMapper.getInstanceBaseInfoList(map);

        return list;
    }


}
