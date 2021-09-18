package com.aspire.ums.cmdb.v2.index.serivce.impl;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenBizSystemDeviceDetailRequest;
import com.aspire.ums.cmdb.index.payload.ScreenBizSystemDeviceDetail;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.index.mapper.ItCloudScreenBizSystemDeviceDetailMapper;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenBizSystemDeviceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenBizSystemDeviceDetailServiceImpl
 * @Description 租户大屏业务系统具体设备
 * @Author luowenbo
 * @Date 2020/5/13 14:19
 * @Version 1.0
 */
@Service
public class ItCloudScreenBizSystemDeviceDetailServiceImpl implements ItCloudScreenBizSystemDeviceDetailService {

    @Autowired
    private ItCloudScreenBizSystemDeviceDetailMapper mapper;

    @Override
    public void batchInsert(List<ScreenBizSystemDeviceDetail> list) {
        if(null != list && !list.isEmpty()) {
            for(ScreenBizSystemDeviceDetail item : list) {
                item.setId(UUIDUtil.getUUID());
                item.setInsertTime(new Date());
            }
            mapper.batchInsert(list);
        }
    }

    @Override
    public void delete(ItCloudScreenBizSystemDeviceDetailRequest req) {
        mapper.delete(req);
    }

    @Override
    public List<ScreenBizSystemDeviceDetail> list(ItCloudScreenBizSystemDeviceDetailRequest req) {
        return mapper.list(req);
    }

    @Override
    public List<Map<String, String>> getDevTypeAndBizSystemList(String monthlyTime) {
        return mapper.getDevTypeAndBizSystemList(monthlyTime);
    }
}
