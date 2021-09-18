package com.aspire.ums.cmdb.maintenance.service.impl;

import com.aspire.ums.cmdb.maintenance.mapper.CmdbIncrementStatistMapper;
import com.aspire.ums.cmdb.maintenance.payload.CmdbIncrementStatistRequest;
import com.aspire.ums.cmdb.maintenance.service.ICmdbIncrementStatistService;
import com.netflix.discovery.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName CmdbIncrementStatistServiceImpl
 * @Description TODO
 * @Author luowenbo
 * @Date 2020/2/19 16:29
 * @Version 1.0
 */
@Service
public class CmdbIncrementStatistServiceImpl implements ICmdbIncrementStatistService {

    @Autowired
    private CmdbIncrementStatistMapper mapper;

    @Override
    public List<Map<String, Object>> statistIncrementByTime(CmdbIncrementStatistRequest req) {
        List<Map<String, Object>> rsList = new LinkedList<>();
        for (int i=1;i<=12;i++){
            Map<String, Object> mp = new HashMap<>();
            mp.put("mouth",i+"");
            mp.put("count",0+"");
            rsList.add(mp);
        }
        List<Map<String, Object>> tmpList = mapper.statistIncrementByTime(req);
        for (Map<String, Object> item : tmpList) {
            Integer mouth = Integer.parseInt(item.get("month").toString());
            Map<String, Object> tmpMp = rsList.get(mouth - 1);
            tmpMp.put("count",item.get("count").toString());
            rsList.set(mouth -1,tmpMp);
        }
        return rsList;
    }
}
