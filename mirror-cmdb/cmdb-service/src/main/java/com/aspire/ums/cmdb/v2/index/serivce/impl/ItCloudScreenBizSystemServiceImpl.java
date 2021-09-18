package com.aspire.ums.cmdb.v2.index.serivce.impl;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenBizSystemNotInspect;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.index.mapper.ItCloudScreenBizSystemMapper;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenBizSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName ItCloudScreenBizSystemServiceImpl
 * @Description
 * @Author luowenbo
 * @Date 2020/3/22 16:59
 * @Version 1.0
 */
@Service
public class ItCloudScreenBizSystemServiceImpl implements ItCloudScreenBizSystemService {

    @Autowired
    private ItCloudScreenBizSystemMapper bizSystemMapper;

    @Override
    public void batchInsertBizSystemNotInpect(List<ScreenBizSystemNotInspect> list) {
        if(null != list && !list.isEmpty()) {
            for(ScreenBizSystemNotInspect item : list) {
                item.setId(UUIDUtil.getUUID());
                item.setInsertTime(new Date());
                item.setIsDelete("0");
            }
            bizSystemMapper.batchInsertBizSystemNotInpect(list);
        }
    }

    @Override
    public List<Map<String,Object>> getBizSystemAllocation(ItCloudScreenRequest req) {
        return bizSystemMapper.getBizSystemAllocation(req);
    }

    @Override
    public List<Map<String, Object>> getBizSystemNotInpect(ItCloudScreenRequest req) {
        return bizSystemMapper.getBizSystemNotInpect(req);
    }

    @Override
    public List<Map<String, Object>> allDataListExport(ItCloudScreenRequest req) {
        return bizSystemMapper.allDataListExport(req);
    }

    @Override
    public List<Map<String, Object>> partDataListExport(ItCloudScreenRequest req) {
        List<Map<String, Object>> rsList = null;
        if(null != req.getModuleFlag()) {
            rsList = bizSystemMapper.calDataListExport(req);
        } else {
            rsList = dealStoreDataFormatter(bizSystemMapper.storeDataListExport(req));
        }
        return rsList;
    }

    // 数据库查出来是多行数据，导出查询的时候是一行显示
    private List<Map<String, Object>> dealStoreDataFormatter(List<Map<String, Object>> data) {
        List<Map<String, Object>> rs = new ArrayList<>();
        int index = 0;
        Map<String, Object> tmp = new HashMap<>();
        for(Map<String, Object> item : data) {
            index++;
            String deviceType = item.get("device_type").toString();
            String utilization = "0.0".equals(item.get("utilization").toString())?"0":item.get("utilization").toString();
            // hangfang 2020.07.30 硬编码加密密钥 key->type
            String type;
            switch (deviceType) {
                case "FC-SAN": type = "fcsan";break;
                case "IP-SAN": type = "ipsan";break;
                case "块存储": type = "kcc";break;
                case "备份存储": type = "bfcc";break;
                case "对象存储": type = "dxcc";break;
                case "文件存储": type = "wjcc";break;
                default:
                    throw new RuntimeException("数据错误");
            }
            tmp.put(type,utilization);
            if(index == 6) {
                tmp.put("department1",item.get("department1"));
                tmp.put("department2",item.get("department2"));
                tmp.put("resource_pool",item.get("resource_pool"));
                tmp.put("biz_system",item.get("biz_system"));
                tmp.put("pod",item.get("pod"));
                tmp.put("business_concat",item.get("business_concat"));
                tmp.put("business_concat_phone",item.get("business_concat_phone"));
                rs.add(tmp);
                // 重置Map的数据
                tmp = new HashMap<>();
                index = 0;
            }
        }
        return rs;
    }
}
