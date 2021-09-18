package com.aspire.ums.cmdb.v2.index.serivce.impl;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenStoreLowUltRequest;
import com.aspire.ums.cmdb.index.payload.ScreenStoreLowUtilization;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.index.mapper.ItCloudScreenStoreLowUtilizationMapper;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenStoreLowUtilizationService;
import com.aspire.ums.cmdb.v2.index.util.MathCalculateUtil;
import com.aspire.ums.cmdb.v2.index.util.ScreenDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenStoreLowUtilizationServiceImpl
 * @Description 云存储低利用率服务类实现类
 * @Author luowenbo
 * @Date 2020/4/24 19:20
 * @Version 1.0
 */
@Service
public class ItCloudScreenStoreLowUtilizationServiceImpl implements ItCloudScreenStoreLowUtilizationService {

    @Autowired
    private ItCloudScreenStoreLowUtilizationMapper mapper;

    @Override
    public void insert(ScreenStoreLowUtilization obj) {
        if (null != obj) {
            obj.setId(UUIDUtil.getUUID());
            obj.setInsertTime(new Date());
            obj.setIsDelete("0");
            mapper.insert(obj);
        }
    }

    @Override
    public void delete(ItCloudScreenStoreLowUltRequest req) {
        mapper.delete(req);
    }

    @Override
    public Map<String, Object> getLowStoreUltCount(ItCloudScreenStoreLowUltRequest req) {
        List<Map<String, Object>> data = mapper.getLowStoreUltCount(req);
        if (null != data && !data.isEmpty()) {
            Map<String,Object> rs = new HashMap<>();
            BigDecimal total = new BigDecimal("0");
            for(Map<String, Object> item : data ){
                total = MathCalculateUtil.bigDecimalAdd(total,item.get("bzCnt").toString());
                String depTypeKey = "内部租户".equals(item.get("depType").toString())?"inside":"outside";
                rs.put(depTypeKey,item.get("bzCnt"));
            }
            rs.put("total",total);
            return ScreenDataUtil.transferDataByMap("CHART",rs);
        }
        return ScreenDataUtil.transferDataByList("CHART",data);
    }

    @Override
    public Map<String, Object> getLowStoreUltListWithDept(ItCloudScreenStoreLowUltRequest req) {
        List<Map<String,Object>> data = mapper.getLowStoreUltListWithDept(req);
        for(Map<String,Object> item : data) {
            item.put("percent",String.format("%.2f",new BigDecimal(item.get("percent").toString()).doubleValue()));
        }
        return ScreenDataUtil.transferDataByList("TABLE",data);
    }
}
