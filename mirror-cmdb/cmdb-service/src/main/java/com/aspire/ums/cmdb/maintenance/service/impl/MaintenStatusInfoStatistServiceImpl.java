package com.aspire.ums.cmdb.maintenance.service.impl;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.mapper.MaintenStatusInfoStatistMapper;
import com.aspire.ums.cmdb.maintenance.payload.MaintenStatusInfoStatistRequest;
import com.aspire.ums.cmdb.maintenance.service.MaintenStatusInfoStatistService;
import com.aspire.ums.cmdb.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class MaintenStatusInfoStatistServiceImpl implements MaintenStatusInfoStatistService {

    @Autowired
    private MaintenStatusInfoStatistMapper maintenanceStatusInfoStatistMapper;

    @Override
    public List<Map<String, Object>> statistMaintenStatusInfo(MaintenStatusInfoStatistRequest request) {
        // 初始化四种状态的数据
        String[] statusName = {"在建","已建","出保","即将出保"};
        Map<String,Map<String,Object>> mpValue = new LinkedHashMap<>();
        for(String s : statusName) {
            Map<String,Object> statusObj = new HashMap<>();
            statusObj.put("maintenStatus",s);
            statusObj.put("maintenNum",0);
            statusObj.put("deviceNum",0);
            mpValue.put(s,statusObj);
        }
        List<Map<String, Object>> resultList = maintenanceStatusInfoStatistMapper.statistMaintenStatusInfo(request);
        for(Map<String, Object> item : resultList) {
            String status = item.get("maintenStatus").toString();
            String maintenNum = item.get("maintenNum").toString();
            String deviceNum = item.get("deviceNum").toString();
            if(mpValue.containsKey(status)) {
                Map<String,Object> tmpObj = mpValue.get(status);
                tmpObj.put("maintenNum",maintenNum);
                tmpObj.put("deviceNum",deviceNum);
            }
        }
        List<Map<String, Object>> result = new ArrayList<>(mpValue.values());
        return result;
    }

    @Override
    public Result<Map<String,Object>> getMaintenProjectList(MaintenStatusInfoStatistRequest request) {
        if (StringUtils.isNotEmpty(request.getPageNo()) && StringUtils.isNotEmpty(request.getPageSize())) {
            request.setPageNo((request.getPageNo() - 1) * request.getPageSize());
        }
        List<Map<String, Object>> list =  maintenanceStatusInfoStatistMapper.getMaintenProjectList(request);
        Integer totalCount = maintenanceStatusInfoStatistMapper.countMaintenProjectList(request);
        return new Result<>(totalCount,list);
    }
}
