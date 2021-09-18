package com.aspire.ums.cmdb.maintenance.service.impl;

import com.aspire.ums.cmdb.maintenance.mapper.MaintenanceStatistAnalysisMapper;
import com.aspire.ums.cmdb.maintenance.payload.MaintenStatistAnalysisRequest;
import com.aspire.ums.cmdb.maintenance.service.MaintenanceStatistAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class MaintenanceStatistAnalysisServiceImpl implements MaintenanceStatistAnalysisService {

    @Autowired
    private MaintenanceStatistAnalysisMapper maintenanceStatistAnalysisMapper;

    @Override
    public List<Map<String,Object>> firstLayer(MaintenStatistAnalysisRequest request) {
        List<Map<String,Object>> rsData = maintenanceStatistAnalysisMapper.firstLayer(request);
        for(Map<String,Object> item : rsData) {
            Map<String,String> params = new HashMap<>();
            String resourcePool = item.get("resourcePool") == null ? "" : item.get("resourcePool").toString();
            String maintenanceProjectType = item.get("maintenanceProjectType") == null ? "" : item.get("maintenanceProjectType").toString();
            params.put("resourcePool",resourcePool);
            params.put("maintenanceProjectType",maintenanceProjectType);
            String closerDate = maintenanceStatistAnalysisMapper.getCloserDate(params)
                    .get("service_end_time").toString();
            item.put("serviceEndTime",closerDate);
        }
        return rsData;
    }

    @Override
    public List<Map<String, Object>> secondLayer(MaintenStatistAnalysisRequest request) {
        List<Map<String,Object>> rsData = maintenanceStatistAnalysisMapper.secondLayer(request);
        for(Map<String,Object> item : rsData) {
            Map<String,String> params = new HashMap<>();
            String resourcePool = item.get("resourcePool") == null ? "" : item.get("resourcePool").toString();
            String maintenanceProjectType = item.get("maintenanceProjectType") == null ? "" : item.get("maintenanceProjectType").toString();
            String deviceType = item.get("deviceType") == null ? "" : item.get("deviceType").toString();
            params.put("resourcePool",resourcePool);
            params.put("maintenanceProjectType",maintenanceProjectType);
            params.put("deviceType",deviceType);
            String closerDate = maintenanceStatistAnalysisMapper.getCloserDate(params)
                    .get("service_end_time").toString();
            item.put("serviceEndTime",closerDate);
        }
        return rsData;
    }

    @Override
    public List<Map<String, Object>> thirdLayer(MaintenStatistAnalysisRequest request) {
        List<Map<String,Object>> rsData = maintenanceStatistAnalysisMapper.thirdLayer(request);
        for(Map<String,Object> item : rsData) {
            Map<String,String> params = new HashMap<>();
            String resourcePool = item.get("resourcePool") == null ? "" : item.get("resourcePool").toString();
            String maintenanceProjectType = item.get("maintenanceProjectType") == null ? "" : item.get("maintenanceProjectType").toString();
            String deviceType = item.get("deviceType") == null ? "" : item.get("deviceType").toString();
            String serviceProduce = item.get("serviceProduce") == null ? "" : item.get("serviceProduce").toString();
            params.put("resourcePool",resourcePool);
            params.put("maintenanceProjectType",maintenanceProjectType);
            params.put("deviceType",deviceType);
            params.put("serviceProduce",serviceProduce);
            String closerDate = maintenanceStatistAnalysisMapper.getCloserDate(params)
                    .get("service_end_time").toString();
            item.put("serviceEndTime",closerDate);
        }
        return rsData;
    }

    @Override
    public List<Map<String, Object>> fourthLayer(MaintenStatistAnalysisRequest request) {
        return maintenanceStatistAnalysisMapper.fourthLayer(request);
    }

    @Override
    public List<Map<String, Object>> maintenancePeriodAnalysis(MaintenStatistAnalysisRequest request) {
        List<Map<String, Object>> rsData = null;
        if(!"".equals(request.getProjectName()) && request.getProjectName() != null) {
            rsData = maintenanceStatistAnalysisMapper.getMaintenPeriodByProjectName(request);
            List<Map<String, Object>> tmpData = new ArrayList<>();
            for(Map<String, Object> item : rsData) {
                String flag = item.get("quarterFlag") == null ? "" : item.get("quarterFlag").toString();
                String time = item.get("serviceEndTime") == null ? "" : item.get("serviceEndTime").toString();
                List<Map<String, Object>> obj = maintenanceStatistAnalysisMapper.getCloserMaintenPeriod(flag,time);
                tmpData.addAll(obj);
            }
            rsData = tmpData;
        } else {
            rsData = maintenanceStatistAnalysisMapper.maintenancePeriodAnalysis(request);
        }
        // 维保项目服务结束时间，续约的合同，不可能存在
        int len = rsData.size();
        for (int i=0;i<len;i++) {
            String quarterFlag = rsData.get(i).get("quarterFlag") == null ? "" : rsData.get(i).get("quarterFlag").toString();
            if(i+1 < len) {
                String nextFlag = rsData.get(i+1).get("quarterFlag") == null ? "" : rsData.get(i+1).get("quarterFlag").toString();
                if(quarterFlag.equals(nextFlag)) {
                    i++;
                } else {
                    Map<String, Object> addMp = new HashMap<>();
                    addMp.put("quarterFlag",quarterFlag);
                    addMp.put("serviceEndTime","1970-01-01");
                    addMp.put("deviceNum","0");
                    addMp.put("money","0");
                    addMp.put("discountRate","0");
                    addMp.put("projectName","");
                    addMp.put("serviceNum","");
                    addMp.put("serviceType","");
                    addMp.put("contractProduce","");
                    addMp.put("serviceProduce","");
                    addMp.put("deviceType","");
                    addMp.put("deviceArea","");
                    rsData.add(addMp);
                }
            } else {
                if(i+1 == len) {
                    Map<String, Object> addMp = new HashMap<>();
                    addMp.put("quarterFlag",quarterFlag);
                    addMp.put("serviceEndTime","1970-01-01");
                    addMp.put("deviceNum","0");
                    addMp.put("money","0");
                    addMp.put("discountRate","0");
                    addMp.put("projectName","");
                    addMp.put("serviceNum","");
                    addMp.put("serviceType","");
                    addMp.put("contractProduce","");
                    addMp.put("serviceProduce","");
                    addMp.put("deviceType","");
                    addMp.put("deviceArea","");
                    rsData.add(addMp);
                }
            }
        }
        Collections.sort(rsData, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String flag1 = o1.get("quarterFlag").toString();
                String flag2 = o2.get("quarterFlag").toString();
                if(flag1.compareTo(flag2) == 0) {
                    String time1 = o1.get("serviceEndTime") == null ? "" : o1.get("serviceEndTime").toString();
                    String time2 = o2.get("serviceEndTime") == null ? "" : o2.get("serviceEndTime").toString();
                    return time1.compareTo(time2);
                }
                return flag1.compareTo(flag2);
            }
        });
        return rsData;
    }
}
